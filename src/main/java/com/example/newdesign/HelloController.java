package com.example.newdesign;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.geometry.Pos;

import java.io.IOException;

public class HelloController {

    @FXML
    private Label welcomeText;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button SignUpButton;

    @FXML
    protected void onSignUpClick() throws IOException {

        Stage stage = (Stage) SignUpButton.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("signUp-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 1200, 800);

        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), scene.getRoot());
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        stage.setScene(scene);
    }

    @FXML
    public void handleLogin() throws IOException {

        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields should be filled");
            return;
        }

        UserDAO dao = new UserDAOImpl();
        User user = dao.login(email, password);

        if (user != null) {


            SessionManager.setUser(user);

            System.out.println("LOGIN USER: " + user.getFirstName()); // debug

            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("main-view.fxml")
            );

            Scene scene = new Scene(loader.load(), 1200, 800);

            Stage stage = (Stage) loginButton.getScene().getWindow();

            FadeTransition fade = new FadeTransition(Duration.seconds(0.5), scene.getRoot());
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();

            stage.setScene(scene);

        } else {
            showAlert("Error", "Invalid email or password");
        }
    }

    // Alert helper
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleForgotPassword() {

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Reset Password");

        VBox layout = new VBox(15);
        layout.setStyle("""
        -fx-background-color: white;
        -fx-padding: 25;
        -fx-alignment: center;
        -fx-background-radius: 15;
    """);

        Label title = new Label("Reset Your Password");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("New password");

        Label message = new Label();
        message.setStyle("-fx-text-fill: red;");

        Button submitBtn = new Button("Update Password");
        submitBtn.setStyle("""
        -fx-background-color: #6A0DAD;
        -fx-text-fill: white;
        -fx-font-weight: bold;
        -fx-padding: 8 20;
        -fx-background-radius: 10;
    """);

        submitBtn.setOnAction(e -> {
            String email = emailField.getText();
            String newPassword = passwordField.getText();

            if (email.isEmpty() || newPassword.isEmpty()) {
                message.setText("All fields are required");
                return;
            }

            UserDAOImpl dao = new UserDAOImpl();
            boolean success = dao.resetPassword(email, newPassword);

            if (success) {
                message.setStyle("-fx-text-fill: green;");
                message.setText("Password updated successfully!");
            } else {
                message.setStyle("-fx-text-fill: red;");
                message.setText("Email not found");
            }
        });

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> dialog.close());

        HBox buttons = new HBox(10, submitBtn, closeBtn);
        buttons.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(title, emailField, passwordField, message, buttons);

        Scene scene = new Scene(layout, 350, 280);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}


