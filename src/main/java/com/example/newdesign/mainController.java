package com.example.newdesign;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class mainController {

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

    public void handleProfileButton() throws Exception{
        Stage stage = (Stage) profileButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("profile-view.fxml"));

        Scene scene = new Scene(loader.load(), 1200, 800);
        stage.setScene(scene);
    }

    @FXML
    private VBox profilelayout;
    public void handleProfClick() throws Exception {
        Stage stage = (Stage) profilelayout.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("profile-view.fxml"));

        Scene scene = new Scene(loader.load(), 1200, 800);
        stage.setScene(scene);

    }
    public void setUser(User user) {
        firstNameLabel.setText(user.getFirstName());
        lastNameLabel.setText(user.getLastName());
        emailLabel.setText(user.getEmail());
        phoneLabel.setText(user.getPhone());
        // loading image is a little bit different than the above
        String imageName = user.getProfilePicture();
        var url = getClass().getResource("/com/example/newdesign/images/" + imageName);

        if(url != null){
            profilePicture.setImage(new Image(url.toExternalForm()));
        }else {
            System.out.println("image not found in main page");
        }



    }
}
