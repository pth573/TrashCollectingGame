package com.example.gametrashcollecting.client.controller;
import com.example.gametrashcollecting.client.model.Client;
import com.example.gametrashcollecting.model.*;
import com.example.gametrashcollecting.server.request.Request;
import com.example.gametrashcollecting.server.request.RequestStatus;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartGameController {

    @FXML
    private ImageView background;

    @FXML
    private ImageView imgUser1;

    @FXML
    private ImageView imgUser2;

    @FXML
    private Label me1;

    @FXML
    private Label me2;

    @FXML
    private Label metal1;

    @FXML
    private Label metal2;

    @FXML
    private Label organic1;

    @FXML
    private Label organic2;

    @FXML
    private Label paper1;

    @FXML
    private Label paper2;

    @FXML
    private Label plastic1;

    @FXML
    private Label plastic2;

    @FXML
    private Label pointUser1;

    @FXML
    private Label pointUser2;

    @FXML
    private Line redLine;

    @FXML
    private Label roundId;

    @FXML
    private Label roundLevel;

    @FXML
    private Button startBtn;

    @FXML
    private Label timeRemain;

    @FXML
    private Label user1;

    @FXML
    private Label user2;

    @FXML
    private Pane gamePane;

    @FXML
    void clickOnStartBtn(ActionEvent event) {

    }

    private Client client;
    private User userCreateRoom;
    private User userJoinRoom;
    private GameSession session;
    private GameSessionPlayer gameSessionPlayerCreate;
    private GameSessionPlayer gameSessionPlayerJoin;
    private List<TrashItem> trashItemList = new ArrayList<>();
    private TrashItem currentFallingTrash;
    private int scoreUser1 = 0;
    private int scoreUser2 = 0;
    private int remainingTime;
    private Scene gameScene;
    private ImageView trashView;
    private int order = 0;
    private Label trashOrderLabel = new Label();
    private Object dataToClient2;
    private User thisUser;
    private Map<String, Label> labelMap = new HashMap<>();

    public void setDataToClient2(Object dataToClient2) {
        this.dataToClient2 = dataToClient2;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setCurrentFallingTrash(TrashItem trashItem){
        this.currentFallingTrash = trashItem;
    }

    public void setThisUser(User thisUser) {
        this.thisUser = thisUser;
    }
    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
        this.gameScene.setOnKeyPressed(event -> {
            try {
                handleKeyPress(event.getCode());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setOrder(int order){
        this.order = order;
    }

    public void setUserCreateRoom(User user) {
        this.userCreateRoom = user;
    }
    public void setUserJoinRoom(User user) {
        this.userJoinRoom = user;
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
    public void setTrashItemList(List<TrashItem> trashItemList) {
        this.trashItemList = trashItemList;
    }

    public void setScoreUser1(int scoreUser1){
        this.scoreUser1 = scoreUser1;
    }
    public void setScoreUser2(int scoreUser2){
        this.scoreUser2 = scoreUser2;
    }



    public void updateUserUI(){
        System.out.println(userCreateRoom.getUsername());
        System.out.println(client.getUser().getUsername());
        if(userCreateRoom.getUsername().equals(client.getUser().getUsername())){
            me1.setVisible(true);
            me2.setVisible(false);
        }
        else {
            me1.setVisible(false);
            me2.setVisible(true);
        }
        user1.setText(userCreateRoom.getUsername());
        user2.setText(userJoinRoom.getUsername());
        Image newImage1 = new Image(getClass().getResourceAsStream(userCreateRoom.getImg()));
        imgUser1.setImage(newImage1);
        Image newImage2 = new Image(getClass().getResourceAsStream(userJoinRoom.getImg()));
        imgUser2.setImage(newImage2);
    }

    public void updateRoomUI(){
        GameRound gameRound = session.getRound();
        roundId.setText("Round: " + gameRound.getId());
        roundLevel.setText("Level: " + gameRound.getDifficulLevel().toString());
        timeRemain.setText("Time remain: " + gameRound.getTimeLimit());
    }

    public void updatePoint() throws IOException {
        pointUser2.setText(String.valueOf(scoreUser2));
        pointUser1.setText(String.valueOf(scoreUser1));
        List<Object> data = new ArrayList<>();


        GameSession gameSession;
        User userCreateRoom = null;
        User userJoinRoom = null;
        User thisUser = null;
        GameSessionPlayer gameSessionPlayerCreate;
        GameSessionPlayer gameSessionPlayerJoin;
        List<TrashItem> trashItemList = new ArrayList<>();

        if(dataToClient2 instanceof List<?>){
            List<Object> outerList = (List<Object>) dataToClient2;
            Object firstElement = outerList.get(0);
            if (firstElement instanceof GameSession) {
                gameSession = (GameSession) firstElement;
            } else {
                gameSession = new GameSession();
            }
            Object secondElement = outerList.get(1);
            if (secondElement instanceof User) {
                userCreateRoom = (User) secondElement;
            } else {
                userCreateRoom = new User();
            }
            Object thirdElement = outerList.get(2);
            if (thirdElement instanceof User) {
                userJoinRoom = (User) thirdElement;
            } else {
                userJoinRoom = new User();
            }
            Object forthElement = outerList.get(3);
            if (forthElement instanceof GameSessionPlayer) {
                gameSessionPlayerCreate = (GameSessionPlayer) forthElement;
            } else {
                gameSessionPlayerCreate = new GameSessionPlayer();
            }
            Object fifthElement = outerList.get(4);
            if (fifthElement instanceof GameSessionPlayer) {
                gameSessionPlayerJoin = (GameSessionPlayer) fifthElement;
            } else {
                gameSessionPlayerJoin = new GameSessionPlayer();
            }
            Object sixthElement = outerList.get(5);
            if (sixthElement instanceof List<?>) {
                List<?> innerList = (List<?>) sixthElement;
                System.out.println("InnerList:" + innerList.size());
                for (Object trashItem : innerList) {
                    if (trashItem instanceof TrashItem) {
                        trashItemList.add((TrashItem) trashItem);
                    }
                }
            }
            Object seventh = outerList.get(6);
            if (seventh instanceof User) {
                thisUser = (User) seventh;
            } else {
                thisUser = new User();
            }

            if(userCreateRoom.getUsername().equals(client.getUser().getUsername())) {

                data.add(userCreateRoom);
                data.add(userJoinRoom);
                data.add(thisUser);
                data.add(Integer.valueOf(scoreUser1));

                gameSessionPlayerCreate.setScore(scoreUser1);
                pointUser1.setText(String.valueOf(scoreUser1));

                System.out.println("SEND REQUEST UPDATE_SCORE_UI");
                System.out.println("SC1: " + scoreUser1);
                Request request = new Request(RequestStatus.UPDATE_SCORE_UI_1, data);
                System.out.println("REQUEST TO SERVER UPDATE_SCORE_UI: " + request);
                client.sendToServer(request);

            }
            else {
                data.add(userCreateRoom);
                data.add(userJoinRoom);
                data.add(thisUser);
                data.add(Integer.valueOf(scoreUser2));

                gameSessionPlayerJoin.setScore(scoreUser2);
                pointUser2.setText(String.valueOf(scoreUser2));
                System.out.println("SC2: " + scoreUser2);
                System.out.println("SEND REQUEST UPDATE_SCORE_UI");
                Request request = new Request(RequestStatus.UPDATE_SCORE_UI_1, data);
                System.out.println("REQUEST TO SERVER UPDATE_SCORE_UI: " + request);
                client.sendToServer(request);
            }
        }
    }

    public void updatePointUser1(int scoreUser1){
        pointUser1.setText(String.valueOf(scoreUser1));
    }

    public void updatePointUser2(int scoreUser2){
        pointUser2.setText(String.valueOf(scoreUser2));
    }

    public void updateUIStartGame() throws IOException {
        if(session.getRound().getDifficulLevel().equals(Level.EASY)){
            remainingTime = session.getRound().getTimeLimit();
        }
        else if(session.getRound().getDifficulLevel().equals(Level.HARD)){
            remainingTime = session.getRound().getTimeLimit() - 10;
        }
        else {
            remainingTime = session.getRound().getTimeLimit() - 5;
        }
        updateRoomUI();
        updatePoint();
        updateUserUI();
        spawnNextTrash();
    }


    private void resetTextStylesPlayer1() {
        organic1.setStyle("-fx-text-fill: black;");
        plastic1.setStyle("-fx-text-fill: black;");
        metal1.setStyle("-fx-text-fill: black;");
        paper1.setStyle("-fx-text-fill: black;");
    }

    private void resetTextStylesPlayer2() {
        organic2.setStyle("-fx-text-fill: black;");
        plastic2.setStyle("-fx-text-fill: black;");
        metal2.setStyle("-fx-text-fill: black;");
        paper2.setStyle("-fx-text-fill: black;");
    }



    private void setTextStyleWithTimeout(Label label, Runnable resetFunction) throws IOException {
        label.setStyle("-fx-text-fill: red;");
        String labelId = label.getId();
        updateColorUI(labelId);


        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> resetFunction.run()));
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void handleKeyPress(KeyCode code) throws IOException {
        if (currentFallingTrash != null) {
            if (userCreateRoom.getUsername().equals(client.getUser().getUsername())) {
                resetTextStylesPlayer1();

                switch (code) {
                    case DIGIT1:
                        setTextStyleWithTimeout(organic1, this::resetTextStylesPlayer1);
                        handleTrashTypeSelection(ItemType.ORGANIC);
                        break;
                    case DIGIT2:
                        setTextStyleWithTimeout(plastic1, this::resetTextStylesPlayer1);
                        handleTrashTypeSelection(ItemType.PLASTIC);
                        break;
                    case DIGIT3:
                        setTextStyleWithTimeout(metal1, this::resetTextStylesPlayer1);
                        handleTrashTypeSelection(ItemType.METAL);
                        break;
                    case DIGIT4:
                        setTextStyleWithTimeout(paper1, this::resetTextStylesPlayer1);
                        handleTrashTypeSelection(ItemType.PAPER);
                        break;
                    default:
                        break;
                }
            } else {
                resetTextStylesPlayer2();

                switch (code) {
                    case DIGIT1:
                        setTextStyleWithTimeout(organic2, this::resetTextStylesPlayer2);
                        handleTrashTypeSelection(ItemType.ORGANIC);
                        break;
                    case DIGIT2:
                        setTextStyleWithTimeout(plastic2, this::resetTextStylesPlayer2);
                        handleTrashTypeSelection(ItemType.PLASTIC);
                        break;
                    case DIGIT3:
                        setTextStyleWithTimeout(metal2, this::resetTextStylesPlayer2);
                        handleTrashTypeSelection(ItemType.METAL);
                        break;
                    case DIGIT4:
                        setTextStyleWithTimeout(paper2, this::resetTextStylesPlayer2);
                        handleTrashTypeSelection(ItemType.PAPER);
                        break;
                    default:
                        break;
                }
            }
        }
    }


    public void updateColorUI(String labelId) throws IOException {
        GameSession gameSession;
        User userCreateRoom = null;
        User userJoinRoom = null;
        User thisUser = null;
        List<Object> data = new ArrayList<>();

        List<TrashItem> trashItemList = new ArrayList<>();
        if (dataToClient2 instanceof List<?>) {
            List<Object> outerList = (List<Object>) dataToClient2;
            Object firstElement = outerList.get(0);
            if (firstElement instanceof GameSession) {
                gameSession = (GameSession) firstElement;
            } else {
                gameSession = new GameSession();
            }
            Object secondElement = outerList.get(1);
            if (secondElement instanceof User) {
                userCreateRoom = (User) secondElement;
            } else {
                userCreateRoom = new User();
            }
            Object thirdElement = outerList.get(2);
            if (thirdElement instanceof User) {
                userJoinRoom = (User) thirdElement;
            } else {
                userJoinRoom = new User();
            }
            Object forthElement = outerList.get(3);
            if (forthElement instanceof GameSessionPlayer) {
                gameSessionPlayerCreate = (GameSessionPlayer) forthElement;
            } else {
                gameSessionPlayerCreate = new GameSessionPlayer();
            }
            Object fifthElement = outerList.get(4);
            if (fifthElement instanceof GameSessionPlayer) {
                gameSessionPlayerJoin = (GameSessionPlayer) fifthElement;
            } else {
                gameSessionPlayerJoin = new GameSessionPlayer();
            }
            Object sixthElement = outerList.get(5);
            if (sixthElement instanceof List<?>) {
                List<?> innerList = (List<?>) sixthElement;
                System.out.println("InnerList:" + innerList.size());
                for (Object trashItem2 : innerList) {
                    if (trashItem2 instanceof TrashItem) {
                        trashItemList.add((TrashItem) trashItem2);
                    }
                }
            }
            Object seventh = outerList.get(6);
            if (seventh instanceof User) {
                thisUser = (User) seventh;
            } else {
                thisUser = new User();
            }

            if (userCreateRoom.getUsername().equals(client.getUser().getUsername())) {
                data.add(userCreateRoom);
                data.add(userJoinRoom);
                data.add(thisUser);
                data.add(order);
                data.add(currentFallingTrash);
                data.add(labelId);
                Request request = new Request(RequestStatus.UPDATE_COLOR_UI, data);
                System.out.println("REQUEST TO SERVER UPDATE_COLOR_UI: " + request);
                client.sendToServer(request);

            } else {
                data.add(userCreateRoom);
                data.add(userJoinRoom);
                data.add(thisUser);
                data.add(order);
                data.add(currentFallingTrash);
                data.add(labelId);
                Request request = new Request(RequestStatus.UPDATE_COLOR_UI, data);
                System.out.println("REQUEST TO SERVER UPDATE_COLOR_UI: " + request);
                client.sendToServer(request);

            }
        }
    }

    public void updateColorUIById(String labelId) {
        Label label = labelMap.get(labelId);
        if (label != null) {
            label.setStyle("-fx-text-fill: red;");
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                label.setStyle("-fx-text-fill: black;");
            }));
            timeline.setCycleCount(1);
            timeline.play();
        } else {
            System.out.println("Label với id " + labelId + " không tồn tại.");
        }
    }

    // Khi ấn nuútthifi tang diem, xu li: xoa, tao cai tiep
    private void handleTrashTypeSelection(ItemType selectedType) throws IOException {
        if(userCreateRoom.getUsername().equals(client.getUser().getUsername())){
            System.out.println("DAY LA USER 1");
        }
        else {
            System.out.println("DAY LA USER 2");
        }
        if (selectedType == currentFallingTrash.getItemType()) {
            if(userCreateRoom.getUsername().equals(client.getUser().getUsername())){
                scoreUser1++;
                pointUser1.setText(String.valueOf(scoreUser1));
                updatePoint();
            }
            else{
                scoreUser2++;
                pointUser2.setText(String.valueOf(scoreUser2));
                updatePoint();
            }
            System.out.println("Correct guess! User1 score increased.");
        } else {
            System.out.println("Wrong guess.");
        }
        if (trashView != null) {
            try {
                sendRemoveTrashRemove();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            gamePane.getChildren().remove(trashView);
            trashView = null;
        }

        currentFallingTrash = null;
        spawnNextTrash(); // Bắt đầu rác mới
    }

    public void removeTrashFromPane() {
        gamePane.getChildren().removeIf(node ->
                node instanceof ImageView &&
                        ((ImageView) node).getImage() != null &&
                        !("background".equals(node.getId()) || "redLine".equals(node.getId()))
        );
    }
    public void sendRemoveTrashRemove() throws IOException {
        GameSession gameSession;
        User userCreateRoom = null;
        User userJoinRoom = null;
        User thisUser = null;
        List<Object> data = new ArrayList<>();

        List<TrashItem> trashItemList = new ArrayList<>();
        if (dataToClient2 instanceof List<?>) {
            List<Object> outerList = (List<Object>) dataToClient2;
            Object firstElement = outerList.get(0);
            if (firstElement instanceof GameSession) {
                gameSession = (GameSession) firstElement;
            } else {
                gameSession = new GameSession();
            }
            Object secondElement = outerList.get(1);
            if (secondElement instanceof User) {
                userCreateRoom = (User) secondElement;
            } else {
                userCreateRoom = new User();
            }
            Object thirdElement = outerList.get(2);
            if (thirdElement instanceof User) {
                userJoinRoom = (User) thirdElement;
            } else {
                userJoinRoom = new User();
            }
            Object forthElement = outerList.get(3);
            if (forthElement instanceof GameSessionPlayer) {
                gameSessionPlayerCreate = (GameSessionPlayer) forthElement;
            } else {
                gameSessionPlayerCreate = new GameSessionPlayer();
            }
            Object fifthElement = outerList.get(4);
            if (fifthElement instanceof GameSessionPlayer) {
                gameSessionPlayerJoin = (GameSessionPlayer) fifthElement;
            } else {
                gameSessionPlayerJoin = new GameSessionPlayer();
            }
            Object sixthElement = outerList.get(5);
            if (sixthElement instanceof List<?>) {
                List<?> innerList = (List<?>) sixthElement;
                System.out.println("InnerList:" + innerList.size());
                for (Object trashItem2 : innerList) {
                    if (trashItem2 instanceof TrashItem) {
                        trashItemList.add((TrashItem) trashItem2);
                    }
                }
            }
            Object seventh = outerList.get(6);
            if (seventh instanceof User) {
                thisUser = (User) seventh;
            } else {
                thisUser = new User();
            }

            if (userCreateRoom.getUsername().equals(client.getUser().getUsername())) {
                data.add(userCreateRoom);
                data.add(userJoinRoom);
                data.add(thisUser);
                data.add(order);
                data.add(currentFallingTrash);
                Request request = new Request(RequestStatus.REMOVE_TRASH, data);
                System.out.println("REQUEST TO SERVER REMOVE_TRASH: " + request);
                client.sendToServer(request);

            } else {
                data.add(userCreateRoom);
                data.add(userJoinRoom);
                data.add(thisUser);
                data.add(order);
                data.add(currentFallingTrash);
                Request request = new Request(RequestStatus.REMOVE_TRASH, data);
                System.out.println("REQUEST TO SERVER REMOVE_TRASH: " + request);
                client.sendToServer(request);

            }
        }

    }


    public void updateTrashUI() throws IOException {
        GameSession gameSession;
        User userCreateRoom = null;
        User userJoinRoom = null;
        User thisUser = null;
        List<Object> data = new ArrayList<>();

        List<TrashItem> trashItemList = new ArrayList<>();
        if (dataToClient2 instanceof List<?>) {
            List<Object> outerList = (List<Object>) dataToClient2;
            Object firstElement = outerList.get(0);
            if (firstElement instanceof GameSession) {
                gameSession = (GameSession) firstElement;
            } else {
                gameSession = new GameSession();
            }
            Object secondElement = outerList.get(1);
            if (secondElement instanceof User) {
                userCreateRoom = (User) secondElement;
            } else {
                userCreateRoom = new User();
            }
            Object thirdElement = outerList.get(2);
            if (thirdElement instanceof User) {
                userJoinRoom = (User) thirdElement;
            } else {
                userJoinRoom = new User();
            }
            Object forthElement = outerList.get(3);
            if (forthElement instanceof GameSessionPlayer) {
                gameSessionPlayerCreate = (GameSessionPlayer) forthElement;
            } else {
                gameSessionPlayerCreate = new GameSessionPlayer();
            }
            Object fifthElement = outerList.get(4);
            if (fifthElement instanceof GameSessionPlayer) {
                gameSessionPlayerJoin = (GameSessionPlayer) fifthElement;
            } else {
                gameSessionPlayerJoin = new GameSessionPlayer();
            }
            Object sixthElement = outerList.get(5);
            if (sixthElement instanceof List<?>) {
                List<?> innerList = (List<?>) sixthElement;
                for (Object trashItem2 : innerList) {
                    if (trashItem2 instanceof TrashItem) {
                        trashItemList.add((TrashItem) trashItem2);
                    }
                }
            }
            Object seventh = outerList.get(6);
            if (seventh instanceof User) {
                thisUser = (User) seventh;
            } else {
                thisUser = new User();
            }
            data.add(userCreateRoom);
            data.add(userJoinRoom);
            data.add(thisUser);
            data.add(order);
            data.add(currentFallingTrash);
            Request request = new Request(RequestStatus.UPDATE_TRASH_UI, data);
            client.sendToServer(request);
        }
    }

    public void setLayOut(TrashItem trashItem) {
        trashView.setLayoutX(trashItem.getX());
        trashView.setLayoutY(trashItem.getY());
    }
    private AnimationTimer currentAnimationTimer;
    private Timeline countdown;

    public void initialize() {
        labelMap.put("organic1", organic1);
        labelMap.put("plastic1", plastic1);
        labelMap.put("metal1", metal1);
        labelMap.put("paper1", paper1);

        labelMap.put("organic2", organic2);
        labelMap.put("plastic2", plastic2);
        labelMap.put("metal2", metal2);
        labelMap.put("paper2", paper2);

        timeRemain.setText(remainingTime + "s");

        countdown = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    remainingTime--;
                    timeRemain.setText(remainingTime + "s");

                    if (remainingTime <= 0) {
                        System.out.println("Đã hết thời gian.");
                        countdown.stop();
                        stopCurrentAnimation();

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

    private void stopCurrentAnimation() {
        if (currentAnimationTimer != null) {
            currentAnimationTimer.stop();
            currentAnimationTimer = null;
        }
    }

    public void spawnNextTrash() {
        order++;
        if (!trashItemList.isEmpty() && order < trashItemList.size()) {
            currentFallingTrash = trashItemList.get(order);
            System.out.println("Trash: " + currentFallingTrash.getItemType());

            trashOrderLabel.setText("Trash #" + order);
            trashOrderLabel.setStyle("-fx-font-size: 16; -fx-text-fill: white;");

            if (!gamePane.getChildren().contains(trashOrderLabel)) {
                gamePane.getChildren().add(trashOrderLabel);
            }

            if (currentFallingTrash.getVy() == 0) {
                currentFallingTrash.setVy(0.1);
            }

            startTrashFallAnimation();
        }
    }

    private void cleanupGamePane() {
        System.out.println("Dọn dẹp giao diện game.");
        gamePane.getChildren().clear();
        currentFallingTrash = null;
    }

    // Animation rác rơi
    private void startTrashFallAnimation() {
        stopCurrentAnimation();

        System.out.println("Tạo Animation cho Trash: " + currentFallingTrash);

        trashView = new ImageView(new Image(getClass().getResource(currentFallingTrash.getImg()).toString()));
        trashView.setFitWidth(45);
        trashView.setFitHeight(45);
        trashView.setLayoutX(currentFallingTrash.getX());
        trashView.setLayoutY(currentFallingTrash.getY());
        trashOrderLabel.setLayoutX(currentFallingTrash.getX() - (Math.abs(trashView.getFitWidth() - trashOrderLabel.getWidth())) / 2);

        currentFallingTrash.setY(0);

        if (!gamePane.getChildren().contains(trashView)) {
            trashView.setId(String.valueOf(order));
            gamePane.getChildren().add(trashView);
        }

        currentAnimationTimer = new AnimationTimer() {
            private boolean hasReachedGround = false;

            @Override
            public void handle(long now) {
                if (remainingTime <= 0) {
                    stop();
                    cleanupGamePane();
                    System.out.println("AnimationTimer dừng do hết thời gian.");
                    return;
                }

                if (!hasReachedGround) {

                    currentFallingTrash.setY(currentFallingTrash.getY() + currentFallingTrash.getVy());
                    trashView.setLayoutY(currentFallingTrash.getY());
                    trashOrderLabel.setLayoutY(currentFallingTrash.getY() - 40);

                    if (trashView.getLayoutY() > redLine.getLayoutY() - trashView.getFitWidth() - (redLine.getEndY() - redLine.getStartY())) {
                        hasReachedGround = true;
                        System.out.println("Trash đã chạm đất.");

                        try {
                            sendRemoveTrashRemove();
                            gamePane.getChildren().remove(trashView);
                            gamePane.getChildren().remove(trashOrderLabel);
                            currentFallingTrash = null;
                            spawnNextTrash();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        };

        currentAnimationTimer.start();
    }

    // Chuyển sang trang hiển thị điểm
    private void loadShowScorePage() throws IOException {
        System.out.println("Thời gian đã hết. Chuyển sang trang mới.");
        List<Object> data = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        session.setEndTime(formattedTime);

        gameSessionPlayerCreate.setScore(scoreUser1);
        gameSessionPlayerJoin.setScore(scoreUser2);

        data.add(session);
        data.add(gameSessionPlayerCreate);
        data.add(gameSessionPlayerJoin);

        Request request = new Request(RequestStatus.SHOW_SCORE_USER, data);
        System.out.println("REQUEST TO SERVER SHOW_SCORE_USER: " + request);
        client.sendToServer(request);
    }
}
