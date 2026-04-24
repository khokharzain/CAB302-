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
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.shape.Circle;
import java.time.format.DateTimeFormatter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class mainController {

    private User currentUser;


    @FXML
    private VBox mainContent;
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

    @FXML
    private Button floatingAISummoner;

    @FXML
    private VBox aiPanel;

    @FXML
    private VBox actionButtonsPanel;

    @FXML
    private Button exitButton;

    @FXML
    private VBox comparePanel;

    @FXML
    private VBox skillSelectionPanel;

    @FXML
    private ComboBox<String> skillCombo;

    @FXML
    private VBox compareSkillSelectionPanel;

    @FXML
    private ComboBox<String> compareSkillCombo;

    @FXML
    private VBox usersToCompareContainer;

    @FXML
    private VBox aiResponseArea;
    @FXML
    private HBox aiHeader;

    private UserDAOImpl userDAO = new UserDAOImpl();
    private List<User> allUsers = new ArrayList<>();
    private List<User> filteredUsers = new ArrayList<>();
    private List<CheckBox> userCheckBoxes = new ArrayList<>();

    @FXML
    public void initialize() {

        loadPosts();
        User sessionUser = SessionManager.getUser();
        if (sessionUser != null) {
            User fullUser = userDAO.getUserById(sessionUser.getId());
            SessionManager.setUser(fullUser);
            setUser(fullUser);
        }

        applyTheme();



        if (aiPanel != null) {
            aiPanel.setVisible(false);
            aiPanel.setManaged(false);
        }

        if (skillSelectionPanel != null) {
            skillSelectionPanel.setVisible(false);
            skillSelectionPanel.setManaged(false);
        }

        if (compareSkillSelectionPanel != null) {
            compareSkillSelectionPanel.setVisible(false);
            compareSkillSelectionPanel.setManaged(false);
        }

        if (comparePanel != null) {
            comparePanel.setVisible(false);
            comparePanel.setManaged(false);
        }

        if (exitButton != null) {
            exitButton.setVisible(false);
            exitButton.setManaged(false);
        }

        if (aiResponseArea != null) {
            aiResponseArea.getChildren().clear();
        }
    }

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
        if(floatingAISummoner != null){
            aiHeader.setStyle("-fx-background-color:" + gradient + "; -fx-background-radius: 13 13 0 0;");
            floatingAISummoner.setStyle(
                    "-fx-background-color: " + gradient+ ";" +
                            "-fx-background-radius: 50;" +
                            "-fx-padding: 15;" +
                            "-fx-cursor: hand;"
            );
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

        if (firstNameLabel != null)
            firstNameLabel.setText(user.getFirstName());

        if (lastNameLabel != null)
            lastNameLabel.setText(user.getLastName());

        if (emailLabel != null)
            emailLabel.setText(user.getEmail());

        if (phoneLabel != null)
            phoneLabel.setText(user.getPhone());

        loadProfileImage();

        if (skillCombo != null) {
            skillCombo.getItems().clear();
            for (Skill skill : currentUser.getLearnSkills()) {
                skillCombo.getItems().add(skill.getSkillName());
            }
        }

        if (compareSkillCombo != null) {
            compareSkillCombo.getItems().clear();
            for (Skill skill : currentUser.getLearnSkills()) {
                compareSkillCombo.getItems().add(skill.getSkillName());
            }
        }

        loadAllUsers();
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
                    profilePicture.setImage(new Image(
                            getClass().getResource("/com/example/newdesign/images/default.png").toString()
                    ));
                }
            } else {
                profilePicture.setImage(new Image(
                        getClass().getResource("/com/example/newdesign/images/default.png").toString()
                ));
            }

           // circling the profile picture

            Circle clip = new Circle(30, 30, 30); // adjust based on size
            profilePicture.setClip(clip);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAllUsers() {
        List<User> basicUsers = userDAO.searchUsers("");
        allUsers.clear();

        java.util.Set<Integer> addedUserIds = new java.util.HashSet<>();

        for (User u : basicUsers) {
            if (currentUser != null && u.getId() != currentUser.getId() && !addedUserIds.contains(u.getId())) {
                User fullUser = userDAO.getUserById(u.getId());
                if (fullUser != null) {
                    allUsers.add(fullUser);
                    addedUserIds.add(fullUser.getId());
                }
            }
        }
    }


    // This part load all posts from database by time and show them in the main content as cards
    public void loadPosts(){

        PostDAO postdao = new PostDaoImpl();
        UserDAO userDao = new UserDAOImpl();

        List<Post> posts = postdao.getAllPosts();

        mainContent.getChildren().clear();

        // 🔥 CRITICAL LINE
        mainContent.setFillWidth(true);

        for (Post post : posts){

            User user = userDao.getUserById(post.getUserId());

            VBox postCard = createPostCard(post, user);

            // 🔥 FORCE FULL WIDTH
            postCard.setMaxWidth(Double.MAX_VALUE);
            VBox.setVgrow(postCard, Priority.NEVER);

            mainContent.getChildren().add(postCard);
        }
    }

    private VBox createPostCard(Post post, User user){

        VBox card = new VBox(10);

        // 🔥 THESE 3 LINES ARE THE KEY FIX
        card.setPrefWidth(900);
        card.setMaxWidth(900);

        card.setPrefHeight(300);   // ⬅️ increase height
        card.setMinHeight(300);

        card.setStyle(
                "-fx-background-color: linear-gradient(from 100% 0% to 0% 0%, " +
                        ThemeManager.primaryBackGround + ", white);" +
                        " -fx-background-radius: 15;"+
                        "-fx-padding: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);"
        );

        HBox userBox = new HBox(10);
        userBox.setAlignment(Pos.CENTER_LEFT);
        userBox.setMaxWidth(Double.MAX_VALUE);

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (profileImage.getImage() == null) {
            profileImage.setImage(new Image(
                    getClass().getResource("/com/example/newdesign/images/default.png").toExternalForm()
            ));
        }

        Circle clip = new Circle(20, 20, 20);
        profileImage.setClip(clip);

        Label userName = new Label(
                user != null ? user.getFirstName() + " " + user.getLastName() : "Unknown"
        );
        userName.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-font-size: 18px;" +
                        "-fx-text-fill: black;"
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy • HH:mm");

        Label date = new Label(post.getCreatedAt().format(formatter));
        date.setStyle("-fx-text-fill: gray; -fx-font-size: 12;");

        VBox nameBox = new VBox(userName, date);

        userBox.getChildren().addAll(profileImage, nameBox);

        //  a single line in the post feed
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #E0E0E0;");
        separator.setMaxWidth(Double.MAX_VALUE);

        Label content = new Label(post.getContent());
        content.setWrapText(true);
        content.setMaxWidth(Double.MAX_VALUE);

        card.getChildren().addAll(userBox, separator,  content);

        return card;
    }





    // this part is for the handling buttons
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

    @FXML
    public void toggleAIPanel() {
        if (aiPanel != null && floatingAISummoner != null) {
            boolean isVisible = aiPanel.isVisible();
            if (!isVisible) {
                floatingAISummoner.setVisible(false);
                floatingAISummoner.setManaged(false);

                aiPanel.setVisible(true);
                aiPanel.setManaged(true);
                aiPanel.setOpacity(0);
                aiPanel.setScaleX(0.8);
                aiPanel.setScaleY(0.8);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), aiPanel);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);

                ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), aiPanel);
                scaleIn.setFromX(0.8);
                scaleIn.setFromY(0.8);
                scaleIn.setToX(1);
                scaleIn.setToY(1);

                ParallelTransition parallel = new ParallelTransition(fadeIn, scaleIn);
                parallel.play();
                animateButtonsSequentially();
            } else {
                FadeTransition fadeOut = new FadeTransition(Duration.millis(200), aiPanel);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(e -> {
                    aiPanel.setVisible(false);
                    aiPanel.setManaged(false);
                    resetToMainMenu();
                });
                fadeOut.play();
            }
        }
    }

    private void animateButtonsSequentially() {
        if (actionButtonsPanel != null && actionButtonsPanel.getChildren() != null) {
            List<javafx.scene.Node> buttons = actionButtonsPanel.getChildren();
            for (int i = 0; i < buttons.size(); i++) {
                javafx.scene.Node button = buttons.get(i);
                button.setOpacity(0);
                button.setTranslateX(-20);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(200), button);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.setDelay(Duration.millis(50 + i * 100));

                TranslateTransition slideIn = new TranslateTransition(Duration.millis(200), button);
                slideIn.setFromX(-20);
                slideIn.setToX(0);
                slideIn.setDelay(Duration.millis(50 + i * 100));

                ParallelTransition parallel = new ParallelTransition(fadeIn, slideIn);
                parallel.play();
            }
        }
    }

    @FXML
    public void closeAIPanel() {
        if (aiPanel != null) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), aiPanel);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> {
                aiPanel.setVisible(false);
                aiPanel.setManaged(false);
                resetToMainMenu();
            });
            fadeOut.play();
        }
    }

    @FXML
    public void resetToMainMenu() {
        if (actionButtonsPanel != null) {
            actionButtonsPanel.setVisible(true);
            actionButtonsPanel.setManaged(true);
        }

        if (exitButton != null) {
            exitButton.setVisible(false);
            exitButton.setManaged(false);
        }

        if (skillSelectionPanel != null) {
            skillSelectionPanel.setVisible(false);
            skillSelectionPanel.setManaged(false);
        }

        if (compareSkillSelectionPanel != null) {
            compareSkillSelectionPanel.setVisible(false);
            compareSkillSelectionPanel.setManaged(false);
        }

        if (comparePanel != null) {
            comparePanel.setVisible(false);
            comparePanel.setManaged(false);
        }

        if (aiResponseArea != null) {
            aiResponseArea.getChildren().clear();
        }

        if (floatingAISummoner != null) {
            floatingAISummoner.setVisible(true);
            floatingAISummoner.setManaged(true);
        }
    }

    private void showActionAndExit() {
        if (actionButtonsPanel != null) {
            actionButtonsPanel.setVisible(false);
            actionButtonsPanel.setManaged(false);
        }

        if (exitButton != null) {
            exitButton.setVisible(true);
            exitButton.setManaged(true);
            exitButton.setOpacity(0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), exitButton);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        }
    }

    private void animateContentFadeIn(VBox container) {
        if (container != null) {
            container.setOpacity(0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), container);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        }
    }

    @FXML
    public void handleFindMatches() {
        if (currentUser == null) {
            showResponse("Error: No user logged in.");
            return;
        }

        List<Skill> learnSkills = currentUser.getLearnSkills();
        if (learnSkills.isEmpty()) {
            showResponse("You haven't added any skills you want to learn!\n\nGo to PROFILE and add skills to LEARN.");
            return;
        }

        skillCombo.getItems().clear();
        for (Skill skill : currentUser.getLearnSkills()) {
            skillCombo.getItems().add(skill.getSkillName());
        }

        showActionAndExit();
        skillSelectionPanel.setVisible(true);
        skillSelectionPanel.setManaged(true);
        animateContentFadeIn(skillSelectionPanel);
    }

    @FXML
    public void hideSkillSelectionPanel() {
        skillSelectionPanel.setVisible(false);
        skillSelectionPanel.setManaged(false);
        resetToMainMenu();
    }

    @FXML
    public void findMatchesForSelectedSkill() {
        String selectedSkill = skillCombo.getValue();

        if (selectedSkill == null || selectedSkill.isEmpty()) {
            showResponse("Please select a skill first!");
            return;
        }

        skillSelectionPanel.setVisible(false);
        skillSelectionPanel.setManaged(false);

        if (allUsers.isEmpty()) {
            loadAllUsers();
        }

        List<UserMatch> matches = new ArrayList<>();

        for (User user : allUsers) {
            int score = 0;
            boolean hasSelectedSkill = false;

            for (Skill skill : user.getTeachSkills()) {
                if (skill.getSkillName().equalsIgnoreCase(selectedSkill)) {
                    hasSelectedSkill = true;
                    score += 50;
                    break;
                }
            }

            for (Skill myTeach : currentUser.getTeachSkills()) {
                for (Skill theirWant : user.getLearnSkills()) {
                    if (myTeach.getSkillName().equalsIgnoreCase(theirWant.getSkillName())) {
                        score += 50;
                        break;
                    }
                }
            }

            if (hasSelectedSkill && score > 0) {
                matches.add(new UserMatch(user, Math.min(score, 100)));
            }
        }

        matches.sort((a, b) -> b.score - a.score);

        if (matches.isEmpty()) {
            StringBuilder availableSkills = new StringBuilder();
            java.util.Set<String> uniqueSkills = new java.util.HashSet<>();
            for (User user : allUsers) {
                for (Skill skill : user.getTeachSkills()) {
                    uniqueSkills.add(skill.getSkillName());
                }
            }

            if (!uniqueSkills.isEmpty()) {
                availableSkills.append("\n\nAvailable skills from other users:\n");
                for (String skill : uniqueSkills) {
                    availableSkills.append("• ").append(skill).append("\n");
                }
            }

            showResponse("No users found who can teach you '" + selectedSkill + "'." + availableSkills.toString());
            return;
        }

        displayMatchesAsButtons(matches);
    }

    private void displayMatchesAsButtons(List<UserMatch> matches) {
        if (aiResponseArea != null) {
            aiResponseArea.getChildren().clear();

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
            scrollPane.setPrefHeight(350);

            VBox contentContainer = new VBox(10);
            contentContainer.setPadding(new Insets(5, 5, 5, 5));

            Label title = new Label("YOUR MATCHES");
            title.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 16));
            title.setStyle("-fx-text-fill: #0C4D3B;");
            title.setPadding(new Insets(0, 0, 10, 0));
            title.setOpacity(0);
            contentContainer.getChildren().add(title);

            FadeTransition titleFade = new FadeTransition(Duration.millis(300), title);
            titleFade.setFromValue(0);
            titleFade.setToValue(1);
            titleFade.play();

            for (int i = 0; i < matches.size(); i++) {
                VBox matchCard = createMatchCard(matches.get(i));
                matchCard.setOpacity(0);
                matchCard.setTranslateX(-30);
                contentContainer.getChildren().add(matchCard);

                FadeTransition cardFade = new FadeTransition(Duration.millis(300), matchCard);
                cardFade.setFromValue(0);
                cardFade.setToValue(1);
                cardFade.setDelay(Duration.millis(100 + i * 80));

                TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), matchCard);
                slideIn.setFromX(-30);
                slideIn.setToX(0);
                slideIn.setDelay(Duration.millis(100 + i * 80));

                ParallelTransition parallel = new ParallelTransition(cardFade, slideIn);
                parallel.play();
            }

            contentContainer.setSpacing(10);
            scrollPane.setContent(contentContainer);
            aiResponseArea.getChildren().add(scrollPane);
        }
    }

    private VBox createMatchCard(UserMatch match) {
        VBox card = new VBox(8);
        card.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 12; -fx-padding: 12; -fx-cursor: hand;");
        card.setPadding(new Insets(12));

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 12; -fx-padding: 12; -fx-cursor: hand;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 12; -fx-padding: 12; -fx-cursor: hand;"));
        card.setOnMouseClicked(e -> openUserProfile(match.user));

        HBox profileRow = new HBox(10);
        profileRow.setAlignment(Pos.CENTER_LEFT);

        ImageView avatarView = new ImageView();
        avatarView.setFitHeight(40);
        avatarView.setFitWidth(40);
        try {
            if (match.user.getProfilePicture() != null) {
                File file = new File("profile_images/" + match.user.getProfilePicture());
                if (file.exists()) {
                    avatarView.setImage(new Image(file.toURI().toString(), false));
                } else {
                    avatarView.setImage(new Image(getClass().getResource("/com/example/newdesign/images/default.png").toString()));
                }
            } else {
                avatarView.setImage(new Image(getClass().getResource("/com/example/newdesign/images/default.png").toString()));
            }
        } catch (Exception e) {
        }

        VBox nameBox = new VBox(3);
        Label nameLabel = new Label(match.user.getFirstName() + " " + match.user.getLastName());
        nameLabel.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 15));
        nameLabel.setStyle("-fx-text-fill: #1F1F1F;");

        Label ratingLabel = new Label(match.user.getFormattedRating());
        ratingLabel.setFont(Font.font("SF Pro Text", FontWeight.NORMAL, 11));
        ratingLabel.setStyle("-fx-text-fill: #FFB800;");

        nameBox.getChildren().addAll(nameLabel, ratingLabel);
        profileRow.getChildren().addAll(avatarView, nameBox);
        HBox.setHgrow(nameBox, Priority.ALWAYS);

        Label percentBadge = new Label(match.score + "% MATCH");
        percentBadge.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 11));
        percentBadge.setStyle("-fx-background-color: #0C4D3B; -fx-text-fill: white; -fx-background-radius: 10;");
        percentBadge.setPadding(new Insets(4, 10, 4, 10));
        percentBadge.setMaxWidth(Double.MAX_VALUE);
        percentBadge.setAlignment(Pos.CENTER);

        Label teachesLabel = new Label("Teaches: " + formatSkills(match.user.getTeachSkills()));
        teachesLabel.setFont(Font.font("SF Pro Text", FontWeight.NORMAL, 11));
        teachesLabel.setStyle("-fx-text-fill: #555555;");
        teachesLabel.setWrapText(true);

        Label wantsLabel = new Label("Wants to learn: " + formatSkills(match.user.getLearnSkills()));
        wantsLabel.setFont(Font.font("SF Pro Text", FontWeight.NORMAL, 11));
        wantsLabel.setStyle("-fx-text-fill: #555555;");
        wantsLabel.setWrapText(true);

        Label reasonLabel = new Label(getMatchReason(match.user));
        reasonLabel.setFont(Font.font("SF Pro Text", FontWeight.NORMAL, 10));
        reasonLabel.setStyle("-fx-text-fill: #888888;");
        reasonLabel.setWrapText(true);

        card.getChildren().addAll(profileRow, percentBadge, teachesLabel, wantsLabel, reasonLabel);
        return card;
    }

    private String getMatchReason(User match) {
        for (Skill myTeach : currentUser.getTeachSkills()) {
            for (Skill theirWant : match.getLearnSkills()) {
                if (myTeach.getSkillName().equalsIgnoreCase(theirWant.getSkillName())) {
                    return "You can teach them " + myTeach.getSkillName() + "!";
                }
            }
        }
        return "They can teach you a skill you want to learn!";
    }

    @FXML
    public void showComparePanel() {
        if (currentUser == null) {
            showResponse("Error: No user logged in.");
            return;
        }

        List<Skill> learnSkills = currentUser.getLearnSkills();
        if (learnSkills.isEmpty()) {
            showResponse("You haven't added any skills you want to learn!\n\nGo to PROFILE and add skills to LEARN first.");
            return;
        }

        compareSkillCombo.getItems().clear();
        for (Skill skill : currentUser.getLearnSkills()) {
            compareSkillCombo.getItems().add(skill.getSkillName());
        }

        showActionAndExit();
        compareSkillSelectionPanel.setVisible(true);
        compareSkillSelectionPanel.setManaged(true);
        animateContentFadeIn(compareSkillSelectionPanel);
    }

    @FXML
    public void hideCompareSkillSelectionPanel() {
        compareSkillSelectionPanel.setVisible(false);
        compareSkillSelectionPanel.setManaged(false);
        resetToMainMenu();
    }

    @FXML
    public void backToSkillSelection() {
        comparePanel.setVisible(false);
        comparePanel.setManaged(false);
        compareSkillSelectionPanel.setVisible(true);
        compareSkillSelectionPanel.setManaged(true);
        animateContentFadeIn(compareSkillSelectionPanel);
    }

    @FXML
    public void showUsersForCompare() {
        String selectedSkill = compareSkillCombo.getValue();

        if (selectedSkill == null || selectedSkill.isEmpty()) {
            showResponse("Please select a skill first!");
            return;
        }

        if (allUsers.isEmpty()) {
            loadAllUsers();
        }

        filteredUsers.clear();

        for (User user : allUsers) {
            for (Skill skill : user.getTeachSkills()) {
                if (skill.getSkillName().equalsIgnoreCase(selectedSkill)) {
                    filteredUsers.add(user);
                    break;
                }
            }
        }

        if (filteredUsers.isEmpty()) {
            StringBuilder availableSkills = new StringBuilder();
            java.util.Set<String> uniqueSkills = new java.util.HashSet<>();
            for (User user : allUsers) {
                for (Skill skill : user.getTeachSkills()) {
                    uniqueSkills.add(skill.getSkillName());
                }
            }

            if (!uniqueSkills.isEmpty()) {
                availableSkills.append("\n\nAvailable skills from other users:\n");
                for (String skill : uniqueSkills) {
                    availableSkills.append("• ").append(skill).append("\n");
                }
            }

            showResponse("No users found who can teach '" + selectedSkill + "'." + availableSkills.toString());
            return;
        }

        compareSkillSelectionPanel.setVisible(false);
        compareSkillSelectionPanel.setManaged(false);

        usersToCompareContainer.getChildren().clear();
        userCheckBoxes.clear();

        Label instruction = new Label("Select TWO users to compare (based on '" + selectedSkill + "'):");
        instruction.setStyle("-fx-font-weight: bold; -fx-text-fill: #0C4D3B; -fx-font-size: 12px;");
        instruction.setPadding(new Insets(0, 0, 10, 0));
        usersToCompareContainer.getChildren().add(instruction);

        for (int i = 0; i < filteredUsers.size(); i++) {
            VBox userCard = createSelectableUserCard(filteredUsers.get(i), selectedSkill);
            userCard.setOpacity(0);
            userCard.setTranslateX(-20);
            usersToCompareContainer.getChildren().add(userCard);

            FadeTransition cardFade = new FadeTransition(Duration.millis(250), userCard);
            cardFade.setFromValue(0);
            cardFade.setToValue(1);
            cardFade.setDelay(Duration.millis(i * 80));

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(250), userCard);
            slideIn.setFromX(-20);
            slideIn.setToX(0);
            slideIn.setDelay(Duration.millis(i * 80));

            ParallelTransition parallel = new ParallelTransition(cardFade, slideIn);
            parallel.play();
        }

        comparePanel.setVisible(true);
        comparePanel.setManaged(true);
        animateContentFadeIn(comparePanel);
    }

    private VBox createSelectableUserCard(User user, String skillName) {
        VBox card = new VBox(6);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #E0E0E0; -fx-border-radius: 10; -fx-padding: 10;");
        card.setPadding(new Insets(10));

        CheckBox selectCheckBox = new CheckBox();
        selectCheckBox.setUserData(user);
        userCheckBoxes.add(selectCheckBox);

        HBox topRow = new HBox(10);
        topRow.setAlignment(Pos.CENTER_LEFT);

        ImageView avatarView = new ImageView();
        avatarView.setFitHeight(40);
        avatarView.setFitWidth(40);
        try {
            if (user.getProfilePicture() != null) {
                File file = new File("profile_images/" + user.getProfilePicture());
                if (file.exists()) {
                    avatarView.setImage(new Image(file.toURI().toString(), false));
                }
            }
        } catch (Exception e) {
        }

        VBox infoBox = new VBox(3);
        Label nameLabel = new Label(user.getFirstName() + " " + user.getLastName());
        nameLabel.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 14));
        nameLabel.setStyle("-fx-text-fill: #1F1F1F;");

        Label ratingLabel = new Label(user.getFormattedRating());
        ratingLabel.setFont(Font.font("SF Pro Text", FontWeight.NORMAL, 11));
        ratingLabel.setStyle("-fx-text-fill: #FFB800;");

        Label skillLabel = new Label("Teaches: " + skillName);
        skillLabel.setFont(Font.font("SF Pro Text", FontWeight.NORMAL, 11));
        skillLabel.setStyle("-fx-text-fill: #0EBB8A;");

        infoBox.getChildren().addAll(nameLabel, ratingLabel, skillLabel);
        topRow.getChildren().addAll(selectCheckBox, avatarView, infoBox);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        String otherSkills = user.getTeachSkills().stream()
                .filter(s -> !s.getSkillName().equalsIgnoreCase(skillName))
                .map(Skill::getSkillName)
                .collect(Collectors.joining(", "));

        VBox detailsBox = new VBox(3);
        if (!otherSkills.isEmpty()) {
            Label otherLabel = new Label("Also teaches: " + otherSkills);
            otherLabel.setFont(Font.font("SF Pro Text", FontWeight.NORMAL, 10));
            otherLabel.setStyle("-fx-text-fill: #888888;");
            detailsBox.getChildren().add(otherLabel);
        }

        card.getChildren().addAll(topRow, detailsBox);
        return card;
    }

    @FXML
    public void handleCompareSelectedUsers() {
        List<User> selectedUsers = new ArrayList<>();

        for (CheckBox cb : userCheckBoxes) {
            if (cb.isSelected()) {
                selectedUsers.add((User) cb.getUserData());
            }
        }

        if (selectedUsers.size() != 2) {
            showResponse("Please select exactly TWO users to compare!\n(You selected " + selectedUsers.size() + ")");
            return;
        }

        User user1 = selectedUsers.get(0);
        User user2 = selectedUsers.get(1);

        displayComparisonAsCards(user1, user2);
        comparePanel.setVisible(false);
        comparePanel.setManaged(false);
    }

    private void displayComparisonAsCards(User user1, User user2) {
        if (aiResponseArea != null) {
            aiResponseArea.getChildren().clear();

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
            scrollPane.setPrefHeight(350);

            VBox contentContainer = new VBox(15);
            contentContainer.setPadding(new Insets(5, 5, 5, 5));

            // Title with compare icon
            ImageView compareIcon = new ImageView();
            compareIcon.setFitHeight(18);
            compareIcon.setFitWidth(18);
            try {
                compareIcon.setImage(new Image(getClass().getResource("/com/example/newdesign/Icons/compare-icon.png").toString()));
            } catch (Exception e) {
                System.out.println("compare-icon.png not found");
            }

            Label titleText = new Label(" USER COMPARISON");
            titleText.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 16));
            titleText.setStyle("-fx-text-fill: #0C4D3B;");

            HBox titleBox = new HBox(5);
            titleBox.setAlignment(Pos.CENTER_LEFT);
            titleBox.getChildren().addAll(compareIcon, titleText);
            titleBox.setOpacity(0);
            contentContainer.getChildren().add(titleBox);

            FadeTransition titleFade = new FadeTransition(Duration.millis(300), titleBox);
            titleFade.setFromValue(0);
            titleFade.setToValue(1);
            titleFade.play();

            HBox comparisonContainer = new HBox(20);
            comparisonContainer.setAlignment(Pos.TOP_CENTER);
            comparisonContainer.setPadding(new Insets(0, 0, 10, 0));
            comparisonContainer.setOpacity(0);

            VBox user1Card = createComparisonCard(user1);
            VBox user2Card = createComparisonCard(user2);
            comparisonContainer.getChildren().addAll(user1Card, user2Card);
            contentContainer.getChildren().add(comparisonContainer);

            FadeTransition containerFade = new FadeTransition(Duration.millis(400), comparisonContainer);
            containerFade.setFromValue(0);
            containerFade.setToValue(1);
            containerFade.play();

            int score1 = calculateMatchScore(user1);
            int score2 = calculateMatchScore(user2);

            VBox recommendationBox = new VBox(10);
            recommendationBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 12; -fx-padding: 15;");
            recommendationBox.setOpacity(0);

            ImageView aiIcon = new ImageView();
            aiIcon.setFitHeight(16);
            aiIcon.setFitWidth(16);
            try {
                aiIcon.setImage(new Image(getClass().getResource("/com/example/newdesign/Icons/ai-icon.png").toString()));
            } catch (Exception e) {
                System.out.println("ai-icon.png not found");
            }

            Label aiText = new Label(" AI RECOMMENDATION");
            aiText.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 14));
            aiText.setStyle("-fx-text-fill: #0C4D3B;");

            HBox aiBox = new HBox(5);
            aiBox.setAlignment(Pos.CENTER_LEFT);
            aiBox.getChildren().addAll(aiIcon, aiText);

            ImageView lightbulbIcon = new ImageView();
            lightbulbIcon.setFitHeight(14);
            lightbulbIcon.setFitWidth(14);
            try {
                lightbulbIcon.setImage(new Image(getClass().getResource("/com/example/newdesign/Icons/lightbulb-icon.png").toString()));
            } catch (Exception e) {
                System.out.println("lightbulb-icon.png not found");
            }

            ImageView checkIcon = new ImageView();
            checkIcon.setFitHeight(14);
            checkIcon.setFitWidth(14);
            try {
                checkIcon.setImage(new Image(getClass().getResource("/com/example/newdesign/Icons/check-icon.png").toString()));
            } catch (Exception e) {
                System.out.println("check-icon.png not found");
            }

            ImageView chartUpIcon = new ImageView();
            chartUpIcon.setFitHeight(14);
            chartUpIcon.setFitWidth(14);
            try {
                chartUpIcon.setImage(new Image(getClass().getResource("/com/example/newdesign/Icons/chart-up-icon.png").toString()));
            } catch (Exception e) {
                System.out.println("chart-up-icon.png not found");
            }

            if (score1 > score2) {
                Label line1Label = new Label(" " + user1.getFirstName() + " " + user1.getLastName() + " is a better match for you!");
                line1Label.setStyle("-fx-text-fill: #0C4D3B; -fx-font-weight: bold; -fx-font-size: 13px;");
                HBox line1Box = new HBox(5);
                line1Box.setAlignment(Pos.CENTER_LEFT);
                line1Box.getChildren().addAll(checkIcon, line1Label);

                Label line2Label = new Label(" Compatibility Score: " + score1 + "% vs " + score2 + "%");
                line2Label.setStyle("-fx-text-fill: #0C4D3B; -fx-font-weight: bold; -fx-font-size: 13px;");
                HBox line2Box = new HBox(5);
                line2Box.setAlignment(Pos.CENTER_LEFT);
                line2Box.getChildren().addAll(chartUpIcon, line2Label);

                Label tipLabel = new Label(" They can teach you: " + formatSkills(getMatchingTeachSkills(user1)));
                tipLabel.setStyle("-fx-text-fill: #0C4D3B; -fx-font-weight: bold; -fx-font-size: 12px;");
                tipLabel.setWrapText(true);
                HBox tipBox = new HBox(5);
                tipBox.setAlignment(Pos.CENTER_LEFT);
                tipBox.getChildren().addAll(lightbulbIcon, tipLabel);

                recommendationBox.getChildren().addAll(aiBox, line1Box, line2Box, tipBox);
            } else if (score2 > score1) {
                Label line1Label = new Label(" " + user2.getFirstName() + " " + user2.getLastName() + " is a better match for you!");
                line1Label.setStyle("-fx-text-fill: #0C4D3B; -fx-font-weight: bold; -fx-font-size: 13px;");
                HBox line1Box = new HBox(5);
                line1Box.setAlignment(Pos.CENTER_LEFT);
                line1Box.getChildren().addAll(checkIcon, line1Label);

                Label line2Label = new Label(" Compatibility Score: " + score2 + "% vs " + score1 + "%");
                line2Label.setStyle("-fx-text-fill: #0C4D3B; -fx-font-weight: bold; -fx-font-size: 13px;");
                HBox line2Box = new HBox(5);
                line2Box.setAlignment(Pos.CENTER_LEFT);
                line2Box.getChildren().addAll(chartUpIcon, line2Label);

                Label tipLabel = new Label(" They can teach you: " + formatSkills(getMatchingTeachSkills(user2)));
                tipLabel.setStyle("-fx-text-fill: #0C4D3B; -fx-font-weight: bold; -fx-font-size: 12px;");
                tipLabel.setWrapText(true);
                HBox tipBox = new HBox(5);
                tipBox.setAlignment(Pos.CENTER_LEFT);
                tipBox.getChildren().addAll(lightbulbIcon, tipLabel);

                recommendationBox.getChildren().addAll(aiBox, line1Box, line2Box, tipBox);
            } else {
                Label line1Label = new Label(" Both users have similar compatibility (" + score1 + "%)");
                line1Label.setStyle("-fx-text-fill: #0C4D3B; -fx-font-weight: bold; -fx-font-size: 13px;");
                HBox line1Box = new HBox(5);
                line1Box.setAlignment(Pos.CENTER_LEFT);
                line1Box.getChildren().addAll(chartUpIcon, line1Label);

                Label tipLabel = new Label(" Consider reaching out to both users to see who responds better!");
                tipLabel.setStyle("-fx-text-fill: #0C4D3B; -fx-font-weight: bold; -fx-font-size: 12px;");
                tipLabel.setWrapText(true);
                HBox tipBox = new HBox(5);
                tipBox.setAlignment(Pos.CENTER_LEFT);
                tipBox.getChildren().addAll(lightbulbIcon, tipLabel);

                recommendationBox.getChildren().addAll(aiBox, line1Box, tipBox);
            }

            contentContainer.getChildren().add(recommendationBox);

            FadeTransition recFade = new FadeTransition(Duration.millis(400), recommendationBox);
            recFade.setFromValue(0);
            recFade.setToValue(1);
            recFade.setDelay(Duration.millis(200));
            recFade.play();

            scrollPane.setContent(contentContainer);
            aiResponseArea.getChildren().add(scrollPane);
        }
    }

    private VBox createComparisonCard(User user) {
        VBox card = new VBox(8);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: #E0E0E0; -fx-border-radius: 15; -fx-padding: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        card.setPrefWidth(280);
        card.setMaxHeight(450);

        HBox profileRow = new HBox(10);
        profileRow.setAlignment(Pos.CENTER_LEFT);

        ImageView avatarView = new ImageView();
        avatarView.setFitHeight(45);
        avatarView.setFitWidth(45);
        try {
            if (user.getProfilePicture() != null) {
                File file = new File("profile_images/" + user.getProfilePicture());
                if (file.exists()) {
                    avatarView.setImage(new Image(file.toURI().toString(), false));
                } else {
                    avatarView.setImage(new Image(getClass().getResource("/com/example/newdesign/images/default.png").toString()));
                }
            } else {
                avatarView.setImage(new Image(getClass().getResource("/com/example/newdesign/images/default.png").toString()));
            }
        } catch (Exception e) {}

        VBox nameBox = new VBox(2);
        Label nameLabel = new Label(user.getFirstName() + " " + user.getLastName());
        nameLabel.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 15));
        nameLabel.setStyle("-fx-text-fill: #1F1F1F;");

        Label usernameLabel = new Label("@" + (user.getUsername() != null ? user.getUsername() : "user"));
        usernameLabel.setFont(Font.font("SF Pro Text", FontWeight.NORMAL, 10));
        usernameLabel.setStyle("-fx-text-fill: #0EBB8A;");

        nameBox.getChildren().addAll(nameLabel, usernameLabel);
        profileRow.getChildren().addAll(avatarView, nameBox);

        // Rating with star icon
        ImageView starIcon = new ImageView();
        starIcon.setFitHeight(14);
        starIcon.setFitWidth(14);
        try {
            starIcon.setImage(new Image(getClass().getResource("/com/example/newdesign/Icons/star-icon.png").toString()));
        } catch (Exception e) {
            System.out.println("star-icon.png not found");
        }

        Label ratingLabel = new Label(" " + user.getFormattedRating());
        ratingLabel.setFont(Font.font("SF Pro Text", FontWeight.NORMAL, 11));
        ratingLabel.setStyle("-fx-text-fill: #FFB800;");

        HBox ratingBox = new HBox(3);
        ratingBox.setAlignment(Pos.CENTER_LEFT);
        ratingBox.getChildren().addAll(starIcon, ratingLabel);

        Separator separator1 = new Separator();
        separator1.setPadding(new Insets(5, 0, 5, 0));

        // Teach Title with icon
        ImageView teachIcon = new ImageView();
        teachIcon.setFitHeight(14);
        teachIcon.setFitWidth(14);
        try {
            teachIcon.setImage(new Image(getClass().getResource("/com/example/newdesign/Icons/teach-icon.png").toString()));
        } catch (Exception e) {
            System.out.println("teach-icon.png not found");
        }

        Label teachTitle = new Label("ABLE TO TEACH");
        teachTitle.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 11));
        teachTitle.setStyle("-fx-text-fill: #0C4D3B;");

        HBox teachTitleBox = new HBox(3);
        teachTitleBox.setAlignment(Pos.CENTER_LEFT);
        teachTitleBox.getChildren().addAll(teachIcon, teachTitle);

        Label teachSkills = new Label(formatSkills(user.getTeachSkills()));
        teachSkills.setFont(Font.font("SF Pro Text", FontWeight.NORMAL, 10));
        teachSkills.setStyle("-fx-text-fill: #555555;");
        teachSkills.setWrapText(true);

        // Learn Title with icon
        ImageView targetIcon = new ImageView();
        targetIcon.setFitHeight(14);
        targetIcon.setFitWidth(14);
        try {
            targetIcon.setImage(new Image(getClass().getResource("/com/example/newdesign/Icons/target-icon.png").toString()));
        } catch (Exception e) {
            System.out.println("target-icon.png not found");
        }

        Label learnTitle = new Label("WANTS TO LEARN");
        learnTitle.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 11));
        learnTitle.setStyle("-fx-text-fill: #0C4D3B;");

        HBox learnTitleBox = new HBox(3);
        learnTitleBox.setAlignment(Pos.CENTER_LEFT);
        learnTitleBox.getChildren().addAll(targetIcon, learnTitle);

        Label learnSkills = new Label(formatSkills(user.getLearnSkills()));
        learnSkills.setFont(Font.font("SF Pro Text", FontWeight.NORMAL, 10));
        learnSkills.setStyle("-fx-text-fill: #555555;");
        learnSkills.setWrapText(true);

        Separator separator2 = new Separator();
        separator2.setPadding(new Insets(5, 0, 5, 0));

        // Reviews Title with icon
        ImageView commentIcon = new ImageView();
        commentIcon.setFitHeight(14);
        commentIcon.setFitWidth(14);
        try {
            commentIcon.setImage(new Image(getClass().getResource("/com/example/newdesign/Icons/comment-icon.png").toString()));
        } catch (Exception e) {
            System.out.println("comment-icon.png not found");
        }

        Label reviewsTitle = new Label(" REVIEWS");
        reviewsTitle.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 11));
        reviewsTitle.setStyle("-fx-text-fill: #0C4D3B;");

        HBox reviewsTitleBox = new HBox(3);
        reviewsTitleBox.setAlignment(Pos.CENTER_LEFT);
        reviewsTitleBox.getChildren().addAll(commentIcon, reviewsTitle);

        VBox reviewsBox = new VBox(4);
        List<Review> reviews = user.getReviews();
        if (reviews != null && !reviews.isEmpty()) {
            for (int i = 0; i < Math.min(2, reviews.size()); i++) {
                Review r = reviews.get(i);
                HBox reviewRow = new HBox(5);
                reviewRow.setAlignment(Pos.CENTER_LEFT);

                Label starsLabel = new Label(getStarString(r.getRating()));
                starsLabel.setStyle("-fx-text-fill: #FFB800; -fx-font-size: 10px;");

                String commentText = r.getComment() != null && !r.getComment().isEmpty() ? r.getComment() : "No comment";
                if (commentText.length() > 50) {
                    commentText = commentText.substring(0, 47) + "...";
                }
                Label commentLabel = new Label(commentText);
                commentLabel.setFont(Font.font("SF Pro Text", FontWeight.NORMAL, 9));
                commentLabel.setStyle("-fx-text-fill: #666666;");
                commentLabel.setWrapText(true);

                reviewRow.getChildren().addAll(starsLabel, commentLabel);
                reviewsBox.getChildren().add(reviewRow);
            }
            if (reviews.size() > 2) {
                Label moreLabel = new Label("+ " + (reviews.size() - 2) + " more reviews");
                moreLabel.setFont(Font.font("SF Pro Text", FontWeight.NORMAL, 9));
                moreLabel.setStyle("-fx-text-fill: #888888;");
                reviewsBox.getChildren().add(moreLabel);
            }
        } else {
            ImageView emptyIcon = new ImageView();
            emptyIcon.setFitHeight(12);
            emptyIcon.setFitWidth(12);
            try {
                emptyIcon.setImage(new Image(getClass().getResource("/com/example/newdesign/Icons/empty-icon.png").toString()));
            } catch (Exception e) {
                System.out.println("empty-icon.png not found");
            }

            Label noReviews = new Label(" No reviews yet");
            noReviews.setFont(Font.font("SF Pro Text", FontWeight.NORMAL, 10));
            noReviews.setStyle("-fx-text-fill: #888888; -fx-font-style: italic;");

            HBox noReviewsBox = new HBox(3);
            noReviewsBox.setAlignment(Pos.CENTER_LEFT);
            noReviewsBox.getChildren().addAll(emptyIcon, noReviews);
            reviewsBox.getChildren().add(noReviewsBox);
        }

        // View Profile Button with icon
        ImageView profileIcon = new ImageView();
        profileIcon.setFitHeight(12);
        profileIcon.setFitWidth(12);
        try {
            profileIcon.setImage(new Image(getClass().getResource("/com/example/newdesign/Icons/profile-icon.png").toString()));
        } catch (Exception e) {
            System.out.println("profile-icon.png not found");
        }

        Button viewProfileBtn = new Button(" VIEW PROFILE");
        viewProfileBtn.setGraphic(profileIcon);
        viewProfileBtn.setFont(Font.font("SF Pro Text", FontWeight.MEDIUM, 11));
        viewProfileBtn.setStyle("-fx-background-color: #0C4D3B; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 6;");
        viewProfileBtn.setMaxWidth(Double.MAX_VALUE);
        viewProfileBtn.setOnAction(e -> openUserProfile(user));

        card.getChildren().addAll(
                profileRow, ratingBox,
                separator1,
                teachTitleBox, teachSkills,
                learnTitleBox, learnSkills,
                separator2,
                reviewsTitleBox, reviewsBox,
                viewProfileBtn
        );

        return card;
    }

    private int calculateMatchScore(User other) {
        int score = 0;
        for (Skill myWant : currentUser.getLearnSkills()) {
            for (Skill theirTeach : other.getTeachSkills()) {
                if (myWant.getSkillName().equalsIgnoreCase(theirTeach.getSkillName())) {
                    score += 50;
                }
            }
        }
        for (Skill myTeach : currentUser.getTeachSkills()) {
            for (Skill theirWant : other.getLearnSkills()) {
                if (myTeach.getSkillName().equalsIgnoreCase(theirWant.getSkillName())) {
                    score += 50;
                }
            }
        }
        return Math.min(score, 100);
    }

    private List<Skill> getMatchingTeachSkills(User other) {
        List<Skill> matches = new ArrayList<>();
        for (Skill myWant : currentUser.getLearnSkills()) {
            for (Skill theirTeach : other.getTeachSkills()) {
                if (myWant.getSkillName().equalsIgnoreCase(theirTeach.getSkillName())) {
                    matches.add(theirTeach);
                }
            }
        }
        return matches;
    }

    @FXML
    public void handleGroupChat() {
        showActionAndExit();
        showResponse("""
            GROUP CHAT FEATURE
            ═══════════════════════════════════
            
            This feature is currently under development!
            """);
    }

    private void openUserProfile(User user) {
        try {
            SessionManager.setSelectedUserForView(user);
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("other-profile-view.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 800);
            Stage stage = (Stage) floatingAISummoner.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            showResponse("Could not open profile: " + e.getMessage());
        }
    }

    private String formatSkills(List<Skill> skills) {
        if (skills == null || skills.isEmpty()) return "None";
        return skills.stream().map(Skill::getSkillName).collect(Collectors.joining(", "));
    }

    private String getStarString(int rating) {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < rating; i++) stars.append("★");
        for (int i = rating; i < 5; i++) stars.append("☆");
        return stars.toString();
    }

    private void showResponse(String text) {
        if (aiResponseArea != null) {
            aiResponseArea.getChildren().clear();
            TextArea ta = new TextArea(text);
            ta.setWrapText(true);
            ta.setEditable(false);
            ta.setStyle("-fx-font-family: 'SF Pro Text', 'Helvetica', Arial, sans-serif; -fx-font-size: 13px; -fx-background-color: #FAFAFA;");
            ta.setPrefHeight(300);
            ta.setOpacity(0);
            aiResponseArea.getChildren().add(ta);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), ta);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        }
    }

    private static class UserMatch {
        User user;
        int score;
        UserMatch(User u, int s) {
            user = u;
            score = s;
        }
    }
}