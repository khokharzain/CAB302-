package com.example.newdesign;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class mainController {

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML

    private Label emailLabel;
    @FXML
    private Label phoneLabel;



    @FXML
    private VBox profileButton;
    public void handleClick() throws Exception {
        Stage stage = (Stage) profileButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("profile-view.fxml"));

        Scene scene = new Scene(loader.load(), 800, 500);
        stage.setScene(scene);

    }
    public void setUser(User user) {
        firstNameLabel.setText(user.getFirstName());
        lastNameLabel.setText(user.getLastName());
        emailLabel.setText(user.getEmail());
        phoneLabel.setText(user.getPhone());
    }
}
