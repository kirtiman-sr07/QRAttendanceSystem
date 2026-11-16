package com.qrattendance.server;

import io.javalin.Javalin;
import com.qrattendance.security.TokenValidator;
import com.qrattendance.db.DatabaseManager;

public class WebServer {
    private static Javalin app;
    private static String activeSubject = "";
    private static String activeSecretWord = "";
    private static boolean sessionActive = false;

    // Called by JavaFX controller when teacher starts class
    public static void setSessionCredentials(String subject, String secretWord) {
        activeSubject = subject;
        activeSecretWord = secretWord;
        sessionActive = true;
    }

    public static void endSession() {
        sessionActive = false;
        activeSubject = "";
        activeSecretWord = "";
    }

    // Start the embedded Javalin server on port 8080
    public static void startServer() {
        if (app != null) {
            return; // Prevent starting multiple instances if already running
        }

        app = Javalin.create().start(8080);

        // 1. Serve the Student Login Web Page (GET /login)
        app.get("/login", ctx -> {
            ctx.html(
                    "<!DOCTYPE html>" +
                            "<html lang='en'>" +
                            "<head>" +
                            "    <meta charset='UTF-8'>" +
                            "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                            "    <title>Student Login - QRAttendance</title>" +
                            "    <style>" +
                            "        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f7f6; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }" +
                            "        .card { background: white; padding: 30px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); width: 100%; max-width: 360px; box-sizing: border-box; }" +
                            "        h2 { color: #2c3e50; margin-top: 0; text-align: center; font-size: 22px; }" +
                            "        .subtitle { color: #7f8c8d; font-size: 13px; text-align: center; margin-bottom: 25px; }" +
                            "        .form-group { margin-bottom: 18px; }" +
                            "        label { display: block; margin-bottom: 6px; font-weight: 600; color: #34495e; font-size: 14px; }" +
                            "        input { width: 100%; padding: 10px 12px; border: 1px solid #dcdde1; border-radius: 6px; font-size: 14px; box-sizing: border-box; }" +
                            "        input:focus { border-color: #3498db; outline: none; }" +
                            "        button { width: 100%; padding: 12px; background-color: #3498db; color: white; border: none; border-radius: 6px; font-size: 16px; font-weight: bold; cursor: pointer; transition: background 0.2s; }" +
                            "        button:hover { background-color: #2980b9; }" +
                            "    </style>" +
                            "</head>" +
                            "<body>" +
                            "    <div class='card'>" +
                            "        <h2>Student Portal</h2>" +
                            "        <div class='subtitle'>Secure Anti-Proxy Check-In</div>" +
                            "        <form action='/login' method='POST'>" +
                            "            <div class='form-group'>" +
                            "                <label for='roll_no'>Roll Number</label>" +
                            "                <input type='text' id='roll_no' name='roll_no' placeholder='e.g., BCA2026001' required>" +
                            "            </div>" +
                            "            <div class='form-group'>" +
                            "                <label for='password'>Password</label>" +
                            "                <input type='password' id='password' name='password' placeholder='Enter password' required>" +
                            "            </div>" +
                            "            <button type='submit'>Sign In</button>" +
                            "        </form>" +
                            "    </div>" +
                            "</body>" +
                            "</html>"
            );
        });

        // 2. Handle Student Login Form Submission (POST /login)
        app.post("/login", ctx -> {
            String rollNo = ctx.formParam("roll_no");
            String password = ctx.formParam("password");

            // Verify credentials against SQLite database
            boolean isAuthenticated = DatabaseManager.authenticateStudent(rollNo, password);

            System.out.println("[LOGIN ATTEMPT] Roll: " + rollNo + " | Success: " + isAuthenticated);

            if (isAuthenticated) {
                // Store student session state in a secure cookie
                ctx.cookie("student_session", rollNo);
                ctx.redirect("/dashboard"); // Redirect to Student Homepage/Dashboard upon successful login
            } else {
                // Render failure message
                ctx.html(
                        "<!DOCTYPE html>" +
                                "<html>" +
                                "<head><title>Login Failed</title><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                                "<style>body { font-family: Arial; background: #f4f6f8; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }" +
                                ".card { background: white; padding: 25px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); width: 100%; max-width: 360px; text-align: center; }" +
                                "h2 { color: #c0392b; } a { color: #3498db; text-decoration: none; font-weight: bold; }</style></head>" +
                                "<body><div class=\"card\">" +
                                "<h2>Login Failed</h2>" +
                                "<p>Invalid Roll Number or Password.</p>" +
                                "<br><a href=\"/login\">Try Again</a>" +
                                "</div></body></html>"
                );
            }
        });

        // 3. Student Dashboard / Homepage (GET /dashboard)
        app.get("/dashboard", ctx -> {
            String studentSession = ctx.cookie("student_session");
            if (studentSession == null || studentSession.isEmpty()) {
                ctx.redirect("/login"); // Kick back to login if session cookie is missing
                return;
            }

            ctx.html(
                    "<!DOCTYPE html>" +
                            "<html lang='en'>" +
                            "<head>" +
                            "    <meta charset='UTF-8'>" +
                            "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                            "    <title>Student Dashboard - QRAttendance</title>" +
                            "    <style>" +
                            "        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f7f6; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }" +
                            "        .card { background: white; padding: 30px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); width: 100%; max-width: 380px; box-sizing: border-box; text-align: center; }" +
                            "        h2 { color: #2c3e50; margin-top: 0; font-size: 22px; }" +
                            "        .badge { background: #e8f8f5; color: #1abc9c; padding: 6px 12px; border-radius: 20px; font-size: 12px; font-weight: bold; display: inline-block; margin-bottom: 20px; }" +
                            "        p { color: #7f8c8d; font-size: 14px; margin-bottom: 25px; }" +
                            "        .btn { display: block; width: 100%; padding: 12px; background-color: #27ae60; color: white; text-decoration: none; border-radius: 6px; font-size: 16px; font-weight: bold; box-sizing: border-box; transition: background 0.2s; margin-bottom: 12px; }" +
                            "        .btn:hover { background-color: #219653; }" +
                            "        .btn-logout { background-color: #e74c3c; }" +
                            "        .btn-logout:hover { background-color: #c0392b; }" +
                            "    </style>" +
                            "</head>" +
                            "<body>" +
                            "    <div class='card'>" +
                            "        <h2>Student Portal</h2>" +
                            "        <div class='badge'>Logged In: " + studentSession + "</div>" +
                            "        <p>Welcome back! Ready to check into your live lecture and beat proxy tracking?</p>" +
                            "        <a href='/scan' class='btn'>📸 Open Live Camera Scanner</a>" +
                            "        <a href='/login' class='btn btn-logout'>Sign Out</a>" +
                            "    </div>" +
                            "</body>" +
                            "</html>"
            );
        });

        // 4. Handle form submissions via HTTP POST from student mobile browsers
        app.post("/submit-attendance", ctx -> {
            String rollNumber = ctx.formParam("rollNumber");
            String qrToken = ctx.formParam("qrToken");

            boolean isValid = sessionActive && TokenValidator.isValidToken(qrToken, activeSubject, activeSecretWord);

            System.out.println("[ATTENDANCE ATTEMPT] Roll: " + rollNumber + " | Valid: " + isValid);

            // Record attendance in SQLite if valid
            if (isValid && rollNumber != null && !rollNumber.isEmpty()) {
                DatabaseManager.addAttendanceLog(1, rollNumber, "PRESENT", java.time.LocalDateTime.now().toString());
            }

            String heading = isValid ? "Attendance Successful!" : "Check-In Failed";
            String textColor = isValid ? "#27ae60" : "#c0392b";
            String message = isValid ? "Successfully verified and recorded." : "Token expired, invalid, or class session inactive.";

            String responsePage = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<title>Check-In Status</title>" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; background-color: #f4f6f8; margin: 0; padding: 20px; display: flex; justify-content: center; align-items: center; height: 100vh; box-sizing: border-box; }" +
                    ".card { background: white; padding: 25px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); width: 100%; max-width: 400px; text-align: center; }" +
                    "h2 { color: " + textColor + "; margin-bottom: 10px; }" +
                    "p { color: #2c3e50; font-size: 14px; margin: 8px 0; }" +
                    ".highlight { background: #f8f9fa; padding: 10px; border-radius: 6px; border: 1px solid #e9ecef; font-family: monospace; font-size: 13px; word-break: break-all; margin-top: 15px; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class=\"card\">" +
                    "<h2>" + heading + "</h2>" +
                    "<p>Roll Number: <strong>" + rollNumber + "</strong></p>" +
                    "<p>" + message + "</p>" +
                    "<div class=\"highlight\">Token: " + qrToken + "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            ctx.html(responsePage);
        });

        System.out.println("Embedded Javalin HTTP server started on port 8080.");
    }

    // Safely stop the server when closing the desktop application
    public static void stopServer() {
        if (app != null) {
            app.stop();
            app = null;
            System.out.println("Embedded Javalin HTTP server stopped.");
        }
    }
}
