package com.qrattendance;

import com.qrattendance.model.Student;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Create a dynamic list to store Student objects
        ArrayList<Student> studentList = new ArrayList<>();

        // Add 5 student records
        studentList.add(new Student(101, "Kirtiman", "kirtiman@example.com", 15));
        studentList.add(new Student(102, "Aarav", "aarav@example.com", 18));
        studentList.add(new Student(103, "Rin", "rin@example.com", 12));
        studentList.add(new Student(104, "Kenji", "kenji@example.com", 19));
        studentList.add(new Student(105, "Sora", "sora@example.com", 14));

        System.out.println("=================================================");
        System.out.println("   QR ATTENDANCE SYSTEM - REGISTERED STUDENTS   ");
        System.out.println("=================================================");

        // Enhanced for-each loop to print details of all 5 students
        for (Student student : studentList) {
            student.displayStudentInfo();
        }

        System.out.println("=================================================");
        System.out.println("Total Registered Students: " + studentList.size());
    }
}