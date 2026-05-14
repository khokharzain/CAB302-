package com.example.newdesign;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AIFloatingController - Handles Find Matches and Compare Users features.
 * Manages AI panel UI, skill selection, user comparison, and profile popups.
 */
public class AIController {

    private User currentUser;

    // ================== AI PANEL ==================
    @FXML private VBox aiPanel;
    @FXML private VBox aiResponseArea;
    @FXML private Button floatingAISummoner;
    @FXML private Button exitButton;

    // ================== ACTION PANELS ==================
    @FXML private VBox actionButtonsPanel;

    // ================== SKILL SELECTION ==================
    @FXML private VBox skillSelectionPanel;
    @FXML private ComboBox<String> skillCombo;

    // ================== COMPARE FEATURE ==================
    @FXML private VBox comparePanel;
    @FXML private VBox compareSkillSelectionPanel;
    @FXML private ComboBox<String> compareSkillCombo;
    @FXML private VBox usersToCompareContainer;

    private UserDAOImpl userDAO = new UserDAOImpl();
    private List<User> allUsers = new ArrayList<>();
    private List<User> filteredUsers = new ArrayList<>();
    private List<CheckBox> userCheckBoxes = new ArrayList<>();

    /**
     * Initializes the AI Controller.
     * Loads the current user from SessionManager, populates skill dropdowns,
     * and sets up the UI components.
     */
    @FXML
    public void initialize() {
        User sessionUser = SessionManager.getUser();
        if (sessionUser != null) {
            User fullUser = userDAO.getUserById(sessionUser.getId());
            SessionManager.setUser(fullUser);
            currentUser = fullUser;
        }

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

        if (skillCombo != null && currentUser != null) {
            skillCombo.getItems().clear();
            for (Skill skill : currentUser.getLearnSkills()) {
                skillCombo.getItems().add(skill.getSkillName());
            }
        }

        if (compareSkillCombo != null && currentUser != null) {
            compareSkillCombo.getItems().clear();
            for (Skill skill : currentUser.getLearnSkills()) {
                compareSkillCombo.getItems().add(skill.getSkillName());
            }
        }

        loadAllUsers();
    }

    /**
     * Loads all users from the database except the current user.
     * Populates the internal list of users for matching and comparison.
     */
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

    /**
     * Toggles the AI panel visibility with smooth fade and scale animations.
     * When opening, the floating AI button disappears and the panel appears.
     * When closing, the panel disappears and the button reappears.
     */
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

    /**
     * Animates the action buttons sliding in sequentially from the left.
     * Creates a progressive reveal effect when the AI panel opens.
     */
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

    /**
     * Closes the AI panel with a fade-out animation.
     * Resets to main menu when animation completes.
     */
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

    /**
     * Resets the AI panel to the main menu state.
     * Shows action buttons, hides exit button, clears response area,
     * and shows the floating AI button.
     */
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

    /**
     * Hides the action buttons and shows the exit button.
     * Used when entering a sub-feature (Find Matches or Compare Users).
     */
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

