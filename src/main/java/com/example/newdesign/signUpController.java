package com.example.newdesign;

import javafx.animation.FadeTransition;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class signUpController {

    // 🖼️ Avatar UI
    @FXML private ImageView profileImageView;
    @FXML private ImageView img1, img2, img3;

    // 🧾 Form fields
    @FXML private Button backToLogin;
    @FXML private TextField fNameField;
    @FXML private TextField lNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;

    // 🧠 Selected avatar (IMPORTANT)
    private String selectedAvatar = "default.png";

    // =============================
    // 🚀 INITIALIZE
    // =============================
    @FXML
    public void initialize() {

        // Load avatar options
        img1.setImage(loadImage("avatar1.png"));
        img2.setImage(loadImage("avatar2.png"));
        img3.setImage(loadImage("avatar3.png"));

        // Default profile picture
        profileImageView.setImage(loadImage("default.png"));

        // Make it circular (nice UI)
        Circle clip = new Circle(25, 25, 25);
        profileImageView.setClip(clip);
    }

    // =============================
    // 🖱️ AVATAR SELECTION
    // =============================
    @FXML
    private void selectAvatar1() {
        selectedAvatar = "avatar1.png";
        profileImageView.setImage(loadImage(selectedAvatar));
    }

    @FXML
    private void selectAvatar2() {
        selectedAvatar = "avatar2.png";
        profileImageView.setImage(loadImage(selectedAvatar));
    }

    @FXML
    private void selectAvatar3() {
        selectedAvatar = "avatar3.png";
        profileImageView.setImage(loadImage(selectedAvatar));
    }

    // =============================
    // 📝 REGISTER USER
    // =============================
    @FXML
    public void handleRegister() {

        String fName = fNameField.getText();
        String lName = lNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();

        // validation
        if (fName.isEmpty() || lName.isEmpty()) {
            showAlert("Error", "Name fields cannot be empty");
            return;
        }

        if (!email.contains("@")) {
            showAlert("Error", "Invalid email");
            return;
        }

        if (!phone.matches("\\d{9,12}")) {
            showAlert("Error", "Invalid phone number");
            return;
        }

        if (password.length() < 6) {
            showAlert("Error", "Password must be at least 6 characters");
            return;
        }


        User user = new User(fName, lName, email, phone, password, selectedAvatar);

        UserDAO dao = new UserDAOImpl();
        dao.addUser(user);

        showAlert("Success", "User registered!");
    }

    // =============================
    // 🔙 BACK BUTTON
    // =============================
    @FXML
    protected void backtoLoginOnClick() throws IOException {
        Stage stage = (Stage) backToLogin.getScene().getWindow();
        FXMLLoader fxmlloader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlloader.load(), 1200, 800);
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), scene.getRoot());
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        stage.setScene(scene);
    }

    // =============================
    // 🧰 HELPER METHODS
    // =============================
    private Image loadImage(String name) {
        var url = getClass().getResource("/com/example/newdesign/images/" + name);

        if (url == null) {
            System.out.println("Image NOT FOUND: " + name);
            return null;
        }

        return new Image(url.toExternalForm());
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
