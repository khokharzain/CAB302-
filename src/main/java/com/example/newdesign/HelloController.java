package com.example.newdesign;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

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

            // ✅ FIX: SET USER FIRST (VERY IMPORTANT)
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
}


