package com.qrattendance;

public class Main {
    public static void main(String[] args) {
        // Student 1 Data Variables
        String studentName = "Kirtiman";
        int rollNumber = 101;
        String email = "kirtiman@example.com";
        double attendancePercentage = 88.5;
        boolean isDefaulter = attendancePercentage < 75.0;

        // Displaying Student Profile to Console
        System.out.println("==========================================");
        System.out.println("   QR ATTENDANCE SYSTEM - STUDENT PROFILE ");
        System.out.println("==========================================");
        System.out.println("Name         : " + studentName);
        System.out.println("Roll Number  : " + rollNumber);
        System.out.println("Email        : " + email);
        System.out.println("Attendance   : " + attendancePercentage + "%");
        System.out.println("Defaulter    : " + (isDefaulter ? "YES (Alert Sent)" : "NO (Eligible)"));
        System.out.println("==========================================");
    }
}