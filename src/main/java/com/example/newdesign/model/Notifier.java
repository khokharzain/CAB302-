package com.example.newdesign.model;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class Notifier {

    private Notifier() {}

    public static void showToast(StackPane root, String message) {

        Label toast = new Label(message);

        toast.setStyle(
                "-fx-background-color: #323232;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 15;"
        );

        root.getChildren().add(toast);
        StackPane.setAlignment(toast, Pos.CENTER);

        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> root.getChildren().remove(toast));
        }).start();
    }
}