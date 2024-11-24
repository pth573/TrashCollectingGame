package com.example.gametrashcollecting.client.controller;

import com.example.gametrashcollecting.client.model.Client;
import com.example.gametrashcollecting.model.*;
import com.example.gametrashcollecting.server.request.Request;
import com.example.gametrashcollecting.server.request.RequestStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapController {

    private Client client;

    @FXML
    private Label chooseLbl;

    @FXML
    private Button chooseRoundBtn;

    @FXML
    private Button coutinueBtn;

    @FXML
    private Button createRoom111;

    @FXML
    private Button createRoom12;

    @FXML
    private Button createRoom13;

    @FXML
    private Button easyBtn;

    @FXML
    private Button hardBtn;

    @FXML
    private Label mapRound;

    @FXML
    private Button mediumBtn;

    @FXML
    private Label timeRound;

    @FXML
    private Label userNameClient111;

    @FXML
    private Label userNameClient12;

    @FXML
    private Label userNameClient13;

    private GameRound gameRound;
    private User userCreateRoom;
    private User userJoinRoom;
    private GameRoom gameRoom;
    private Level level = Level.EASY;



    @FXML
    void clickOnEasyBtn(ActionEvent event) {
        System.out.println("Click1");
        level = Level.EASY;
    }

    @FXML
    void clickOnHardBtn(ActionEvent event) {
        System.out.println("Click2");
        level = Level.HARD;
    }

    @FXML
    void clickOnMediumBtn(ActionEvent event) {
        System.out.println("Click3");
        level = Level.MEDIUM;
    }

    @FXML
    void clickOnToChooseBtn(ActionEvent event) throws IOException {
    }

    @FXML
    void clickOnToContinueBtn(ActionEvent event) throws IOException {
        System.out.println("Level" + level);
        gameRound.setDifficulLevel(level);
        System.out.println("Continue BTN");
        System.out.println(gameRound);

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        List<Object> list = new ArrayList<>();
        GameSession gameSession = new GameSession();
        gameSession.setRoom(gameRoom);
        gameSession.setRound(gameRound);
        gameSession.setStartTime(formattedTime);
        list.add(gameSession);
        list.add(userCreateRoom);
        list.add(userJoinRoom);

        Request request = new Request(RequestStatus.START_GAME, list);
        client.sendToServer(request);
    }

    @FXML
    void clickOnToCreateRoomBtn(ActionEvent event) {

    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setUserCreateRoom(User userCreateRoom) {
        this.userCreateRoom = userCreateRoom;
    }

    public void setUserJoinRoom(User userJoinRoom) {
        this.userJoinRoom = userJoinRoom;
    }

    public void setGameRoom(GameRoom gameRoom) {
        this.gameRoom = gameRoom;
    }

    public void setGameRound(GameRound gameRound) {
        this.gameRound = gameRound;
    }

    @FXML
    private HBox hboxMaps;


    @FXML
    private void clickOnStartBtn1(ActionEvent event) {
    }

    public void loadRounds(List<GameRound> rounds) {
        hboxMaps.getChildren().clear();

        for (GameRound round : rounds) {
            VBox vbox = createRoundBox(round);
            hboxMaps.getChildren().add(vbox);
        }
    }

    private VBox createRoundBox(GameRound round) {
        VBox vbox = new VBox();

        Label nameLabel = new Label(round.getRoundName());
        nameLabel.setStyle("-fx-font-weight: 100px;");
        nameLabel.setFont(new javafx.scene.text.Font("Elephant", 20));
        nameLabel.setAlignment(javafx.geometry.Pos.CENTER);

        Label timeLabel = new Label("Time: " + round.getTimeLimit() + "s");
        timeLabel.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(164);
        imageView.setFitWidth(164);

        Image newImage1 = new Image(getClass().getResourceAsStream(round.getImg()));
        imageView.setImage(newImage1);

        Button chooseButton = new Button("Choose");
        chooseButton.setPrefHeight(30);
        chooseButton.setPrefWidth(215);
        chooseButton.setStyle("-fx-background-color: #2b2b2b; -fx-font-size: 14;");
        chooseButton.setTextFill(javafx.scene.paint.Color.WHITE);

        chooseButton.setOnAction(e -> chooseRound(round));

        vbox.getChildren().addAll(nameLabel, timeLabel, imageView, chooseButton);
        vbox.setSpacing(10);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);

        return vbox;
    }

    private void chooseRound(GameRound round) {
        System.out.println("Selected round: " + round.getRoundName());
        this.gameRound = round;
        System.out.println(gameRound);
    }
}
