package com.qrattendance;

public class Main {
    public static void main(String[] args) {
        String studentName = "Kirtiman";
        int totalClasses = 20;
        int attendedClasses = 14;

        // Calculate attendance percentage
        double percentage = ((double) attendedClasses / totalClasses) * 100;

        System.out.println("==========================================");
        System.out.println("   ATTENDANCE EVALUATION - " + studentName);
        System.out.println("==========================================");
        System.out.println("Classes Attended : " + attendedClasses + "/" + totalClasses);
        System.out.println("Current Percentage: " + String.format("%.2f", percentage) + "%");

        // 1. Conditional Logic: Defaulter Check
        if (percentage >= 75.0) {
            System.out.println("Status            : ELIGIBLE (Safe)");
        } else if (percentage >= 60.0) {
            System.out.println("Status            : WARNING (Close to Defaulter Limit)");
        } else {
            System.out.println("Status            : DEFAULTER (Alert Triggered)");
        }

        // 2. Loop Logic: Simulating 5-Day QR Attendance Log
        System.out.println("\n--- 5-Day QR Scan Simulation ---");
        for (int day = 1; day <= 5; day++) {
            System.out.println("Day " + day + ": QR Code scanned successfully at 09:00 AM.");
        }
    }
}