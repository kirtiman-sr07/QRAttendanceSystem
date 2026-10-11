package com.qrattendance;

import com.qrattendance.db.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting QR Attendance System...");

        // Initialize the database and table
        DatabaseManager.initializeDatabase();
    }
}