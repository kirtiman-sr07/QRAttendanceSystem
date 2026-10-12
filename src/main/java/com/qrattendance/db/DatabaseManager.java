package com.qrattendance.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

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
}