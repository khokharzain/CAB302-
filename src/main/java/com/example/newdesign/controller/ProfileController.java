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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProfileController {

    private User currentUser;

    @FXML private ImageView profileImage;
    @FXML private Label fullNameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label locationLabel;
    @FXML private Label joinDateLabel;
    @FXML private Label bioLabel;
    @FXML private Label ratingLabel;

    @FXML private VBox teachSkillsContainer;
    @FXML private VBox learnSkillsContainer;
    @FXML private VBox hobbiesContainer;
    @FXML private VBox reviewsContainer;
    @FXML private StackPane popupLayer;

    @FXML private Button addTeachSkillButton;
    @FXML private Button addLearnSkillButton;
    @FXML private Button addHobbyButton;
    @FXML private Button postButton;
    @FXML private Button editProfileButton;
    @FXML private Button requestPageButton;
    @FXML private Button logoutButton;

    @FXML private HBox headerBar;
    @FXML private HBox bottomNav;
    @FXML private HBox membersContainer;

    @FXML
    public void initialize() {
        currentUser = SessionManager.getUser();

        if (currentUser != null) {
            loadProfileData();
            loadSkills();
            loadHobbies();
            loadReviews();
            loadGroupMembers();
        } else {
            showAlert("Error", "No user logged in");
        }

        profileImage.setOnMouseClicked(e -> chooseProfilePicture());
        applyTheme();
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

        if (headerBar != null) {
            headerBar.setStyle(headerStyle);
        }

        if (bottomNav != null) {
            bottomNav.setStyle("-fx-background-color: " + gradient + ";");
        }

        if (editProfileButton != null) {
            editProfileButton.setStyle("-fx-background-color:" + ThemeManager.primaryStart + ";" +
                    " -fx-text-fill: white;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 8 20;" +
                    " -fx-background-radius: 20;");
        }

        if (addTeachSkillButton != null) {
            String addButtonStyle = "-fx-background-color: " + ThemeManager.primaryBackGround + ";" +
                    "-fx-text-fill: " + ThemeManager.primaryStart + ";" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 8 15;" +
                    "-fx-background-radius: 15;" +
                    "-fx-cursor: hand;";

            addTeachSkillButton.setStyle(addButtonStyle);

            if (addLearnSkillButton != null) {
                addLearnSkillButton.setStyle(addButtonStyle);
            }

            if (addHobbyButton != null) {
                addHobbyButton.setStyle(addButtonStyle);
            }
        }
    }

    private void loadProfileData() {
        fullNameLabel.setText(currentUser.getFullName());
        usernameLabel.setText("@" + (currentUser.getUsername() != null ? currentUser.getUsername() : "user"));
        emailLabel.setText(currentUser.getEmail());
        phoneLabel.setText(currentUser.getPhone() != null ? currentUser.getPhone() : "Not provided");
        locationLabel.setText(currentUser.getLocation() != null ? currentUser.getLocation() : "Not specified");
        bioLabel.setText(currentUser.getBio() != null ? currentUser.getBio() : "No bio yet. Click Edit Profile to add one.");

        if (currentUser.getJoinDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
            joinDateLabel.setText("Member since: " + currentUser.getJoinDate().format(formatter));
        } else {
            joinDateLabel.setText("Member since: Recently");
        }

        ratingLabel.setText(currentUser.getFormattedRating());
        loadProfileImage();
    }

    private void loadSkills() {
        teachSkillsContainer.getChildren().clear();
        learnSkillsContainer.getChildren().clear();

        List<Skill> teachSkills = currentUser.getTeachSkills();
        if (teachSkills.isEmpty()) {
            Label emptyLabel = new Label("No skills added yet");
            emptyLabel.setStyle("-fx-text-fill: #888888; -fx-font-style: italic;");
            teachSkillsContainer.getChildren().add(emptyLabel);
        } else {
            for (Skill skill : teachSkills) {
                teachSkillsContainer.getChildren().add(createSkillRow(skill));
            }
        }

        List<Skill> learnSkills = currentUser.getLearnSkills();
        if (learnSkills.isEmpty()) {
            Label emptyLabel = new Label("No skills added yet");
            emptyLabel.setStyle("-fx-text-fill: #888888; -fx-font-style: italic;");
            learnSkillsContainer.getChildren().add(emptyLabel);
        } else {
            for (Skill skill : learnSkills) {
                learnSkillsContainer.getChildren().add(createSkillRow(skill));
            }
        }
    }

    private void loadHobbies() {
        hobbiesContainer.getChildren().clear();

        List<Hobby> hobbies = currentUser.getHobbies();
        if (hobbies == null || hobbies.isEmpty()) {
            Label emptyLabel = new Label("No hobbies added yet");
            emptyLabel.setStyle("-fx-text-fill: #888888; -fx-font-style: italic;");
            hobbiesContainer.getChildren().add(emptyLabel);
        } else {
            for (Hobby hobby : hobbies) {
                hobbiesContainer.getChildren().add(createHobbyRow(hobby));
            }
        }
    }

    private void loadReviews() {
        reviewsContainer.getChildren().clear();

        List<Review> reviews = currentUser.getReviews();
        if (reviews == null || reviews.isEmpty()) {
            Label emptyLabel = new Label("No reviews yet");
            emptyLabel.setStyle("-fx-text-fill: #888888; -fx-font-style: italic;");
            reviewsContainer.getChildren().add(emptyLabel);
        } else {
            for (Review review : reviews) {
                reviewsContainer.getChildren().add(createReviewRow(review));
            }
        }
    }

    private HBox createSkillRow(Skill skill) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-padding: 8; -fx-background-color: #F5F5F5; -fx-background-radius: 8;");
        row.setPrefHeight(40);

        String proficiencyText = skill.getProficiency() != null ? " (" + skill.getProficiency() + ")" : "";
        Label skillLabel = new Label(skill.getSkillName() + proficiencyText);
        skillLabel.setStyle("-fx-text-fill: #1F1F1F; -fx-font-size: 14px;");
        skillLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(skillLabel, Priority.ALWAYS);

        Button removeButton = new Button("Remove");
        removeButton.setStyle("-fx-background-color: #E57373; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 11px;");
        removeButton.setOnAction(e -> {
            currentUser.removeSkill(skill);
            loadSkills();

            UserDAOImpl dao = new UserDAOImpl();
            dao.removeSkill(skill.getId());
        });

        row.getChildren().addAll(skillLabel, removeButton);
        return row;
    }

    private HBox createHobbyRow(Hobby hobby) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-padding: 8; -fx-background-color: #F5F5F5; -fx-background-radius: 8;");
        row.setPrefHeight(40);

        Label hobbyLabel = new Label(hobby.getHobbyName());
        hobbyLabel.setStyle("-fx-text-fill: #1F1F1F; -fx-font-size: 14px;");
        hobbyLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(hobbyLabel, Priority.ALWAYS);

        Button removeButton = new Button("Remove");
        removeButton.setStyle("-fx-background-color: #E57373; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 11px;");
        removeButton.setOnAction(e -> {
            currentUser.removeHobby(hobby);
            loadHobbies();

            UserDAOImpl dao = new UserDAOImpl();
            dao.removeHobby(hobby.getId());
        });

        row.getChildren().addAll(hobbyLabel, removeButton);
        return row;
    }

    private HBox createReviewRow(Review review) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-padding: 10; -fx-background-color: #FAFAFA; -fx-background-radius: 8; -fx-border-color: #E0E0E0; -fx-border-radius: 8;");

        String stars = getStarString(review.getRating());

        Label ratingLabel = new Label(stars);
        ratingLabel.setStyle("-fx-text-fill: gold;");
        ratingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gold");

        Label commentLabel = new Label(review.getComment());
        commentLabel.setStyle("-fx-text-fill: #333333; -fx-font-size: 13px;");
        commentLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(commentLabel, Priority.ALWAYS);

        row.getChildren().addAll(ratingLabel, commentLabel);
        return row;
    }

    private String getStarString(int rating) {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < rating; i++) {
            stars.append("★");
        }
        for (int i = rating; i < 5; i++) {
            stars.append("☆");
        }
        return stars.toString();
    }

    @FXML
    private void handleAddTeachSkill() {
        showAddSkillDialog(SkillType.TEACH);
    }

    @FXML
    private void handleAddLearnSkill() {
        showAddSkillDialog(SkillType.LEARN);
    }

    @FXML
    private void handleAddHobby() {
        showAddHobbyDialog();
    }

    private void showAddSkillDialog(SkillType type) {
        Stage dialog = new Stage();
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialog.setTitle(type == SkillType.TEACH ? "Add Skill You Can Teach" : "Add Skill You Want to Learn");

        VBox layout = new VBox(15);
        layout.setStyle("-fx-background-color: white; -fx-padding: 25; -fx-alignment: center; -fx-background-radius: 15;");

        Label title = new Label(type == SkillType.TEACH ? "Add a Skill You Can Teach" : "Add a Skill You Want to Learn");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0C4D3B;");

        TextField skillNameField = new TextField();
        skillNameField.setPromptText("Skill name (e.g., Java, Guitar)");
        skillNameField.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 10; -fx-padding: 10;");

        ComboBox<Proficiency> proficiencyBox = new ComboBox<>();
        proficiencyBox.getItems().setAll(Proficiency.values());
        proficiencyBox.setValue(Proficiency.BEGINNER);
        proficiencyBox.setStyle("-fx-background-radius: 10;");

        Label message = new Label();
        message.setStyle("-fx-text-fill: red;");

        Button addBtn = new Button("Add Skill");
        addBtn.setStyle("-fx-background-color: " + ThemeManager.primaryStart + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 10;");

        addBtn.setOnAction(e -> {
            String skillName = skillNameField.getText().trim();
            if (skillName.isEmpty()) {
                message.setText("Skill name is required");
                return;
            }

            Skill skill = new Skill(0, skillName, type);
            skill.setProficiency(proficiencyBox.getValue());
            currentUser.addSkill(skill);

            skill.setUserId(currentUser.getId());
            UserDAOImpl dao = new UserDAOImpl();
            dao.addSkill(skill);

            dialog.close();
            loadSkills();
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #999999; -fx-text-fill: white; -fx-padding: 8 20; -fx-background-radius: 10;");
        cancelBtn.setOnAction(e -> dialog.close());

        HBox buttons = new HBox(10, addBtn, cancelBtn);
        buttons.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(title, skillNameField, proficiencyBox, message, buttons);
        Scene scene = new Scene(layout, 350, 300);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void showAddHobbyDialog() {
        Stage dialog = new Stage();
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialog.setTitle("Add Hobby");

        VBox layout = new VBox(15);
        layout.setStyle("-fx-background-color: white; -fx-padding: 25; -fx-alignment: center; -fx-background-radius: 15;");

        Label title = new Label("Add a Hobby");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0C4D3B;");

        TextField hobbyNameField = new TextField();
        hobbyNameField.setPromptText("Hobby name (e.g., Hiking, Chess)");
        hobbyNameField.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 10; -fx-padding: 10;");

        Label message = new Label();
        message.setStyle("-fx-text-fill: red;");

        Button addBtn = new Button("Add Hobby");
        addBtn.setStyle("-fx-background-color: " + ThemeManager.primaryStart + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 10;");

        addBtn.setOnAction(e -> {
            String hobbyName = hobbyNameField.getText().trim();
            if (hobbyName.isEmpty()) {
                message.setText("Hobby name is required");
                return;
            }

            Hobby hobby = new Hobby(0, hobbyName);
            currentUser.addHobby(hobby);

            hobby.setUserId(currentUser.getId());
            UserDAOImpl dao = new UserDAOImpl();
            dao.addHobby(hobby);

            dialog.close();
            loadHobbies();
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #999999; -fx-text-fill: white; -fx-padding: 8 20; -fx-background-radius: 10;");
        cancelBtn.setOnAction(e -> dialog.close());

        HBox buttons = new HBox(10, addBtn, cancelBtn);
        buttons.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(title, hobbyNameField, message, buttons);
        Scene scene = new Scene(layout, 350, 250);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    @FXML
    private void handleEditProfile() {
        showEditProfileDialog();
    }

    private void showEditProfileDialog() {
        Stage dialog = new Stage();
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialog.setTitle("Edit Profile");

        VBox layout = new VBox(15);
        layout.setStyle("-fx-background-color: white; -fx-padding: 25; -fx-alignment: center; -fx-background-radius: 15;");

        Label title = new Label("Edit Your Profile");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0C4D3B;");

        TextField fullNameField = new TextField(currentUser.getFullName());
        fullNameField.setPromptText("Full Name");
        fullNameField.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 10; -fx-padding: 10;");

        TextField usernameField = new TextField(currentUser.getUsername());
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 10; -fx-padding: 10;");

        TextField emailField = new TextField(currentUser.getEmail());
        emailField.setPromptText("Email");
        emailField.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 10; -fx-padding: 10;");

        TextField phoneField = new TextField(currentUser.getPhone());
        phoneField.setPromptText("Phone");
        phoneField.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 10; -fx-padding: 10;");

        TextField locationField = new TextField(currentUser.getLocation());
        locationField.setPromptText("Location");
        locationField.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 10; -fx-padding: 10;");

        TextArea bioArea = new TextArea(currentUser.getBio());
        bioArea.setPromptText("Bio");
        bioArea.setPrefRowCount(4);
        bioArea.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 10;");

        Label message = new Label();
        message.setStyle("-fx-text-fill: red;");

        Button saveBtn = new Button("Save Changes");
        saveBtn.setStyle("-fx-background-color: #0C4D3B; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30; -fx-background-radius: 10;");

        saveBtn.setOnAction(e -> {
            String[] names = fullNameField.getText().trim().split(" ", 2);
            currentUser.setFirstName(names[0]);
            currentUser.setLastName(names.length > 1 ? names[1] : "");
            currentUser.setUsername(usernameField.getText().trim());
            currentUser.setEmail(emailField.getText().trim());
            currentUser.setPhone(phoneField.getText().trim());
            currentUser.setLocation(locationField.getText().trim());
            currentUser.setBio(bioArea.getText().trim());

            UserDAOImpl dao = new UserDAOImpl();
            dao.updateUserProfile(currentUser);

            SessionManager.setUser(currentUser);
            loadProfileData();

            dialog.close();
            showAlert("Success", "Profile updated successfully!");
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #999999; -fx-text-fill: white; -fx-padding: 10 30; -fx-background-radius: 10;");
        cancelBtn.setOnAction(e -> dialog.close());

        HBox buttons = new HBox(15, saveBtn, cancelBtn);
        buttons.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(title, fullNameField, usernameField, emailField, phoneField, locationField, bioArea, message, buttons);

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(scrollPane, 500, 600);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    @FXML
    public void chooseProfilePicture() {
        if (currentUser == null) {
            return;
        }

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
                if (!destDir.exists()) {
                    destDir.mkdir();
                }

                File dest = new File(destDir, newFileName);
                Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

                saveProfilePicture(newFileName);
                currentUser.setProfilePicture(newFileName);
                SessionManager.setUser(currentUser);
                loadProfileImage();
                showAlert("Success", "Profile picture updated!");

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to save profile picture");
            }
        }
    }

    private void saveProfilePicture(String filename) {
        try {
            java.sql.Connection conn = DBconnection.connect();
            String sql = "UPDATE Users SET profile_picture = ? WHERE email = ?";
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
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
                    Image image = new Image(file.toURI().toString(), false);
                    profileImage.setImage(image);
                    return;
                }
            }

            Image defaultImage = new Image(
                    getClass().getResource("/com/example/newdesign/images/default.png").toString()
            );
            profileImage.setImage(defaultImage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHome() throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) profileImage.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void handleSearch() throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("search-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) profileImage.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void handlePostButton() throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("post-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) postButton.getScene().getWindow();

        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), scene.getRoot());
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        stage.setScene(scene);
    }

    @FXML
    private void handleAIChat() throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("requests-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) requestPageButton.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void handleProfile() {
        loadProfileData();
        loadSkills();
        loadHobbies();
        loadReviews();
    }

    @FXML
    private void handleLogout() throws Exception {
        SessionManager.clear();

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) logoutButton.getScene().getWindow();

        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), scene.getRoot());
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        stage.setScene(scene);
    }

    private void loadGroupMembers() {
        List<User> members = getMyGroupMembers();
        membersContainer.getChildren().clear();

        for (User user : members) {
            VBox memberCard = new VBox(5);
            memberCard.setAlignment(Pos.CENTER);

            ImageView avatar = new ImageView();
            avatar.setFitHeight(50);
            avatar.setFitWidth(50);

            try {
                if (user.getProfilePicture() != null) {
                    File file = new File("profile_images/" + user.getProfilePicture());
                    if (file.exists()) {
                        avatar.setImage(new Image(file.toURI().toString()));
                    }
                }
            } catch (Exception ignored) {
            }

            if (avatar.getImage() == null) {
                avatar.setImage(new Image(
                        getClass().getResource("/com/example/newdesign/images/default.png").toString()
                ));
            }

            avatar.setClip(new Circle(25, 25, 25));

            Label name = new Label(user.getFullName());
            name.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: black");

            avatar.setOnMouseClicked(e -> showUserPopUp(user));

            memberCard.getChildren().addAll(avatar, name);
            membersContainer.getChildren().add(memberCard);
        }
    }

    private List<User> getMyGroupMembers() {
        PostParticipantDAO participantDAO = new PostParticipantDaoImpl();
        UserDAO userDAO = new UserDAOImpl();
        PostDAO postDAO = new PostDaoImpl();

        List<User> members = new ArrayList<>();
        Set<Integer> added = new HashSet<>();

        List<Integer> myPosts = participantDAO.getPostIdsByUser(currentUser.getId());

        for (int postId : myPosts) {
            List<Integer> users = participantDAO.getUserIdsByPost(postId);

            Post post = postDAO.getPostById(postId);
            if (post != null) {
                int ownerId = post.getUserId();

                if (ownerId != currentUser.getId() && !added.contains(ownerId)) {
                    User owner = userDAO.getUserById(ownerId);
                    if (owner != null) {
                        members.add(owner);
                        added.add(ownerId);
                    }
                }
            }

            for (int id : users) {
                if (id != currentUser.getId() && !added.contains(id)) {
                    User u = userDAO.getUserById(id);
                    if (u != null) {
                        members.add(u);
                        added.add(id);
                    }
                }
            }
        }

        return members;
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
        card.setStyle(
                "-fx-background-color: " + ThemeManager.primaryBackGround + ";" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 20;" +
                        "-fx-border-radius: 20;" +
                        "-fx-border-color: " + ThemeManager.primaryStart + ";" +
                        "-fx-border-width: 5;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);"
        );

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
        imageView.setClip(new Circle(25, 25, 25));

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

        Label reviewTitle = new Label("Leave a Review");
        reviewTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        ComboBox<Integer> ratingBox = new ComboBox<>();
        ratingBox.getItems().addAll(1, 2, 3, 4, 5);
        ratingBox.setPromptText("Rating / 5");

        TextArea reviewArea = new TextArea();
        reviewArea.setPromptText("Write your review...");
        reviewArea.setPrefRowCount(3);
        reviewArea.setWrapText(true);
        reviewArea.setMaxWidth(260);

        Button submitReviewButton = new Button("Submit Review");
        submitReviewButton.setStyle(
                "-fx-background-color: " + ThemeManager.primaryStart + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 7 12;"
        );

        submitReviewButton.setOnAction(e -> {
            Integer rating = ratingBox.getValue();
            String comment = reviewArea.getText().trim();

            if (rating == null || comment.isEmpty()) {
                Notifier.showToast(popupLayer, "Please select a rating and write a review.");
                return;
            }

            User reviewer = SessionManager.getUser();

            if (reviewer == null) {
                Notifier.showToast(popupLayer, "You must be logged in to leave a review.");
                return;
            }

            Review review = new Review(
                    0,
                    reviewer.getId(),
                    user.getId(),
                    0,
                    rating,
                    comment,
                    java.time.LocalDateTime.now()
            );

            UserDAOImpl dao = new UserDAOImpl();
            boolean saved = dao.addReview(review);

            if (saved) {
                Notifier.showToast(popupLayer, "Review submitted successfully.");
                ratingBox.setValue(null);
                reviewArea.clear();
            } else {
                Notifier.showToast(popupLayer, "Review could not be saved.");
            }
        });

        card.getChildren().addAll(
                topBar,
                header,
                email,
                bio,
                skills,
                hobbies,
                reviewTitle,
                ratingBox,
                reviewArea,
                submitReviewButton
        );

        overlay.getChildren().add(card);

        overlay.setOnMouseClicked(e -> popupLayer.setVisible(false));
        card.setOnMouseClicked(e -> e.consume());

        popupLayer.getChildren().add(overlay);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}