package com.example.newdesign.controller;

import com.example.newdesign.model.*;
import com.example.newdesign.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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

    // ✅ NEW: container for trending skills (ADD THIS IN FXML TOO)
    @FXML
    private VBox suggestionsContainer;

    private UserDAOImpl userDAO = new UserDAOImpl();


    @FXML
    public void initialize() {

        // Load top 3 trending skills
        List<String> topSkills = userDAO.getMostWantedSkills(3);
        showSuggestions(topSkills);

        // Runs every time user types something
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {

            if (newValue == null || newValue.trim().isEmpty()) {
                resultsContainer.getChildren().clear();
                return;
            }

            List<User> users = userDAO.searchUsers(newValue.trim());
            updateResults(users);
        });
    }

    //  NEW: show clickable trending skills
    private void showSuggestions(List<String> skills) {

        suggestionsContainer.getChildren().clear();

        Label title = new Label(" Trending Skills");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        suggestionsContainer.getChildren().add(title);

        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER);

        for (String skill : skills) {

            Button skillBtn = new Button(skill);
            skillBtn.prefHeight(20);

            skillBtn.setStyle(
                    "-fx-background-color: #EFEFEF;" +
                            "-fx-border-color:#147A5E;"+
                            "-fx-border-radius:15;"+
                            "-fx-border-width:1;"+
                            "-fx-text-fill: black;" +
                            "-fx-background-radius: 15;" +
                            "-fx-padding: 5 15;"
            );

            // When clicked → search that skill
            skillBtn.setOnAction(e -> {
                searchField.setText(skill);
                List<User> users = userDAO.searchUsers(skill);
                updateResults(users);
            });

            row.getChildren().add(skillBtn);
        }

        suggestionsContainer.getChildren().add(row);
    }


    private void updateResults(List<User> users) {

        resultsContainer.getChildren().clear();

        for (User user : users) {

            HBox card = new HBox(15);
            card.setStyle(
                    "-fx-background-color: #EAEAEA;" +
                            "-fx-border-color: #147A5E;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius:15;" +
                            "-fx-background-radius:15;" +
                            "-fx-padding: 10;"
            );
            card.setPrefHeight(60);

            ImageView imageView = new ImageView();
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);

            Image image;

            try {
                File file = new File("profile_images/" + user.getProfilePicture());

                if (file.exists()) {
                    image = new Image(file.toURI().toString());
                } else {
                    image = new Image(
                            getClass().getResource("/com/example/newdesign/images/default.png").toString()
                    );
                }

            } catch (Exception e) {
                image = new Image(
                        getClass().getResource("/com/example/newdesign/images/default.png").toString()
                );
            }

            imageView.setImage(image);

            Label nameLabel = new Label(
                    user.getFirstName() + " " + user.getLastName()
            );
            nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            card.getChildren().addAll(imageView, nameLabel);

            card.setOnMouseEntered(e ->
                    card.setStyle("-fx-background-color: #C9F5D3; -fx-padding: 10; -fx-background-radius: 15;")
            );

            card.setOnMouseExited(e ->
                    card.setStyle(
                            "-fx-background-color: #EAEAEA;" +
                                    "-fx-border-color: #147A5E;" +
                                    "-fx-border-width: 1;" +
                                    "-fx-border-radius:15;" +
                                    "-fx-background-radius:15;" +
                                    "-fx-padding: 10;"
                    )
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