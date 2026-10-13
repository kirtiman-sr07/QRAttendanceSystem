package com.qrattendance;

import com.qrattendance.db.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting QR Attendance System...");

        // 1. Initialize the database and table
        DatabaseManager.initializeDatabase();

        // 2. Insert a new student record programmatically
        DatabaseManager.addStudent(101, "Kirtiman", "kirtiman@example.com");

        // 3. Query and display all students to verify insertion
        DatabaseManager.getAllStudents();
    }
}