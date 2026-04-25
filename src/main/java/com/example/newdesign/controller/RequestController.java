package com.example.newdesign.controller;

import com.example.newdesign.*;
import com.example.newdesign.model.*;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.List;

public class RequestController {

    @FXML
    private VBox requestsContainer;
    @FXML
    private Button homeButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button postButton;
    @FXML
    private Button requestPageButton;
    @FXML
    private Button profileButton;


    //the theme changer field ids
    @FXML
    private HBox headerBar;
    @FXML
    private HBox bottomNav;

    // stackpane for popup layer
    @FXML
    private StackPane popupLayer;


    private JoinRequestDao requestDAO = new JoinRequestDaoImpl();
    private UserDAO userDAO = new UserDAOImpl();
    private PostDAO postDAO = new PostDaoImpl();
    private PostParticipantDAO participantDAO = new PostParticipantDaoImpl();
    User currentUser;

    public void initialize() {
        loadRequests();
        applyTheme();
    }


    // ThemeChanging function in here
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
            bottomNav.setStyle("-fx-background-color: " + gradient + ";");}

    // ================= LOAD REQUESTS =================
    private void loadRequests() {

        User currentUser = SessionManager.getUser();

        if (currentUser == null) {
            System.out.println("No user logged in");
            return;
        }

        List<JoinRequest> requests =
                requestDAO.getRequestsForUserPosts(currentUser.getId());

        requestsContainer.getChildren().clear();

        for (JoinRequest req : requests) {
            VBox card = createRequestCard(req);
            requestsContainer.getChildren().add(card);
        }
    }

    // ================= CREATE CARD =================
    private VBox createRequestCard(JoinRequest request) {

        User requester = userDAO.getUserById(request.getRequesterId());
        Post post = postDAO.getPostById(request.getPostId());


        if (post == null) {
            VBox errorCard = new VBox();
            errorCard.getChildren().add(
                    new Label("Post not found (ID: " + request.getPostId() + ")")
            );
            return errorCard;
        }

        VBox card = new VBox(10);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #ddd;" +
                        "-fx-border-radius: 10;"
        );

        //  requester name
        Label name = new Label(
                requester.getFirstName() + " " + requester.getLastName()
        );
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        // 📝 post content
        Label content = new Label("Wants to join: " + post.getContent());
        content.setWrapText(true);

        // 🔘 buttons
        HBox actions = new HBox(10);

        Button acceptBtn = new Button("Accept");
        Button rejectBtn = new Button("Reject");

        acceptBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        rejectBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        actions.getChildren().addAll(acceptBtn, rejectBtn);

        // ================= ACCEPT =================
        acceptBtn.setOnAction(e -> {

            int currentCount = participantDAO.getUserIdsByPost(post.getId()).size();

            if (currentCount >= post.getMaxParticipants()) {

                return;
            }

            participantDAO.add(
                    request.getPostId(),
                    request.getRequesterId()
            );

            requestDAO.updateStatus(request.getId(), "ACCEPTED");



            loadRequests();
            refreshMainPosts();
        });

        // ================= REJECT =================
        rejectBtn.setOnAction(e -> {

            requestDAO.updateStatus(request.getId(), "REJECTED");



            loadRequests();
        });
        card.setOnMouseClicked(e -> {
            User fullUser = userDAO.getUserById(requester.getId());
            showUserPopUp(fullUser);
        });

        card.getChildren().addAll(name, content, actions);




        return card;
    }

    // ================= REFRESH MAIN =================
    private void refreshMainPosts() {
        if (mainController.instance != null) {
            mainController.instance.loadPosts();
        }
    }








    //getting userInfomration
    // this is just an popup layer that pops up after clicking user card
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










    //handling all Buttons in here
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

    public void handleRequestPage() throws Exception{
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("requests-view.fxml")
        );
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) requestPageButton.getScene().getWindow();
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), scene.getRoot());
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        stage.setScene(scene);

    }

    public void handlePostButton() throws Exception{
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("post-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) postButton.getScene().getWindow();
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