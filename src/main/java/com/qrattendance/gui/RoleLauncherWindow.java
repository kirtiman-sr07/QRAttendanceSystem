package com.qrattendance.gui;

import com.qrattendance.db.DatabaseManager;
import com.qrattendance.server.WebServer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class RoleLauncherWindow extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("QRAttendance — Select Portal");

        DatabaseManager.initializeDatabase();
        WebServer.startServer();

        Label title = new Label("QRAttendance Workspace");
        title.getStyleClass().add("app-title");

        Label subtitle = new Label("Choose your portal to continue");
        subtitle.getStyleClass().add("muted-label");

        Button teacherBtn = new Button("Teacher Session Portal");
        teacherBtn.getStyleClass().add("btn-primary");
        teacherBtn.setMaxWidth(Double.MAX_VALUE);

        Button hodBtn = new Button("HOD / Admin Portal");
        hodBtn.getStyleClass().add("btn-secondary");
        hodBtn.setMaxWidth(Double.MAX_VALUE);

        Button studentBtn = new Button("Student Web Portal (Copy Link)");
        studentBtn.getStyleClass().add("btn-secondary");
        studentBtn.setMaxWidth(Double.MAX_VALUE);

        Label infoLabel = new Label("Embedded server active on port 8080");
        infoLabel.getStyleClass().add("muted-label");

        teacherBtn.setOnAction(e -> {
            try {
                Stage teacherStage = new Stage();
                new ClassSessionWindow().start(teacherStage);
                primaryStage.close();
            } catch (Exception ex) {
                infoLabel.setStyle("-fx-text-fill: #dc2626;");
                infoLabel.setText("Failed to open Teacher Portal: " + ex.getMessage());
            }
        });

        hodBtn.setOnAction(e -> HodAdminWindow.openLoginWindow());

        studentBtn.setOnAction(e -> {
            String url = "http://localhost:8080/login";
            ClipboardContent content = new ClipboardContent();
            content.putString(url);
            Clipboard.getSystemClipboard().setContent(content);
            infoLabel.setStyle("-fx-text-fill: #059669; -fx-font-weight: bold;");
            infoLabel.setText("Copied " + url + " (open in browser / cloudflared)");
        });

        VBox card = new VBox(
                12,
                title,
                subtitle,
                teacherBtn,
                hodBtn,
                studentBtn,
                infoLabel
        );
        card.getStyleClass().add("card");
        card.setMaxWidth(420);

        VBox root = new VBox(card);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("root-bg");

        Scene scene = new Scene(root, 480, 380);
        URL cssUrl = getClass().getResource("/styles/app.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        WebServer.stopServer();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
