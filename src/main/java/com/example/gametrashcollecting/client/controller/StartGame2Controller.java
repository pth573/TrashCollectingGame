package com.example.gametrashcollecting.client.controller;
import com.example.gametrashcollecting.client.model.Client;
import com.example.gametrashcollecting.client.model.TrashBin;
import com.example.gametrashcollecting.model.*;
import com.example.gametrashcollecting.server.request.Request;
import com.example.gametrashcollecting.server.request.RequestStatus;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartGame2Controller {

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
    private Pane gamePane;

    @FXML
    private Label pointUser1;

    @FXML
    private Label pointUser2;

    @FXML
    private Label roundId;

    @FXML
    private Label roundLevel;

    @FXML
    private ImageView thungGiay;

    @FXML
    private ImageView thungHuuCo;

    @FXML
    private ImageView thungKimLoai;

    @FXML
    private ImageView thungNhua;

    @FXML
    private Label timeRemain;

    @FXML
    private Label user1;

    @FXML
    private Label user2;

    private TrashBin thungGiayBin;
    private TrashBin thungHuuCoBin;
    private TrashBin thungKimLoaiBin;
    private TrashBin thungNhuaBin;


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
    private int score = 0;

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
        this.gameScene.setOnKeyPressed(event -> handleKeyPressed(event));
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
        pointUser2.setText("POINT: " + String.valueOf(scoreUser2));
        pointUser1.setText("POINT: " + String.valueOf(scoreUser1));
        List<Object> data = new ArrayList<>();


        GameSession gameSession;
        User userCreateRoom = null;
        User userJoinRoom = null;
        User thisUser = null;
        GameSessionPlayer gameSessionPlayerCreate;
        GameSessionPlayer gameSessionPlayerJoin;
        List<TrashItem> trashItemList = new ArrayList<>();
//
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
                pointUser1.setText("POINT: " + String.valueOf(scoreUser1));

                System.out.println("SEND REQUEST UPDATE_SCORE_UI");
                System.out.println("SC1: " + scoreUser1);
                Request request = new Request(RequestStatus.UPDATE_SCORE_UI, data);
                System.out.println("REQUEST TO SERVER UPDATE_SCORE_UI: " + request);
                client.sendToServer(request);

            }
            else {
                data.add(userCreateRoom);
                data.add(userJoinRoom);
                data.add(thisUser);
                data.add(Integer.valueOf(scoreUser2));

                gameSessionPlayerJoin.setScore(scoreUser2);
                pointUser2.setText("POINT: " + String.valueOf(scoreUser2));
                System.out.println("SC2: " + scoreUser2);
                System.out.println("SEND REQUEST UPDATE_SCORE_UI");
                Request request = new Request(RequestStatus.UPDATE_SCORE_UI, data);
                System.out.println("REQUEST TO SERVER UPDATE_SCORE_UI: " + request);
                client.sendToServer(request);
            }
        }
    }

    public void updatePointUser1(int scoreUser1){
        pointUser1.setText("POINT: " + String.valueOf(scoreUser1));
    }

    public void updatePointUser2(int scoreUser2){
        pointUser2.setText("POINT: "+ String.valueOf(scoreUser2));
    }

    public void updateUIStartGame() throws IOException {
        remainingTime = session.getRound().getTimeLimit();
        timeRemain.setText(remainingTime + "s");
        updateRoomUI();
        updatePoint();
        updateUserUI();
        spawnNextTrash();
    }

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


    private Timeline countdown;

    public void initialize() {
        thungGiayBin = new TrashBin(ItemType.PAPER, thungGiay);
        thungHuuCoBin = new TrashBin(ItemType.ORGANIC, thungHuuCo);
        thungKimLoaiBin = new TrashBin(ItemType.METAL, thungKimLoai);
        thungNhuaBin = new TrashBin(ItemType.PLASTIC, thungNhua);

        timeRemain.setText(remainingTime + "s");

        countdown = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    remainingTime--;
                    timeRemain.setText(remainingTime + "s");

                    if (remainingTime <= 0) {
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


    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode().toString().equals("LEFT")) {
            currentFallingTrash.setX(currentFallingTrash.getX() - 15);
            trashView.setLayoutX(trashView.getLayoutX() - 15);
            trashOrderLabel.setLayoutX(currentFallingTrash.getX() - (Math.abs(trashView.getFitWidth() - trashOrderLabel.getWidth())) / 2);

        } else if (event.getCode().toString().equals("RIGHT")) {
            currentFallingTrash.setX(currentFallingTrash.getX() + 15);
            trashView.setLayoutX(trashView.getLayoutX() + 15);
            trashOrderLabel.setLayoutX(currentFallingTrash.getX() - (Math.abs(trashView.getFitWidth() - trashOrderLabel.getWidth())) / 2);
        }
    }

    // lấy rác trong DB
    public void spawnNextTrash() {
        order++;
        if (!trashItemList.isEmpty()) {
            currentFallingTrash = trashItemList.get(order);
            System.out.println("Trash: " + currentFallingTrash.getItemType());

            trashOrderLabel.setText("Trash #" + order);
            trashOrderLabel.setStyle("-fx-font-size: 16; -fx-text-fill: white;");

            if (!gamePane.getChildren().contains(trashOrderLabel)) {
                gamePane.getChildren().add(trashOrderLabel);
            }

            if (currentFallingTrash.getVy() == 0) {
                if(session.getRound().getDifficulLevel().equals(Level.EASY)){
                    currentFallingTrash.setVy(0.3);
                }
                else if(session.getRound().getDifficulLevel().equals(Level.HARD)){
                    currentFallingTrash.setVy(0.8);
                }
                else currentFallingTrash.setVy(0.5);

            }
            startTrashFallAnimation();
        }
    }


    private void cleanupGamePane() {
        System.out.println("Dọn dẹp giao diện game.");
        gamePane.getChildren().clear();
        currentFallingTrash = null;
    }

    private void handleTrashTypeSelection(int score) throws IOException {
        if(userCreateRoom.getUsername().equals(client.getUser().getUsername())){
            scoreUser1 = score;
            pointUser1.setText(String.valueOf(scoreUser1));
            updatePoint();
        }
        else{
            scoreUser2 = score;
            pointUser2.setText(String.valueOf(scoreUser2));
            updatePoint();
        }
        System.out.println("Correct guess! User1 score increased.");
    }


    private void startTrashFallAnimation() {
        System.out.println("TRAASSH1: " + currentFallingTrash);
        if(session.getRound().getDifficulLevel().equals(Level.EASY)){
            currentFallingTrash.setVy(0.3);
        }
        else if(session.getRound().getDifficulLevel().equals(Level.HARD)){
            currentFallingTrash.setVy(0.8);
        }
        else {
            currentFallingTrash.setVy(0.5);
        }
        trashView = new ImageView(new Image(getClass().getResource(currentFallingTrash.getImg()).toString()));
        trashView.setFitWidth(45);
        trashView.setFitHeight(45);
        trashView.setLayoutX(currentFallingTrash.getX());
        trashView.setLayoutY(currentFallingTrash.getY());
        trashOrderLabel.setLayoutX(currentFallingTrash.getX() - (Math.abs(trashView.getFitWidth() - trashOrderLabel.getWidth())) / 2);
        currentFallingTrash.setX(currentFallingTrash.getX());
        currentFallingTrash.setY(0);

        if (!gamePane.getChildren().contains(trashView)) {
            trashView.setId(String.valueOf(order));
            gamePane.getChildren().add(trashView);
        }

        new AnimationTimer() {
            private boolean hasReachedGround = false;

            @Override
            public void handle(long now) {
                synchronized (this) {
                    if (remainingTime <= 0) {
                        stop();
                        cleanupGamePane();
                        System.out.println("AnimationTimer dừng do hết thời gian.");
                        return;
                    }

                    if (trashView.getLayoutY() > background.getFitHeight()  ) {
                        System.out.println("Rác đã vượt qua đáy màn hình. Tạo rác mới.");
                        score -= 5;
                        if(score < 0) score = 0;
                        try {
                            handleTrashTypeSelection(score);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        gamePane.getChildren().remove(trashView);
                        gamePane.getChildren().remove(trashOrderLabel);
                        currentFallingTrash = null;
                        spawnNextTrash();
                    }

                    if (!hasReachedGround) {
                        currentFallingTrash.setY(currentFallingTrash.getY() + currentFallingTrash.getVy());
                        trashView.setLayoutY(currentFallingTrash.getY());
                        trashOrderLabel.setLayoutY(currentFallingTrash.getY() - 40);

                        if (trashView.getLayoutY() > thungGiay.getLayoutY() - trashView.getFitHeight()) {
                            // Kiểm tra tất cả các thùng
                            if (isTrashInBin(trashView, thungGiay, ItemType.PAPER)) {
                                hasReachedGround = true;
                                if (currentFallingTrash.getItemType() == ItemType.PAPER) {
                                    score += 10;
                                    try {
                                        handleTrashTypeSelection(score);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    System.out.println("Điểm cộng thêm: 10. Điểm hiện tại: " + score);
                                } else {
                                    // Nếu rác không đúng thùng, trừ 5 điểm
                                    score -= 5;
                                    if(score < 0) score = 0;
                                    try {
                                        handleTrashTypeSelection(score);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    System.out.println("Rác không đúng loại thùng! Trừ 5 điểm. Điểm hiện tại: " + score);
                                }
                            } else if (isTrashInBin(trashView, thungHuuCo, ItemType.ORGANIC)) {
                                hasReachedGround = true;
                                if (currentFallingTrash.getItemType() == ItemType.ORGANIC) {
                                    score += 10;
                                    try {
                                        handleTrashTypeSelection(score);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    System.out.println("Điểm cộng thêm: 10. Điểm hiện tại: " + score);
                                } else {
                                    score -= 5;
                                    if(score < 0) score = 0;
                                    try {
                                        handleTrashTypeSelection(score);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    System.out.println("Rác không đúng loại thùng! Trừ 5 điểm. Điểm hiện tại: " + score);
                                }
                            } else if (isTrashInBin(trashView, thungKimLoai, ItemType.METAL)) {
                                hasReachedGround = true;
                                if (currentFallingTrash.getItemType() == ItemType.METAL) {
                                    score += 10;
                                    try {
                                        handleTrashTypeSelection(score);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    System.out.println("Điểm cộng thêm: 10. Điểm hiện tại: " + score);
                                } else {
                                    score -= 5;
                                    if(score < 0) score = 0;
                                    try {
                                        handleTrashTypeSelection(score);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    System.out.println("Rác không đúng loại thùng! Trừ 5 điểm. Điểm hiện tại: " + score);
                                }
                            } else if (isTrashInBin(trashView, thungNhua, ItemType.PLASTIC)) {
                                hasReachedGround = true;
                                if (currentFallingTrash.getItemType() == ItemType.PLASTIC) {
                                    score += 10;
                                    try {
                                        handleTrashTypeSelection(score);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    System.out.println("Điểm cộng thêm: 10. Điểm hiện tại: " + score);
                                } else {
                                    score -= 5;
                                    if(score < 0) score = 0;
                                    try {
                                        handleTrashTypeSelection(score);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    System.out.println("Rác không đúng loại thùng! Trừ 5 điểm. Điểm hiện tại: " + score);
                                }
                            }
                            if (hasReachedGround) {
                                gamePane.getChildren().remove(trashView);
                                gamePane.getChildren().remove(trashOrderLabel);
                                currentFallingTrash = null;
                                spawnNextTrash();
                            }
                        }
                    }
                }
            }
        }.start();
    }


    private boolean isTrashInBin(ImageView trashView, ImageView binView, ItemType correctType) {
        // Kiểm tra vị trí x và y
        return trashView.getLayoutX() >= binView.getLayoutX() && trashView.getLayoutX() <= binView.getLayoutX() + binView.getFitWidth() &&
                trashView.getLayoutY() > binView.getLayoutY() - trashView.getFitHeight();
    }

}
