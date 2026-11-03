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
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.qrattendance.security.QRCodeGenerator;

public class ClassSessionWindow extends Application {

    private Timeline qrTimeline;

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

        // 3. Event-Driven Action Handler with Stream-Based Refresh
        startButton.setOnAction(e -> {
            String subject = subjectField.getText().trim();
            String secretWord = secretCodeField.getText().trim();

            if (subject.isEmpty() || secretWord.isEmpty()) {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("Error: All fields must be filled!");
                return;
            }

            if (qrTimeline != null) {
                qrTimeline.stop();
            }

            Runnable refreshAction = () -> {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
                String dynamicPayload = subject + "_" + secretWord + "_" + timestamp;
                String outputPath = "active_class_qr.png";

                // Generate new QR PNG on disk
                QRCodeGenerator.generateQRCode(dynamicPayload, 300, 300, outputPath);

                // Load via FileInputStream to bypass JavaFX URL cache and prevent flickering
                try (FileInputStream fileInputStream = new FileInputStream(outputPath)) {
                    Image qrImage = new Image(fileInputStream);
                    qrImageView.setImage(qrImage);

                    statusLabel.setStyle("-fx-text-fill: green;");
                    statusLabel.setText("Active QR Refreshed! Time: " + timestamp);
                } catch (IOException ex) {
                    statusLabel.setStyle("-fx-text-fill: red;");
                    statusLabel.setText("Error loading QR image: " + ex.getMessage());
                }
            };

            // Run immediately, then every 15 seconds
            refreshAction.run();
            qrTimeline = new Timeline(new KeyFrame(Duration.seconds(15), event -> refreshAction.run()));
            qrTimeline.setCycleCount(Timeline.INDEFINITE);
            qrTimeline.play();
        });

        VBox root = new VBox(12, titleLabel, subjectField, secretCodeField, startButton, statusLabel, qrImageView);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 380, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        if (qrTimeline != null) {
            qrTimeline.stop();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}