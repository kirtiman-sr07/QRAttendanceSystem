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

### Day 6 - SQLite & JDBC Driver Verification
- **Verified**: Confirmed `sqlite-jdbc` dependency is active in `pom.xml` and loaded via Maven.
- **Status**: Project configuration ready for local database creation.

### Day 7 - SQL Fundamentals
- **Learned**: Core SQL concepts (Tables, Columns, Rows) and basic commands (`CREATE TABLE`, `INSERT INTO`, `SELECT * FROM`).
- **Status**: Database query theory completed.

### Day 8 - SQLite Database & Table Creation
- **Learned**: JDBC connection strings (`jdbc:sqlite:...`), `Connection`, `Statement`, and error handling via `try-catch`.
- **Implemented**: Created `DatabaseManager.java` in `com.qrattendance.db` to automatically generate `attendance.db` and the `students` table on startup.
- **Status**: Code compiled and executed successfully, `attendance.db` file generated.

### Day 9 - JDBC Querying & ResultSet
- **Learned**: How to execute `SELECT` queries using `Statement.executeQuery()` and iterate through rows using `ResultSet`.
- **Implemented**: Added `getAllStudents()` method in `DatabaseManager.java` to read data from `attendance.db`.
- **Status**: Code compiled and executed successfully.

### Day 10 - Programmatic Database Insertion
- **Learned**: Secure data manipulation using `PreparedStatement` and parameter binding (`pstmt.setInt`, `pstmt.setString`).
- **Implemented**: Added `addStudent()` method in `DatabaseManager.java` to programmatically insert student records into `attendance.db`.
- **Status**: End-of-week deliverable successfully met and verified via terminal output.

### Day 11 - Multi-Table Database Design (`classes`)
- **Learned**: Relational database design concepts and structuring secondary tables.
- **Implemented**: Added `initializeClassesTable()` method in `DatabaseManager.java` to create the `classes` table in `attendance.db`.
- **Status**: Code compiled and executed successfully.