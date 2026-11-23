package com.qrattendance.gui;

import com.qrattendance.analytics.ClassAnalyticsAssistant;
import com.qrattendance.db.DatabaseManager;
import com.qrattendance.security.QRCodeGenerator;
import com.qrattendance.server.WebServer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClassSessionWindow extends Application {

    private Timeline qrTimeline;
    private int timeLeft = 15;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("QRAttendance — Classroom & Admin Workspace");

        DatabaseManager.initializeDatabase();
        WebServer.startServer();

        // --- Top Role & Portal Bar ---
        Label brandTitle = new Label("QRAttendance");
        brandTitle.getStyleClass().add("app-title");

        Label brandSub = new Label("Teacher Portal  •  Port 8080 Active");
        brandSub.getStyleClass().add("app-subtitle");

        VBox brandBox = new VBox(2, brandTitle, brandSub);

        Button studentUrlBtn = new Button("Copy Student URL");
        studentUrlBtn.getStyleClass().add("btn-secondary");

        Button hodAdminButton = new Button("HOD / Admin");
        hodAdminButton.getStyleClass().add("btn-secondary");
        hodAdminButton.setOnAction(e -> HodAdminWindow.openLoginWindow());

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, brandBox, topSpacer, studentUrlBtn, hodAdminButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getStyleClass().add("topbar");

        // --- Card 1: Live Class Session & Dynamic QR ---
        Label sessionTitle = new Label("Live Class Session");
        sessionTitle.getStyleClass().add("section-title");

        Label sessionHint = new Label("Start a lecture to generate a rotating 15-second anti-proxy QR code.");
        sessionHint.getStyleClass().add("muted-label");

        TextField subjectField = new TextField();
        subjectField.setPromptText("Subject Code (e.g., JAVA_101)");
        subjectField.getStyleClass().add("text-field");

        PasswordField secretPasswordField = new PasswordField();
        secretPasswordField.setPromptText("Secret Code Word (e.g., coffee99)");
        secretPasswordField.getStyleClass().add("password-field");

        TextField secretPlainField = new TextField();
        secretPlainField.setPromptText("Secret Code Word (e.g., coffee99)");
        secretPlainField.getStyleClass().add("text-field");
        secretPlainField.setManaged(false);
        secretPlainField.setVisible(false);

        secretPasswordField.textProperty().bindBidirectional(secretPlainField.textProperty());

        ToggleButton eyeToggle = new ToggleButton("Show");
        eyeToggle.getStyleClass().add("btn-secondary");
        eyeToggle.setFocusTraversable(false);
        eyeToggle.setOnAction(e -> {
            boolean show = eyeToggle.isSelected();
            eyeToggle.setText(show ? "Hide" : "Show");
            secretPlainField.setVisible(show);
            secretPlainField.setManaged(show);
            secretPasswordField.setVisible(!show);
            secretPasswordField.setManaged(!show);
        });

        StackPane secretInputStack = new StackPane(secretPasswordField, secretPlainField);
        HBox.setHgrow(secretInputStack, Priority.ALWAYS);
        HBox secretRow = new HBox(8, secretInputStack, eyeToggle);
        secretRow.setAlignment(Pos.CENTER_LEFT);

        Button startButton = new Button("Start Class & Auto-Refresh QR");
        startButton.getStyleClass().add("btn-primary");

        Label countdownLabel = new Label("Next Refresh: --s");
        countdownLabel.getStyleClass().add("countdown-pill");

        Region actionSpacer = new Region();
        HBox.setHgrow(actionSpacer, Priority.ALWAYS);

        HBox sessionActionRow = new HBox(10, startButton, actionSpacer, countdownLabel);
        sessionActionRow.setAlignment(Pos.CENTER_LEFT);

        Label statusLabel = new Label("Ready to launch a new session.");
        statusLabel.getStyleClass().add("muted-label");

        Label urlNoticeLabel = new Label("Student Web Portal: http://localhost:8080/login");
        urlNoticeLabel.getStyleClass().add("muted-label");

        studentUrlBtn.setOnAction(e -> {
            String url = "http://localhost:8080/login";
            ClipboardContent content = new ClipboardContent();
            content.putString(url);
            Clipboard.getSystemClipboard().setContent(content);
            urlNoticeLabel.setStyle("-fx-text-fill: #059669; -fx-font-weight: bold;");
            urlNoticeLabel.setText("Copied " + url + " (use your cloudflared HTTPS link on mobile)");
        });

        ImageView qrImageView = new ImageView();
        qrImageView.setFitWidth(190);
        qrImageView.setFitHeight(190);
        qrImageView.setPreserveRatio(true);

        VBox qrFrame = new VBox(qrImageView);
        qrFrame.setAlignment(Pos.CENTER);
        qrFrame.getStyleClass().add("qr-frame");

        VBox sessionCard = new VBox(
                12,
                sessionTitle,
                sessionHint,
                subjectField,
                secretRow,
                sessionActionRow,
                statusLabel,
                urlNoticeLabel,
                qrFrame
        );
        sessionCard.getStyleClass().add("card");

        // --- Card 2: Smart Class Analytics Assistant ---
        Label aiTitle = new Label("Smart Class Analytics Assistant");
        aiTitle.getStyleClass().add("section-title");

        Label aiHint = new Label("Query live attendance percentages, defaulters, session turnout, or roll numbers.");
        aiHint.getStyleClass().add("muted-label");

        TextField queryField = new TextField();
        queryField.setPromptText("Ask e.g. 'Which students have below 75% attendance?'");
        queryField.getStyleClass().add("text-field");
        HBox.setHgrow(queryField, Priority.ALWAYS);

        Button askButton = new Button("Ask");
        askButton.getStyleClass().add("btn-primary");

        HBox queryRow = new HBox(8, queryField, askButton);
        queryRow.setAlignment(Pos.CENTER_LEFT);

        Button defaultersBtn = new Button("Below 75%");
        defaultersBtn.getStyleClass().add("btn-chip");

        Button turnoutBtn = new Button("Today's Turnout");
        turnoutBtn.getStyleClass().add("btn-chip");

        Button studentSampleBtn = new Button("Student: BCA2026001");
        studentSampleBtn.getStyleClass().add("btn-chip");

        HBox quickPromptRow = new HBox(8, defaultersBtn, turnoutBtn, studentSampleBtn);
        quickPromptRow.setAlignment(Pos.CENTER_LEFT);

        TextArea aiOutputArea = new TextArea();
        aiOutputArea.setEditable(false);
        aiOutputArea.setWrapText(true);
        aiOutputArea.setPrefRowCount(6);
        aiOutputArea.getStyleClass().add("output-area");
        aiOutputArea.setPromptText("Analytics results from SQLite will appear here...");

        Runnable runAnalyticsQuery = () -> aiOutputArea.setText(ClassAnalyticsAssistant.answerQuery(queryField.getText()));
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

        studentSampleBtn.setOnAction(e -> {
            queryField.setText("BCA2026001");
            runAnalyticsQuery.run();
        });

        VBox analyticsCard = new VBox(12, aiTitle, aiHint, queryRow, quickPromptRow, aiOutputArea);
        analyticsCard.getStyleClass().add("card");

        // --- Start Session & QR Rotation ---
        startButton.setOnAction(e -> {
            String subject = subjectField.getText().trim();
            String secretWord = secretPasswordField.getText().trim();

            if (subject.isEmpty() || secretWord.isEmpty()) {
                statusLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
                statusLabel.setText("Please enter both a Subject Code and a Secret Code Word.");
                return;
            }

            if (eyeToggle.isSelected()) {
                eyeToggle.fire();
            }

            int sessionId = DatabaseManager.createClassSession(subject, "Instructor", LocalDateTime.now().toString());
            WebServer.setSessionCredentials(sessionId, subject, secretWord);

            if (qrTimeline != null) {
                qrTimeline.stop();
            }

            Runnable generateAndDisplayQR = () -> {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
                String dynamicPayload = subject + "_" + secretWord + "_" + timestamp;
                String outputPath = "active_class_qr.png";

                QRCodeGenerator.generateQRCode(dynamicPayload, 300, 300, outputPath);

                try (FileInputStream fileInputStream = new FileInputStream(outputPath)) {
                    qrImageView.setImage(new Image(fileInputStream));
                    statusLabel.setStyle("-fx-text-fill: #059669; -fx-font-weight: bold;");
                    statusLabel.setText("Session #" + sessionId + " active (" + subject + ") • Refreshed at " + timestamp);
                } catch (IOException ex) {
                    statusLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
                    statusLabel.setText("Error loading QR image: " + ex.getMessage());
                }
            };

            timeLeft = 15;
            generateAndDisplayQR.run();
            countdownLabel.setText("Next Refresh: " + timeLeft + "s");

            qrTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                timeLeft--;
                if (timeLeft <= 0) {
                    generateAndDisplayQR.run();
                    timeLeft = 15;
                }
                countdownLabel.setText("Next Refresh: " + timeLeft + "s");
            }));
            qrTimeline.setCycleCount(Timeline.INDEFINITE);
            qrTimeline.play();
        });

        // --- Scrollable Content & Scene ---
        VBox cardsColumn = new VBox(16, sessionCard, analyticsCard);
        cardsColumn.setPadding(new Insets(20, 22, 24, 22));
        cardsColumn.setMaxWidth(680);

        HBox centeredContainer = new HBox(cardsColumn);
        centeredContainer.setAlignment(Pos.TOP_CENTER);
        HBox.setHgrow(cardsColumn, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(centeredContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        VBox shell = new VBox(topBar, scrollPane);
        shell.getStyleClass().add("root-bg");

        Scene scene = new Scene(shell, 640, 800);
        URL cssUrl = getClass().getResource("/styles/app.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(480);
        primaryStage.setMinHeight(560);
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
