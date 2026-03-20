package com.example.newdesign;

import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

import javafx.scene.control.Alert;
public class signUpController
{
    @FXML
    private Button backToLogin;
    @FXML
    private TextField fNameField;

    @FXML
    private TextField lNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private PasswordField passwordField;

    //show alert function
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleRegister() {

        String fName = fNameField.getText();
        String lName = lNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();

        //  validation here
        if (fName.isEmpty() || lName.isEmpty()) {
            showAlert("Error", "Name fields cannot be empty");
            return;
        }

        if (!email.contains("@")) {
            showAlert("Error", "Invalid email");
            return;
        }

        if (!phone.matches("\\d{9,12}")) {
            showAlert("Error", "Invalid phone number");
            return;
        }

        if (password.length() < 6) {
            showAlert("Error", "Password must be at least 6 characters");
            return;
        }

        // safe to create user now
        User user = new User(fName, lName, email, phone, password);

        UserDAO dao = new UserDAOImpl();
        dao.addUser(user);

        showAlert("Success", "User registered!");
    }


    @FXML
    protected void backtoLoginOnClick() throws IOException{
        Stage stage = (Stage) backToLogin.getScene().getWindow();
        FXMLLoader fxmlloader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlloader.load(), 800,500);
        stage.setScene(scene);

    }


}
