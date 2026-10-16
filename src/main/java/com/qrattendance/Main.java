package com.qrattendance;

import com.qrattendance.db.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting QR Attendance System - Week 3 Day 3...");

        // 1. Ensure all tables exist
        DatabaseManager.initializeDatabase();
        DatabaseManager.initializeClassesTable();
        DatabaseManager.initializeAttendanceLogsTable();

        // 2. Test an HOD Override update (e.g., student 101, class 1 -> ON-DUTY)
        DatabaseManager.updateAttendanceStatus(101, 1, "ON-DUTY");
    }
}
