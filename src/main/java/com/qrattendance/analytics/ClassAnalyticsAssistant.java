package com.qrattendance.analytics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassAnalyticsAssistant {
    private static final String URL = "jdbc:sqlite:attendance.db";

    public static String answerQuery(String rawQuery) {
        if (rawQuery == null || rawQuery.trim().isEmpty()) {
            return "Enter an analytics query (e.g., \"Eligibility compliance summary\", \"Today's turnout\", or \"Subject breakdown\").";
        }

        String q = rawQuery.trim().toLowerCase(Locale.ROOT);

        // 1. Help / Capabilities
        if (q.contains("help") || q.contains("what can you do") || q.equals("?")) {
            return getHelpText();
        }

        // 2. Subject-wise breakdown
        if (q.contains("subject") || q.contains("course") || q.contains("breakdown")) {
            return getSubjectBreakdownReport();
        }

        // 3. Eligibility / Compliance overview
        if (q.contains("eligib") || q.contains("compliance") || q.contains("standing") || q.contains("overall")) {
            return getEligibilityComplianceReport(75.0);
        }

        // 4. Defaulters / Below threshold (default 75%)
        if (q.contains("below") || q.contains("under") || q.contains("defaulter")
                || q.contains("shortage") || q.contains("low attendance") || q.contains("less than")) {
            double threshold = extractThreshold(q, 75.0);
            return getDefaultersReport(threshold);
        }

        // 5. Explicit individual roll number lookup (only when a specific roll ID is queried)
        Matcher rollMatcher = Pattern.compile("\\b([a-z]{2,6}\\d{2,10})\\b", Pattern.CASE_INSENSITIVE)
                .matcher(rawQuery.trim());
        if (rollMatcher.find() && !q.contains("summary") && !q.contains("turnout")) {
            return getStudentReport(rollMatcher.group(1).toUpperCase(Locale.ROOT));
        }

        // 6. Today's / Session turnout summary (aggregated, privacy-safe)
        if (q.contains("today") || q.contains("turnout") || q.contains("summary")
                || q.contains("present") || q.contains("how many") || q.contains("count")) {
            return getTodayTurnoutSummary();
        }

        // Default fallback: official snapshot + supported prompts
        return getTodayTurnoutSummary() + "\n\n" + getHelpText();
    }

    private static double extractThreshold(String lowerQuery, double defaultValue) {
        Matcher m = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*%").matcher(lowerQuery);
        if (m.find()) {
            try {
                return Double.parseDouble(m.group(1));
            } catch (NumberFormatException ignored) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private static String maskRollNo(String rollNo) {
        if (rollNo == null || rollNo.length() <= 3) {
            return "***";
        }
        return rollNo.substring(0, rollNo.length() - 3) + "***";
    }

    private static String getEligibilityComplianceReport(double threshold) {
        String sql = "SELECT s.roll_no, " +
                "COUNT(a.log_id) AS total_classes, " +
                "SUM(CASE WHEN a.status = 'PRESENT' OR a.status = 'ON-DUTY' THEN 1 ELSE 0 END) AS present_classes " +
                "FROM students s " +
                "LEFT JOIN attendance_logs a ON s.roll_no = a.student_roll_no " +
                "GROUP BY s.roll_no";

        int totalStudents = 0;
        int eligibleCount = 0;
        int atRiskCount = 0;
        double sumPercentages = 0.0;

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                totalStudents++;
                int totalClasses = rs.getInt("total_classes");
                int presentClasses = rs.getInt("present_classes");
                double pct = totalClasses > 0 ? ((double) presentClasses / totalClasses) * 100.0 : 0.0;
                sumPercentages += pct;

                if (pct >= threshold) {
                    eligibleCount++;
                } else {
                    atRiskCount++;
                }
            }
        } catch (SQLException e) {
            return "[Analytics DB Error] " + e.getMessage();
        }

        double avgRate = totalStudents > 0 ? (sumPercentages / totalStudents) : 0.0;
        double compliancePct = totalStudents > 0 ? ((double) eligibleCount / totalStudents) * 100.0 : 0.0;

        return String.format(Locale.ROOT,
                "OFFICIAL ELIGIBILITY & COMPLIANCE REPORT (Threshold: %.0f%%)\n" +
                        "• Registered Cohort: %d students\n" +
                        "• Eligible (>= %.0f%%): %d students (%.1f%%)\n" +
                        "• Below Threshold (< %.0f%%): %d students\n" +
                        "• Mean Cohort Attendance: %.1f%%",
                threshold, totalStudents, threshold, eligibleCount, compliancePct, threshold, atRiskCount, avgRate);
    }

    private static String getDefaultersReport(double threshold) {
        String sql = "SELECT s.roll_no, " +
                "COUNT(a.log_id) AS total_classes, " +
                "SUM(CASE WHEN a.status = 'PRESENT' OR a.status = 'ON-DUTY' THEN 1 ELSE 0 END) AS present_classes " +
                "FROM students s " +
                "LEFT JOIN attendance_logs a ON s.roll_no = a.student_roll_no " +
                "GROUP BY s.roll_no " +
                "ORDER BY s.roll_no";

        StringBuilder listSb = new StringBuilder();
        int totalStudents = 0;
        int defaulterCount = 0;

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                totalStudents++;
                String rollNo = rs.getString("roll_no");
                int totalClasses = rs.getInt("total_classes");
                int presentClasses = rs.getInt("present_classes");

                double pct = totalClasses > 0 ? ((double) presentClasses / totalClasses) * 100.0 : 0.0;
                if (pct < threshold) {
                    defaulterCount++;
                    listSb.append(String.format(Locale.ROOT,
                            "  • %s — %.1f%% (%d/%d periods)\n",
                            maskRollNo(rollNo), pct, presentClasses, totalClasses));
                }
            }
        } catch (SQLException e) {
            return "[Analytics DB Error] " + e.getMessage();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.ROOT,
                "ATTENDANCE SHORTAGE ADVISORY (< %.0f%%)\n" +
                        "• Flagged Accounts: %d of %d students\n",
                threshold, defaulterCount, totalStudents));

        if (defaulterCount == 0) {
            sb.append("• Status: All registered students meet the attendance requirement.");
        } else {
            sb.append("• Masked Cohort Breakdown (query exact Roll ID for individual record):\n")
                    .append(listSb.toString().trim());
        }

        return sb.toString().trim();
    }

    private static String getTodayTurnoutSummary() {
        String todayPrefix = LocalDate.now().toString(); // yyyy-MM-dd
        int totalStudents = 0;

        String countStudentsSql = "SELECT COUNT(*) AS cnt FROM students";
        String latestSessionSql = "SELECT class_id, subject_name, date_time FROM classes ORDER BY class_id DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            try (ResultSet rs = stmt.executeQuery(countStudentsSql)) {
                if (rs.next()) {
                    totalStudents = rs.getInt("cnt");
                }
            }

            int latestSessionId = -1;
            String latestSubject = "N/A";
            String latestDateTime = "";
            try (ResultSet rs = stmt.executeQuery(latestSessionSql)) {
                if (rs.next()) {
                    latestSessionId = rs.getInt("class_id");
                    latestSubject = rs.getString("subject_name");
                    latestDateTime = rs.getString("date_time");
                }
            }

            if (latestSessionId == -1) {
                return "OFFICIAL TURNOUT REPORT\n• No class sessions have been recorded yet.";
            }

            int presentCount = 0;
            int onDutyCount = 0;
            String statusBreakdownSql = "SELECT status, COUNT(*) AS cnt FROM attendance_logs WHERE class_id = ? GROUP BY status";
            try (PreparedStatement pstmt = conn.prepareStatement(statusBreakdownSql)) {
                pstmt.setInt(1, latestSessionId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String st = rs.getString("status");
                        int c = rs.getInt("cnt");
                        if ("ON-DUTY".equalsIgnoreCase(st)) {
                            onDutyCount += c;
                        } else if ("PRESENT".equalsIgnoreCase(st)) {
                            presentCount += c;
                        }
                    }
                }
            }

            int verifiedTotal = presentCount + onDutyCount;
            double latestTurnoutPct = totalStudents > 0 ? ((double) verifiedTotal / totalStudents) * 100.0 : 0.0;

            int todaySessions = 0;
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) AS cnt FROM classes WHERE date_time LIKE ?")) {
                pstmt.setString(1, todayPrefix + "%");
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        todaySessions = rs.getInt("cnt");
                    }
                }
            }

            int todayCheckIns = 0;
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) AS cnt FROM attendance_logs WHERE timestamp LIKE ?")) {
                pstmt.setString(1, todayPrefix + "%");
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        todayCheckIns = rs.getInt("cnt");
                    }
                }
            }

            String sessionDay = (latestDateTime != null && latestDateTime.length() >= 10)
                    ? latestDateTime.substring(0, 10)
                    : todayPrefix;

            return String.format(Locale.ROOT,
                    "OFFICIAL TURNOUT REPORT (%s)\n" +
                            "• Active / Latest Session: %s • #%d (%s)\n" +
                            "• Verified Attendance: %d / %d students (%.1f%%) — Present: %d, On-Duty: %d\n" +
                            "• Daily Activity (%s): %d session(s) conducted • %d total check-ins",
                    todayPrefix,
                    sessionDay, latestSessionId, latestSubject,
                    verifiedTotal, totalStudents, latestTurnoutPct, presentCount, onDutyCount,
                    todayPrefix, todaySessions, todayCheckIns);

        } catch (SQLException e) {
            return "[Analytics DB Error] " + e.getMessage();
        }
    }

    private static String getSubjectBreakdownReport() {
        String sql = "SELECT COALESCE(c.subject_name, 'UNASSIGNED') AS subj, " +
                "COUNT(DISTINCT c.class_id) AS session_count, " +
                "SUM(CASE WHEN a.status = 'PRESENT' OR a.status = 'ON-DUTY' THEN 1 ELSE 0 END) AS verified_logs " +
                "FROM classes c " +
                "LEFT JOIN attendance_logs a ON c.class_id = a.class_id " +
                "GROUP BY subj " +
                "ORDER BY subj ASC";

        StringBuilder sb = new StringBuilder("SUBJECT-WISE ATTENDANCE SUMMARY\n");
        boolean found = false;

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                found = true;
                String subj = rs.getString("subj");
                int sessions = rs.getInt("session_count");
                int logs = rs.getInt("verified_logs");
                double avgPerSession = sessions > 0 ? ((double) logs / sessions) : 0.0;
                sb.append(String.format(Locale.ROOT,
                        "• %s: %d session(s) • %d verified check-ins (avg %.1f/session)\n",
                        subj, sessions, logs, avgPerSession));
            }
        } catch (SQLException e) {
            return "[Analytics DB Error] " + e.getMessage();
        }

        if (!found) {
            sb.append("• No subject sessions recorded yet.");
        }
        return sb.toString().trim();
    }

    private static String getStudentReport(String rollNo) {
        String sql = "SELECT s.roll_no, s.name, " +
                "COUNT(a.log_id) AS total_classes, " +
                "SUM(CASE WHEN a.status = 'PRESENT' OR a.status = 'ON-DUTY' THEN 1 ELSE 0 END) AS present_classes " +
                "FROM students s " +
                "LEFT JOIN attendance_logs a ON s.roll_no = a.student_roll_no " +
                "WHERE UPPER(s.roll_no) = ? " +
                "GROUP BY s.roll_no, s.name";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, rollNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return "No student record found for Roll Number: " + rollNo;
                }
                String name = rs.getString("name");
                int total = rs.getInt("total_classes");
                int present = rs.getInt("present_classes");
                double pct = total > 0 ? ((double) present / total) * 100.0 : 0.0;

                return String.format(Locale.ROOT,
                        "INDIVIDUAL STUDENT AUDIT: %s\n" +
                                "• Name: %s\n" +
                                "• Attended: %d / %d periods (%.1f%%)\n" +
                                "• Academic Standing: %s",
                        rollNo, name, present, total, pct,
                        pct >= 75.0 ? "Eligible (>= 75%)" : "Shortage Warning (< 75%)");
            }
        } catch (SQLException e) {
            return "[Analytics DB Error] " + e.getMessage();
        }
    }

    private static String getHelpText() {
        return "Supported Official Queries:\n" +
                "• \"Today's turnout\" — Aggregated session and daily check-in counts\n" +
                "• \"Eligibility compliance summary\" — Cohort >=75% vs. <75% standing\n" +
                "• \"Below 75%\" — Masked shortage advisory without public roll exposure\n" +
                "• \"Subject breakdown\" — Session and check-in averages by course";
    }
}
