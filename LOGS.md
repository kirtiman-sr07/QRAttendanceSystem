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

### Day 24 - Dynamic Anti-Proxy Payload Generation
- **Learned**: Utilizing Java's `LocalDateTime` and `DateTimeFormatter` to append precise temporal signatures to data payloads for security verification.
- **Implemented**: Integrated real-time timestamps with subject codes and secret words, passing the concatenated payload directly into `QRCodeGenerator`.
- **Status**: Tested and verified dynamic PNG generation containing timestamped anti-proxy signatures.

### Day 25 - Live JavaFX QR Code Rendering
- **Learned**: Utilizing JavaFX `Image` and `ImageView` components to dynamically load and display local image assets within desktop UI layouts.
- **Implemented**: Integrated `ImageView` into `ClassSessionWindow.java`, allowing teachers to generate and project live timestamped QR codes instantly with a single click.
- **Status**: Verified complete end-of-week deliverable via desktop execution and live mobile scanning.

### Week 5 Summary & Milestone Completion: Dynamic Anti-Proxy QR Generation Engine
- **Overview**: Successfully engineered and integrated a cryptographic, dynamic QR code generation engine embedded directly within the JavaFX desktop teacher portal, solving the core anti-proxy attendance requirement.
- **Key Technical Accomplishments**:
  - Integrated Google ZXing (`core` and `javase`) dependencies via Maven to handle matrix barcode encoding.
  - Developed the core rendering logic (`QRCodeGenerator.java`) utilizing `QRCodeWriter` and `MatrixToImageWriter` to convert raw text strings into high-resolution PNG QR images.
  - Developed the teacher desktop interface (`ClassSessionWindow.java`) featuring dedicated input fields for subject codes and dynamic secret words (`coffee99`) with validation guard clauses.
  - Engineered a secure anti-proxy payload format combining `Subject + SecretWord + Timestamp` using Java's `LocalDateTime` and `DateTimeFormatter`.
  - Integrated JavaFX `Image` and `ImageView` components to instantly load and project the live, timestamped QR code onto the desktop screen for mobile scanning.
- **Status**: Week 5 successfully concluded, tested, and verified via end-to-end execution.

### Day 26 - JavaFX Timeline & Thread Safety
- **Learned**: The architectural differences between backend background schedulers (`ScheduledExecutorService`) and UI-bound timers (`Timeline`), focusing on JavaFX Application Thread safety.
- **Implemented**: Researched and structured `Timeline` and `KeyFrame` syntax for 15-second repeating intervals.
- **Status**: Concept understood, prepared for integration into `ClassSessionWindow.java`.

### Day 27 - Automated 15-Second QR Refresh Loop
- **Learned**: Encapsulating UI refresh logic into reusable `Runnable` blocks and binding them to repeating JavaFX `Timeline` intervals.
- **Implemented**: Upgraded `ClassSessionWindow.java` to automatically re-generate payloads, update physical PNGs, and refresh `ImageView` contents every 15 seconds upon session launch.
- **Status**: Tested and verified live automated rotation successfully.

### Day 28 - Flicker-Free Stream-Based Image Loading
- **Learned**: Overcoming JavaFX aggressive URL string caching ("Ghost Cache") by utilizing `FileInputStream` data streams for dynamic asset reloading.
- **Implemented**: Upgraded `ClassSessionWindow.java` refresh loop to stream PNG file updates directly into memory, ensuring smooth, flicker-free rendering.
- **Status**: Tested and verified seamless periodic UI updates without memory caching bottlenecks.

### Day 29 - Visual 1-Second Countdown Timer UI
- **Learned**: Synchronizing visual text updates with backend state cycles using a unified 1-second JavaFX `Timeline` decrement loop.
- **Implemented**: Added `countdownLabel` to `ClassSessionWindow.java` tracking real-time expiration ticks down to zero prior to automated QR refreshes.
- **Status**: Tested and verified synchronized visual countdown alongside seamless image rotation.

