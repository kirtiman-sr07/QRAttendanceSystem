package com.qrattendance.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

        // 2. Event-Driven Action Handler
        startButton.setOnAction(e -> {
            String subject = subjectField.getText().trim();
            String secretWord = secretCodeField.getText().trim();

            // Guard clause for empty inputs
            if (subject.isEmpty() || secretWord.isEmpty()) {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("Error: All fields must be filled!");
                return;
            }

            // Success feedback (Tomorrow we will pass this into our QRCodeGenerator!)
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Success! Active Subject: " + subject + " | Code: " + secretWord);
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
