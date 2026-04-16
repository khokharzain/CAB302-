package com.example.newdesign;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

import java.io.File;

public class mainController {

    private User currentUser;

    @FXML
    private ImageView profilePicture;

    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Button profileButton;
    @FXML
    private Button postButton;

    @FXML
    private VBox profilelayout;

    @FXML
    private Button searchButton;

    @FXML
    private HBox headerBar;

    @FXML
    private HBox bottomNav;

    @FXML
    private Region profileStrip;

    // ================= INIT =================
    @FXML
    public void initialize() {
        User user = SessionManager.getUser();

        if (user != null) {
            setUser(user);
        }

        applyTheme();
    }

    // ================= THEME =================
    private void applyTheme() {
        String gradient = "linear-gradient(to right, "
                + ThemeManager.primaryStart + ", "
                + ThemeManager.primaryEnd + ")";

        String headerStyle =
                "-fx-background-color: " + gradient + ";" +
                        "-fx-background-radius:15;" +
                        "-fx-border-radius:15;" +
                        "-fx-effect: dropshadow(gaussian, #899793, 15, 0.5, 0, 0);";

        if (headerBar != null)
            headerBar.setStyle(headerStyle);

        if (bottomNav != null)
            bottomNav.setStyle("-fx-background-color: " + gradient + ";");

        if (profileStrip != null)
            profileStrip.setStyle("-fx-background-color: " + gradient + ";");
    }

    // ================= THEME BUTTONS =================
    public void setGreen() {
        ThemeManager.primaryStart = "#0C4D3B";
        ThemeManager.primaryEnd = "#0EBB8A";
        ThemeManager.primaryBackGround = "#DCFFE4";
        applyTheme();
    }

    public void setBlue() {
        ThemeManager.primaryStart = "#1E88E5";
        ThemeManager.primaryEnd = "#64B5F6";
        ThemeManager.primaryBackGround = "#DCF3FF";
        applyTheme();
    }

    public void setPurple() {
        ThemeManager.primaryStart = "#6A1B9A";
        ThemeManager.primaryEnd = "#BA68C8";
        ThemeManager.primaryBackGround = "#DFC5E6";
        applyTheme();
    }

    public void setOrange() {
        ThemeManager.primaryStart = "#EF6C00";
        ThemeManager.primaryEnd = "#FFB74D";
        ThemeManager.primaryBackGround = "#FFDCB6";
        applyTheme();
    }

    public void setRed() {
        ThemeManager.primaryStart = "#C62828";
        ThemeManager.primaryEnd = "#EF5350";
        ThemeManager.primaryBackGround = "#E9AEAE";
        applyTheme();
    }


    // ================= USER =================
    public void setUser(User user) {
        this.currentUser = user;

        if (firstNameLabel != null)
            firstNameLabel.setText(user.getFirstName());

        if (lastNameLabel != null)
            lastNameLabel.setText(user.getLastName());

        if (emailLabel != null)
            emailLabel.setText(user.getEmail());

        if (phoneLabel != null)
            phoneLabel.setText(user.getPhone());

        loadProfileImage();
    }

    public User getCurrentUser() {
        return currentUser;
    }


    private void loadProfileImage() {
        try {
            if (currentUser.getProfilePicture() != null) {
                File file = new File("profile_images/" + currentUser.getProfilePicture());

                if (file.exists()) {
                    profilePicture.setImage(new Image(file.toURI().toString(), false));
                    return;
                }
            }

            profilePicture.setImage(new Image(
                    getClass().getResource("/com/example/newdesign/images/default.png").toString()
            ));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ================= NAVIGATION =================


    //handling post page

    public void handlePostButton() throws Exception{
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("post-view.fxml"));

        Scene scene = new Scene(loader.load(), 1200, 800);

        Stage stage = (Stage)  postButton.getScene().getWindow();
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), scene.getRoot());
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        stage.setScene(scene);

    }
    public void handleProfileButton() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("profile-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 1200, 800);

        Stage stage = (Stage) profileButton.getScene().getWindow();
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), scene.getRoot());
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        stage.setScene(scene);
    }

    public void handleProfClick() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("profile-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 1200, 800);

        Stage stage = (Stage) profilelayout.getScene().getWindow();
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), scene.getRoot());
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        stage.setScene(scene);
    }

    public void handleSearchButton() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("search-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 1200, 800);
        SessionManager.setUser(currentUser);

        Stage stage = (Stage) searchButton.getScene().getWindow();
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), scene.getRoot());
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        stage.setScene(scene);
    }
}