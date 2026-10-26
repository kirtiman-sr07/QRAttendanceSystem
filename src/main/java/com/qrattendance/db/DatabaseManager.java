package com.qrattendance.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class DatabaseManager {
    // 1. The connection string telling SQLite what file to open/create
    private static final String URL = "jdbc:sqlite:attendance.db";

    public static void initializeDatabase() {
        // 2. Open a connection and create a statement box
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            // 3. The SQL command we learned yesterday
            String sql = "CREATE TABLE IF NOT EXISTS students (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "roll_no INTEGER, " +
                    "name TEXT, " +
                    "email TEXT)";

            // 4. Execute the SQL command on the database
            stmt.execute(sql);
            System.out.println("Database 'attendance.db' and 'students' table initialized successfully!");

        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    // Fetch and print all students from the database
    public static void getAllStudents() {
        String sql = "SELECT * FROM students";

        // 1. Open connection and execute query
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- DATABASE RECORDS: STUDENTS TABLE ---");

            // 2. Loop through the ResultSet row by row
            while (rs.next()) {
                int id = rs.getInt("id");
                int rollNo = rs.getInt("roll_no");
                String name = rs.getString("name");
                String email = rs.getString("email");

                // Print each row neatly to the terminal
                System.out.println("ID: " + id + " | Roll No: " + rollNo + " | Name: " + name + " | Email: " + email);
            }

        } catch (SQLException e) {
            System.out.println("Error querying database: " + e.getMessage());
        }
    }

    // Insert a new student record into the database
    public static void addStudent(int rollNo, String name, String email) {
        String sql = "INSERT INTO students (roll_no, name, email) VALUES (?, ?, ?)";

        // 1. Open connection and create a PreparedStatement with question-mark placeholders
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 2. Safely bind the values to the placeholders (1st ?, 2nd ?, 3rd ?)
            pstmt.setInt(1, rollNo);
            pstmt.setString(2, name);
            pstmt.setString(3, email);

            // 3. Execute the insert command
            pstmt.executeUpdate();
            System.out.println("Successfully added student: " + name);

        } catch (SQLException e) {
            System.out.println("Error inserting student: " + e.getMessage());
        }
    }

    // Initialize the classes table
    public static void initializeClassesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS classes (" +
                "class_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "subject_name TEXT, " +
                "teacher_name TEXT, " +
                "date_time TEXT)";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Table 'classes' initialized successfully!");

        } catch (SQLException e) {
            System.out.println("Error creating classes table: " + e.getMessage());
        }
    }

    // Initialize the attendance_logs table
    public static void initializeAttendanceLogsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "roll_no INTEGER UNIQUE," +
                "name TEXT," +
                "email TEXT)";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Table 'attendance_logs' initialized successfully!");

        } catch (SQLException e) {
            System.out.println("Error creating attendance_logs table: " + e.getMessage());
        }
    }

    // Update a student's attendance status (e.g., ABSENT to ON-DUTY override)
    public static void updateAttendanceStatus(int rollNo, int classId, String newStatus) {
        String sql = "UPDATE attendance_logs SET status = ? WHERE student_roll_no = ? AND class_id = ?";

        // 1. Open connection and create a secure PreparedStatement
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 2. Bind the parameters safely
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, rollNo);
            pstmt.setInt(3, classId);

            // 3. Execute the update
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
                int rollNo = rs.getInt("student_roll_no");
                int totalClasses = rs.getInt("total_classes");
                int presentClasses = rs.getInt("present_classes");

                // Calculate percentage (cast to double to avoid integer division rounding bugs)
                double percentage = 0.0;
                if (totalClasses > 0) {
                    percentage = ((double) presentClasses / totalClasses) * 100.0;
                }

                System.out.println("Roll No: " + rollNo + " | Attended: " + presentClasses + "/" + totalClasses +
                        " | Percentage: " + String.format("%.2f", percentage) + "%");

                // Check 75% university rule
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

    // Insert a dummy attendance record into the logs table
    public static void addAttendanceLog(int classId, int studentRollNo, String status, String timestamp) {
        String sql = "INSERT INTO attendance_logs (class_id, student_roll_no, status, timestamp) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, classId);
            pstmt.setInt(2, studentRollNo);
            pstmt.setString(3, status);
            pstmt.setString(4, timestamp);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error inserting attendance log: " + e.getMessage());
        }
    }
}
