package com.example.newdesign;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
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
    private VBox profilelayout;

    //the getter for the search button in main page
    @FXML
    private Button searchButton;



    // getting user form sessionManager
    @FXML
    public void initialize() {
        User user = SessionManager.getUser();

        if (user != null) {
            setUser(user);
        }
    }


    // ✅ Set user when logged in
    public void setUser(User user) {
        this.currentUser = user;

        firstNameLabel.setText(user.getFirstName());
        lastNameLabel.setText(user.getLastName());
        emailLabel.setText(user.getEmail());
        phoneLabel.setText(user.getPhone());

        loadProfileImage();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // ✅ Load profile image (from DB or local folder)
    private void loadProfileImage() {
        try {
            if (currentUser.getProfilePicture() != null) {
                File file = new File("profile_images/" + currentUser.getProfilePicture());

                if (file.exists()) {
                    profilePicture.setImage(null);
                    profilePicture.setImage(new Image(file.toURI().toString(), false));
                    return;
                }
            }

            // fallback default
            profilePicture.setImage(new Image(
                    getClass().getResource("/com/example/newdesign/images/default.png").toString()
            ));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Open profile page (Button)
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

    // ✅ Open profile page (Click card)
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


    public void handleSearchButton() throws Exception{
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