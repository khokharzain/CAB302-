package com.example.newdesign;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class SearchController {

    @FXML
    private Button profileButton;

    @FXML
    private Button homeButton;

    @FXML
    private TextField searchField;

    @FXML
    private VBox resultsContainer;

    private UserDAOImpl userDAO = new UserDAOImpl();





    @FXML
    public void initialize() {

        // Runs every time user types something
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {

            // If empty → clear results
            if (newValue == null || newValue.trim().isEmpty()) {
                resultsContainer.getChildren().clear();
                return;
            }

            // Get users from database
            List<User> users = userDAO.searchUsers(newValue.trim());

            // Update UI
            updateResults(users);
        });
    }

    private void updateResults(List<User> users) {

        resultsContainer.getChildren().clear();

        for (User user : users) {

            // Card container
            HBox card = new HBox(15);
            card.setStyle(
                    "-fx-background-color: #C9CABC;" +
                            "-fx-border-color: #147A5E;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius:15;" +
                            "-fx-background-radius:15;" +
                            "-fx-padding: 10;"

            );
            card.setPrefHeight(60);

            // Profile image
            ImageView imageView = new ImageView();
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);

            Image image;

            try {
                // Try loading from uploaded folder
                File file = new File("profile_images/" + user.getProfilePicture());

                if (file.exists()) {
                    image = new Image(file.toURI().toString());
                } else {
                    // fallback to default image in resources
                    image = new Image(
                            getClass().getResource("/com/example/newdesign/images/default.png").toString()
                    );
                }

            } catch (Exception e) {
                // fallback safety
                image = new Image(
                        getClass().getResource("/com/example/newdesign/images/default.png").toString()
                );
            }

            imageView.setImage(image);

            // Name label
            Label nameLabel = new Label(
                    user.getFirstName() + " " + user.getLastName()
            );
            nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            card.getChildren().addAll(imageView, nameLabel);

            // Optional: hover effect (makes it feel modern)
            card.setOnMouseEntered(e ->
                    card.setStyle("-fx-background-color: #EAEAEA; -fx-padding: 10; -fx-background-radius: 15;")
            );

            card.setOnMouseExited(e ->
                    card.setStyle("-fx-background-color: #C9CABC;" +
            "-fx-border-color: #147A5E;" +
                    "-fx-border-width: 1;" +
                            "-fx-border-radius:15;" +
                            "-fx-background-radius:15;" +
                            "-fx-padding: 10;")
            );

            resultsContainer.getChildren().add(card);
        }
    }

    @FXML
    private void handleHomeButton() throws Exception{
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));

        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) homeButton.getScene().getWindow();
        stage.setScene(scene);

    }

    @FXML
    private void handleProfileButton() throws Exception{
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("profile-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) profileButton.getScene().getWindow();
        stage.setScene(scene);
    }
}