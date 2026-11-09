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

        // Serve a mobile-optimized HTML web page at the /scan endpoint
        app.get("/scan", ctx -> {
            String htmlPage = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<title>QRAttendance Mobile Check-In</title>" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; background-color: #f4f6f8; margin: 0; padding: 20px; display: flex; justify-content: center; align-items: center; height: 100vh; }" +
                    ".card { background: white; padding: 25px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); width: 100%; max-width: 400px; text-align: center; }" +
                    "h2 { color: #2c3e50; margin-bottom: 10px; }" +
                    "p { color: #7f8c8d; font-size: 14px; margin-bottom: 20px; }" +
                    ".badge { background: #e8f8f5; color: #1abc9c; padding: 6px 12px; border-radius: 20px; font-size: 12px; font-weight: bold; display: inline-block; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class=\"card\">" +
                    "<h2>QRAttendance Portal</h2>" +
                    "<p>Secure Mobile Check-In Interface</p>" +
                    "<div class=\"badge\">Server Connected</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            ctx.html(htmlPage);
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
