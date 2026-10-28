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

### Week 1 Summary & Milestone Completion: Core Java & OOP Fundamentals
- **Overview**: Established the core project architecture and mastered foundational object-oriented programming concepts in Java.
- **Key Technical Accomplishments**:
  - Initialized a structured Maven project layout using JDK 25 and configured version control (`.gitignore`).
  - Practiced primitive data types, reference handling, conditional control flows, and iterative loops.
  - Designed and implemented the `Student` class encapsulating private attributes and object constructors.
  - Harnessed Java collections (`ArrayList`) and enhanced `for-each` loops to manage dynamic sets of student records.
- **Status**: Week 1 core functionality successfully verified and completed.


## Week 2: SQLite Database & JDBC Integration

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

### Week 2 Summary & Milestone Completion: SQLite Database & JDBC Integration
- **Overview**: Integrated local relational data storage into the application using SQLite and the JDBC API.
- **Key Technical Accomplishments**:
  - Configured and verified the `sqlite-jdbc` dependency via Maven for local database connectivity.
  - Mastered foundational SQL commands (`CREATE TABLE`, `INSERT INTO`, `SELECT`).
  - Engineered `DatabaseManager.java` to automate database and table initialization (`attendance.db`, `students`).
  - Implemented secure data retrieval using `ResultSet` iteration and safe programmatic insertion via parameter-bound `PreparedStatement`.
- **Status**: Week 2 database persistence pipeline fully achieved and verified.


## Week 3: Relational Architecture & Attendance Analytics

### Day 11 - Multi-Table Database Design (`classes`)
- **Learned**: Relational database design concepts and structuring secondary tables.
- **Implemented**: Added `initializeClassesTable()` method in `DatabaseManager.java` to create the `classes` table in `attendance.db`.
- **Status**: Code compiled and executed successfully.

### Day 12 - Relational Database Bridge (`attendance_logs`)
- **Learned**: How relational mapping tables connect primary entities (`students` and `classes`) using foreign key references (`student_roll_no` and `class_id`).
- **Implemented**: Added `initializeAttendanceLogsTable()` method in `DatabaseManager.java` to create the `attendance_logs` table in `attendance.db`.
- **Status**: Code compiled and executed successfully.

### Day 13 - SQL UPDATE Queries & HOD Overrides
- **Learned**: How to modify existing database records using SQL `UPDATE` combined with `WHERE` clauses and `PreparedStatement`.
- **Implemented**: Added `updateAttendanceStatus()` method in `DatabaseManager.java` to handle status changes (e.g., `ABSENT` to `ON-DUTY`).
- **Status**: Code compiled and executed successfully.

### Day 14 - SQL Aggregate Functions & Percentage Logic
- **Learned**: How to use SQL aggregate functions (`COUNT()`, `SUM()`, `GROUP BY`) and handle division casting in Java.
- **Implemented**: Added `checkAttendancePercentages()` method in `DatabaseManager.java` to calculate attendance ratios and flag students below 75%.
- **Status**: Code compiled and executed successfully.

### Day 15 - End-of-Week Deliverable: Attendance Percentages & Threshold Warnings
- **Learned**: End-to-end integration of multi-table insertion, aggregate SQL queries, and conditional threshold alerts.
- **Implemented**: Added `addAttendanceLog()` method and ran a complete test script in `Main.java` processing 10 attendance logs across 3 students.
- **Status**: Week 3 deliverable fully achieved and verified in terminal output.

### Week 3 Summary & Milestone Completion: Relational Architecture & Attendance Analytics
- **Overview**: Expanded the database schema into a multi-table relational architecture and implemented business logic for attendance tracking and reporting.
- **Key Technical Accomplishments**:
  - Designed secondary relational tables (`classes` and `attendance_logs`) linked via foreign key references.
  - Implemented database modification queries (`UPDATE`) with `WHERE` clauses to handle administrative overrides (e.g., HOD status changes).
  - Harnessed advanced SQL aggregate functions (`COUNT`, `SUM`, `GROUP BY`) alongside Java division casting.
  - Built threshold alert logic to automatically calculate attendance ratios and flag students falling below critical requirements.
