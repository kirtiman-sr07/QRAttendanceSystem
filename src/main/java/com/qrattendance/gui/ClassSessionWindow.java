package com.qrattendance.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.qrattendance.security.QRCodeGenerator;

public class ClassSessionWindow extends Application {

    private Timeline qrTimeline; // Tracks the background timer loop

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

        Button startButton = new Button("Start Class & Auto-Refresh QR");
        Label statusLabel = new Label();

        // 2. ImageView Setup
        ImageView qrImageView = new ImageView();
        qrImageView.setFitWidth(200);
        qrImageView.setFitHeight(200);
        qrImageView.setPreserveRatio(true);

        // 3. Event-Driven Action Handler with Timeline Integration
        startButton.setOnAction(e -> {
            String subject = subjectField.getText().trim();
            String secretWord = secretCodeField.getText().trim();

            // Guard clause for empty inputs
            if (subject.isEmpty() || secretWord.isEmpty()) {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("Error: All fields must be filled!");
                return;
            }

            // If a timeline is already running from a previous click, stop it first
            if (qrTimeline != null) {
                qrTimeline.stop();
            }

            // Define the refresh action block (executed immediately and every 15 seconds)
            Runnable refreshAction = () -> {
                // Generate precise current timestamp string
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

                // Combine into single dynamic anti-proxy payload
                String dynamicPayload = subject + "_" + secretWord + "_" + timestamp;

                // Define output file path
                String outputPath = "active_class_qr.png";

                // Call ZXing QR code generator engine
                QRCodeGenerator.generateQRCode(dynamicPayload, 300, 300, outputPath);

                // Load and display the generated QR code live in the ImageView container
                Image qrImage = new Image("file:" + outputPath);
                qrImageView.setImage(qrImage);

                // Provide success feedback with timestamp update
                statusLabel.setStyle("-fx-text-fill: green;");
                statusLabel.setText("Active QR Refreshed! Time: " + timestamp);
            };

            // Run the generation action immediately upon clicking start
            refreshAction.run();

            // Setup the JavaFX Timeline to repeat every 15 seconds indefinitely
            qrTimeline = new Timeline(new KeyFrame(Duration.seconds(15), event -> refreshAction.run()));
            qrTimeline.setCycleCount(Timeline.INDEFINITE);
            qrTimeline.play();
        });

        // 4. Layout Container Setup
        VBox root = new VBox(12, titleLabel, subjectField, secretCodeField, startButton, statusLabel, qrImageView);
        root.setPadding(new Insets(20));

        // 5. Scene and Stage Configuration
        Scene scene = new Scene(root, 380, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Ensure background timeline stops cleanly when the window is closed
        if (qrTimeline != null) {
            qrTimeline.stop();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
