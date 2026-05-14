package com.example.newdesign.controller;

import com.example.newdesign.*;
import com.example.newdesign.model.*;
import com.example.newdesign.controller.AIController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.scene.shape.Circle;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.util.List;

public class mainController {

    // ================== STATE ==================
    private User currentUser;

    // ================== MAIN LAYOUT ==================
    @FXML private VBox mainContent;
    @FXML private StackPane popupLayer;

    // ================== HEADER ==================
    @FXML private HBox headerBar;
    @FXML private Region profileStrip;

    // ================== NAVIGATION ==================
    @FXML private HBox bottomNav;
    @FXML private Button profileButton;
    @FXML private Button postButton;
    @FXML private Button searchButton;
    @FXML private Button requestPageButton;

    // ================== PROFILE SECTION ==================
    @FXML private VBox profilelayout;
    @FXML private ImageView profilePicture;
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;

    // ================== AI PANEL REFERENCE ==================
    @FXML private VBox aiPanel;
    @FXML private HBox aiHeader;
    @FXML private VBox aiResponseArea;
    @FXML private Button floatingAISummoner;
    @FXML private Button exitButton;
    @FXML private VBox actionButtonsPanel;
    @FXML private VBox skillSelectionPanel;
    @FXML private ComboBox<String> skillCombo;
    @FXML private VBox comparePanel;
    @FXML private VBox compareSkillSelectionPanel;
    @FXML private ComboBox<String> compareSkillCombo;
    @FXML private VBox usersToCompareContainer;

    private AIController aiController;
    public static mainController instance;

    @FXML
    public void initialize() {
        instance = this;
        User sessionUser = SessionManager.getUser();
        if (sessionUser != null) {
            User fullUser = new UserDAOImpl().getUserById(sessionUser.getId());
            SessionManager.setUser(fullUser);
            setUser(fullUser);
        }

        applyTheme();
        loadPosts();

        // Initialize AI Controller and delegate
        aiController = new AIController();
        injectAIControllerComponents();
        aiController.initialize();
    }

