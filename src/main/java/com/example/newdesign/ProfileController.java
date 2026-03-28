package com.example.newdesign;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;

public class ProfileController {

    private User currentUser;

    @FXML
    private ImageView profileImage;

    @FXML
    public void initialize() {
        profileImage.setOnMouseClicked(e -> chooseProfilePicture());
    }

    public void setUser(User user) {
        this.currentUser = user;
        loadProfileImage();
    }

    private void chooseProfilePicture() {
        if (currentUser == null) return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                String safeEmail = currentUser.getEmail()
                        .replace("@", "_")
                        .replace(".", "_");

                String newFileName = safeEmail + ".png";

                File destDir = new File("profile_images");
                if (!destDir.exists()) destDir.mkdir();

                File dest = new File(destDir, newFileName);

                Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // ✅ Save to DB
                saveProfilePicture(newFileName);

                // ✅ Update current user object
                currentUser.setProfilePicture(newFileName);

                // ✅ Refresh UI
                loadProfileImage();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveProfilePicture(String filename) {
        try {
            Connection conn = DBconnection.connect();

            String sql = "UPDATE Users SET profile_picture = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, filename);
            stmt.setString(2, currentUser.getEmail());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProfileImage() {
        try {
            if (currentUser.getProfilePicture() != null) {
                File file = new File("profile_images/" + currentUser.getProfilePicture());

                if (file.exists()) {
                    profileImage.setImage(null);
                    profileImage.setImage(new Image(file.toURI().toString(), false));
                    return;
                }
            }

            profileImage.setImage(new Image(
                    getClass().getResource("/com/example/newdesign/images/default.png").toString()
            ));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ BACK BUTTON
    @FXML
    private void handleBack() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("main-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 1200, 800);

        mainController controller = loader.getController();
        controller.setUser(currentUser);

        Stage stage = (Stage) profileImage.getScene().getWindow();
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), scene.getRoot());
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        stage.setScene(scene);
    }
}