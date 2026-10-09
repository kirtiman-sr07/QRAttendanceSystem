# Project Daily Development Logs

## Week 1: Environment Setup & Core Basics

### Day 1 - Environment & Project Architecture
- **Implemented**: Created Maven project architecture in IntelliJ IDEA with JDK 25.
- **Configured**: `.gitignore`, package structure (`com.qrattendance.*`), dependencies, and project documentation.
- **Git**: Pushed initial setup to GitHub repository (`kirtiman-sr07/QRAttendanceSystem`).

### Day 2 - Java Variables & Data Types
- **Learned**: Primitive types (`int`, `double`, `boolean`), reference types (`String`), and console output formatting.
- **Implemented**: Created `Main.java` in `com.qrattendance` to model and print student profile attributes.
- **Status**: Code compiled and executed successfully via IntelliJ runner.

### Day 3 - Control Flow & Loops
- **Learned**: Conditional logic (`if`, `else if`, `else`) and iteration (`for` loops).
- **Implemented**: Logic for calculating attendance thresholds and simulating a 5-day QR scan log in `Main.java`.
- **Status**: Tested and output verified in console.

### Day 4 - Java Classes & Objects (OOP Basics)
- **Learned**: Object-Oriented Programming fundamentals (Classes, Objects, Constructors, Encapsulation).
- **Implemented**: Created `Student.java` inside `com.qrattendance.model` with private fields and methods. Tested instantiation in `Main.java`.
- **Status**: Code compiled and executed successfully.

### Day 5 - Java ArrayList & Iteration (Week 1 Deliverable)
- **Learned**: Dynamic arrays using `java.util.ArrayList` and enhanced `for-each` loop collection traversal.
- **Implemented**: Created a dynamic list storing 5 `Student` objects and printed their details in `Main.java`.
- **Deliverable**: Week 1 core functionality verified and completed.

### Day 6 (Week 2, Day 1) - SQLite & JDBC Driver Verification
- **Verified**: Confirmed `sqlite-jdbc` dependency is active in `pom.xml` and loaded via Maven.
- **Status**: Project configuration ready for local database creation.