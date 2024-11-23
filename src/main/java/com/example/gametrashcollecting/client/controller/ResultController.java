package com.example.gametrashcollecting.client.controller;

import com.example.gametrashcollecting.client.model.Client;
import com.example.gametrashcollecting.model.*;
import com.example.gametrashcollecting.server.request.Request;
import com.example.gametrashcollecting.server.request.RequestStatus;
import com.example.gametrashcollecting.server.utils.ClientManager;
import com.mysql.cj.Session;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultController {

    @FXML
    private ImageView cupUser1;

    @FXML
    private ImageView cupUser2;

    @FXML
    private ImageView equalmg;

    @FXML
    private ImageView mapImg;

    @FXML
    private Label mapLevelLbl;

    @FXML
    private Label mapNameLbl;

    @FXML
    private Label mapTimeLbl;

    @FXML
    private TextField maxScoreSessionUser1Lbl;

    @FXML
    private TextField maxScoreSessionUser2Lbl;

    @FXML
    private Label pointSessionHeader;

    @FXML
    private Label resultSessionHeader;

    @FXML
    private TextField scoreSessionUser1Lbl;

    @FXML
    private TextField scoreSessionUser2Lbl;

    @FXML
    private TextField timeSessionLbl;

    @FXML
    private Label totalPointHeader;

    @FXML
    private TextField totalPointUser1Lbl;

    @FXML
    private TextField totalPointUser2Lbl;

    @FXML
    private Label minuteContinueLbl;
//
//    @FXML
//    private Button backBtn;

    @FXML
    private Label userCreateNameLbl;

    @FXML
    private Label userJoinNameLbl;


    private Client client;
    private GameSession session;
    private GameSessionPlayer gameSessionPlayerCreate;
    private GameSessionPlayer gameSessionPlayerJoin;
    private int minuteContinue;

    private User thisUser;

    public void setThisUser(User thisUser) {
        this.thisUser = thisUser;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setMinuteContinue(int minuteContinue) {
        this.minuteContinue = minuteContinue;
    }

    @FXML
    void clickOnToBackBtn(ActionEvent event) throws IOException {
        Request request = new Request(RequestStatus.BACK_MAIN_SCREEN, thisUser);
        client.sendToServer(request);
    }

    public void setSession(GameSession session) {
        this.session = session;
    }
    public void setGameSessionPlayer(GameSessionPlayer gameSessionPlayer) {
        this.gameSessionPlayerCreate = gameSessionPlayer;
    }
    public void setGameSessionPlayerJoin(GameSessionPlayer gameSessionPlayerJoin) {
        this.gameSessionPlayerJoin = gameSessionPlayerJoin;
    }

    private void loadShowScorePage() throws IOException {
        System.out.println("Thời gian đã hết. Chuyển sang trang mới.");
        LocalDateTime currentTime = LocalDateTime.now();
        GameRoom gameRoom = gameSessionPlayerCreate.getSession().getRoom();
        if(client.getUser().getUsername().equals(gameSessionPlayerCreate.getUser().getUsername())) {
            List<Object> data = new ArrayList<>();
            data.add(gameSessionPlayerCreate.getUser());
            data.add(gameRoom);
            Request request = new Request(RequestStatus.CREATEROOM, data);
            System.out.println("REQUEST TO SERVER BACK_ROOM: " + request);
            client.sendToServer(request);
        }
        else {
            Map<String, Integer> data = new HashMap<>();
            data.put("roomId", gameRoom.getId());
            data.put("userId", gameSessionPlayerJoin.getUser().getId());

            Request request = new Request(RequestStatus.JOIN_ROOM, data);
            client.sendToServer(request);
        }
    }

    private Timeline countdown;

    public void initialize() {
        minuteContinueLbl.setText("Waiting " + minuteContinue + " s to continue...");

        countdown = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    minuteContinue--;
                    minuteContinueLbl.setText("Waiting " + minuteContinue + " s to continue...");

                    if (minuteContinue <= 0) {
                        System.out.println("Đã hết thời gian.");
                        countdown.stop();

                        try {
                            loadShowScorePage();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
        );

        countdown.setCycleCount(Timeline.INDEFINITE);
        countdown.play();
    }



    public void initial(){
        maxScoreSessionUser1Lbl.setEditable(false);
        maxScoreSessionUser2Lbl.setEditable(false);
        scoreSessionUser1Lbl.setEditable(false);
        scoreSessionUser2Lbl.setEditable(false);
        totalPointUser1Lbl.setEditable(false);
        totalPointUser2Lbl.setEditable(false);
        timeSessionLbl.setEditable(false);
    }

    public void showScore(GameSession session, GameSessionPlayer gameSessionPlayerCreate, GameSessionPlayer gameSessionPlayerJoin, int scoreMaxRoundUserCreate, int scoreMaxRoundUserJoin){
        userCreateNameLbl.setText("User: " + gameSessionPlayerCreate.getUser().getUsername());
        userJoinNameLbl.setText("User: " + gameSessionPlayerJoin.getUser().getUsername());
        maxScoreSessionUser1Lbl.setText(String.valueOf(Math.max(scoreMaxRoundUserCreate, gameSessionPlayerCreate.getScore())));
        maxScoreSessionUser2Lbl.setText(String.valueOf(Math.max(scoreMaxRoundUserJoin, gameSessionPlayerJoin.getScore())));
        scoreSessionUser1Lbl.setText(String.valueOf(gameSessionPlayerCreate.getScore()));
        scoreSessionUser2Lbl.setText(String.valueOf(gameSessionPlayerJoin.getScore()));
        totalPointUser1Lbl.setText(String.valueOf(gameSessionPlayerCreate.getUser().getTotalPoints()));
        totalPointUser2Lbl.setText(String.valueOf(gameSessionPlayerJoin.getUser().getTotalPoints()));
        mapNameLbl.setText(session.getRound().getRoundName());
        mapTimeLbl.setText("Time: " + String.valueOf(session.getRound().getTimeLimit()));
        Image newImage = new Image(getClass().getResourceAsStream(session.getRound().getImg()));
        ImageView img = new ImageView(newImage);
        img.setFitWidth(164);
        img.setFitHeight(261);
        mapImg.setImage(newImage);
        mapImg.setFitWidth(164);
        mapImg.setFitHeight(261);



        mapLevelLbl.setText(String.valueOf(session.getRound().getDifficulLevel()));
        timeSessionLbl.setText(session.getStartTime() + " - " + session.getEndTime());
        System.out.println("TRUE1");
        System.out.println(gameSessionPlayerCreate.getScore() + " " + gameSessionPlayerJoin.getScore());
        if(gameSessionPlayerCreate.getScore() > gameSessionPlayerJoin.getScore()){
            System.out.println("TRUE2");
            cupUser1.setVisible(true);
            cupUser2.setVisible(false);
            equalmg.setVisible(false);
        }
        else if(gameSessionPlayerCreate.getScore() < gameSessionPlayerJoin.getScore()){
            System.out.println("TRUE3");
            cupUser1.setVisible(false);
            cupUser2.setVisible(true);
            equalmg.setVisible(false);
        }
        else {
            System.out.println("TRUE4");
            cupUser1.setVisible(false);
            cupUser2.setVisible(false);
            equalmg.setVisible(true);
        }

    }
}
