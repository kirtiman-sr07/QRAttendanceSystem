package com.qrattendance.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentRegistration extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Registration - QR Attendance System");

        // 1. Create UI components
        Label titleLabel = new Label("New Student Registration");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter Full Name");

        TextField rollNoField = new TextField();
        rollNoField.setPromptText("Enter Roll Number (e.g., 101)");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter Email Address");

        Button saveButton = new Button("Save Student");
        Label messageLabel = new Label();

        // 2. Button click event handler (Validation & UI Feedback)
        saveButton.setOnAction(e -> {
            String name = nameField.getText();
            String rollNoText = rollNoField.getText();
            String email = emailField.getText();

            // Basic empty check validation
            if (name.isEmpty() || rollNoText.isEmpty() || email.isEmpty()) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Error: All fields must be filled out!");
            } else {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Captured: " + name + " (Roll: " + rollNoText + ")");
                // Note: Tomorrow on Day 4, we will hook this up to SQLite!
            }
        });

        // 3. Arrange components vertically in a VBox layout with padding
        VBox root = new VBox(12); // 12 pixels spacing
        root.setPadding(new Insets(20));
        root.getChildren().addAll(titleLabel, nameField, rollNoField, emailField, saveButton, messageLabel);

        // 4. Set the Scene and show the Stage
        Scene scene = new Scene(root, 350, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}