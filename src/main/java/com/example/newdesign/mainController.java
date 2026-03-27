package com.example.newdesign;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

        ProfileController controller = loader.getController();
        controller.setUser(currentUser); // 🔥 CRITICAL

        Stage stage = (Stage) profileButton.getScene().getWindow();
        stage.setScene(scene);
    }

    // ✅ Open profile page (Click card)
    public void handleProfClick() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("profile-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 1200, 800);

        ProfileController controller = loader.getController();
        controller.setUser(currentUser); // 🔥 CRITICAL

        Stage stage = (Stage) profilelayout.getScene().getWindow();
        stage.setScene(scene);
    }
}