### Day 30 (Week 6, Day 5) - Production Memory Stability & Stress Testing
- **Learned**: Monitoring JVM heap memory behavior, Garbage Collection "sawtooth" cycles, and preventing resource leaks during continuous UI asset reloading.
- **Implemented**: Conducted a continuous 5-minute stress test of the automated 15-second refresh loop and countdown timer under live runtime conditions.
- **Status**: Verified complete memory stability, zero resource leaks, and seamless multi-cycle UI performance.


### Week 6 Summary & Milestone Completion: 15-Second Dynamic QR Auto-Refresh
- **Overview**: Successfully engineered and integrated an automated, time-based cryptographic rotation engine into the JavaFX desktop teacher portal, eliminating static screenshot exploitation and ensuring robust anti-proxy concurrency.
- **Key Technical Accomplishments**:
  - Implemented event-driven UI scheduling using JavaFX `Timeline` and `KeyFrame` execution loops.
  - Upgraded the background engine to automatically recalculate timestamps, regenerate PNG matrices, and refresh payloads every 15 seconds.
  - Resolved aggressive JavaFX URL string caching ("Ghost Cache") by engineering stream-based `FileInputStream` asset loading for flicker-free rendering.
  - Synchronized a real-time, visual 1-second decrementing `countdownLabel` with the background rotation cycle to provide transparent classroom feedback.
  - Conducted rigorous 5-minute memory stress testing, confirming stable JVM heap consumption and clean Garbage Collection performance.
- **Status**: Week 6 successfully concluded, tested, and verified via end-to-end execution.

### Day 31 - Integrating Javalin Web Server Framework
- **Learned**: The architecture of embedded lightweight HTTP servers (Javalin/Jetty) and how desktop applications can host local micro-services for network communication.
- **Implemented**: Added Javalin (`io.javalin`) and SLF4J logger dependencies to `pom.xml` to prepare for local Wi-Fi mobile request handling.
- **Status**: Library dependencies successfully downloaded and synced via Maven.

### Day 32 - Booting Embedded Javalin HTTP Server
- **Learned**: Managing multi-threaded application lifecycles where a background embedded server (`Javalin`) runs concurrently with a JavaFX desktop user interface.
- **Implemented**: Created `WebServer.java` to handle server boot/shutdown routines on port 8080 and integrated startup/stop hooks into `ClassSessionWindow.java`.
- **Status**: Tested and verified local HTTP endpoint response via `http://localhost:8080/scan`.

### Day 33 - Serving Responsive Mobile HTML via Javalin
- **Learned**: Constructing dynamic HTML string responses and utilizing mobile viewport meta tags to render responsive web pages from an embedded Java server.
- **Implemented**: Upgraded the `/scan` GET route in `WebServer.java` to serve a styled, card-based mobile interface using Javalin's `ctx.html()` method.
- **Status**: Tested and verified rendered HTML layout successfully in browser.

### Day 34 - Building Mobile Check-In Form and POST Route Handler
- **Learned**: Handling HTTP POST form submissions (`ctx.formParam()`) in Javalin and structuring mobile-responsive input forms.
- **Implemented**: Added input fields for `rollNumber` and `qrToken` inside the HTML UI on `/scan` and created a companion `POST /submit-attendance` endpoint in `WebServer.java`.
- **Status**: Tested and verified end-to-end form rendering and request parameter extraction in browser.

### Day 35 - Local Wi-Fi Mobile-to-Desktop Integration & Testing
- **Learned**: Local network routing, binding embedded servers to host network interfaces, and cross-device HTTP packet transmission over Wi-Fi subnets.
- **Implemented**: Connected smartphone browser to laptop embedded Javalin server via local IP address, successfully transmitting form data (`POST`) into the running Java desktop runtime.
- **Status**: Tested and verified end-to-end mobile check-in data flow successfully.

