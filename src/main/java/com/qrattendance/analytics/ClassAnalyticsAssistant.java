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
            return "Please enter a question (e.g., \"Which students have below 75% attendance?\" or \"Summarize today's turnout\").";
        }

        String q = rawQuery.trim().toLowerCase(Locale.ROOT);

        // 1. Help / Capabilities
        if (q.contains("help") || q.contains("what can you do") || q.equals("?")) {
            return getHelpText();
        }

        // 2. Defaulters / Below threshold (default 75%)
        if (q.contains("below") || q.contains("under") || q.contains("defaulter")
                || q.contains("shortage") || q.contains("low attendance") || q.contains("less than")) {
            double threshold = extractThreshold(q, 75.0);
            return getDefaultersReport(threshold);
        }

        // 3. Specific roll number lookup (e.g., BCA2026001)
        Matcher rollMatcher = Pattern.compile("\\b([a-z]{2,6}\\d{2,10})\\b", Pattern.CASE_INSENSITIVE)
                .matcher(rawQuery.trim());
        if (rollMatcher.find() && !q.contains("summary") && !q.contains("turnout")) {
            return getStudentReport(rollMatcher.group(1).toUpperCase(Locale.ROOT));
        }

        // 4. Today's / Session turnout summary
        if (q.contains("today") || q.contains("turnout") || q.contains("summary")
                || q.contains("present") || q.contains("how many") || q.contains("count")) {
            return getTodayTurnoutSummary();
        }

        // Default fallback: show overall snapshot + supported queries
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

    private static String getDefaultersReport(double threshold) {
        String sql = "SELECT s.roll_no, s.name, " +
                "COUNT(a.log_id) AS total_classes, " +
                "SUM(CASE WHEN a.status = 'PRESENT' OR a.status = 'ON-DUTY' THEN 1 ELSE 0 END) AS present_classes " +
                "FROM students s " +
                "LEFT JOIN attendance_logs a ON s.roll_no = a.student_roll_no " +
                "GROUP BY s.roll_no, s.name " +
                "ORDER BY s.roll_no";

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.ROOT, "=== STUDENTS BELOW %.0f%% ATTENDANCE ===\n", threshold));

        int defaulterCount = 0;
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String rollNo = rs.getString("roll_no");
                String name = rs.getString("name");
                int totalClasses = rs.getInt("total_classes");
                int presentClasses = rs.getInt("present_classes");

                double pct = totalClasses > 0 ? ((double) presentClasses / totalClasses) * 100.0 : 0.0;
                if (pct < threshold) {
                    defaulterCount++;
                    sb.append(String.format(Locale.ROOT,
                            "- %s (%s): %.1f%% (%d/%d classes)\n",
                            rollNo, name, pct, presentClasses, totalClasses));
                }
            }

            if (defaulterCount == 0) {
                sb.append("No students are currently below ")
                        .append(String.format(Locale.ROOT, "%.0f%%", threshold))
                        .append(" attendance.");
            }
        } catch (SQLException e) {
            return "[Analytics DB Error] " + e.getMessage();
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
            try (ResultSet rs = stmt.executeQuery(latestSessionSql)) {
                if (rs.next()) {
                    latestSessionId = rs.getInt("class_id");
                    latestSubject = rs.getString("subject_name");
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append("=== ATTENDANCE TURNOUT SUMMARY ===\n");

            if (latestSessionId == -1) {
                sb.append("No class sessions have been started yet.");
                return sb.toString();
            }

            String sessionLogsSql = "SELECT a.student_roll_no, s.name, a.status, a.timestamp " +
                    "FROM attendance_logs a " +
                    "LEFT JOIN students s ON a.student_roll_no = s.roll_no " +
                    "WHERE a.class_id = ? " +
                    "ORDER BY a.log_id ASC";

            int presentInLatest = 0;
            StringBuilder roster = new StringBuilder();
            try (PreparedStatement pstmt = conn.prepareStatement(sessionLogsSql)) {
                pstmt.setInt(1, latestSessionId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        presentInLatest++;
                        String roll = rs.getString("student_roll_no");
                        String name = rs.getString("name");
                        String status = rs.getString("status");
                        String ts = rs.getString("timestamp");
                        roster.append(String.format(Locale.ROOT,
                                "  • %s%s — %s (%s)\n",
                                roll,
                                (name != null ? " / " + name : ""),
                                status,
                                ts));
                    }
                }
            }

            int todayTotalLogs = 0;
            String todaySql = "SELECT COUNT(*) AS cnt FROM attendance_logs WHERE timestamp LIKE ?";
            try (PreparedStatement pstmt = conn.prepareStatement(todaySql)) {
                pstmt.setString(1, todayPrefix + "%");
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        todayTotalLogs = rs.getInt("cnt");
                    }
                }
            }

            sb.append(String.format(Locale.ROOT,
                    "Latest Session: #%d (%s)\nChecked In: %d / %d registered students\nTotal Check-Ins Today (%s): %d\n",
                    latestSessionId, latestSubject, presentInLatest, totalStudents, todayPrefix, todayTotalLogs));

            if (presentInLatest > 0) {
                sb.append("Checked-In List:\n").append(roster.toString().trim());
            } else {
                sb.append("No students checked in yet for this session.");
            }

            return sb.toString();

        } catch (SQLException e) {
            return "[Analytics DB Error] " + e.getMessage();
        }
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
                        "=== STUDENT RECORD: %s ===\nName: %s\nAttended: %d / %d classes (%.1f%%)\nStatus: %s",
                        rollNo, name, present, total, pct, pct >= 75.0 ? "Eligible (>=75%)" : "Below 75% Threshold");
            }
        } catch (SQLException e) {
            return "[Analytics DB Error] " + e.getMessage();
        }
    }

    private static String getHelpText() {
        return "Try asking:\n" +
                "- \"Which students have below 75% attendance?\"\n" +
                "- \"Summarize today's attendance turnout\"\n" +
                "- \"BCA2026001\"";
    }
}
