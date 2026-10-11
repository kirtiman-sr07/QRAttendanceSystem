package com.qrattendance.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
}