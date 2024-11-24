package com.example.gametrashcollecting.client.controller;
import com.example.gametrashcollecting.client.model.Client;
import com.example.gametrashcollecting.model.User;
import com.example.gametrashcollecting.model.UserStatus;
import com.example.gametrashcollecting.server.request.Request;
import com.example.gametrashcollecting.server.request.RequestStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainScreenController {
    private Client client;
//    private List<User> friendList;

    @FXML
    private Button createRoom;

    @FXML
    private Button findRoom;

    @FXML
    private ImageView friend;

    @FXML
    private ImageView history;

    @FXML
    private ImageView inviteFriend;

    @FXML
    private ImageView rank;

    @FXML
    private Label usename;

    @FXML
    private AnchorPane userBox;

    @FXML
    private ImageView userInfo;

    @FXML
    private VBox userOnlineVbox;

    @FXML
    private Label userStatus;

    @FXML
    private Label userTotalRank;

    @FXML
    private Label top1;

    @FXML
    private Label top2;

    @FXML
    private Label top3;

    @FXML
    private ImageView userImg;

//    @FXML
//    private AnchorPane userNameClient;


    @FXML
    private Label userNameClient;

//    @FXML
//    private Button findRoom;

    @FXML
    private AnchorPane findRoomDialog; // Dialog sẽ ẩn/hiện

    @FXML
    private TextField roomIdField; // TextField để nhập ID phòng

    private User thisUser;

    public void setThisUser(User thisUser) {
        this.thisUser = thisUser;
    }



    // Sự kiện khi nhấn nút "Tìm phòng" trên giao diện chính
//    @FXML
//    private void clickOnFindRoomBtn() {
////        findRoomDialog.setVisible(true); // Hiển thị dialog
//
//        Platform.runLater(() -> {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/room-lisist-view.fxml"));
//            Parent root = null;
//            try {
//                root = loader.load();
//                MainScreenController mainScreenController = loader.getController();
//                mainScreenController.setClient(client);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            Scene gameScene = new Scene(root);
//            stage.setScene(gameScene);
//            stage.show();
//        });
//    }
    public void setClient(Client client) {
        this.client = client;
    }


    @FXML
    private void clickOnStartBtn(ActionEvent event) {
        // Code xử lý khi nút Start được nhấn
    }

    @FXML
    public void showHistorySession(MouseEvent event) throws IOException {
        Request request = new Request(RequestStatus.GET_LIST_HISTORY_SESSION, thisUser);
        client.sendToServer(request);
    }


    @FXML
    public void showUserList(MouseEvent event) throws IOException {
        Request request = new Request(RequestStatus.SHOW_USER_LIST, thisUser);
        client.sendToServer(request);
    }


    @FXML
    private void clickOnFindRoomBtn(ActionEvent event) throws IOException {
//        List<User> data = new ArrayList<>();
        User user = client.getUser();
//        data.add(userCreateRoom);
//        data.add(userJoinRoom);

        Request request = new Request(RequestStatus.FIND_ALL_ROOM, user);
        client.sendToServer(request);

    }


    @FXML
    private void closeDialog() {
        findRoomDialog.setVisible(false);
    }

    @FXML
    private void findRoom() {
        System.out.println("Find Room");
        String roomId = roomIdField.getText();

        if (roomId != null && !roomId.isEmpty()) {
            System.out.println("Đang tìm phòng với ID: " + roomId);
            findRoomDialog.setVisible(false);
        } else {
            System.out.println("Vui lòng nhập ID phòng.");
        }
    }


//    @FXML
//    public void clickOnFindRoomBtn() {
//    }
//

    @FXML
    void clickOnToCreateRoomBtn(ActionEvent event) throws IOException {
        System.out.println("CreateRoom Btn clicked");
        createRoom(this.client);
    }

    public void createRoom(Client client) throws IOException {
        RequestStatus requestStatus = RequestStatus.CREATEROOM;
        List<Object> data = new ArrayList<>();
        data.add(client.getUser());
//        Request requestCreateRoom = new Request(requestStatus, client.getUser());
        Request requestCreateRoom = new Request(requestStatus, data);
        client.sendToServer(requestCreateRoom);
    }

//    public void setFriendList(List<User> friendList){
//        this.friendList = friendList;
//    }

    public void getUser(){

    }
    public void updateUI(List<User> friendList, List<User> top3UserPoint){
        updateRank(top3UserPoint);
        updateFriendList(friendList);
        userNameClient.setText("Hi " + client.getUser().getUsername() + "!");
        Image img = new Image(getClass().getResourceAsStream(client.getUser().getImg()));
        userImg.setImage(img);
    }

    private void updateRank(List<User> top3UserPoint){
        top1.setText(String.valueOf(top3UserPoint.get(0).getUsername()));
        top2.setText(String.valueOf(top3UserPoint.get(1).getUsername()));
        top3.setText(String.valueOf(top3UserPoint.get(2).getUsername()));
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
                statusLabel.setTextFill(javafx.scene.paint.Color.web("#208c12"));
            } else if (user.getStatus().equals(UserStatus.OFFLINE)) {
                statusLabel.setText("Offline (" + user.getLastLogin() + " phút trước)");
                statusLabel.setTextFill(javafx.scene.paint.Color.web("#9e9e9e"));
            } else if (user.getStatus().equals(UserStatus.PLAYING)) {
                statusLabel.setText("Playing");
                statusLabel.setTextFill(javafx.scene.paint.Color.web("#862727"));
            }
            userOnlineVbox.getChildren().addAll(usernameLabel, statusLabel);
        }
    }
}
