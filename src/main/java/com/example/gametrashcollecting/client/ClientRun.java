package com.example.gametrashcollecting.client;

import com.example.gametrashcollecting.client.controller.LoginController;
import com.example.gametrashcollecting.client.model.Client;
import com.example.gametrashcollecting.client.util.SocketManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

//
//public class ClientRun extends Application {
//    private ClientMain clientMain;
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        SocketManager socketManager = new SocketManager("localhost", 8080);
//        clientMain = new ClientMain(socketManager);
////        System.out.println(socketManager.toString());
//
//
//
//        FXMLLoader fxmlLoader = new FXMLLoader(ClientMain.class.getResource("/com/example/gametrashcollecting/login-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
//
//        LoginController loginController = fxmlLoader.getController();
//        loginController.setClientMain(clientMain);
//        stage.setTitle("Trash Collecting Game!");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//}
//
//


public class ClientRun extends Application {
    private Client client;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("/com/example/gametrashcollecting/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        LoginController loginController = fxmlLoader.getController();
        client = new Client("172.20.10.3", 8080, stage);
        loginController.setClient(client);



        stage.setTitle("Trash Collecting Game!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}