### Week 7 Summary & Milestone Completion: Embedded Lightweight Web Server for Mobile Scans
- **Overview**: Successfully embedded a high-performance lightweight HTTP server (`Javalin`) directly inside the JavaFX desktop application lifecycle, enabling real-time student mobile browser communication over local Wi-Fi.
- **Key Technical Accomplishments**:
  - Integrated Javalin and SLF4J dependencies via Maven for embedded micro-service hosting.
  - Engineered `WebServer.java` with thread-safe start and stop routines synchronized with desktop application window lifecycles.
  - Served responsive, mobile-optimized HTML check-in interfaces complete with viewport scaling and structured CSS card layouts.
  - Built robust `GET /scan` rendering and `POST /submit-attendance` parameter extraction handlers.
  - Performed rigorous local Wi-Fi network testing, validating seamless mobile-to-desktop data payload delivery.
- **Status**: Week 7 successfully concluded, tested, and verified via end-to-end execution.

## Month 3: Web Server, Mobile Check-in & Attendance Logic

### Week 8: Token Validation & Thread Safety (Concurrent Scans)
- **Day 36 (Week 8, Day 1)**: Created `TokenValidator.java` to verify cryptographic time-hashes against a 30-second temporal tolerance window and updated `WebServer.java` with active session credential bindings.
- **Status**: Tested and verified token parsing, expiration checking, and rejection logic.

### Day 37 - (Week 8, Day 2): Enforcing 30-Second Token Expiration Window
- **Learned**: Calculating temporal delta using `ChronoUnit.SECONDS.between()` to invalidate outdated cryptographic payloads.
- **Implemented**: Updated `TokenValidator.java` to strictly reject QR tokens older than 30 seconds, preventing stale screenshot cheating.
- **Status**: Tested and verified expiration logic via terminal logs.

### Day 38 - Phase 2 Step 1: Student Authentication Database Setup
- **Learned**: Designing persistent authentication schemas in SQLite using text-based unique roll numbers and password hashing fields.
- **Implemented**: Upgraded `DatabaseManager.java` to initialize the secure `students` table (`roll_no`, `name`, `email`, `password_hash`), seeded a default test student (`BCA2026001`), added `authenticateStudent()` query logic, and hooked database initialization into `ClassSessionWindow.java` startup.
- **Status**: Verified table creation and student account seeding successfully on app launch.

### Day 38 - Phase 2 & Phase 3.1: Student Authentication, Session Routing, & Dashboard
- **Learned**: Implementing cookie-based session tracking in Javalin to manage authenticated student states, and designing redirection routing (`/login` -> `/dashboard` -> `/scan`).
- **Implemented**: 
  - Upgraded `DatabaseManager.java` with overloaded `addStudent` methods to maintain backward compatibility between legacy code and the new alphanumeric `roll_no` / `password_hash` schema.
  - Added the secure `POST /login` authentication handler with SQLite query validation and secure session cookie issuance (`student_session`).
  - Created the **Student Dashboard (`GET /dashboard`)** homepage displaying profile validation badges and navigation to the live camera scanner.
- **Status**: Successfully verified end-to-end login flow, session tracking, and redirection to the student portal dashboard.

### Phase 3: Live Mobile Camera QR Scanning & End-to-End Tunnel Verification
- **Learned**:
  - Integrating `html5-qrcode` in a mobile web view to access the device rear camera (`facingMode: "environment"`) and auto-submit decoded payloads without manual input.
  - Why mobile browsers enforce HTTPS/secure origins for camera APIs (`getUserMedia`), and how reverse tunnels (`cloudflared`) bridge a local Javalin server (`localhost:8080`) to external mobile networks.
  - Balancing security and network latency using a 15-second visual QR rotation paired with a 30-second server-side token acceptance window.
- **Implemented**:
  - Replaced manual token input on `GET /scan` with an authenticated `html5-qrcode` live camera scanner and zero-touch form auto-submission to `POST /submit-attendance`.
  - Bound attendance logging directly to the server-side `student_session` cookie and removed raw payload exposure from the student confirmation UI.
  - Upgraded `ClassSessionWindow.java` with a masked `PasswordField` and `👁` visibility toggle button so the teacher's secret code word stays hidden during projection.
- **Status**: Phase 3 complete. Verified end-to-end smartphone camera scan over 4G via Cloudflare HTTPS tunnel (`Token age: 3 seconds`, `Valid: true`).

