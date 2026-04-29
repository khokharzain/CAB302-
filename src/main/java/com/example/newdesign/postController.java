package com.example.newdesign;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;

import java.io.File;

public class postController {

    @FXML private HBox headerBar;
    @FXML private HBox bottomNav;

    @FXML private TextArea postTextArea;
    @FXML private Button publishButton;

    @FXML private Label userName;
    @FXML private ImageView profileImage;
    @FXML private StackPane rootPane;


    @FXML private TextField maxParticipantsField;

    private PostDAO postDAO = new PostDaoImpl();

    public void initialize(){
        loadCurrentUser();
        applyTheme();
    }

    public void applyTheme(){
        String gradient = "linear-gradient(to right, "
                + ThemeManager.primaryStart + ", "
                + ThemeManager.primaryEnd + ")";

        if (headerBar != null){
            headerBar.setStyle("-fx-background-color: " + gradient + ";");
        }

        if (bottomNav != null){
            bottomNav.setStyle("-fx-background-color: " + gradient + ";");
        }

        if(postTextArea != null){
            postTextArea.setStyle("-fx-background-radius: 10;");
        }

        if(publishButton != null){
            publishButton.setStyle("-fx-background-color:" + gradient + "; -fx-text-fill: white;");
        }
    }

    private void loadCurrentUser() {

        User currentUser = SessionManager.getUser();
        if (currentUser == null) return;

        userName.setText(
                currentUser.getFirstName() + " " + currentUser.getLastName()
        );

        try {
            if (currentUser.getProfilePicture() != null) {

                File file = new File("profile_images/" + currentUser.getProfilePicture());

                if (file.exists()) {
                    profileImage.setImage(new Image(file.toURI().toString()));
                    applyCircleClip();
                    return;
                }
            }

            profileImage.setImage(new Image(
                    getClass().getResource("/com/example/newdesign/images/default.png").toExternalForm()
            ));

            applyCircleClip();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyCircleClip() {
        Circle clip = new Circle(22.5, 22.5, 22.5);
        profileImage.setClip(clip);
    }

    // 🚀 FIXED PUBLISH METHOD
    public void handlePublish(){

        String content = postTextArea.getText();

        if(content == null || content.isEmpty()){
            Notifier.showToast(rootPane, "Post cannot be empty");
            return;
        }

        User currentUser = SessionManager.getUser();
        if(currentUser == null){
            System.out.println("No user logged in");
            return;
        }

        //  get max participants
        int maxParticipants = 1;

        try {
            if (maxParticipantsField != null && !maxParticipantsField.getText().isEmpty()) {
                maxParticipants = Integer.parseInt(maxParticipantsField.getText());
            }
        } catch (Exception e) {
            Notifier.showToast(rootPane, "Invalid number");
            return;
        }

        // NEW CONSTRUCTOR
        Post post = new Post(currentUser.getId(), content, maxParticipants);

        postDAO.addPost(post);

        postTextArea.clear();
        if(maxParticipantsField != null) maxParticipantsField.clear();

        Notifier.showToast(rootPane, "Post published!");
    }

    // ================= NAVIGATION =================

    public void handleSearchButton() throws Exception{
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("search-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) headerBar.getScene().getWindow();
        stage.setScene(scene);
    }

    public void handlePostButton()throws Exception{
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("post-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) headerBar.getScene().getWindow();
        stage.setScene(scene);
    }

    public void handleProfileButton()throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("profile-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) headerBar.getScene().getWindow();
        stage.setScene(scene);
    }

    public void handleHomeButton()throws Exception{
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) headerBar.getScene().getWindow();
        stage.setScene(scene);
    }
}