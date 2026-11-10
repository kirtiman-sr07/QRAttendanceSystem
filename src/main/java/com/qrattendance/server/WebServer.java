package com.qrattendance.server;

import io.javalin.Javalin;

public class WebServer {
    private static Javalin app;

    // Start the embedded Javalin server on port 8080
    public static void startServer() {
        if (app != null) {
            return; // Prevent starting multiple instances if already running
        }

        app = Javalin.create().start(8080);

        // Serve a mobile-optimized HTML check-in form at the /scan endpoint
        app.get("/scan", ctx -> {
            String htmlPage = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<title>QRAttendance Mobile Check-In</title>" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; background-color: #f4f6f8; margin: 0; padding: 20px; display: flex; justify-content: center; align-items: center; height: 100vh; box-sizing: border-box; }" +
                    ".card { background: white; padding: 25px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); width: 100%; max-width: 400px; text-align: center; }" +
                    "h2 { color: #2c3e50; margin-bottom: 5px; }" +
                    "p { color: #7f8c8d; font-size: 14px; margin-bottom: 20px; }" +
                    ".badge { background: #e8f8f5; color: #1abc9c; padding: 5px 10px; border-radius: 20px; font-size: 11px; font-weight: bold; display: inline-block; margin-bottom: 15px; }" +
                    ".form-group { margin-bottom: 15px; text-align: left; }" +
                    "label { font-size: 13px; font-weight: bold; color: #34495e; display: block; margin-bottom: 5px; }" +
                    "input[type=\"text\"] { width: 100%; padding: 10px; box-sizing: border-box; border: 1px solid #bdc3c7; border-radius: 6px; font-size: 14px; }" +
                    "button { background: #3498db; color: white; border: none; padding: 12px; width: 100%; border-radius: 6px; font-size: 16px; font-weight: bold; cursor: pointer; transition: background 0.2s; }" +
                    "button:hover { background: #2980b9; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class=\"card\">" +
                    "<h2>QRAttendance Portal</h2>" +
                    "<p>Secure Mobile Check-In Interface</p>" +
                    "<div class=\"badge\">Server Connected</div>" +
                    "<form action=\"/submit-attendance\" method=\"POST\">" +
                    "<div class=\"form-group\">" +
                    "<label>Roll Number:</label>" +
                    "<input type=\"text\" name=\"rollNumber\" placeholder=\"e.g., BCA2026001\" required>" +
                    "</div>" +
                    "<div class=\"form-group\">" +
                    "<label>Scanned QR Token:</label>" +
                    "<input type=\"text\" name=\"qrToken\" placeholder=\"Paste scanned payload\" required>" +
                    "</div>" +
                    "<button type=\"submit\">Submit Attendance</button>" +
                    "</form>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            ctx.html(htmlPage);
        });

        // Handle form submissions via HTTP POST from student mobile browsers
        app.post("/submit-attendance", ctx -> {
            String rollNumber = ctx.formParam("rollNumber");
            String qrToken = ctx.formParam("qrToken");

            String responsePage = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<title>Check-In Status</title>" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; background-color: #f4f6f8; margin: 0; padding: 20px; display: flex; justify-content: center; align-items: center; height: 100vh; box-sizing: border-box; }" +
                    ".card { background: white; padding: 25px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); width: 100%; max-width: 400px; text-align: center; }" +
                    "h2 { color: #27ae60; margin-bottom: 10px; }" +
                    "p { color: #2c3e50; font-size: 14px; margin: 8px 0; }" +
                    ".highlight { background: #f8f9fa; padding: 10px; border-radius: 6px; border: 1px solid #e9ecef; font-family: monospace; font-size: 13px; word-break: break-all; margin-top: 15px; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class=\"card\">" +
                    "<h2>Attendance Received!</h2>" +
                    "<p>Roll Number: <strong>" + rollNumber + "</strong></p>" +
                    "<p>Successfully transmitted to Java Backend.</p>" +
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