package com.qrattendance.security;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCodeGenerator {

    /**
     * Generates a PNG QR code image from any input text string.
     *
     * @param text     The payload string to encode (e.g., class details, secret words, timestamps)
     * @param width    Width of the QR code image in pixels
     * @param height   Height of the QR code image in pixels
     * @param filePath The local file path where the PNG image will be saved
     */
    public static void generateQRCode(String text, int width, int height, String filePath) {
        try {
            // 1. Initialize the ZXing QR Code Writer
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            // 2. Encode the text into a BitMatrix 2D grid
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            // 3. Define the destination file path on your system
            Path path = FileSystems.getDefault().getPath(filePath);

            // 4. Render the BitMatrix into a physical PNG image file
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            System.out.println("QR Code successfully generated at: " + filePath);

        } catch (WriterException | IOException e) {
            System.out.println("Error generating QR code: " + e.getMessage());
        }
    }
}
