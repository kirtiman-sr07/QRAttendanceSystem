package com.qrattendance.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:attendance.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            // 1. Updated students table with TEXT roll_no and password_hash for Phase 2 authentication
            String studentsSql = "CREATE TABLE IF NOT EXISTS students (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "roll_no TEXT UNIQUE NOT NULL, " +
                    "name TEXT NOT NULL, " +
                    "email TEXT NOT NULL, " +
                    "password_hash TEXT NOT NULL" +
                    ");";
            stmt.execute(studentsSql);

            // Seed a default test student account for login testing (Roll: BCA2026001, Pass: password123)
            String seedStudent = "INSERT OR IGNORE INTO students (roll_no, name, email, password_hash) " +
                    "VALUES ('BCA2026001', 'Kirtiman Student', 'kirtiman@student.com', 'password123');";
            stmt.execute(seedStudent);

            // 2. Initialize Classes Table
            String classesSql = "CREATE TABLE IF NOT EXISTS classes (" +
                    "class_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "subject_name TEXT, " +
                    "teacher_name TEXT, " +
                    "date_time TEXT" +
                    ");";
            stmt.execute(classesSql);

            // 3. Initialize Attendance Logs Table (Fixed bug from previous code)
            String logsSql = "CREATE TABLE IF NOT EXISTS attendance_logs (" +
                    "log_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "class_id INTEGER, " +
                    "student_roll_no TEXT, " +
                    "status TEXT, " +
                    "timestamp TEXT" +
                    ");";
            stmt.execute(logsSql);

            System.out.println("[DB] Database and all core tables initialized successfully!");

        } catch (SQLException e) {
            System.out.println("[DB ERROR] Error initializing database: " + e.getMessage());
        }
    }

    // Authenticate student login credentials for Phase 2
    public static boolean authenticateStudent(String rollNo, String password) {
        String sql = "SELECT * FROM students WHERE roll_no = ? AND password_hash = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, rollNo.trim());
            pstmt.setString(2, password.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
                // If a matching row is found, login is valid!
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("[DB ERROR] Authentication query failed: " + e.getMessage());
            return false;
        }
    }

    // Fetch and print all students from the database
    public static void getAllStudents() {
        String sql = "SELECT * FROM students";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- DATABASE RECORDS: STUDENTS TABLE ---");

            while (rs.next()) {
                int id = rs.getInt("id");
                String rollNo = rs.getString("roll_no");
                String name = rs.getString("name");
                String email = rs.getString("email");

                System.out.println("ID: " + id + " | Roll No: " + rollNo + " | Name: " + name + " | Email: " + email);
            }

        } catch (SQLException e) {
            System.out.println("Error querying database: " + e.getMessage());
        }
    }

    // Insert a new student record into the database
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

    // Update a student's attendance status (e.g., ABSENT to ON-DUTY override)
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

    // Calculate attendance percentages and check against the 75% threshold
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

                double percentage = 0.0;
                if (totalClasses > 0) {
                    percentage = ((double) presentClasses / totalClasses) * 100.0;
                }

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

    // Insert an attendance record into the logs table
    public static void addAttendanceLog(int classId, String studentRollNo, String status, String timestamp) {
        String sql = "INSERT INTO attendance_logs (class_id, student_roll_no, status, timestamp) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, classId);
            pstmt.setString(2, studentRollNo);
            pstmt.setString(3, status);
            pstmt.setString(4, timestamp);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error inserting attendance log: " + e.getMessage());
        }
    }
}
