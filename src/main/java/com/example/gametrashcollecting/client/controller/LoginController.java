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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    private Client client;

    @FXML
    private Label lblUsername;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginBtn;

    @FXML
    private Hyperlink linkRegister;


    @FXML
    private AnchorPane loginRegistration;

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    public void clickOnLoginBtn(ActionEvent event) throws IOException, ClassNotFoundException {
        System.out.println("Login Btn clicked");
        login(this.client);
    }


    @FXML
    public void goToRegisterForm(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/register-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            RegisterController registerController = fxmlLoader.getController();
            registerController.setClient(client);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void switchMainScreen(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/main-screen.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login(Client client) throws IOException, ClassNotFoundException {
        RequestStatus requestStatus = RequestStatus.LOGIN;
        String username = this.username.getText();
        String password = this.password.getText();
        System.out.println(username + " " + password);
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", username);
        userInfo.put("password", password);
        Request request = new Request(requestStatus, userInfo);
        System.out.println(request);
        if (client.getOos() == null) {
            System.out.println("ObjectOutputStream is null.");
        } else {
            System.out.println("ObjectOutputStream is not null.");
        }
        if(client.getSocket().isClosed()) {
            System.out.println("Socket is closed.");
        }
        client.sendToServer(request);
    }
}
