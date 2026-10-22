package com.qrattendance;

import com.qrattendance.db.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting QR Attendance System - Week 3 Final Deliverable...\n");

        // 1. Ensure all tables exist
        DatabaseManager.initializeDatabase();
        DatabaseManager.initializeClassesTable();
        DatabaseManager.initializeAttendanceLogsTable();

        // 2. Insert 10 dummy attendance records for 3 students (Roll Nos: 101, 102, 103)
        // Student 101: 4 classes, 4 present -> 100%
        DatabaseManager.addAttendanceLog(1, 101, "PRESENT", "2026-10-18 09:00");
        DatabaseManager.addAttendanceLog(2, 101, "PRESENT", "2026-10-18 10:00");
        DatabaseManager.addAttendanceLog(3, 101, "PRESENT", "2026-10-18 11:00");
        DatabaseManager.addAttendanceLog(4, 101, "PRESENT", "2026-10-18 12:00");

        // Student 102: 4 classes, 3 present, 1 absent -> 75%
        DatabaseManager.addAttendanceLog(1, 102, "PRESENT", "2026-10-18 09:00");
        DatabaseManager.addAttendanceLog(2, 102, "PRESENT", "2026-10-18 10:00");
        DatabaseManager.addAttendanceLog(3, 102, "ABSENT",  "2026-10-18 11:00");
        DatabaseManager.addAttendanceLog(4, 102, "PRESENT", "2026-10-18 12:00");

        // Student 103: 2 classes, 1 present, 1 absent -> 50% (Below 75% warning!)
        DatabaseManager.addAttendanceLog(1, 103, "PRESENT", "2026-10-18 09:00");
        DatabaseManager.addAttendanceLog(2, 103, "ABSENT",  "2026-10-18 10:00");

        System.out.println("Inserted 10 test attendance logs successfully.");

        // 3. Run percentage calculations and output warnings
        DatabaseManager.checkAttendancePercentages();
    }
}
