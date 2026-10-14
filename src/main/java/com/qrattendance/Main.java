package com.qrattendance;

import com.qrattendance.db.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting QR Attendance System - Week 3...");

        // 1. Initialize existing tables
        DatabaseManager.initializeDatabase();

        // 2. Initialize the new classes table
        DatabaseManager.initializeClassesTable();
    }
}