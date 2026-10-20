package com.qrattendance.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TeacherLogin extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Teacher Login - QR Attendance System");

        // 1. Create UI components
        Label titleLabel = new Label("Teacher Login Portal");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField userField = new TextField();
        userField.setPromptText("Enter Username");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter Password");

        Button loginButton = new Button("Login");
        Label messageLabel = new Label();

        // 2. Add event handling when the Login button is clicked
        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();

            // Simple validation check (we'll connect this to SQLite later if needed)
            if (username.equals("admin") && password.equals("password123")) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Login Successful! Welcome, Professor.");
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Invalid Username or Password!");
            }
        });

        // 3. Arrange components vertically in a VBox layout with padding
        VBox root = new VBox(12); // 12 pixels spacing
        root.setPadding(new Insets(20));
        root.getChildren().addAll(titleLabel, userField, passField, loginButton, messageLabel);

        // 4. Set the Scene and show the Stage
        Scene scene = new Scene(root, 350, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}