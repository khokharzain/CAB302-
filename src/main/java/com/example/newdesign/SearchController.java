package com.example.newdesign;

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
import javafx.scene.layout.BorderPane;
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

    //Added new buttons @zain and Amir to allow, Dysfunctional AI Page, now alllows access to request page.
    @FXML
    private Button requestPageButton;
    //Added new PostPage option to be able to access request page through post page @zain and @amir
    @FXML
    private Button postPageButton;

    @FXML
    private Label searchLabel;

    // container for trending skills (ADD THIS IN FXML TOO)
    @FXML
    private VBox suggestionsContainer;

    @FXML
    private StackPane popupLayer;

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
            card.setOnMouseClicked(e -> {
                User fullUser = userDAO.getUserById(user.getId());
                showUserPopUp(fullUser);
            });

            resultsContainer.getChildren().add(card);
        }
    }

    // this is the container that show all informations from the choosen user.

    private void showUserPopUp(User user) {

        popupLayer.getChildren().clear();
        popupLayer.setVisible(true);

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");
        overlay.setAlignment(Pos.CENTER);

        BorderPane popup = new BorderPane();
        popup.setPrefSize(900, 620);
        popup.setMaxSize(900, 620);
        popup.setStyle(
                "-fx-background-color:" +  ThemeManager.primaryBackGround + ";" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 25;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 30, 0, 0, 8);"
        );

        Button closeBtn = new Button("✕");
        closeBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #7a0f6f;" +
                        "-fx-cursor: hand;"
        );
        closeBtn.setOnAction(e -> popupLayer.setVisible(false));

        HBox topBar = new HBox(closeBtn);
        topBar.setAlignment(Pos.TOP_RIGHT);
        popup.setTop(topBar);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(false);

        try {
            Image image;
            if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
                File file = new File("profile_images/" + user.getProfilePicture());
                image = file.exists()
                        ? new Image(file.toURI().toString())
                        : new Image(getClass().getResource("/com/example/newdesign/images/default.png").toString());
            } else {
                image = new Image(getClass().getResource("/com/example/newdesign/images/default.png").toString());
            }
            imageView.setImage(image);
        } catch (Exception e) {
            imageView.setImage(new Image(getClass().getResource("/com/example/newdesign/images/default.png").toString()));
        }

        imageView.setClip(new javafx.scene.shape.Circle(60, 60, 60));

        Label name = new Label(user.getFullName());
        name.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #222;");

        Label username = new Label("@" + user.getUsername());
        username.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        Label email = new Label("Email: " + user.getEmail());
        email.setWrapText(true);

        VBox leftCard = new VBox(14, imageView, name, username, email);
        leftCard.setAlignment(Pos.TOP_CENTER);
        leftCard.setPrefWidth(260);
        leftCard.setStyle(
                "-fx-background-color:" +  ThemeManager.primaryBackGround + ";" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 25;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 18, 0, 0, 6);"
        );

        VBox rightPanel = new VBox(18);
        rightPanel.setPrefWidth(560);
        rightPanel.setStyle(
                "-fx-background-color:" +  ThemeManager.primaryBackGround + ";" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 30;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 18, 0, 0, 6);"
        );

        Label bioTitle = new Label("Bio");
        bioTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label bio = new Label(user.getBio() == null || user.getBio().isEmpty() ? "No bio added yet." : user.getBio());
        bio.setWrapText(true);
        bio.setStyle("-fx-font-size: 15px;");

        Label skillsTitle = new Label("Skills");
        skillsTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label skills = new Label(
                user.getSkills() == null || user.getSkills().isEmpty()
                        ? "No skills added yet."
                        : user.getSkills().stream()
                        .map(Skill::toString)
                        .collect(java.util.stream.Collectors.joining(", "))
        );
        skills.setWrapText(true);
        skills.setStyle("-fx-font-size: 15px;");

        Label hobbiesTitle = new Label("Hobbies");
        hobbiesTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label hobbies = new Label(
                user.getHobbies() == null || user.getHobbies().isEmpty()
                        ? "No hobbies added yet."
                        : user.getHobbies().stream()
                        .map(Hobby::toString)
                        .collect(java.util.stream.Collectors.joining(", "))
        );
        hobbies.setWrapText(true);
        hobbies.setStyle("-fx-font-size: 15px;");

        rightPanel.getChildren().addAll(
                bioTitle, bio,
                skillsTitle, skills,
                hobbiesTitle, hobbies
        );

        HBox content = new HBox(25, leftCard, rightPanel);
        content.setAlignment(Pos.CENTER);

        popup.setCenter(content);

        overlay.getChildren().add(popup);
        overlay.setOnMouseClicked(e -> popupLayer.setVisible(false));
        popup.setOnMouseClicked(e -> e.consume());

        popupLayer.getChildren().add(overlay);
    }


//Allowed a new FXML Function to be able to be directed to the requests page through the search page. @zain and @amir
    @FXML
    private void handleRequestPage() throws Exception {
        FXMLLoader fxmlloader = new FXMLLoader(HelloApplication.class.getResource("requests-view.fxml"));
        Scene scene = new Scene(fxmlloader.load(), 1200, 800);
        Stage stage = (Stage) requestPageButton.getScene().getWindow();
        stage.setScene(scene);

    }

    @FXML
    private void handlePostPage() throws Exception {
        FXMLLoader fxmlloader = new FXMLLoader(HelloApplication.class.getResource("post-view.fxml"));
        Scene scene = new Scene(fxmlloader.load(), 1200, 800);
        Stage stage = (Stage) postPageButton.getScene().getWindow();
        stage.setScene(scene);

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