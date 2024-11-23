package com.example.gametrashcollecting.client.controller;

import com.example.gametrashcollecting.client.model.Client;
import com.example.gametrashcollecting.model.GameRoom;
import com.example.gametrashcollecting.model.User;
import com.example.gametrashcollecting.server.request.Request;
import com.example.gametrashcollecting.server.request.RequestStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomListViewController {
    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    private HBox roomListVbox;


    @FXML
    private TextField idRoom;

    @FXML
    private Button searchBtn;

    @FXML
    private Label user1;


    @FXML
    private Button backBtn;

    private User userCreateRoom;
    private User userJoinRoom;
    private GameRoom gameRoom;
    private User thisUser;

    public void setThisUser(User thisUser) {
        this.thisUser = thisUser;
    }

    public void setUserCreateRoom(User user) {
        this.userCreateRoom = user;
    }

    public void setUserJoinRoom(User user) {
        this.userJoinRoom = user;
    }

    public void setGameRoom(GameRoom gameRoom) {
        this.gameRoom = gameRoom;
    }


    @FXML
    void clickOnToBackBtn(ActionEvent event) throws IOException {
        Request request = new Request(RequestStatus.BACK_MAIN_SCREEN, thisUser);
        client.sendToServer(request);
    }

    @FXML
    void clickOnSearchBtn(ActionEvent event) throws IOException {
        Request request = new Request(RequestStatus.FIND_ROOM, idRoom.getText());
        client.sendToServer(request);
    }

    @FXML
    private void clickOnStartBtn1(ActionEvent event) {
    }
    public void loadRoom(GameRoom room) {
        roomListVbox.getChildren().clear();
        roomListVbox.setAlignment(Pos.TOP_LEFT);

        VBox roomBox = new VBox();

        roomListVbox.setSpacing(100);
        roomBox.setPrefWidth(700.0);

        Label roomIdLabel = new Label("Room Id: " + room.getId());
        roomIdLabel.setPrefHeight(18.0);
        roomIdLabel.setStyle("-fx-font-size: 13;");
        roomIdLabel.setMinWidth(180); // Đặt giới hạn chiều rộng

        Label roomNameLabel = new Label("Room Name: " + room.getRoomName());
        roomNameLabel.setPrefHeight(18.0);
        roomNameLabel.setStyle("-fx-font-size: 13;");
        roomNameLabel.setMinWidth(180); // Đặt giới hạn chiều rộng

        Label maxPlayerLabel = new Label("Max Player: " + room.getMaxPlayer());
        maxPlayerLabel.setPrefHeight(18.0);
        maxPlayerLabel.setStyle("-fx-font-size: 13;");
        maxPlayerLabel.setMinWidth(180); // Đặt giới hạn chiều rộng

        Label currentPlayerLabel = new Label("Current Player: " + room.getCurrentPlayer());
        currentPlayerLabel.setPrefHeight(18.0);
        currentPlayerLabel.setStyle("-fx-font-size: 13;");
        currentPlayerLabel.setMinWidth(180); // Đặt giới hạn chiều rộng

        Label statusLabel = new Label("Room Status: " + room.getStatus());
        statusLabel.setPrefHeight(18.0);
        statusLabel.setStyle("-fx-font-size: 13;");
        statusLabel.setMinWidth(180);


        Button joinButton = new Button("Join");
        joinButton.setPrefHeight(31.0);
        joinButton.setPrefWidth(72.0);
        joinButton.setStyle("-fx-background-color: #2b2b2b; -fx-font-size: 14;");
        joinButton.setTextFill(javafx.scene.paint.Color.WHITE);
        joinButton.setOnAction(event -> {
            try {
                joinRoom(room);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        roomBox.getChildren().addAll(roomIdLabel, roomNameLabel, maxPlayerLabel, currentPlayerLabel, statusLabel, joinButton);

        roomBox.setSpacing(5);

        roomListVbox.getChildren().add(roomBox);
    }




    public void loadRoomList(List<GameRoom> rooms) {
        System.out.println("Room list:" + rooms.size());
        roomListVbox.getChildren().clear();
        roomListVbox.setAlignment(Pos.TOP_LEFT);

        for (GameRoom room : rooms) {
            VBox roomBox = new VBox();

            roomListVbox.setSpacing(100);
            roomBox.setPrefWidth(700.0);

            Label roomIdLabel = new Label("Room Id: " + room.getId());
            roomIdLabel.setPrefHeight(18.0);
            roomIdLabel.setStyle("-fx-font-size: 13;");
            roomIdLabel.setMinWidth(180);

            Label roomNameLabel = new Label("Room Name: " + room.getRoomName());
            roomNameLabel.setPrefHeight(18.0);
            roomNameLabel.setStyle("-fx-font-size: 13;");
            roomNameLabel.setMinWidth(180);

            Label maxPlayerLabel = new Label("Max Player: " + room.getMaxPlayer());
            maxPlayerLabel.setPrefHeight(18.0);
            maxPlayerLabel.setStyle("-fx-font-size: 13;");
            maxPlayerLabel.setMinWidth(180);

            Label currentPlayerLabel = new Label("Current Player: " + room.getCurrentPlayer());
            currentPlayerLabel.setPrefHeight(18.0);
            currentPlayerLabel.setStyle("-fx-font-size: 13;");
            currentPlayerLabel.setMinWidth(180);

            Label statusLabel = new Label("Room Status: " + room.getStatus());
            statusLabel.setPrefHeight(18.0);
            statusLabel.setStyle("-fx-font-size: 13;");
            statusLabel.setMinWidth(180);


            Button joinButton = new Button("Join");
            joinButton.setPrefHeight(31.0);
            joinButton.setPrefWidth(72.0);
            joinButton.setStyle("-fx-background-color: #2b2b2b; -fx-font-size: 14;");
            joinButton.setTextFill(javafx.scene.paint.Color.WHITE);
            joinButton.setOnAction(event -> {
                try {
                    joinRoom(room);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            roomBox.getChildren().addAll(roomIdLabel, roomNameLabel, maxPlayerLabel, currentPlayerLabel, statusLabel, joinButton);

            roomBox.setSpacing(5);

            roomListVbox.getChildren().add(roomBox);
        }
    }


    private void joinRoom(GameRoom room) throws IOException {
        System.out.println("Joining room: " + room.getRoomName());
        Map<String, Integer> data = new HashMap<>();
        data.put("roomId", room.getId());
        data.put("userId", client.getUser().getId());
        Request request = new Request(RequestStatus.JOIN_ROOM, data);
        client.sendToServer(request);
    }
}