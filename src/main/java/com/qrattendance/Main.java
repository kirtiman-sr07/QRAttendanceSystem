package com.qrattendance;

import com.qrattendance.security.QRCodeGenerator;

public class Main {
    public static void main(String[] args) {
        System.out.println("Testing QR Code Generation Engine...");

        // Test string representing a sample class payload
        String samplePayload = "JAVA_CLASS_101_COFFEE99";
        String outputPath = "class_qr.png";

        // Generate a 300x300 PNG QR code
        QRCodeGenerator.generateQRCode(samplePayload, 300, 300, outputPath);
    }
}
