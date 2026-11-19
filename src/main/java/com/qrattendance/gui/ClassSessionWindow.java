package com.qrattendance.gui;

import com.qrattendance.analytics.ClassAnalyticsAssistant;
import com.qrattendance.db.DatabaseManager;
import com.qrattendance.security.QRCodeGenerator;
import com.qrattendance.server.WebServer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClassSessionWindow extends Application {

    private Timeline qrTimeline;
    private int timeLeft = 15; // Tracks countdown seconds

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Teacher Session Portal - QRAttendance");

        // Initialize SQLite database and tables
        DatabaseManager.initializeDatabase();

        // Boot up the embedded background web server for mobile scans on port 8080
        WebServer.startServer();

        // 1. Session Controls Setup
        Label titleLabel = new Label("Launch Live Class Session");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField subjectField = new TextField();
        subjectField.setPromptText("Enter Subject Code (e.g., JAVA_101)");

        // Masked Secret Word Field + Eye Toggle
        PasswordField secretPasswordField = new PasswordField();
        secretPasswordField.setPromptText("Enter Secret Code Word (e.g., coffee99)");

        TextField secretPlainField = new TextField();
        secretPlainField.setPromptText("Enter Secret Code Word (e.g., coffee99)");
        secretPlainField.setManaged(false);
        secretPlainField.setVisible(false);

        // Two-way sync so typing in either field updates the other
        secretPasswordField.textProperty().bindBidirectional(secretPlainField.textProperty());

        ToggleButton eyeToggle = new ToggleButton("👁");
        eyeToggle.setFocusTraversable(false);
        eyeToggle.setOnAction(e -> {
            boolean show = eyeToggle.isSelected();
            secretPlainField.setVisible(show);
            secretPlainField.setManaged(show);
            secretPasswordField.setVisible(!show);
            secretPasswordField.setManaged(!show);
        });

        StackPane secretInputStack = new StackPane(secretPasswordField, secretPlainField);
        HBox.setHgrow(secretInputStack, Priority.ALWAYS);
        HBox secretRow = new HBox(6, secretInputStack, eyeToggle);

        Button startButton = new Button("Start Class & Auto-Refresh QR");
        Label statusLabel = new Label();

        // Visual Countdown Label Setup
        Label countdownLabel = new Label("Next Refresh In: --s");
        countdownLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #e67e22;");

        // 2. ImageView Setup
        ImageView qrImageView = new ImageView();
        qrImageView.setFitWidth(180);
        qrImageView.setFitHeight(180);
        qrImageView.setPreserveRatio(true);

        // 3. Smart Class Analytics (AI Assistant) Setup
        Label aiLabel = new Label("Smart Class Analytics Assistant");
        aiLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        TextField queryField = new TextField();
        queryField.setPromptText("Ask e.g. 'Which students have below 75% attendance?'");
        HBox.setHgrow(queryField, Priority.ALWAYS);

        Button askButton = new Button("Ask AI");

        TextArea aiOutputArea = new TextArea();
        aiOutputArea.setEditable(false);
        aiOutputArea.setWrapText(true);
        aiOutputArea.setPrefRowCount(6);
        aiOutputArea.setPromptText("Analytics insights from SQLite will appear here...");

        Button defaultersBtn = new Button("Below 75%");
        Button turnoutBtn = new Button("Today's Turnout");

        Runnable runAnalyticsQuery = () -> {
            String result = ClassAnalyticsAssistant.answerQuery(queryField.getText());
            aiOutputArea.setText(result);
        };

        askButton.setOnAction(e -> runAnalyticsQuery.run());
        queryField.setOnAction(e -> runAnalyticsQuery.run());

        defaultersBtn.setOnAction(e -> {
            queryField.setText("Which students have below 75% attendance?");
            runAnalyticsQuery.run();
        });

        turnoutBtn.setOnAction(e -> {
            queryField.setText("Summarize today's attendance turnout");
            runAnalyticsQuery.run();
        });

        HBox queryRow = new HBox(6, queryField, askButton);
        HBox quickPromptRow = new HBox(6, defaultersBtn, turnoutBtn);

        // 4. Event-Driven Action Handler with Unified Ticker
        startButton.setOnAction(e -> {
            String subject = subjectField.getText().trim();
            String secretWord = secretPasswordField.getText().trim();

            if (subject.isEmpty() || secretWord.isEmpty()) {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("Error: All fields must be filled!");
                return;
            }

            // Auto-hide secret word when projecting
            if (eyeToggle.isSelected()) {
                eyeToggle.fire();
            }

            // Create a unique class session in SQLite and register credentials with WebServer
            int sessionId = DatabaseManager.createClassSession(subject, "Instructor", LocalDateTime.now().toString());
            WebServer.setSessionCredentials(sessionId, subject, secretWord);

            if (qrTimeline != null) {
                qrTimeline.stop();
            }

            // Helper method logic encapsulated for immediate run & periodic refresh
            Runnable generateAndDisplayQR = () -> {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
                String dynamicPayload = subject + "_" + secretWord + "_" + timestamp;
                String outputPath = "active_class_qr.png";

                // Generate new QR PNG on disk
                QRCodeGenerator.generateQRCode(dynamicPayload, 300, 300, outputPath);

                // Load via FileInputStream to bypass cache and prevent flickering
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

            // Execute immediately on click
            timeLeft = 15;
            generateAndDisplayQR.run();
            countdownLabel.setText("Next Refresh In: " + timeLeft + "s");

            // Setup a 1-second ticking Timeline that handles both countdown and QR rotation
            qrTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                timeLeft--;

                if (timeLeft <= 0) {
                    generateAndDisplayQR.run();
                    timeLeft = 15;
                }

                countdownLabel.setText("Next Refresh In: " + timeLeft + "s");
            }));

            qrTimeline.setCycleCount(Timeline.INDEFINITE);
            qrTimeline.play();
        });

        // 5. Layout Container Setup
        VBox root = new VBox(
                10,
                titleLabel,
                subjectField,
                secretRow,
                startButton,
                statusLabel,
                countdownLabel,
                qrImageView,
                new Separator(),
                aiLabel,
                queryRow,
                quickPromptRow,
                aiOutputArea
        );
        root.setPadding(new Insets(18));

        // 6. Scene and Stage Configuration
        Scene scene = new Scene(root, 500, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        if (qrTimeline != null) {
            qrTimeline.stop();
        }
        WebServer.stopServer();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
