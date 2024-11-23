package com.example.gametrashcollecting.client.controller;

import com.example.gametrashcollecting.client.model.Client;
import com.example.gametrashcollecting.server.request.Request;
import com.example.gametrashcollecting.server.request.RequestStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterController {

    private Client client;

    @FXML
    private Label errorConfirmed;

    @FXML
    private Button goToLogin;

    @FXML
    private Label lbUsername;

    @FXML
    private AnchorPane loginRegistration;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField passwordConfirm;

    @FXML
    private TextField username;

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    void clickOnRegisterBtn(ActionEvent event) throws IOException, ClassNotFoundException {
        System.out.println("Click on Register BTN");
        register(client);
    }

    @FXML
    void goToLogin(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/login-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.out.println("Register Btn clicked");
            e.printStackTrace();
        }
    }


    public void register(Client client) throws IOException, ClassNotFoundException {
        RequestStatus requestStatus = RequestStatus.REGISTER;
        String username = this.username.getText();
        String password = this.password.getText();
        String passwordConfirm = this.passwordConfirm.getText();
        if(passwordConfirm.equals(password)) {
            System.out.println("Passwords match");
        }
        else{
            errorConfirmed.setText("Passwords do not match");
            errorConfirmed.setStyle("-fx-text-fill: red;");
        }

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", username);
        userInfo.put("password", password);
        Request request = new Request(requestStatus, userInfo);
        client.getOos().writeObject(request);
        client.getOos().flush();

    }
}
