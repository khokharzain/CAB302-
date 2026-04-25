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
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

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
    @FXML
    private HBox headerBar;

    @FXML
    private HBox bottomNav;

    @FXML
    private Label searchLabel;

    // container for trending skills (ADD THIS IN FXML TOO)
    @FXML
    private VBox suggestionsContainer;

    @FXML
    private StackPane popupLayer;

    private UserDAOImpl userDAO = new UserDAOImpl();
    public static User Otheruser;

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

        applyTheme();
    }

    private void applyTheme(){
        String gradient = "linear-gradient(to right, " +
                ThemeManager.primaryStart + "," +
                ThemeManager.primaryEnd + ")";

        String headerStyle =
                "-fx-background-color: " + gradient + ";" +
                        "-fx-background-radius:15;" +
                        "-fx-border-radius:15;" +
                        "-fx-effect: dropshadow(gaussian, #899793, 15, 0.5, 0, 0);";

        if(headerBar != null){
            headerBar.setStyle(headerStyle);


        }

        if(bottomNav != null){
            bottomNav.setStyle("-fx-background-color:" + gradient + ";");

        }
        if(searchLabel!= null){
            searchLabel.setStyle("-fx-background-color: transparent;-fx-text-fill:" + gradient + "; -fx-font-size: 36px; -fx-font-weight: bold;");
        }

        if(searchField != null){
            if (searchField != null) {
                searchField.setStyle(
                        "-fx-background-color: #EFEFEF;" +
                                "-fx-border-color: " + ThemeManager.primaryStart + ";" +
                                "-fx-border-radius: 25;" +
                                "-fx-border-width: 2;" +
                                "-fx-background-radius: 25;" +
                                "-fx-padding: 0 15;" +
                                "-fx-font-size: 14px;"
                );
            }
        }
    }

    //   show clickable trending skills
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
                            "-fx-border-color:"+ThemeManager.primaryStart + ";"+
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
                            "-fx-border-color: "+ ThemeManager.primaryStart + ";" +
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
                    card.setStyle("-fx-background-color: " + ThemeManager.primaryBackGround + "; -fx-padding: 10; -fx-background-radius: 15;")
            );

            card.setOnMouseExited(e ->
                    card.setStyle(
                            "-fx-background-color: #EAEAEA;" +
                                    "-fx-border-color: " + ThemeManager.primaryStart + ";" +
                                    "-fx-border-width: 1;" +
                                    "-fx-border-radius:15;" +
                                    "-fx-background-radius:15;" +
                                    "-fx-padding: 10;"
                    )
            );

            //Set to view profile of clicked box
            card.setOnMouseClicked(event -> {
                try {
                    Otheruser = userDAO.getUserById(user.getId());
                    handleOtherProfileButton();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            resultsContainer.getChildren().add(card);
        }
    }

    // this is the container that show all information from the choosen user.

    private void showUserPopUp(User user) {

        popupLayer.getChildren().clear();
        popupLayer.setVisible(true);


        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.4);");
        overlay.setAlignment(Pos.CENTER);

        // the layout
        VBox card = new VBox(15);
        card.setMaxWidth(300);
        card.setMaxHeight(400);
        card.setStyle(
                "-fx-background-color: " + ThemeManager.primaryBackGround+ ";" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 20;" +
                        "-fx-border-radius: 20;" +
                        "-fx-border-color: " + ThemeManager.primaryStart + ";" +
                        "-fx-border-width: 5;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);"
        );

        // ===== TOP BAR =====
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_RIGHT);

        Button closeBtn = new Button("X");
        closeBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: " + ThemeManager.primaryStart + ";" +
                        "-fx-font-weight: bold;"
        );
        closeBtn.setOnAction(e -> popupLayer.setVisible(false));

        topBar.getChildren().add(closeBtn);

        // ===== PROFILE (IMAGE + NAME) =====
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        Image image;
        try {
            if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
                File file = new File("profile_images/" + user.getProfilePicture());
                if (file.exists()) {
                    image = new Image(file.toURI().toString());
                } else {
                    image = new Image(getClass()
                            .getResource("/com/example/newdesign/images/default.png")
                            .toString());
                }
            } else {
                image = new Image(getClass()
                        .getResource("/com/example/newdesign/images/default.png")
                        .toString());
            }
        } catch (Exception e) {
            image = new Image(getClass()
                    .getResource("/com/example/newdesign/images/default.png")
                    .toString());
        }

        imageView.setImage(image);

        // make image round
        imageView.setClip(new javafx.scene.shape.Circle(25, 25, 25));

        VBox nameBox = new VBox(2);

        Label name = new Label(user.getFullName());
        name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label username = new Label("@" + user.getUsername());
        username.setStyle("-fx-text-fill: gray;");

        nameBox.getChildren().addAll(name, username);
        header.getChildren().addAll(imageView, nameBox);

        // ===== INFO =====

        Label email = new Label("email: " + user.getEmail());

        Label bio = new Label("Bio: " + (user.getBio() == null ? "No bio" : user.getBio()));
        bio.setWrapText(true);

        Label skills = new Label(
                "Skills: " + (user.getSkills() == null || user.getSkills().isEmpty() ? "None" :
                        user.getSkills().stream()
                                .map(Skill::toString)
                                .collect(java.util.stream.Collectors.joining(", "))
                )
        );

        Label hobbies = new Label(
                "Hobbies: " + (user.getHobbies() == null || user.getHobbies().isEmpty() ? "None" :
                        user.getHobbies().stream()
                                .map(Hobby::toString)
                                .collect(java.util.stream.Collectors.joining(", "))
                )
        );

        // ===== ADD ALL =====
        card.getChildren().addAll(topBar, header, email, bio, skills, hobbies);

        overlay.getChildren().add(card);

        // click outside closes popup
        overlay.setOnMouseClicked(e -> popupLayer.setVisible(false));

        // prevent closing when clicking card
        card.setOnMouseClicked(e -> e.consume());

        popupLayer.getChildren().add(overlay);
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

    //Other User Profile views
    @FXML
    private void handleOtherProfileButton() throws Exception{
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("otherUserProfile-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) profileButton.getScene().getWindow();
        stage.setScene(scene);
    }



}