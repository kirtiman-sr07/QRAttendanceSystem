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
        // 2. Open a connection and create a statement statement box
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
}