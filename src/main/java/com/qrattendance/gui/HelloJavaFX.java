package com.qrattendance.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloJavaFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Create a text label
        Label label = new Label("Welcome to the QR Attendance GUI!");

        // 2. Put the label inside a Vertical Box (VBox) container
        VBox root = new VBox(10); // 10 pixels of spacing between items
        root.getChildren().add(label);

        // 3. Create a Scene containing our VBox layout (width: 400, height: 200)
        Scene scene = new Scene(root, 400, 200);

        // 4. Set up the Stage (Window) and show it
        primaryStage.setTitle("QR Attendance System - Week 4");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