    /**
     * Animates a container fading in.
     * Used for skill selection panels and compare panels.
     *
     * @param container the VBox to animate
     */
    private void animateContentFadeIn(VBox container) {
        if (container != null) {
            container.setOpacity(0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), container);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        }
    }

    /**
     * Handles the Find Matches button click.
     * Validates user has learn skills, then shows skill selection dropdown.
     */
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

    /**
     * Hides the skill selection panel and returns to main menu.
     */
    @FXML
    public void hideSkillSelectionPanel() {
        skillSelectionPanel.setVisible(false);
        skillSelectionPanel.setManaged(false);
        resetToMainMenu();
    }

    /**
     * Finds users who can teach the selected skill and displays match cards.
     * Calculates match scores based on skill compatibility.
     */
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

    /**
     * Displays match cards in a scrollable container with progressive animations.
     *
     * @param matches the list of matching users with their scores
     */
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

    /**
     * Creates a visual card for a user match.
     * Displays profile picture, name, rating, match percentage, skills, and reason.
     *
     * @param match the user match data
     * @return a VBox containing the match card
     */
    private VBox createMatchCard(UserMatch match) {
        VBox card = new VBox(8);
        card.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 12; -fx-padding: 12; -fx-cursor: hand;");
        card.setPadding(new Insets(12));

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 12; -fx-padding: 12; -fx-cursor: hand;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 12; -fx-padding: 12; -fx-cursor: hand;"));
        card.setOnMouseClicked(e -> showUserPopUp(match.user));

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

    /**
     * Generates a human-readable reason why a user is a good match.
     *
     * @param match the user to check
     * @return a string explaining the match reason
     */
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

    /**
     * Shows the Compare Users skill selection panel.
     */
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

    /**
     * Hides the compare skill selection panel and returns to main menu.
     */
    @FXML
    public void hideCompareSkillSelectionPanel() {
        compareSkillSelectionPanel.setVisible(false);
        compareSkillSelectionPanel.setManaged(false);
        resetToMainMenu();
    }

    /**
     * Returns to skill selection panel from user selection panel.
     */
    @FXML
    public void backToSkillSelection() {
        comparePanel.setVisible(false);
        comparePanel.setManaged(false);
        compareSkillSelectionPanel.setVisible(true);
        compareSkillSelectionPanel.setManaged(true);
        animateContentFadeIn(compareSkillSelectionPanel);
    }

    /**
     * Shows a list of users who can teach the selected skill with checkboxes.
     * Users can select exactly two users to compare.
     */
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
        instruction.setStyle("-fx-font-weight: bold; -fx-text-fill: #0C4D3B; -fx-font-size: 12px");
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

    /**
     * Creates a selectable user card with a checkbox.
     *
     * @param user the user to display
     * @param skillName the skill they teach
     * @return a VBox containing the selectable user card
     */
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

    /**
     * Compares two selected users and displays side-by-side comparison cards.
     */
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

    /**
     * Displays a side-by-side comparison of two users with skills, ratings, reviews, and AI recommendation.
     *
     * @param user1 the first user to compare
     * @param user2 the second user to compare
     */
    private void displayComparisonAsCards(User user1, User user2) {
        if (aiResponseArea != null) {
            aiResponseArea.getChildren().clear();

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
            scrollPane.setPrefHeight(350);

            VBox contentContainer = new VBox(15);
            contentContainer.setPadding(new Insets(5, 5, 5, 5));

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
                line1Label.setStyle("-fx-text-fill: #0C4D3B; -fx-font-weight: bold; -fx-font-size: 13px");
                HBox line1Box = new HBox(5);
                line1Box.setAlignment(Pos.CENTER_LEFT);
                line1Box.getChildren().addAll(checkIcon, line1Label);

                Label line2Label = new Label(" Compatibility Score: " + score1 + "% vs " + score2 + "%");
                line2Label.setStyle("-fx-text-fill: #0C4D3B; -fx-font-weight: bold; -fx-font-size: 13px");
                HBox line2Box = new HBox(5);
                line2Box.setAlignment(Pos.CENTER_LEFT);
                line2Box.getChildren().addAll(chartUpIcon, line2Label);

                Label tipLabel = new Label(" They can teach you: " + formatSkills(getMatchingTeachSkills(user1)));
                tipLabel.setStyle("-fx-text-fill: #0C4D3B; -fx-font-weight: bold; -fx-font-size: 12px");
                tipLabel.setWrapText(true);
                HBox tipBox = new HBox(5);
                tipBox.setAlignment(Pos.CENTER_LEFT);
                tipBox.getChildren().addAll(lightbulbIcon, tipLabel);

                recommendationBox.getChildren().addAll(aiBox, line1Box, line2Box, tipBox);
            } else if (score2 > score1) {
                Label line1Label = new Label(" " + user2.getFirstName() + " " + user2.getLastName() + " is a better match for you!");
                line1Label.setStyle("-fx-text-fill: #0C4D3B; -fx-font-weight: bold; -fx-font-size: 13px");
                HBox line1Box = new HBox(5);
                line1Box.setAlignment(Pos.CENTER_LEFT);
                line1Box.getChildren().addAll(checkIcon, line1Label);

                Label line2Label = new Label(" Compatibility Score: " + score2 + "% vs " + score1 + "%");
                line2Label.setStyle("-fx-text-fill: #0C4D3B; -fx-font-weight: bold; -fx-font-size: 13px");
                HBox line2Box = new HBox(5);
                line2Box.setAlignment(Pos.CENTER_LEFT);
                line2Box.getChildren().addAll(chartUpIcon, line2Label);

                Label tipLabel = new Label(" They can teach you: " + formatSkills(getMatchingTeachSkills(user2)));
                tipLabel.setStyle("-fx-text-fill: #0C4D3B; -fx-font-weight: bold; -fx-font-size: 12px");
                tipLabel.setWrapText(true);
                HBox tipBox = new HBox(5);
                tipBox.setAlignment(Pos.CENTER_LEFT);
                tipBox.getChildren().addAll(lightbulbIcon, tipLabel);

                recommendationBox.getChildren().addAll(aiBox, line1Box, line2Box, tipBox);
            } else {
                Label line1Label = new Label(" Both users have similar compatibility (" + score1 + "%)");
                line1Label.setStyle("-fx-text-fill: #0C4D3B; -fx-font-weight: bold; -fx-font-size: 13px");
                HBox line1Box = new HBox(5);
                line1Box.setAlignment(Pos.CENTER_LEFT);
                line1Box.getChildren().addAll(chartUpIcon, line1Label);

                Label tipLabel = new Label(" Consider reaching out to both users to see who responds better!");
                tipLabel.setStyle("-fx-text-fill: #0C4D3B; -fx-font-weight: bold; -fx-font-size: 12px");
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

    /**
     * Creates a comparison card for a single user.
     * Displays profile, rating, skills they teach, skills they want to learn, and reviews.
     *
     * @param user the user to display
     * @return a VBox containing the comparison card
     */
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
                starsLabel.setStyle("-fx-text-fill: #FFB800; -fx-font-size: 10px");

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
        viewProfileBtn.setOnAction(e -> showUserPopUp(user));

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

    /**
     * Calculates the compatibility match score between the current user and another user.
     * Score is calculated as:
     * - +50 points for each skill the other user teaches that current user wants to learn
     * - +50 points for each skill current user teaches that other user wants to learn
     * - Maximum score is capped at 100%
     *
     * @param other the other user to compare with
     * @return match score between 0 and 100
     */
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

    /**
     * Returns a list of skills that the other user can teach that the current user wants to learn.
     *
     * @param other the other user
     * @return list of matching teach skills
     */
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

    /**
     * Placeholder for Group Chat feature.
     * Shows a message indicating the feature is under development.
     */
    @FXML
    public void handleGroupChat() {
        showActionAndExit();
        showResponse("""
            GROUP CHAT FEATURE
            ═══════════════════════════════════
            
            This feature is currently under development!
            """);
    }

    /**
     * Displays a popup with user details (profile picture, name, bio, skills, hobbies, rating).
     * Same popup as used in the Search page.
     *
     * @param user the user to display
     */
    private void showUserPopUp(User user) {
        Stage stage = (Stage) floatingAISummoner.getScene().getWindow();
        StackPane root = (StackPane) stage.getScene().getRoot();

        StackPane popupLayer = new StackPane();
        popupLayer.setVisible(true);
        popupLayer.setStyle("-fx-background-color: rgba(0,0,0,0.4);");
        popupLayer.setAlignment(Pos.CENTER);

        VBox card = new VBox(15);
        card.setMaxWidth(300);
        card.setMaxHeight(400);
        card.setStyle(
                "-fx-background-color: #DCFFE4;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 20;" +
                        "-fx-border-radius: 20;" +
                        "-fx-border-color: #0C4D3B;" +
                        "-fx-border-width: 5;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);"
        );

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_RIGHT);
        Button closeBtn = new Button("X");
        closeBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #0C4D3B;" +
                        "-fx-font-weight: bold;"
        );
        closeBtn.setOnAction(e -> root.getChildren().remove(popupLayer));
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
        Label usernameLabel = new Label("@" + user.getUsername());
        usernameLabel.setStyle("-fx-text-fill: gray;");
        nameBox.getChildren().addAll(name, usernameLabel);
        header.getChildren().addAll(imageView, nameBox);

        Label email = new Label("Email: " + user.getEmail());
        Label bio = new Label("Bio: " + (user.getBio() == null ? "No bio" : user.getBio()));
        bio.setWrapText(true);

        Label skills = new Label(
                "Skills: " + (user.getSkills() == null || user.getSkills().isEmpty() ? "None" :
                        user.getSkills().stream()
                                .map(Skill::toString)
                                .collect(java.util.stream.Collectors.joining(", ")))
        );

        Label hobbies = new Label(
                "Hobbies: " + (user.getHobbies() == null || user.getHobbies().isEmpty() ? "None" :
                        user.getHobbies().stream()
                                .map(Hobby::toString)
                                .collect(java.util.stream.Collectors.joining(", ")))
        );

        Label rating = new Label("Rating: " + user.getFormattedRating());
        rating.setStyle("-fx-text-fill: gold");

        Separator separator = new Separator();

        card.getChildren().addAll(topBar, header, separator, email, rating, bio, skills, hobbies);
        popupLayer.getChildren().add(card);

        popupLayer.setOnMouseClicked(e -> root.getChildren().remove(popupLayer));
        card.setOnMouseClicked(e -> e.consume());

        root.getChildren().add(popupLayer);
        StackPane.setAlignment(popupLayer, Pos.CENTER);
    }

    /**
     * Formats a list of skills into a comma-separated string.
     *
     * @param skills the list of skills to format
     * @return comma-separated skill names, or "None" if empty/null
     */
    private String formatSkills(List<Skill> skills) {
        if (skills == null || skills.isEmpty()) return "None";
        return skills.stream().map(Skill::getSkillName).collect(Collectors.joining(", "));
    }

    /**
     * Converts a numerical rating into a string of star symbols.
     *
     * @param rating the rating (1-5)
     * @return a string of filled stars (★) and empty stars (☆)
     */
    private String getStarString(int rating) {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < rating; i++) stars.append("★");
        for (int i = rating; i < 5; i++) stars.append("☆");
        return stars.toString();
    }

    /**
     * Displays a text response in the AI response area with a fade-in animation.
     *
     * @param text the text to display
     */
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

    /**
     * Inner class to hold a user and their match score.
     */
    private static class UserMatch {
        User user;
        int score;
        UserMatch(User u, int s) {
            user = u;
            score = s;
        }
    }
}