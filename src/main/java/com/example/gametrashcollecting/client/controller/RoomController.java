package com.example.gametrashcollecting.client.controller;

import com.example.gametrashcollecting.client.model.Client;
import com.example.gametrashcollecting.model.GameRoom;
import com.example.gametrashcollecting.model.GameRound;
import com.example.gametrashcollecting.model.User;
import com.example.gametrashcollecting.model.UserStatus;
import com.example.gametrashcollecting.server.request.Request;
import com.example.gametrashcollecting.server.request.RequestStatus;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RoomController {
    private Client client;

    @FXML
    private Label currPlayer;

    @FXML
    private ImageView imgUser1;

    @FXML
    private ImageView imgUser2;

    @FXML
    private Label mapBtn;

    @FXML
    private ImageView mapImg;

    @FXML
    private Label maxPlayer;

    @FXML
    private Label roomId;

    @FXML
    private Label roomName;

    @FXML
    private Label roomStatus;

    @FXML
    private Button startBtn;

    @FXML
    private Label usename;

    @FXML
    private Label user1;

    @FXML
    private Label user2;

    @FXML
    private AnchorPane userBox;

    @FXML
    private VBox userOnlineVbox;

    @FXML
    private Label userStatus;

    @FXML
    private Label me1;

    @FXML
    private Label me2;

    @FXML
    private ImageView outRoom;
    @FXML
    void clickOnOutRoom(MouseEvent event) throws IOException {
        List<User> data = new ArrayList<>();
        data.add(userCreateRoom);
        data.add(userJoinRoom);
//        User user = client.getUser();
        Request request = new Request(RequestStatus.OUT_ROOM, data);
        client.sendToServer(request);


    }


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
    void clickOnStartBtn(ActionEvent event) {

    }

    @FXML
    void onSelectMap(MouseEvent event) throws IOException {
        System.out.println("Selected Map");
        sendSelectMapRequest();
    }

    @FXML
    void onSelectMap1(MouseEvent event) throws IOException {
        System.out.println("Selected Map1");
        sendSelectMapRequest();
    }

    void sendSelectMapRequest() throws IOException {
        List<Object> listData = new ArrayList<>();
        System.out.println(userCreateRoom);
        System.out.println(userJoinRoom);
        System.out.println(gameRoom);


        listData.add(userCreateRoom);
        listData.add(userJoinRoom);
        listData.add(gameRoom);
        Request request = new Request(RequestStatus.CHOOSE_MAP, listData);
        client.sendToServer(request);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void updateFriendList(List<User> friendList){
        userOnlineVbox.getChildren().clear();
        for (User user : friendList) {
            Label usernameLabel = new Label(user.getUsername());
            usernameLabel.setPrefHeight(18.0);
            usernameLabel.setPrefWidth(167.0);

            // Tạo Label cho trạng thái người dùng
            Label statusLabel = new Label();
            statusLabel.setPrefHeight(18.0);
            statusLabel.setPrefWidth(136.0);
            statusLabel.setStyle("-fx-background-color: #d4d4d4; -fx-font-size: 11; -fx-padding: 0 5;");

            if (user.getStatus().equals(UserStatus.ONLINE)) {
                statusLabel.setText("Online");
                statusLabel.setTextFill(javafx.scene.paint.Color.web("#208c12")); // Màu xanh cho online
            } else if (user.getStatus().equals(UserStatus.OFFLINE)) {
                statusLabel.setText("Offline (" + user.getLastLogin() + " phút trước)");
                statusLabel.setTextFill(javafx.scene.paint.Color.web("#9e9e9e")); // Màu xám cho offline
            } else if (user.getStatus().equals(UserStatus.PLAYING)) {
                statusLabel.setText("Playing");
                statusLabel.setTextFill(javafx.scene.paint.Color.web("#862727")); // Màu đỏ cho playing
            }
            userOnlineVbox.getChildren().addAll(usernameLabel, statusLabel);
        }
    }

    public void updateGameRoom(GameRoom gameRoom){
        roomId.setText("Room Id: " + gameRoom.getId());
        roomName.setText("Room Name: " + gameRoom.getRoomName());
        maxPlayer.setText("Max Player: " + gameRoom.getMaxPlayer());
        currPlayer.setText("Current Player: " + gameRoom.getCurrentPlayer());
        roomStatus.setText("Room Status: " + gameRoom.getStatus());
    }

    // updateUI user 2 when client2 join room
    public void updateUI(List<User> friendList, GameRoom gameRoom, User user, User otherUser){
        updateFriendList(friendList);
        updateGameRoom(gameRoom);
        user1.setText(user.getUsername());
        user2.setText(otherUser.getUsername());
        me1.setVisible(true);
        me2.setVisible(false);
    }

    public void updateUI(List<User> friendList, GameRoom gameRoom, User user){
        updateFriendList(friendList);
        updateGameRoom(gameRoom);
        user1.setText(user.getUsername());
        user2.setVisible(false);
        imgUser2.setVisible(false);
        me2.setVisible(false);

        Image newImage1 = new Image(getClass().getResourceAsStream(client.getUser().getImg()));
        imgUser1.setImage(newImage1);
        Image newImage2 = new Image(getClass().getResourceAsStream(user.getImg()));
        imgUser2.setImage(newImage2);

    }

    // updateUI user1 when user2 join

    public void updateUIJoin(List<User> friendList, GameRoom gameRoom){
        updateFriendList(friendList);
        updateGameRoom(gameRoom);
        user2.setVisible(true);
        imgUser2.setVisible(true);
        me2.setVisible(true);
        me1.setVisible(false);
    }
    public void updateUIUser1(User user){
        user1.setText(user.getUsername());
    }
    public void updateUIUser2(User user){
        user2.setText(user.getUsername());
    }
}
