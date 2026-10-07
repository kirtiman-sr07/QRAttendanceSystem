package com.qrattendance.model;

public class Student {
    // 1. Private Fields (Encapsulation)
    private int rollNumber;
    private String name;
    private String email;
    private int attendanceCount;

    // 2. Constructor
    public Student(int rollNumber, String name, String email, int attendanceCount) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.email = email;
        this.attendanceCount = attendanceCount;
    }

    // 3. Getters and Setters
    public int getRollNumber() {
        return rollNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAttendanceCount() {
        return attendanceCount;
    }

    public void markPresent() {
        this.attendanceCount++;
    }

    // 4. Helper Method to Print Details
    public void displayStudentInfo() {
        System.out.println("ID: " + rollNumber + " | Name: " + name + " | Email: " + email + " | Attendance: " + attendanceCount + " classes");
    }
}