### Phase 4: Anti-Proxy Validation & Duplicate Prevention Engine
- **Learned**:
  - Enforcing data integrity at the database storage layer using composite `UNIQUE(class_id, student_roll_no)` constraints and unique indexes (`CREATE UNIQUE INDEX`) rather than relying solely on application-level checks.
  - How SQLite handles index creation on pre-populated tables (requiring deduplication of legacy test rows before applying unique indexes) and how to catch `SQLITE_CONSTRAINT_UNIQUE` exceptions cleanly in JDBC.
  - Designing dynamic session lifecycles (`Statement.RETURN_GENERATED_KEYS`) so every lecture generates an isolated `class_id` instead of hardcoding a static session.
- **Implemented**:
  - Upgraded `DatabaseManager.java` with `createClassSession(...)` to generate unique `class_id` entries per lecture, deduplicate legacy rows, and enforce `UNIQUE(class_id, student_roll_no)` on `attendance_logs`.
  - Introduced `DatabaseManager.LogStatus` (`SUCCESS`, `DUPLICATE`, `ERROR`) to classify database insertion outcomes.
  - Updated `WebServer.java` (`POST /submit-attendance`) and `ClassSessionWindow.java` to track `activeSessionId` and render a dedicated orange **"Already Checked In!"** rejection screen when duplicate scans are blocked.
- **Status**: Phase 4 complete. Verified temporal expiration (30s window), same-session duplicate blocking (`Session ID: 7` & `8`), and automatic reset when launching a new class session.

### Phase 5: Teacher AI Feature / Smart Class Analytics Assistant
- **Learned**:
  - Translating natural-language teacher queries (attendance shortages, turnout summaries, and student roll-number lookups) into parameterized and aggregated SQLite SQL queries.
  - Designing reliable, offline-first analytics inside a JavaFX desktop portal so live demos and classroom queries run instantaneously without external API dependencies.
- **Implemented**:
  - Created `ClassAnalyticsAssistant.java` (`com.qrattendance.analytics`) to query `students`, `classes`, and `attendance_logs` in `attendance.db` and return formatted reports for threshold shortages (default `<75%`), session/daily turnout, and individual student records.
  - Added the **Smart Class Analytics Assistant** panel to `ClassSessionWindow.java` with query input, Enter-key and button triggers, quick-prompt buttons (`Below 75%`, `Today's Turnout`), and a read-only output `TextArea`.
- **Status**: Phase 5 complete. Verified threshold check, turnout summary, and student roll-number lookup in the live JavaFX portal.

### Phase 6: HOD / Admin Panel & Attendance Overrides
- **Learned**:
  - Wrapping multi-table state changes (`UPDATE`/`INSERT` on `attendance_logs` plus `INSERT` into `attendance_audit_log`) in a single JDBC transaction (`setAutoCommit(false)` / `commit()`) so status overrides and audit records stay consistent.
  - Building filterable JavaFX `TableView` interfaces backed by parameterized SQLite queries (`LIKE` filters on date and subject) with mandatory reason tracking for administrative accountability.
- **Implemented**:
  - Added the `attendance_audit_log` schema and `DatabaseManager.overrideAttendanceStatus(...)` transaction method in `DatabaseManager.java`.
  - Created `HodAdminWindow.java` with HOD authentication, date/subject filtering, row click-to-fill override controls (`ON-DUTY`, `PRESENT`, `ABSENT`), mandatory reason validation, and a live **Audit Trail History** tab.
  - Added an **HOD / Admin Portal** launcher button to `ClassSessionWindow.java`.
- **Status**: Phase 6 complete. Verified HOD login, subject/date filtering, manual status override, and persistent audit log entry.

### Session Checkpoint: HOD Override & Audit Trail Verification
- **What Changed**:
  - Verified HOD sign-in and multi-column attendance log viewing across sessions in `HodAdminWindow.java`.
  - Tested filtering attendance records by date (`2026-11-18`) and subject (`JAVA_101`), plus resetting filters cleanly.
  - Executed a live manual override on `BCA2026001` for Session `6` (`PRESENT` -> `ABSENT`, reason: `"Absent"`) via `DatabaseManager.overrideAttendanceStatus(...)` and confirmed the persisted record in the **Audit Trail History** tab.