- **Status**: Week 3 backend analytics deliverable successfully validated.


## Week 4: Desktop Interface & Database Integration

### Day 16 - First JavaFX GUI Window
- **Learned**: Configuring JavaFX dependencies and the Maven `javafx-maven-plugin` to bypass module restrictions.
- **Implemented**: Created `HelloJavaFX.java` and successfully launched the first desktop application window.
- **Status**: GUI environment operational.

### Day 17 - Teacher Login Desktop Window
- **Learned**: Implementing interactive JavaFX controls (`TextField`, `PasswordField`, `Button`) and event-driven action handlers (`setOnAction`).
- **Implemented**: Created `TeacherLogin.java` with credential validation logic and styled layout padding.
- **Status**: Tested and verified via `mvn javafx:run`.

### Day 18 - Student Registration Window UI
- **Learned**: Designing multi-field data input forms using JavaFX text controls and layout containers.
- **Implemented**: Created `StudentRegistration.java` with text fields for name, roll number, and email, alongside validation logic.
- **Status**: Tested and verified via `mvn javafx:run`.

### Day 19 - Connecting JavaFX UI to SQLite Backend
- **Learned**: Event-driven data binding, parsing text inputs to numeric types safely with `NumberFormatException`, and invoking backend database logic from a UI controller.
- **Implemented**: Connected the "Save Student" button in `StudentRegistration.java` to invoke `DatabaseManager.addStudent()`.
- **Status**: Tested and verified successfully via `mvn javafx:run`.

### Day 20 - End-to-End GUI to Database Integration & Audit
- **Learned**: End-to-end verification of GUI input mapping to SQLite persistent storage.
- **Implemented**: Connected JavaFX `StudentRegistration` form to `DatabaseManager.addStudent()` and verified records using the terminal SQLite CLI.
- **Status**: Week 4 Deliverable Complete!

### Week 4 Summary & Milestone Completion: GUI Architecture & Database Persistence
- **Overview**: Successfully engineered and integrated the desktop user interface layer using JavaFX with persistent data storage via SQLite (`attendance.db`).
- **Key Technical Accomplishments**:
  - Configured Maven runtime plugins and JavaFX module dependencies (`javafx-controls`, `javafx-fxml`).
  - Developed functional desktop views (`TeacherLogin`, `StudentRegistration`) using standard layout containers (`VBox`).
  - Implemented robust input validation, exception handling (`NumberFormatException`), and early exit guard clauses.
  - Established secure backend data binding via `DatabaseManager.addStudent()` utilizing `PreparedStatement` to safely persist records.
  - Conducted end-to-end verification and data integrity checks via the SQLite command-line interface.
- **Status**: Week 4 successfully concluded and verified.

### Day 21 - Integrating ZXing Barcode Library
- **Learned**: The role of the ZXing library in encoding string data into graphical 2D matrix barcodes.
- **Implemented**: Added Google ZXing `core` and `javase` dependencies to `pom.xml`.
- **Status**: Library downloaded and synced successfully via Maven.

### Day 22 - Core QR Code Generation Function
- **Learned**: How ZXing's `QRCodeWriter` and `BitMatrix` convert text into a 2D matrix, rendered via `MatrixToImageWriter`.
- **Implemented**: Created `QRCodeGenerator.java` in `com.qrattendance.security` with a reusable method to output PNG QR images.
- **Status**: Tested and verified physical PNG generation via test execution.

### Day 23 - Secret Code Word Input UI
- **Learned**: Designing interactive JavaFX layout containers (`VBox`) equipped with multiple text inputs and event-driven guard clauses.
- **Implemented**: Created `ClassSessionWindow.java` featuring input fields for subject codes and dynamic secret code words.
- **Status**: Tested and verified user input capture and validation feedback successfully.
