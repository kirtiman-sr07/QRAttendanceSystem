package com.qrattendance.gui;

import com.qrattendance.db.DatabaseManager;
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

        // 2. Connect Button to SQLite Database
        saveButton.setOnAction(e -> {
            String name = nameField.getText();
            String rollNoText = rollNoField.getText();
            String email = emailField.getText();

            // Validate empty fields
            if (name.isEmpty() || rollNoText.isEmpty() || email.isEmpty()) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Error: All fields must be filled out!");
                return;
            }

            try {
                // Convert roll number string to an integer
                int rollNo = Integer.parseInt(rollNoText);

                // Call your Week 2/3 DatabaseManager method to insert into SQLite!
                DatabaseManager.addStudent(rollNo, name, email);

                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Success! Saved " + name + " to database.");

                // Clear input fields for the next student
                nameField.clear();
                rollNoField.clear();
                emailField.clear();

            } catch (NumberFormatException ex) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Error: Roll Number must be a valid number!");
            }
        });

        // 3. Arrange components vertically in a VBox layout with padding
        VBox root = new VBox(12);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(titleLabel, nameField, rollNoField, emailField, saveButton, messageLabel);

        // 4. Set the Scene and show the Stage
        Scene scene = new Scene(root, 350, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