    private void injectAIControllerComponents() {
        try {
            java.lang.reflect.Field field;

            field = AIController.class.getDeclaredField("aiPanel"); field.setAccessible(true); field.set(aiController, aiPanel);
            field = AIController.class.getDeclaredField("aiResponseArea"); field.setAccessible(true); field.set(aiController, aiResponseArea);
            field = AIController.class.getDeclaredField("floatingAISummoner"); field.setAccessible(true); field.set(aiController, floatingAISummoner);
            field = AIController.class.getDeclaredField("exitButton"); field.setAccessible(true); field.set(aiController, exitButton);
            field = AIController.class.getDeclaredField("actionButtonsPanel"); field.setAccessible(true); field.set(aiController, actionButtonsPanel);
            field = AIController.class.getDeclaredField("skillSelectionPanel"); field.setAccessible(true); field.set(aiController, skillSelectionPanel);
            field = AIController.class.getDeclaredField("skillCombo"); field.setAccessible(true); field.set(aiController, skillCombo);
            field = AIController.class.getDeclaredField("comparePanel"); field.setAccessible(true); field.set(aiController, comparePanel);
            field = AIController.class.getDeclaredField("compareSkillSelectionPanel"); field.setAccessible(true); field.set(aiController, compareSkillSelectionPanel);
            field = AIController.class.getDeclaredField("compareSkillCombo"); field.setAccessible(true); field.set(aiController, compareSkillCombo);
            field = AIController.class.getDeclaredField("usersToCompareContainer"); field.setAccessible(true); field.set(aiController, usersToCompareContainer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyTheme() {
        String gradient = "linear-gradient(to right, "
                + ThemeManager.primaryStart + ", "
                + ThemeManager.primaryEnd + ")";

        String headerStyle = "-fx-background-color: " + gradient + ";" +
                "-fx-background-radius:15;" +
                "-fx-border-radius:15;" +
                "-fx-effect: dropshadow(gaussian, #899793, 15, 0.5, 0, 0);";

        if (headerBar != null) headerBar.setStyle(headerStyle);
        if (bottomNav != null) bottomNav.setStyle("-fx-background-color: " + gradient + ";");
        if (profileStrip != null) profileStrip.setStyle("-fx-background-color: " + gradient + ";");

        // Set AI panel header background gradient (the colored bar behind the icon and X button)
        if (aiHeader != null) {
            aiHeader.setStyle("-fx-background-color: " + gradient + "; -fx-background-radius: 13 13 0 0;");
        }

        // Style the floating AI button
        if (floatingAISummoner != null) {
            floatingAISummoner.setStyle("-fx-background-color: " + gradient + "; -fx-background-radius: 50; -fx-padding: 8; -fx-cursor: hand;");
        }
    }

    public void setGreen() {
        ThemeManager.primaryStart = "#0C4D3B";
        ThemeManager.primaryEnd = "#0EBB8A";
        ThemeManager.primaryBackGround = "#F1FBF0";
        applyTheme();
        loadPosts();
    }

    public void setBlue() {
        ThemeManager.primaryStart = "#1E88E5";
        ThemeManager.primaryEnd = "#64B5F6";
        ThemeManager.primaryBackGround = "#F0F8FB";
        applyTheme();
        loadPosts();
    }

    public void setPurple() {
        ThemeManager.primaryStart = "#6A1B9A";
        ThemeManager.primaryEnd = "#BA68C8";
        ThemeManager.primaryBackGround = "#FAF0FB";
        applyTheme();
        loadPosts();
    }

    public void setOrange() {
        ThemeManager.primaryStart = "#EF6C00";
        ThemeManager.primaryEnd = "#FFB74D";
        ThemeManager.primaryBackGround = "#FBF4F0";
        applyTheme();
        loadPosts();
    }

    public void setRed() {
        ThemeManager.primaryStart = "#C62828";
        ThemeManager.primaryEnd = "#EF5350";
        ThemeManager.primaryBackGround = "#FBF0F0";
        applyTheme();
        loadPosts();
    }

    public void setUser(User user) {
        this.currentUser = user;
        if (firstNameLabel != null) firstNameLabel.setText(user.getFirstName());
        if (lastNameLabel != null) lastNameLabel.setText(user.getLastName());
        if (emailLabel != null) emailLabel.setText(user.getEmail());
        if (phoneLabel != null) phoneLabel.setText(user.getPhone());
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
                } else {
                    profilePicture.setImage(new Image(getClass().getResource("/com/example/newdesign/images/default.png").toString()));
                }
            } else {
                profilePicture.setImage(new Image(getClass().getResource("/com/example/newdesign/images/default.png").toString()));
            }
            Circle clip = new Circle(30, 30, 30);
            profilePicture.setClip(clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPosts() {
        PostDAO postdao = new PostDaoImpl();
        UserDAO userDao = new UserDAOImpl();
        List<Post> posts = postdao.getAllPosts();
        mainContent.getChildren().clear();
        for (Post post : posts) {
            User user = userDao.getUserById(post.getUserId());
            StackPane card = createPostCard(post, user);
            mainContent.getChildren().add(card);
        }
    }

    private StackPane createPostCard(Post post, User user) {
        PostParticipantDAO participantDAO = new PostParticipantDaoImpl();
        JoinRequestDao requestDAO = new JoinRequestDaoImpl();
        UserDAO userDAO = new UserDAOImpl();
        StackPane root = new StackPane();
        VBox card = new VBox(12);
        card.setPrefWidth(900);
        card.setMaxWidth(900);
        card.setPrefHeight(320);
        card.setStyle("-fx-background-color: linear-gradient(from 100% 0% to 0% 0%, " +
                ThemeManager.primaryBackGround + ", white);" +
                "-fx-background-radius: 15;" +
                "-fx-padding: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        HBox userBox = new HBox(10);
        userBox.setAlignment(Pos.CENTER_LEFT);
        ImageView profileImage = new ImageView();
        profileImage.setFitWidth(40);
        profileImage.setFitHeight(40);
        try {
            if (user != null && user.getProfilePicture() != null) {
                File file = new File("profile_images/" + user.getProfilePicture());
                if (file.exists()) {
                    profileImage.setImage(new Image(file.toURI().toString()));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        if (profileImage.getImage() == null) {
            profileImage.setImage(new Image(getClass().getResource("/com/example/newdesign/images/default.png").toExternalForm()));
        }
        profileImage.setClip(new Circle(20, 20, 20));
        profileImage.setOnMouseClicked(e -> showUserPopUp(user));
        Label userName = new Label(user != null ? user.getFirstName() + " " + user.getLastName() : "Unknown");
        userName.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy • HH:mm");
        Label date = new Label(post.getCreatedAt().format(formatter));
        date.setStyle("-fx-text-fill: gray; -fx-font-size: 12;");
        VBox nameBox = new VBox(userName, date);
        userBox.getChildren().addAll(profileImage, nameBox);

        Label content = new Label(post.getContent());
        content.setWrapText(true);

        HBox boxContainer = new HBox(8);
        List<Integer> participants = participantDAO.getUserIdsByPost(post.getId());
        int max = post.getMaxParticipants();
        User currentUser = SessionManager.getUser();

        for (int i = 0; i < max; i++) {
            VBox box = new VBox();
            box.setPrefSize(100, 100);
            box.setAlignment(Pos.CENTER);
            box.setStyle("-fx-border-color: " + ThemeManager.primaryEnd + "; -fx-border-radius: 5; -fx-background-radius: 5;");
            if (i < participants.size()) {
                User pUser = userDAO.getUserById(participants.get(i));
                ImageView avatar = new ImageView();
                avatar.setFitWidth(60);
                avatar.setFitHeight(60);
                try {
                    if (pUser != null && pUser.getProfilePicture() != null) {
                        File file = new File("profile_images/" + pUser.getProfilePicture());
                        if (file.exists()) {
                            avatar.setImage(new Image(file.toURI().toString()));
                        }
                    }
                } catch (Exception e) { e.printStackTrace(); }
                if (avatar.getImage() == null) {
                    avatar.setImage(new Image(getClass().getResource("/com/example/newdesign/images/default.png").toExternalForm()));
                }
                avatar.setClip(new Circle(30, 30, 30));
                Label name = new Label(pUser.getFirstName());
                name.setStyle("-fx-font-size: 10px;");
                box.getChildren().addAll(avatar, name);
                avatar.setOnMouseClicked(e -> showUserPopUp(pUser));
            } else {
                Label plus = new Label("+");
                plus.setStyle("-fx-font-size: 18px; -fx-text-fill: gray;");
                Label slotLabel = new Label("Member " + (i + 1));
                slotLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");
                box.getChildren().addAll(plus, slotLabel);
                final int currentPostId = post.getId();
                box.setOnMouseClicked(e -> {
                    if (currentUser == null) {
                        Notifier.showToast(root, "Please login first");
                        return;
                    }
                    if (!requestDAO.exists(currentPostId, currentUser.getId())) {
                        requestDAO.create(currentPostId, currentUser.getId(), "PENDING");
                        Notifier.showToast(root, "Request sent!");
                    } else {
                        Notifier.showToast(root, "Already requested!");
                    }
                });
            }
            boxContainer.getChildren().add(box);
        }

        Separator separator = new Separator();
        card.getChildren().addAll(userBox, separator, content, boxContainer);
        root.getChildren().add(card);
        return root;
    }

    private void showUserPopUp(User user) {
        popupLayer.getChildren().clear();
        popupLayer.setVisible(true);
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.4);");
        overlay.setAlignment(Pos.CENTER);
        VBox card = new VBox(15);
        card.setMaxWidth(300);
        card.setMaxHeight(400);
        card.setStyle("-fx-background-color: " + ThemeManager.primaryBackGround + ";" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 20;" +
                "-fx-border-radius: 20;" +
                "-fx-border-color: " + ThemeManager.primaryStart + ";" +
                "-fx-border-width: 5;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_RIGHT);
        Button closeBtn = new Button("X");
        closeBtn.setStyle("-fx-background-color: transparent;" +
                "-fx-text-fill: " + ThemeManager.primaryStart + ";" +
                "-fx-font-weight: bold;");
        closeBtn.setOnAction(e -> popupLayer.setVisible(false));
        topBar.getChildren().add(closeBtn);

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
                    image = new Image(getClass().getResource("/com/example/newdesign/images/default.png").toString());
                }
            } else {
                image = new Image(getClass().getResource("/com/example/newdesign/images/default.png").toString());
            }
        } catch (Exception e) {
            image = new Image(getClass().getResource("/com/example/newdesign/images/default.png").toString());
        }
        imageView.setImage(image);
        imageView.setClip(new javafx.scene.shape.Circle(25, 25, 25));
        VBox nameBox = new VBox(2);
        Label name = new Label(user.getFullName());
        name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label username = new Label("@" + user.getUsername());
        username.setStyle("-fx-text-fill: gray;");
        nameBox.getChildren().addAll(name, username);
        header.getChildren().addAll(imageView, nameBox);

        Label email = new Label("email: " + user.getEmail());
        Label bio = new Label("Bio: " + (user.getBio() == null ? "No bio" : user.getBio()));
        bio.setWrapText(true);
        Label skills = new Label("Skills: " + (user.getSkills() == null || user.getSkills().isEmpty() ? "None" :
                user.getSkills().stream().map(Skill::toString).collect(java.util.stream.Collectors.joining(", "))));
        Label hobbies = new Label("Hobbies: " + (user.getHobbies() == null || user.getHobbies().isEmpty() ? "None" :
                user.getHobbies().stream().map(Hobby::toString).collect(java.util.stream.Collectors.joining(", "))));
        Label rating = new Label("rating: " + String.valueOf(user.getAverageRating()));
        rating.setStyle("_fx-text-fill: gold");
        Separator separator = new Separator();
        card.getChildren().addAll(topBar, header, separator, email, rating, bio, skills, hobbies);
        overlay.getChildren().add(card);
        overlay.setOnMouseClicked(e -> popupLayer.setVisible(false));
        card.setOnMouseClicked(e -> e.consume());
        popupLayer.getChildren().add(overlay);
    }

    // ================= AI DELEGATION METHODS =================
    @FXML public void toggleAIPanel() { if (aiController != null) aiController.toggleAIPanel(); }
    @FXML public void closeAIPanel() { if (aiController != null) aiController.closeAIPanel(); }
    @FXML public void resetToMainMenu() { if (aiController != null) aiController.resetToMainMenu(); }
    @FXML public void handleFindMatches() { if (aiController != null) aiController.handleFindMatches(); }
    @FXML public void hideSkillSelectionPanel() { if (aiController != null) aiController.hideSkillSelectionPanel(); }
    @FXML public void findMatchesForSelectedSkill() { if (aiController != null) aiController.findMatchesForSelectedSkill(); }
    @FXML public void showComparePanel() { if (aiController != null) aiController.showComparePanel(); }
    @FXML public void hideCompareSkillSelectionPanel() { if (aiController != null) aiController.hideCompareSkillSelectionPanel(); }
    @FXML public void backToSkillSelection() { if (aiController != null) aiController.backToSkillSelection(); }
    @FXML public void showUsersForCompare() { if (aiController != null) aiController.showUsersForCompare(); }
    @FXML public void handleCompareSelectedUsers() { if (aiController != null) aiController.handleCompareSelectedUsers(); }
    @FXML public void handleGroupChat() { if (aiController != null) aiController.handleGroupChat(); }

    // ================= NAVIGATION BUTTONS =================
    public void handlePostButton() throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("post-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) postButton.getScene().getWindow();
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), scene.getRoot());
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        stage.setScene(scene);
    }

    public void handleProfileButton() throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("profile-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) profileButton.getScene().getWindow();
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), scene.getRoot());
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        stage.setScene(scene);
    }

    public void handleRequestPage() throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("requests-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) requestPageButton.getScene().getWindow();
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), scene.getRoot());
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        stage.setScene(scene);
    }

    public void handleProfClick() throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("profile-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) profilelayout.getScene().getWindow();
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), scene.getRoot());
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        stage.setScene(scene);
    }

    public void handleSearchButton() throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("search-view.fxml"));
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