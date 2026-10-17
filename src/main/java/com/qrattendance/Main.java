package com.qrattendance;

import com.qrattendance.db.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting QR Attendance System - Week 3 Day 4...");

        // 1. Ensure all tables exist
        DatabaseManager.initializeDatabase();
        DatabaseManager.initializeClassesTable();
        DatabaseManager.initializeAttendanceLogsTable();

        // 2. Test attendance percentage report
        DatabaseManager.checkAttendancePercentages();
    }
}