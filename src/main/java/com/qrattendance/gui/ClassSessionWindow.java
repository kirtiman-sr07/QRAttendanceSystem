package com.qrattendance.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.qrattendance.security.QRCodeGenerator;

public class ClassSessionWindow extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Teacher Session Portal - QRAttendance");

        // 1. UI Controls Setup
        Label titleLabel = new Label("Launch Live Class Session");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField subjectField = new TextField();
        subjectField.setPromptText("Enter Subject Code (e.g., JAVA_101)");

        TextField secretCodeField = new TextField();
        secretCodeField.setPromptText("Enter Secret Code Word (e.g., coffee99)");

        Button startButton = new Button("Start Class & Generate QR");
        Label statusLabel = new Label();

        // 2. Event-Driven Action Handler (Day 4: Dynamic Anti-Proxy Payload)
        startButton.setOnAction(e -> {
            String subject = subjectField.getText().trim();
            String secretWord = secretCodeField.getText().trim();

            // Guard clause for empty inputs
            if (subject.isEmpty() || secretWord.isEmpty()) {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("Error: All fields must be filled!");
                return;
            }

            // Generate precise current timestamp string
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

            // Combine into single dynamic anti-proxy payload
            String dynamicPayload = subject + "_" + secretWord + "_" + timestamp;

            // Define output file path
            String outputPath = "active_class_qr.png";

            // Call ZXing QR code generator engine
            QRCodeGenerator.generateQRCode(dynamicPayload, 300, 300, outputPath);

            // Provide success feedback
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("QR Generated! Payload: " + dynamicPayload);
        });

        // 3. Layout Container Setup
        VBox root = new VBox(12, titleLabel, subjectField, secretCodeField, startButton, statusLabel);
        root.setPadding(new Insets(20));

        // 4. Scene and Stage Configuration
        Scene scene = new Scene(root, 380, 260);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}