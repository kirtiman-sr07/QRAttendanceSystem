package com.qrattendance;

import com.qrattendance.db.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting QR Attendance System...");

        // 1. Initialize the database and table
        DatabaseManager.initializeDatabase();

        // 2. Query and display all students from the database
        DatabaseManager.getAllStudents();
    }
}