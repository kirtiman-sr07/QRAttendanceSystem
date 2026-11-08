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

        // Define a temporary test route for mobile browsers
        app.get("/scan", ctx -> {
            ctx.result("QRAttendance Backend Active: Server is successfully connected to local network!");
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