- **Takeaway**:
  - Clicking a table row to auto-fill `Session ID` and `Roll No` speeds up manual corrections, while requiring a non-empty reason keeps every override traceable in SQLite.

### Session Checkpoint: CSS Styling & UI Polish Pass (Nov 22, 2026)
- **What Changed**:
  - Created `src/main/resources/styles/app.css` and wired a card-based JavaFX layout (`ScrollPane`, top bar, session card, and analytics card) in `ClassSessionWindow.java`[cite: 11, 14, 15, 16].
  - Tested live session startup (`Session #10`, subject `java`), 15-second refresh countdown, **Copy Student URL**, **Ask AI** quick actions, and **HOD / Admin** login flow[cite: 14, 15, 16, 18].
- **Issues Spotted for Next Pass**:
  - Clicking **Copy Student URL** overwrote the active session status message (`Session #10 active...`) instead of keeping both visible[cite: 14, 15, 16].
  - Raw session numbering (`#10`) needs clearer daily/period context, HOD login shows a plain hint, and user/credential creation (teachers/students) plus a GitHub-square style student home page are still needed.

### Session Checkpoint: Non-Destructive Student URL Copy (Nov 23, 2026)
- **What Changed**:
  - Added a dedicated `urlNoticeLabel` in `ClassSessionWindow.java` so clicking **Copy Student URL** copies `http://localhost:8080/login` without overwriting the active class session status banner.

### Session Checkpoint: Role Launcher Flow & Back Navigation Note (Nov 24, 2026)
- **What Changed**:
  - Designed `RoleLauncherWindow.java` as a single entry screen offering **Teacher Session Portal**, **HOD / Admin Portal**, and **Student Web Portal (Copy Link)** with shared `app.css` styling.
- **Note for Next Pass**:
  - Add a **Back** button / step-back action on sub-views so users can return to the previous screen instead of relying on closing windows or manual clicks by mistake.

### Session Checkpoint: Smarter Session & Period Labeling (Nov 25, 2026)
- **What Changed**:
  - Added `DatabaseManager.getDailySessionNumber(String datePrefix)` in `DatabaseManager.java` to count class sessions created on a given date (`yyyy-MM-dd`) via `date_time`.
  - Updated `ClassSessionWindow.java` so active session status banners display `yyyy-MM-dd • Period #N (Session #ID • SUBJECT)` instead of a bare running session number.
  - Updated `HodAdminWindow.java` (`AttendanceRow`, `fetchAttendanceRows`, and the `TableView`) with a **Date / Session** column (`yyyy-MM-dd • #ID`) while preserving exact numeric `classId` selection for overrides.
- **Status**: Priority 2 complete; ready to move on to Priority 3 (HOD login cleanup & teacher/student credential manager).

### Session Checkpoint: Clean HOD Login & User/Credential Manager (Nov 26, 2026)
- **What Changed**:
  - Added the `teachers` table (with default seeded `TCH001`) plus `registerStudent(...)`, `registerTeacher(...)`, and `authenticateTeacher(...)` in `DatabaseManager.java`.
  - Cleaned up `HodAdminWindow.openLoginWindow()` to remove exposed login credential hints and added the **Manage Users & Credentials** tab for creating student and teacher accounts directly from the HOD dashboard.
- **Next Session**:
  - Add a live directory view of registered students/teachers in the HOD tab and connect teacher login authentication before starting Priority 4.

### Session Checkpoint: Official-Grade Analytics & Action Triggers (Nov 27, 2026)
- **What Changed**:
  - Updated `ClassAnalyticsAssistant.java` to produce privacy-aware official summaries (aggregate turnout, eligibility compliance, masked roll numbers in shortage reports, and subject-wise breakdown) while still supporting explicit roll-number lookups when queried directly.
  - Updated `ClassSessionWindow.java` action chips to **Today's Turnout**, **Eligibility Summary**, **Below 75%**, and **Subject Breakdown**.
- **Next Session**:
  - Start Priority 5: Student Home Page with a GitHub-style attendance square grid and student insights/AI prompts.
