package com.example.newdesign.controller;

import com.example.newdesign.HelloApplication;
import com.example.newdesign.model.*;
import com.example.newdesign.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    @FXML private Button sendButton;
    @FXML private Button backButton;
    @FXML private Button addUserButton;
    @FXML private Button homeButton;
    @FXML private Button searchButton;
    @FXML private Button profileButton;


    boolean editingOrSending = true; //FALSE = Editing TRUE = Sending
    Message editingMessage;

    private MessageDAOImpl messageDAO = new MessageDAOImpl();
    private User currentUser = SessionManager.getUser();
    private User recieverUser = SearchController.Otheruser;

    @FXML
    public void initialize(){
        loadMessages();
        applyTheme();
    }


    // MAIN CONTENTS

    private void loadMessages(){
        messageContainer.getChildren().clear();

        List<Message> messages = messageDAO.getMessages(currentUser.getId(), recieverUser.getId());

        if(messages != null)
        {
            for(Message message: messages)
            {
                messageContainer.getChildren().add(createMessageContainer(message));
            }
        }

    }

    private HBox createMessageContainer(Message message){
        HBox row = new HBox(10);

        row.setStyle("-fx-padding: 8; -fx-background-color: #f0eded; -fx-background-radius: 8; -fx-pref-width: 1175");
        row.setPrefHeight(40);

        Label messageLabel = new Label(message.getMessageText());

        row.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(messageLabel, Priority.ALWAYS);

        if(message.getSenderId() == currentUser.getId()){
            row.setAlignment(Pos.CENTER_RIGHT);
            row.getChildren().addAll(messageLabel);
        }
        else{
            row.setAlignment(Pos.CENTER_LEFT);
            Button editButton = new Button("Edit");
            Button deleteButton = new Button("Delete");
            editButton.setStyle("-fx-background-color: #f0d16c; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 11px;");
            deleteButton.setStyle("-fx-background-color: #E57373; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 11px;");
            editButton.setAlignment(Pos.BASELINE_RIGHT);
            deleteButton.setAlignment(Pos.BASELINE_RIGHT);

            deleteButton.setOnAction(e -> {
                messageDAO.deleteMessage(message.getId());
                loadMessages();
            });

            editButton.setOnAction(e -> {
                messageField.setText(message.getMessageText());
                editingOrSending = false; //Editing
                editingMessage = message;
            });


            row.getChildren().addAll(messageLabel, editButton, deleteButton);
        }

        return row;
    }


    @FXML
    private void handleSendButton(){
        String text = messageField.getText();
        if(editingOrSending){
            if(!text.isEmpty()){
                messageDAO.addMessage(text, currentUser.getId(), recieverUser.getId());
                loadMessages();
                messageField.setText("");
            }
            else{
                showAlert("Empty Message", "Please send thoughtful messages");
            }
        }
        else{
            messageDAO.editMessage(editingMessage.getId(), text);
            loadMessages();
            messageField.setText("");
            editingOrSending = true;
        }


    }


    // Buttons

    @FXML
    private void handleHomeButton() throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) homeButton.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void handleProfileButton() throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("profile-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) profileButton.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void handleSearchButton() throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("search-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) searchButton.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void handleOtherUserButton() throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("otherUserProfile-view.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) backButton.getScene().getWindow();
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

    //Themes
    // Applying theme color in here
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

    }

}
