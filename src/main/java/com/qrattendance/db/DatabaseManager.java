package com.qrattendance.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:attendance.db";

    public enum LogStatus {
        SUCCESS,
        DUPLICATE,
        ERROR
    }

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            // 1. Students table
            String studentsSql = "CREATE TABLE IF NOT EXISTS students (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "roll_no TEXT UNIQUE NOT NULL, " +
                    "name TEXT NOT NULL, " +
                    "email TEXT NOT NULL, " +
                    "password_hash TEXT NOT NULL" +
                    ");";
            stmt.execute(studentsSql);

            // Seed default test student (Roll: BCA2026001, Pass: password123)
            String seedStudent = "INSERT OR IGNORE INTO students (roll_no, name, email, password_hash) " +
                    "VALUES ('BCA2026001', 'Kirtiman Student', 'kirtiman@student.com', 'password123');";
            stmt.execute(seedStudent);

            // 2. Classes / Sessions Table
            String classesSql = "CREATE TABLE IF NOT EXISTS classes (" +
                    "class_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "subject_name TEXT, " +
                    "teacher_name TEXT, " +
                    "date_time TEXT" +
                    ");";
            stmt.execute(classesSql);

            // 3. Attendance Logs Table with strict UNIQUE(class_id, student_roll_no) anti-duplicate constraint
            String logsSql = "CREATE TABLE IF NOT EXISTS attendance_logs (" +
                    "log_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "class_id INTEGER NOT NULL, " +
                    "student_roll_no TEXT NOT NULL, " +
                    "status TEXT NOT NULL, " +
                    "timestamp TEXT NOT NULL, " +
                    "UNIQUE(class_id, student_roll_no)" +
                    ");";
            stmt.execute(logsSql);

            // Clean up any pre-existing duplicate rows from earlier test runs before locking the unique index
            stmt.execute("DELETE FROM attendance_logs WHERE log_id NOT IN (" +
                    "SELECT MIN(log_id) FROM attendance_logs GROUP BY class_id, student_roll_no);");

            // Enforce unique index on (class_id, student_roll_no)
            stmt.execute("CREATE UNIQUE INDEX IF NOT EXISTS idx_unique_session_student " +
                    "ON attendance_logs(class_id, student_roll_no);");

            // 4. Attendance Audit Log Table for HOD / Admin manual overrides
            String auditSql = "CREATE TABLE IF NOT EXISTS attendance_audit_log (" +
                    "audit_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "class_id INTEGER NOT NULL, " +
                    "student_roll_no TEXT NOT NULL, " +
                    "old_status TEXT NOT NULL, " +
                    "new_status TEXT NOT NULL, " +
                    "changed_by TEXT NOT NULL, " +
                    "reason TEXT NOT NULL, " +
                    "changed_at TEXT NOT NULL" +
                    ");";
            stmt.execute(auditSql);

            System.out.println("[DB] Database and all core tables (including audit log) initialized!");

        } catch (SQLException e) {
            System.out.println("[DB ERROR] Error initializing database: " + e.getMessage());
        }
    }

    // Create a new class session row and return its generated class_id
    public static int createClassSession(String subjectName, String teacherName, String dateTime) {
        String sql = "INSERT INTO classes (subject_name, teacher_name, date_time) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, subjectName);
            pstmt.setString(2, teacherName);
            pstmt.setString(3, dateTime);
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int sessionId = keys.getInt(1);
                    System.out.println("[DB] Created new class session ID: " + sessionId + " (" + subjectName + ")");
                    return sessionId;
                }
            }
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Failed to create class session: " + e.getMessage());
        }
        return 1;
    }

    // Count how many class sessions have been created on a given date (yyyy-MM-dd)
    public static int getDailySessionNumber(String datePrefix) {
        String sql = "SELECT COUNT(*) FROM classes WHERE date_time LIKE ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, datePrefix + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Math.max(1, rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Failed to count daily sessions: " + e.getMessage());
        }
        return 1;
    }

    // Authenticate student login credentials
    public static boolean authenticateStudent(String rollNo, String password) {
        String sql = "SELECT * FROM students WHERE roll_no = ? AND password_hash = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, rollNo.trim());
            pstmt.setString(2, password.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("[DB ERROR] Authentication query failed: " + e.getMessage());
            return false;
        }
    }

    public static void getAllStudents() {
        String sql = "SELECT * FROM students";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- DATABASE RECORDS: STUDENTS TABLE ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        " | Roll No: " + rs.getString("roll_no") +
                        " | Name: " + rs.getString("name") +
                        " | Email: " + rs.getString("email"));
            }
        } catch (SQLException e) {
            System.out.println("Error querying database: " + e.getMessage());
        }
    }

    public static void addStudent(String rollNo, String name, String email, String passwordHash) {
        String sql = "INSERT INTO students (roll_no, name, email, password_hash) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, rollNo);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, passwordHash);

            pstmt.executeUpdate();
            System.out.println("Successfully added student: " + name);

        } catch (SQLException e) {
            System.out.println("Error inserting student: " + e.getMessage());
        }
    }

    public static void addStudent(String rollNo, String name, String email) {
        addStudent(rollNo, name, email, "password123");
    }

    public static void addStudent(int rollNo, String name, String email) {
        addStudent(String.valueOf(rollNo), name, email, "password123");
    }

    public static void updateAttendanceStatus(String rollNo, int classId, String newStatus) {
        String sql = "UPDATE attendance_logs SET status = ? WHERE student_roll_no = ? AND class_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setString(2, rollNo);
            pstmt.setInt(3, classId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successfully updated Roll No " + rollNo + " for Class ID " + classId + " to " + newStatus);
            } else {
                System.out.println("No matching attendance record found to update.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating attendance status: " + e.getMessage());
        }
    }

    // HOD / Admin override that updates attendance_logs (or inserts if missing for a session) and records an audit entry
    public static boolean overrideAttendanceStatus(
            int classId,
            String studentRollNo,
            String newStatus,
            String changedBy,
            String reason
    ) {
        if (studentRollNo == null || studentRollNo.trim().isEmpty()
                || newStatus == null || newStatus.trim().isEmpty()
                || reason == null || reason.trim().isEmpty()) {
            return false;
        }

        String cleanRoll = studentRollNo.trim();
        String cleanStatus = newStatus.trim().toUpperCase();
        String cleanActor = (changedBy == null || changedBy.trim().isEmpty()) ? "HOD_ADMIN" : changedBy.trim();
        String cleanReason = reason.trim();
        String nowStr = LocalDateTime.now().toString();

        String selectSql = "SELECT status FROM attendance_logs WHERE class_id = ? AND student_roll_no = ?";
        String updateSql = "UPDATE attendance_logs SET status = ? WHERE class_id = ? AND student_roll_no = ?";
        String insertLogSql = "INSERT INTO attendance_logs (class_id, student_roll_no, status, timestamp) VALUES (?, ?, ?, ?)";
        String auditSql = "INSERT INTO attendance_audit_log " +
                "(class_id, student_roll_no, old_status, new_status, changed_by, reason, changed_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL)) {
            conn.setAutoCommit(false);

            String oldStatus = "ABSENT";
            boolean rowExists = false;

            try (PreparedStatement selStmt = conn.prepareStatement(selectSql)) {
                selStmt.setInt(1, classId);
                selStmt.setString(2, cleanRoll);
                try (ResultSet rs = selStmt.executeQuery()) {
                    if (rs.next()) {
                        rowExists = true;
                        oldStatus = rs.getString("status");
                    }
                }
            }

            if (rowExists) {
                try (PreparedStatement updStmt = conn.prepareStatement(updateSql)) {
                    updStmt.setString(1, cleanStatus);
                    updStmt.setInt(2, classId);
                    updStmt.setString(3, cleanRoll);
                    updStmt.executeUpdate();
                }
            } else {
                try (PreparedStatement insStmt = conn.prepareStatement(insertLogSql)) {
                    insStmt.setInt(1, classId);
                    insStmt.setString(2, cleanRoll);
                    insStmt.setString(3, cleanStatus);
                    insStmt.setString(4, nowStr);
                    insStmt.executeUpdate();
                }
            }

            try (PreparedStatement audStmt = conn.prepareStatement(auditSql)) {
                audStmt.setInt(1, classId);
                audStmt.setString(2, cleanRoll);
                audStmt.setString(3, oldStatus);
                audStmt.setString(4, cleanStatus);
                audStmt.setString(5, cleanActor);
                audStmt.setString(6, cleanReason);
                audStmt.setString(7, nowStr);
                audStmt.executeUpdate();
            }

            conn.commit();
            System.out.println("[AUDIT] Session " + classId + " | Roll " + cleanRoll + ": " + oldStatus + " -> " + cleanStatus + " by " + cleanActor);
            return true;

        } catch (SQLException e) {
            System.out.println("[DB ERROR] Override transaction failed: " + e.getMessage());
            return false;
        }
    }

    public static void checkAttendancePercentages() {
        String sql = "SELECT student_roll_no, " +
                "COUNT(*) AS total_classes, " +
                "SUM(CASE WHEN status = 'PRESENT' OR status = 'ON-DUTY' THEN 1 ELSE 0 END) AS present_classes " +
                "FROM attendance_logs GROUP BY student_roll_no";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- ATTENDANCE PERCENTAGE REPORT ---");
            boolean hasRecords = false;
            while (rs.next()) {
                hasRecords = true;
                String rollNo = rs.getString("student_roll_no");
                int totalClasses = rs.getInt("total_classes");
                int presentClasses = rs.getInt("present_classes");
                double percentage = totalClasses > 0 ? ((double) presentClasses / totalClasses) * 100.0 : 0.0;

                System.out.println("Roll No: " + rollNo + " | Attended: " + presentClasses + "/" + totalClasses +
                        " | Percentage: " + String.format("%.2f", percentage) + "%");
                if (percentage < 75.0) {
                    System.out.println("  ⚠️ WARNING: Attendance is below 75% threshold!");
                }
            }
            if (!hasRecords) {
                System.out.println("No attendance logs found in database yet.");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating attendance: " + e.getMessage());
        }
    }

    // Insert an attendance record and strictly catch UNIQUE constraint violations (double-dipping)
    public static LogStatus addAttendanceLog(int classId, String studentRollNo, String status, String timestamp) {
        String sql = "INSERT INTO attendance_logs (class_id, student_roll_no, status, timestamp) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, classId);
            pstmt.setString(2, studentRollNo);
            pstmt.setString(3, status);
            pstmt.setString(4, timestamp);

            pstmt.executeUpdate();
            return LogStatus.SUCCESS;

        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("SQLITE_CONSTRAINT_UNIQUE")) {
                System.out.println("[SECURITY] Blocked duplicate check-in for Roll: " + studentRollNo + " in Session ID: " + classId);
                return LogStatus.DUPLICATE;
            }
            System.out.println("[DB ERROR] Error inserting attendance log: " + e.getMessage());
            return LogStatus.ERROR;
        }
    }
}
