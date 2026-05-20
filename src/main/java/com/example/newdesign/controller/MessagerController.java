package com.example.newdesign.controller;

import com.example.newdesign.HelloApplication;
import com.example.newdesign.model.*;
import com.example.newdesign.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;


public class MessagerController {

    // ========== FXML Components ==========
    @FXML private TextField messageField;

    @FXML private VBox messageContainer;

    @FXML private HBox headerBar;
    @FXML private HBox bottomNav;

    @FXML private Button SendButton;
    @FXML private Button backButton;
    @FXML private Button addUserButton;
    @FXML private Button homeButton;
    @FXML private Button searchButton;
    @FXML private Button profileButton;
    @FXML private Button postPageButton;
    @FXML private Button requestPageButton;


    boolean editingOrSending = true; //FALSE = Editing TRUE = Sending
    Message editingMessage;

    private MessageDAOImpl messageDAO = new MessageDAOImpl();
    private User currentUser;
    private User recieverUser;
    private int currentGroupid;

    @FXML
    public void initialize(){

        applyTheme();
        currentUser = SessionManager.getUser();
        messageContainer.setFillWidth(true);
    }

    public void setSelectedUser(User user){

        this.recieverUser = user;

        loadMessages();
    }

    // MAIN CONTENTS

    /**
     * A method that gets a list of messages and creates Message objects to
     * display on the UI
     */
    private void loadMessages(){

        if(recieverUser == null){
            return;
        }

        messageContainer.getChildren().clear();

        List<Message> messages =
                messageDAO.getMessages(
                        currentUser.getId(),
                        recieverUser.getId()
                );

        if(messages != null && !messages.isEmpty())
        {
            currentGroupid = messages.getFirst().getGroupId();
            for(Message message: messages)
            {
                messageContainer.getChildren().add(createMessageContainer(message));
            }
        }
        else
        {
            //Assume this is a new group chat (No ID)
            // Create a new group ID by grabbing the max ID and adding 1
            currentGroupid = messageDAO.getMaxGroupId() + 1;
        }

    }

    /**
     * A method that creates the Message UI to display on the interface
     * It creates a different UI depending if it's the User's message or the Reciever's
     * A User's Message will have an edit and delete button while a reciever's does not
     *
     * @param message
     * @return row
     */
    private HBox createMessageContainer(Message message) {



        HBox container = new HBox();
        container.setPadding(new Insets(6, 12, 6, 12));
        container.setMaxWidth(Double.MAX_VALUE);
        container.prefWidthProperty().bind(messageContainer.widthProperty());



        HBox row = new HBox(10);
        row.setPadding(new Insets(10, 14, 10, 14));
        row.setMinHeight(40);
        row.setMaxWidth(420);


        Label messageLabel = new Label(message.getMessageText());

        messageLabel.setWrapText(true);

        messageLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-family: 'Segoe UI';"
        );





        if (message.getSenderId() == currentUser.getId()) {

            container.setAlignment(Pos.CENTER_RIGHT);

            row.setStyle(
                    "-fx-background-color: linear-gradient(to bottom right, " +
                            ThemeManager.primaryStart + ", " +
                            ThemeManager.primaryEnd + ");" +

                            "-fx-background-radius: 22 22 6 22;" +
                            "-fx-border-radius: 22 22 6 22;" +

                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 10, 0.2, 0, 3);"
            );



            MenuButton menuButton = new MenuButton("⋮");

            MenuItem editItem = new MenuItem("Edit");
            MenuItem deleteItem = new MenuItem("Delete");

            menuButton.getItems().addAll(editItem, deleteItem);

            menuButton.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 15px;" +
                            "-fx-cursor: hand;"
            );


            editItem.setOnAction(e -> {

                messageField.setText(message.getMessageText());

                editingOrSending = false;
                editingMessage = message;
            });



            deleteItem.setOnAction(e -> {

                messageDAO.deleteMessage(message.getId());

                Platform.runLater(this::loadMessages);
            });

            row.getChildren().addAll(messageLabel, menuButton);
        }


        else {

            container.setAlignment(Pos.CENTER_LEFT);

            row.setStyle(
                    "-fx-background-color: rgba(100,100,100,0.3);" +
                            "-fx-background-radius: 22 22 22 6;" +
                            "-fx-border-radius: 22 22 22 6;"
            );

            messageLabel.setStyle(
                    "-fx-text-fill: black;" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-family: 'Segoe UI';"
            );

            row.getChildren().add(messageLabel);
        }



        row.setOnMouseEntered(e -> row.setOpacity(0.92));

        row.setOnMouseExited(e -> row.setOpacity(1));



        container.getChildren().add(row);

        return container;
    }

    /**
     * Send button is multifunctional
     * If editingOrSending is TRUE: The button will Add the message to the DB and display it
     * If editingOrSending is FALSE: The button will edit the message before reloading the messages
     */
    @FXML
    private void handleSendButton() {

        String text = messageField.getText();

        if (editingOrSending) {

            if (!text.isEmpty()) {

                // CHECK IF RECEIVER EXISTS
                if (recieverUser == null) {
                    showAlert("Error", "No receiver selected.");
                    return;
                }

                messageDAO.addMessage(
                        text,
                        currentUser.getId(),
                        recieverUser.getId(),
                        currentGroupid
                );

                loadMessages();
                messageField.setText("");
            }
            else {
                showAlert("Empty Message", "Please send thoughtful messages");
            }

        }
        else {

            messageDAO.editMessage(editingMessage.getId(), text);

            loadMessages();
            messageField.setText("");

            editingOrSending = true;
        }
    }

    // Buttons
    /**
     * Any Button with this method changes the Scene to the Home Screen
     * @throws Exception
     */
    @FXML
    private void handleHomeButton() throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) homeButton.getScene().getWindow();
        stage.setScene(scene);
    }

    /**
     * Any Button with this method changes the Scene to the User Profile Screen
     * @throws Exception
     */
    @FXML
    private void handleProfileButton() throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("profile-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) profileButton.getScene().getWindow();
        stage.setScene(scene);
    }

    /**
     * Any Button with this method changes the Scene to the Search Screen
     * @throws Exception
     */
    @FXML
    private void handleSearchButton() throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("search-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) searchButton.getScene().getWindow();
        stage.setScene(scene);
    }

    /**
     * Any Button with this method changes the Scene to the Other User (Selected User) Profile Screen
     * @throws Exception
     */
    @FXML
    private void handleOtherUserButton() throws Exception {

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("otherUserProfile-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 1200, 800);

        // GET CONTROLLER
        OtherUserProfileController controller = loader.getController();

        // PASS THE USER
        controller.setSelectedUser(recieverUser);

        Stage stage = (Stage) backButton.getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }
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


    // ========== Helper ==========

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Applies the chosen theme to the header and footer
     */
    private void applyTheme(){
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
        if(SendButton != null)
        {
            SendButton.setStyle("-fx-background-color: " + ThemeManager.primaryEnd + ";");
        }


    }

}
