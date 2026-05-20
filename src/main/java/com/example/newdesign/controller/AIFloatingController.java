package com.example.newdesign.controller;

import com.example.newdesign.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AIFloatingController {

    @FXML private VBox comparePanel;
    @FXML private ComboBox<String> user1Combo;
    @FXML private ComboBox<String> user2Combo;
    @FXML private VBox responseArea;
    @FXML private Button backButton;
    @FXML private TextArea welcomeArea;

    private User currentUser;
    private UserDAOImpl userDAO = new UserDAOImpl();
    private List<User> allUsers = new ArrayList<>();
    private OllamaService ollamaService = new OllamaService();

    @FXML
    public void initialize() {
        currentUser = SessionManager.getUser();
        if (currentUser != null) {
            loadAllUsers();
        }

        comparePanel.setVisible(false);
        comparePanel.setManaged(false);
        backButton.setVisible(false);
        backButton.setManaged(false);
    }

    private void loadAllUsers() {
        List<User> basicUsers = userDAO.searchUsers("");
        allUsers.clear();

        for (User u : basicUsers) {
            if (u.getId() != currentUser.getId()) {
                User fullUser = userDAO.getUserById(u.getId());
                allUsers.add(fullUser);
            }
        }

        if (user1Combo != null && user2Combo != null) {
            user1Combo.getItems().clear();
            user2Combo.getItems().clear();
            for (User u : allUsers) {
                String display = u.getFullName() + " (" + u.getUsername() + ")";
                user1Combo.getItems().add(display);
                user2Combo.getItems().add(display);
            }
        }
    }

    @FXML
    public void closePopup() {
        Stage stage = (Stage) welcomeArea.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void resetToMainMenu() {
        welcomeArea.setVisible(true);
        welcomeArea.setManaged(true);
        welcomeArea.setText("👋 Hello! I'm your AI Assistant.\n\nWhat would you like to do today?");

        backButton.setVisible(false);
        backButton.setManaged(false);
        comparePanel.setVisible(false);
        comparePanel.setManaged(false);

        responseArea.getChildren().clear();
        TextArea ta = new TextArea("Click any button to get started...");
        ta.setWrapText(true);
        ta.setEditable(false);
        ta.setStyle("-fx-font-family: 'SF Pro Text', 'Helvetica', Arial, sans-serif; -fx-font-size: 13px; -fx-background-color: #FAFAFA; -fx-border: none;");
        responseArea.getChildren().add(ta);
    }

    private void showResponse(String text) {
        welcomeArea.setVisible(false);
        welcomeArea.setManaged(false);
        backButton.setVisible(true);
        backButton.setManaged(true);

        responseArea.getChildren().clear();
        TextArea ta = new TextArea(text);
        ta.setWrapText(true);
        ta.setEditable(false);
        ta.setStyle("-fx-font-family: 'SF Pro Text', 'Helvetica', Arial, sans-serif; -fx-font-size: 13px; -fx-background-color: #FAFAFA; -fx-border: none;");
        ta.setPrefHeight(180);
        responseArea.getChildren().add(ta);
    }

    @FXML
    public void handleFindMatches() {
        if (currentUser == null) {
            showResponse("❌ Error: No user logged in.");
            return;
        }

        if (currentUser.getTeachSkills().isEmpty() && currentUser.getLearnSkills().isEmpty()) {
            showResponse("❌ You haven't added any skills!\n\nGo to PROFILE and add:\n• Skills you can TEACH\n• Skills you want to LEARN");
            return;
        }

        List<UserMatch> matches = calculateMatches();

        if (matches.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("❌ NO MATCHES FOUND\n\n");
            sb.append("Your skills:\n");
            sb.append("📚 You teach: ").append(formatSkills(currentUser.getTeachSkills())).append("\n");
            sb.append("🎯 You want: ").append(formatSkills(currentUser.getLearnSkills())).append("\n\n");
            sb.append("Other users (").append(allUsers.size()).append("):\n");

            if (allUsers.isEmpty()) {
                sb.append("❌ No other users found!\nCreate another account to test matches.");
            } else {
                for (User u : allUsers) {
                    sb.append("\n• ").append(u.getFullName()).append("\n");
                    sb.append("  Teaches: ").append(formatSkills(u.getTeachSkills())).append("\n");
                    sb.append("  Wants: ").append(formatSkills(u.getLearnSkills())).append("\n");
                }
            }
            showResponse(sb.toString());
            return;
        }

        StringBuilder response = new StringBuilder();
        response.append("🤖 YOUR BEST MATCHES\n");
        response.append("═══════════════════════════════════\n\n");

        for (int i = 0; i < Math.min(3, matches.size()); i++) {
            UserMatch match = matches.get(i);
            String medal = i == 0 ? "⭐ TOP MATCH" : (i == 1 ? "🥈 RECOMMENDED" : "🥉 ALSO GOOD");

            response.append(String.format("""
                %s: %s (%d%% match)
                ─────────────────────────
                📚 Teaches: %s
                🎯 Wants to learn: %s
                💡 %s
                
                """,
                    medal,
                    match.user.getFullName(),
                    match.score,
                    formatSkills(match.user.getTeachSkills()),
                    formatSkills(match.user.getLearnSkills()),
                    getMatchReason(match.user)
            ));
        }

        response.append("═══════════════════════════════════\n");
        response.append("👉 Go to SEARCH page to connect!");

        showResponse(response.toString());
    }

    @FXML
    public void showComparePanel() {
        welcomeArea.setVisible(false);
        welcomeArea.setManaged(false);
        backButton.setVisible(true);
        backButton.setManaged(true);
        comparePanel.setVisible(true);
        comparePanel.setManaged(true);
    }

    @FXML
    public void hideComparePanel() {
        comparePanel.setVisible(false);
        comparePanel.setManaged(false);
        resetToMainMenu();
    }

    @FXML
    public void handleCompareUsers() {
        String selected1 = user1Combo.getValue();
        String selected2 = user2Combo.getValue();

        if (selected1 == null || selected2 == null) {
            showResponse("❌ Please select two users to compare");
            return;
        }

        if (selected1.equals(selected2)) {
            showResponse("❌ Please select two different users");
            return;
        }

        User user1 = findUserByDisplayName(selected1);
        User user2 = findUserByDisplayName(selected2);

        int score1 = calculateMatchScore(user1);
        int score2 = calculateMatchScore(user2);

        String response = String.format("""
            📊 USER COMPARISON
            ═══════════════════════════════════
            
            %-20s %-20s
            ───────────────────────────────────
            Teaches: %-17s %-17s
            Wants:   %-17s %-17s
            Match:   %d%%                 %d%%
            ═══════════════════════════════════
            
            ✅ %s is the better match!
            
            💡 Why? Their compatibility score is higher.
            """,
                user1.getFullName(), user2.getFullName(),
                truncate(formatSkills(user1.getTeachSkills()), 17),
                truncate(formatSkills(user2.getTeachSkills()), 17),
                truncate(formatSkills(user1.getLearnSkills()), 17),
                truncate(formatSkills(user2.getLearnSkills()), 17),
                score1, score2,
                score1 > score2 ? user1.getFullName() : user2.getFullName()
        );

        showResponse(response);
        hideComparePanel();
    }

    @FXML
    public void handleScheduleSession() {
        String response = """
            📅 SCHEDULE SESSION
            ═══════════════════════════════════
            
            To schedule a skill exchange session:
            
            1️⃣ First find a match using "FIND MATCHES"
            2️⃣ Go to SEARCH page and click on the user
            3️⃣ Start a conversation to arrange time
            
            💡 Suggested time slots:
            • Weekdays: 5 PM - 8 PM
            • Weekends: 10 AM - 6 PM
            
            💡 Suggested locations:
            • Online (Zoom/Google Meet)
            • Local coffee shop
            • Library study room
            
            ═══════════════════════════════════
            💬 Click BACK to return to menu
            """;

        showResponse(response);
    }

    private static class UserMatch {
        User user;
        int score;
        UserMatch(User u, int s) { user = u; score = s; }
    }

    private List<UserMatch> calculateMatches() {
        List<UserMatch> matches = new ArrayList<>();
        for (User u : allUsers) {
            int score = calculateMatchScore(u);
            if (score > 0) matches.add(new UserMatch(u, score));
        }
        matches.sort((a, b) -> b.score - a.score);
        return matches;
    }

    private int calculateMatchScore(User other) {
        int score = 0;
        for (Skill myWant : currentUser.getLearnSkills())
            for (Skill theirTeach : other.getTeachSkills())
                if (myWant.getSkillName().equalsIgnoreCase(theirTeach.getSkillName())) score += 50;
        for (Skill myTeach : currentUser.getTeachSkills())
            for (Skill theirWant : other.getLearnSkills())
                if (myTeach.getSkillName().equalsIgnoreCase(theirWant.getSkillName())) score += 50;
        return Math.min(score, 100);
    }

    private String getMatchReason(User match) {
        for (Skill myTeach : currentUser.getTeachSkills())
            for (Skill theirWant : match.getLearnSkills())
                if (myTeach.getSkillName().equalsIgnoreCase(theirWant.getSkillName()))
                    return "You can teach them " + myTeach.getSkillName() + "!";
        for (Skill myWant : currentUser.getLearnSkills())
            for (Skill theirTeach : match.getTeachSkills())
                if (myWant.getSkillName().equalsIgnoreCase(theirTeach.getSkillName()))
                    return "They can teach you " + myWant.getSkillName() + "!";
        return "Your skills complement each other well.";
    }

    private String formatSkills(List<Skill> skills) {
        if (skills == null || skills.isEmpty()) return "None";
        return skills.stream().map(Skill::getSkillName).collect(Collectors.joining(", "));
    }

    private User findUserByDisplayName(String name) {
        for (User u : allUsers)
            if ((u.getFullName() + " (" + u.getUsername() + ")").equals(name))
                return u;
        return null;
    }

    private String truncate(String text, int length) {
        if (text == null || text.length() <= length) return text;
        return text.substring(0, length - 3) + "...";
    }
}