package com.qrattendance;

import com.qrattendance.db.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting QR Attendance System - Week 3 Day 2...");

        // 1. Initialize existing tables
        DatabaseManager.initializeDatabase();
        DatabaseManager.initializeClassesTable();

        // 2. Initialize the new attendance logs table
        DatabaseManager.initializeAttendanceLogsTable();
    }
}