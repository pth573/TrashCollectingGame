package com.example.gametrashcollecting.server.controller;
import com.example.gametrashcollecting.model.*;
import com.example.gametrashcollecting.server.dao.*;
import com.example.gametrashcollecting.server.request.Request;
import com.example.gametrashcollecting.server.request.RequestStatus;
import com.example.gametrashcollecting.server.statusReponse.ResponseStatus;
import com.example.gametrashcollecting.server.statusReponse.Status;
import com.example.gametrashcollecting.server.utils.ClientManager;
import com.example.gametrashcollecting.server.utils.DatabaseConnectionManager;
import com.mysql.cj.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameSessionController {
    private static GameSessionDAO gameSessionDAO;
    private static GameRoundDAO gameRoundDAO;
    private static GameRoomDAO gameRoomDAO;
    private static RoundTrashItemDAO roundTrashItemDAO;
    private static GameSessionPlayerDAO gameSessionPlayerDAO;
    private static DatabaseConnectionManager dbManager;

    public GameSessionController(GameSessionDAO gameSessionDAO, GameRoundDAO gameRoundDAO, GameSessionPlayerDAO gameSessionPlayerDAO, GameRoomDAO gameRoomDAO, RoundTrashItemDAO roundTrashItemDAO) {
        this.gameSessionDAO = gameSessionDAO;
        this.gameRoundDAO = gameRoundDAO;
        this.gameRoomDAO = gameRoomDAO;
        this.gameSessionPlayerDAO = gameSessionPlayerDAO;
        this.roundTrashItemDAO = roundTrashItemDAO;
        dbManager = new DatabaseConnectionManager();
    }

//    public static ResponseStatus createGameSession(Object dataFromClient, Client client, Map<String, Client> clientMap) throws Exception {
//
//        gameSessionDAO = new GameSessionDAO();
//        GameSession gameSession = gameSessionDAO.createGameSession();
//        if(gameSession != null) {
//            return new ResponseStatus(Status.CREATE_SESSION_SUCCESSFUL,gameSession);
//        }
//        else{
//            return new ResponseStatus(Status.CREATE_SESSION_FAIL);
//        }
//    }



    public static ResponseStatus startGame(Object dataFromClient, Client client, Map<String, Client> clientMap) throws Exception {
        GameSession gameSession = null;
        User userCreateRoom = null;
        User userJoinRoom = null;
        /// sai vi tri usercreat,...

        if (dataFromClient instanceof List) {
            List<?> outerList = (List<?>) dataFromClient;
            Object firstElement = outerList.get(0);
            if (firstElement instanceof GameSession) {
                gameSession = (GameSession) firstElement;
            } else {
                gameSession = new GameSession();
            }
//            Object secondElement = outerList.get(1);
//            if (secondElement instanceof User) {
//                userJoinRoom = (User) secondElement;
//            } else {
//                userJoinRoom = new User();
//            }
//            Object thirdElement = outerList.get(2);
//            if (thirdElement instanceof User) {
//                userCreateRoom = (User) thirdElement;
//            } else {
//                userCreateRoom = new User();
//            }

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



            System.out.println("ID:");
            System.out.println(userCreateRoom);
            System.out.println(userJoinRoom);
            System.out.println("gameSession.getRound(): " + gameSession.getRound());
            System.out.println("gameSession.getRoom(): " + gameSession.getRoom());
            gameSessionDAO = new GameSessionDAO();
            GameSession gameSessionDB = gameSessionDAO.createGameSession(gameSession.getRound(), gameSession.getRoom());
            System.out.println("gameSessionDB: " + gameSessionDB);

            gameSessionPlayerDAO = new GameSessionPlayerDAO();
            GameSessionPlayer gameSessionPlayerCreate = gameSessionPlayerDAO.createGameSessionPlayer(gameSessionDB, userCreateRoom);
            GameSessionPlayer gameSessionPlayerJoin = gameSessionPlayerDAO.createGameSessionPlayer(gameSessionDB, userJoinRoom);

            roundTrashItemDAO = new RoundTrashItemDAO();
            List<TrashItem> trashItemList = roundTrashItemDAO.findListTrashByRound(gameSession.getRound());
            System.out.println(gameSession.getRound());
            for (TrashItem trashItem : trashItemList) {
                System.out.println(trashItem);
            }


            List<Object> dataList = null;
            if (dataFromClient instanceof List<?>) {
                dataList = (List<Object>) dataFromClient;
            } else {
                System.out.println("Object không phải là List");
            }
            // client1-chu phong
            dataList.add(gameSessionPlayerCreate);
            dataList.add(gameSessionPlayerJoin);
//            dataList.add(trashItemList);



            // Tạo thêm 50 TrashItem mới
            List<TrashItem> newTrashItems = new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i < 100; i++) {
                // Chọn ngẫu nhiên một TrashItem từ danh sách đã có
                TrashItem baseTrashItem = trashItemList.get(random.nextInt(trashItemList.size()));

                // Sinh ngẫu nhiên tọa độ x, y
                double x = random.nextDouble() * 540; // Giả sử chiều rộng 800
                double y = random.nextDouble() * 750; // Giả sử chiều cao 600

                // Tạo TrashItem mới với thuộc tính giống baseTrashItem
                TrashItem newTrashItem = new TrashItem(
                        i,        // id (có thể thay đổi nếu cần thiết)
                        baseTrashItem.getItemType(),   // loại
                        baseTrashItem.getRound(),       // round
                        baseTrashItem.getImg(),         // hình ảnh
                        x,                               // tọa độ x
                        0,                               // tọa độ y
                        0,                               // vx
                        0.5                                // vy
                );

                // Thêm vào danh sách mới
                newTrashItems.add(newTrashItem);
            }

            // In danh sách TrashItem mới
            for (TrashItem item : newTrashItems) {
                System.out.println(item);
            }


            dataList.add(newTrashItems);



            // client2-
            Client client2 = ClientManager.getInstance().getClient(userJoinRoom.getUsername());
            System.out.println("HELLLOOOOOOOOOOO");
            System.out.println(userJoinRoom.getUsername());
            ResponseStatus responseStatus = new ResponseStatus(Status.START_GAME_SUCCESS, dataList);
            client2.getOos().writeObject(responseStatus);
            client2.getOos().flush();

            return new ResponseStatus(Status.START_GAME_SUCCESS, dataList);


        }
        return new ResponseStatus(Status.START_GAME_FAIL, "Start game fail");
    }

    public static ResponseStatus updateUIPoint(Object dataFromClient, Client client, Map<String, Client> clientMap) throws Exception {
        User userCreateRoom = null;
        User userJoinRoom = null;
        User thisUser = null;
        int score1 = 0;
        int score2 = 0;

        if (dataFromClient instanceof List) {
            List<?> outerList = (List<?>) dataFromClient;
            System.out.println("List data updatePoint from client:" + outerList.size());
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

            Object seventh = outerList.get(6);
            if (seventh instanceof User) {
                thisUser = (User) seventh;
            } else {
                thisUser = new User();
            }
            Object score = outerList.get(7);
            if (score instanceof String) {
                score1 = Integer.parseInt((String) score);
            } else {
                score1 = 0;
            }
            Object scoree = outerList.get(8);
            if (scoree instanceof String) {
                score2 = Integer.parseInt((String) scoree);
            } else {
                score2 = 0;
            }
        }
        System.out.println("SC1: " + score1);
        System.out.println("SC2: " + score2);
        if(thisUser.getUsername().equals(userCreateRoom.getUsername())) {
            System.out.println("11111");
            Client clientOther = ClientManager.getInstance().getClient(userJoinRoom.getUsername());
            ResponseStatus responseStatus = new ResponseStatus(Status.START_GAME_UPDATE_CLIENT_2_SUCCESS, dataFromClient);
            clientOther.getOos().writeObject(responseStatus);
            return new ResponseStatus(Status.OTHER, "");
        }
        else {
            System.out.println("2222");
            Client clientOther = ClientManager.getInstance().getClient(userCreateRoom.getUsername());
            ResponseStatus responseStatus = new ResponseStatus(Status.START_GAME_UPDATE_CLIENT_2_SUCCESS, dataFromClient);
            clientOther.getOos().writeObject(responseStatus);
            return new ResponseStatus(Status.OTHER, "");
        }
    }


    public static ResponseStatus updateScoreUI_1(Object dataFromClient, Client client, Map<String, Client> clientMap) throws Exception {
        System.out.println("44444444");


        User userCreateRoom = null;
        User userJoinRoom = null;
        User thisUser = null;
        Integer score = 0;

        if (dataFromClient instanceof List) {
            List<?> outerList = (List<?>) dataFromClient;

            Object secondElement = outerList.get(0);
            if (secondElement instanceof User) {
                userCreateRoom = (User) secondElement;
            } else {
                userCreateRoom = new User();
            }
            System.out.println("1: " + userCreateRoom);

            Object thirdElement = outerList.get(1);
            if (thirdElement instanceof User) {
                userJoinRoom = (User) thirdElement;
            } else {
                userJoinRoom = new User();
            }
            System.out.println("2: " + userJoinRoom);

            Object seventh = outerList.get(2);
            if (seventh instanceof User) {
                thisUser = (User) seventh;
            } else {
                thisUser = new User();
            }
            System.out.println("5: " + thisUser);

            Object x = outerList.get(3);
            if(x instanceof Integer) {
                score =  (Integer) x;
            }
        }

        System.out.println("SC: " + score);
        if(thisUser.getUsername().equals(userCreateRoom.getUsername())) {
            System.out.println("11111");
            Client clientOther = ClientManager.getInstance().getClient(userJoinRoom.getUsername());
            ResponseStatus responseStatus = new ResponseStatus(Status.UPDATE_SCORE_UI_1_SUCCESS, dataFromClient);
            clientOther.getOos().writeObject(responseStatus);
            return new ResponseStatus(Status.OTHER, "");
        }
        else {
            System.out.println("2222");
            Client clientOther = ClientManager.getInstance().getClient(userCreateRoom.getUsername());
            ResponseStatus responseStatus = new ResponseStatus(Status.UPDATE_SCORE_UI_1_SUCCESS, dataFromClient);
            clientOther.getOos().writeObject(responseStatus);
            return new ResponseStatus(Status.OTHER, "");
        }
    }

    public static ResponseStatus updateScoreUI(Object dataFromClient, Client client, Map<String, Client> clientMap) throws Exception {
        System.out.println("44444444");


        User userCreateRoom = null;
        User userJoinRoom = null;
        User thisUser = null;
        Integer score = 0;

        if (dataFromClient instanceof List) {
            List<?> outerList = (List<?>) dataFromClient;

            Object secondElement = outerList.get(0);
            if (secondElement instanceof User) {
                userCreateRoom = (User) secondElement;
            } else {
                userCreateRoom = new User();
            }
            System.out.println("1: " + userCreateRoom);

            Object thirdElement = outerList.get(1);
            if (thirdElement instanceof User) {
                userJoinRoom = (User) thirdElement;
            } else {
                userJoinRoom = new User();
            }
            System.out.println("2: " + userJoinRoom);

            Object seventh = outerList.get(2);
            if (seventh instanceof User) {
                thisUser = (User) seventh;
            } else {
                thisUser = new User();
            }
            System.out.println("5: " + thisUser);

            Object x = outerList.get(3);
            if(x instanceof Integer) {
              score =  (Integer) x;
            }
        }

        System.out.println("SC: " + score);
        if(thisUser.getUsername().equals(userCreateRoom.getUsername())) {
            System.out.println("11111");
            Client clientOther = ClientManager.getInstance().getClient(userJoinRoom.getUsername());
            ResponseStatus responseStatus = new ResponseStatus(Status.UPDATE_SCORE_UI_SUCCESS, dataFromClient);
            clientOther.getOos().writeObject(responseStatus);
            return new ResponseStatus(Status.OTHER, "");
        }
        else {
            System.out.println("2222");
            Client clientOther = ClientManager.getInstance().getClient(userCreateRoom.getUsername());
            ResponseStatus responseStatus = new ResponseStatus(Status.UPDATE_SCORE_UI_SUCCESS, dataFromClient);
            clientOther.getOos().writeObject(responseStatus);
            return new ResponseStatus(Status.OTHER, "");
        }
    }

    public static ResponseStatus updateTrashUI(Object dataFromClient, Client client, Map<String, Client> clientMap) throws IOException {
        User userCreateRoom = null;
        User userJoinRoom = null;
        User thisUser = null;

        TrashItem trashItem;
        int order;

        if (dataFromClient instanceof List) {
            List<?> outerList = (List<?>) dataFromClient;

            Object secondElement = outerList.get(0);
            if (secondElement instanceof User) {
                userCreateRoom = (User) secondElement;
            } else {
                userCreateRoom = new User();
            }
//            System.out.println("1: " + userCreateRoom);

            Object thirdElement = outerList.get(1);
            if (thirdElement instanceof User) {
                userJoinRoom = (User) thirdElement;
            } else {
                userJoinRoom = new User();
            }
//            System.out.println("2: " + userJoinRoom);

            Object seventh = outerList.get(2);
            if (seventh instanceof User) {
                thisUser = (User) seventh;
            } else {
                thisUser = new User();
            }
//            System.out.println("5: " + thisUser);
            Object nineElement = outerList.get(3);
            if (nineElement instanceof Integer) {
                order = (Integer) nineElement;
            } else {
                order = 0;
            }
            Object eighthElement = outerList.get(4);
            if (eighthElement instanceof TrashItem) {
                trashItem = (TrashItem) eighthElement;
            } else {
                trashItem= null;
            }
//            System.out.println(trashItem);
            if(thisUser.getUsername().equals(userCreateRoom.getUsername())) {
//                System.out.println("11111");
                Client clientOther = ClientManager.getInstance().getClient(userJoinRoom.getUsername());
                ResponseStatus responseStatus = new ResponseStatus(Status.UPDATE_TRASH_UI_SUCCESS, dataFromClient);
//                System.out.println(responseStatus);
                clientOther.getOos().writeObject(responseStatus);
            }
            else {
//                System.out.println("2222");
                Client clientOther = ClientManager.getInstance().getClient(userCreateRoom.getUsername());
                ResponseStatus responseStatus = new ResponseStatus(Status.UPDATE_TRASH_UI_SUCCESS, dataFromClient);
//                System.out.println(responseStatus);
                clientOther.getOos().writeObject(responseStatus);
            }
        }
        return new ResponseStatus(Status.OTHER, "");
    }

    public static ResponseStatus updateColorUI(Object dataFromClient, Client client, Map<String, Client> clientMap) throws IOException {
        User userCreateRoom = null;
        User userJoinRoom = null;
        User thisUser = null;

        TrashItem trashItem;
        String colorId;
        int order;

        if (dataFromClient instanceof List) {
            List<?> outerList = (List<?>) dataFromClient;

            Object secondElement = outerList.get(0);
            if (secondElement instanceof User) {
                userCreateRoom = (User) secondElement;
            } else {
                userCreateRoom = new User();
            }
            System.out.println("1: " + userCreateRoom);

            Object thirdElement = outerList.get(1);
            if (thirdElement instanceof User) {
                userJoinRoom = (User) thirdElement;
            } else {
                userJoinRoom = new User();
            }
            System.out.println("2: " + userJoinRoom);

            Object seventh = outerList.get(2);
            if (seventh instanceof User) {
                thisUser = (User) seventh;
            } else {
                thisUser = new User();
            }
            System.out.println("5: " + thisUser);

            Object nineElement = outerList.get(3);
            if (nineElement instanceof Integer) {
                order = (Integer) nineElement;
            } else {
                order = 0;
            }


            Object eighthElement = outerList.get(4);
            if (eighthElement instanceof TrashItem) {
                trashItem = (TrashItem) eighthElement;
            } else {
                trashItem= null;
            }

            System.out.println(trashItem);

            Object tenthElement = outerList.get(5);
            if (tenthElement instanceof String) {
                colorId = (String) tenthElement;
            } else {
                colorId = null;
            }
            System.out.println(colorId);

            if(thisUser.getUsername().equals(userCreateRoom.getUsername())) {
                Client clientOther = ClientManager.getInstance().getClient(userJoinRoom.getUsername());
                ResponseStatus responseStatus = new ResponseStatus(Status.UPDATE_COLOR_UI_SUCCESS, dataFromClient);
                clientOther.getOos().writeObject(responseStatus);
                return new ResponseStatus(Status.OTHER, "");
            }
            else {
                Client clientOther = ClientManager.getInstance().getClient(userCreateRoom.getUsername());
                ResponseStatus responseStatus = new ResponseStatus(Status.UPDATE_COLOR_UI_SUCCESS, dataFromClient);
                clientOther.getOos().writeObject(responseStatus);
                return new ResponseStatus(Status.OTHER, "");
            }
        }
        return new ResponseStatus(Status.OTHER, "");
    }



    public static ResponseStatus removeTrash(Object dataFromClient, Client client, Map<String, Client> clientMap) throws IOException {
        User userCreateRoom = null;
        User userJoinRoom = null;
        User thisUser = null;

        TrashItem trashItem;
        int order;

        if (dataFromClient instanceof List) {
            List<?> outerList = (List<?>) dataFromClient;

            Object secondElement = outerList.get(0);
            if (secondElement instanceof User) {
                userCreateRoom = (User) secondElement;
            } else {
                userCreateRoom = new User();
            }
            System.out.println("1: " + userCreateRoom);

            Object thirdElement = outerList.get(1);
            if (thirdElement instanceof User) {
                userJoinRoom = (User) thirdElement;
            } else {
                userJoinRoom = new User();
            }
            System.out.println("2: " + userJoinRoom);

            Object seventh = outerList.get(2);
            if (seventh instanceof User) {
                thisUser = (User) seventh;
            } else {
                thisUser = new User();
            }
            System.out.println("5: " + thisUser);

            Object nineElement = outerList.get(3);
            if (nineElement instanceof Integer) {
                order = (Integer) nineElement;
            } else {
                order = 0;
            }


            Object eighthElement = outerList.get(4);
            if (eighthElement instanceof TrashItem) {
                trashItem = (TrashItem) eighthElement;
            } else {
                trashItem= null;
            }

            System.out.println(trashItem);


            if(thisUser.getUsername().equals(userCreateRoom.getUsername())) {
                System.out.println("3333");
                Client clientOther = ClientManager.getInstance().getClient(userJoinRoom.getUsername());
                ResponseStatus responseStatus = new ResponseStatus(Status.REMOVE_TRASH_SUCCESS, dataFromClient);
                clientOther.getOos().writeObject(responseStatus);
                return new ResponseStatus(Status.OTHER, "");
            }
            else {
                System.out.println("44444");
                Client clientOther = ClientManager.getInstance().getClient(userCreateRoom.getUsername());
                ResponseStatus responseStatus = new ResponseStatus(Status.REMOVE_TRASH_SUCCESS, dataFromClient);
                clientOther.getOos().writeObject(responseStatus);
                return new ResponseStatus(Status.OTHER, "");
            }
        }
        return new ResponseStatus(Status.OTHER, "");
//        return new ResponseStatus(Status.REMOVE_TRASH_SUCCESS, dataFromClient);
    }

    public static ResponseStatus updateSessionAndScoreAfterPlaying(Object dataFromClient, Client client, Map<String, Client> clientMap) throws IOException, SQLException {
        GameSession session = null;
        GameSessionPlayer gameSessionPlayerCreate = null;
        GameSessionPlayer gameSessionPlayerJoin = null;
        User thisUser = null;

        if (dataFromClient instanceof List) {
            List<?> outerList = (List<?>) dataFromClient;

            Object firstElement = outerList.get(0);
            if (firstElement instanceof GameSession) {
                session = (GameSession) firstElement;
            } else {
                session = new GameSession();
            }
            System.out.println("1: " + session);

            Object secondElement = outerList.get(1);
            if (secondElement instanceof GameSessionPlayer) {
                gameSessionPlayerCreate = (GameSessionPlayer) secondElement;
            } else {
                gameSessionPlayerCreate = new GameSessionPlayer();
            }
            System.out.println("2: " + gameSessionPlayerCreate);

            Object thirdElement = outerList.get(2);
            if (thirdElement instanceof GameSessionPlayer) {
                gameSessionPlayerJoin = (GameSessionPlayer) thirdElement;
            } else {
                gameSessionPlayerJoin = new GameSessionPlayer();
            }
            System.out.println("3: " + gameSessionPlayerJoin);


            System.out.println("4: " + gameSessionPlayerCreate.getSession());
            System.out.println("5: " + gameSessionPlayerJoin.getSession());
//            Object forthElement = outerList.get(3);
//            if (forthElement instanceof User) {
//                thisUser = (User) forthElement;
//            } else {
//                thisUser = new User();
//            }
//            System.out.println("4: " + thisUser);

            gameSessionPlayerCreate.getSession().setEndTime(session.getEndTime());
            session = gameSessionPlayerCreate.getSession();

//            GameRoundDAO gameRoundDAO = new GameRoundDAO();
//            GameRound gameRound = gameRoundDAO.findGameRoundById(session.getId());

            GameSessionDAO gameSessionDAO = new GameSessionDAO();
            GameSession gameSessionDB = gameSessionDAO.findGameSessionById(session);
            System.out.println("G SS DB: " + gameSessionDB);


//            GameRoundDAO gameRoundDAO = new GameRoundDAO();
//            GameRound gameRound = gameRoundDAO.findGameRoundById(gameSessionDB.getRound().getId());
//            gameSessionDB.setRound(gameRound);

            gameSessionDB = gameSessionDAO.updateGameSession(gameSessionDB);

            GameRoundDAO gameRoundDAO = new GameRoundDAO();
            GameRound gameRound = gameRoundDAO.findGameRoundById(gameSessionDB.getRound().getId());
            gameSessionDB.setRound(gameRound);

            System.out.println("GAME SS: " + gameSessionDB);

            GameSessionPlayerDAO gameSessionPlayerDAO = new GameSessionPlayerDAO();
            GameSessionPlayer gameSessionPlayerCreateDB = gameSessionPlayerDAO.findGameSessionPlayerById(gameSessionPlayerCreate.getId());
            GameSessionPlayer gameSessionPlayerJoinDB = gameSessionPlayerDAO.findGameSessionPlayerById(gameSessionPlayerJoin.getId());

            gameSessionPlayerCreateDB.setSession(gameSessionDB);
            gameSessionPlayerJoinDB.setSession(gameSessionDB);
            gameSessionPlayerCreateDB.setScore(gameSessionPlayerCreate.getScore());
            gameSessionPlayerJoinDB.setScore(gameSessionPlayerJoin.getScore());


            System.out.println("GameSSPLCREATE0: " + gameSessionPlayerCreateDB);
            System.out.println("GameSSPLJOIN0: " + gameSessionPlayerJoinDB);

            gameSessionPlayerCreateDB = gameSessionPlayerDAO.updateGameSessionPlayer(gameSessionPlayerCreateDB);
            gameSessionPlayerJoinDB = gameSessionPlayerDAO.updateGameSessionPlayer(gameSessionPlayerJoinDB);

            System.out.println("GameSSPLCREATE: " + gameSessionPlayerCreateDB);
            System.out.println("GameSSPLJOIN: " + gameSessionPlayerJoinDB);

            int scoreMaxRoundUserCreate = gameSessionPlayerDAO.findScoreMaxRound(gameSessionPlayerCreate, gameSessionPlayerCreate.getSession().getRound());
            int scoreMaxRoundUserJoin = gameSessionPlayerDAO.findScoreMaxRound(gameSessionPlayerJoin, gameSessionPlayerCreate.getSession().getRound());


            int totalPointsUserCreate = gameSessionPlayerDAO.findUserTotalPoint(gameSessionPlayerCreateDB.getUser());
            int totalPointsUserJoin = gameSessionPlayerDAO.findUserTotalPoint(gameSessionPlayerJoinDB.getUser());
            UserDAO userDAO = new UserDAO();
            System.out.println("TOTAL1: " + Math.max(totalPointsUserCreate, gameSessionPlayerCreateDB.getScore()));
            System.out.println("TOTAL2: " + Math.max(totalPointsUserJoin, gameSessionPlayerJoinDB.getScore()));
            userDAO.updateUserTotalPoint(gameSessionPlayerCreateDB.getUser().getId(), Math.max(totalPointsUserCreate, gameSessionPlayerCreateDB.getScore()));
            userDAO.updateUserTotalPoint(gameSessionPlayerJoinDB.getUser().getId(),  Math.max(totalPointsUserJoin, gameSessionPlayerJoinDB.getScore()));


            gameSessionPlayerDAO.updateGameSessionPlayer(gameSessionPlayerCreateDB);
            gameSessionPlayerDAO.updateGameSessionPlayer(gameSessionPlayerJoinDB);


            List<Object> data = new ArrayList<>();
            data.add(gameSessionDB);
            data.add(gameSessionPlayerCreateDB);
            data.add(gameSessionPlayerJoinDB);
            data.add(scoreMaxRoundUserCreate);
            data.add(scoreMaxRoundUserJoin);



//            String userNameCreate = gameSessionPlayerCreateDB.getUser().getUsername();
//            String userNameJoin = gameSessionPlayerJoinDB.getUser().getUsername();

//            ResponseStatus responseStatus = new ResponseStatus(Status.SHOW_SCORE_USER_SUCCESS, dataFromClient);
//            client.getOos().writeObject(responseStatus);
            return new ResponseStatus(Status.SHOW_SCORE_USER_SUCCESS, data);
        }
        return new ResponseStatus(Status.SHOW_SCORE_USER_FAIL, "SHOW_SCORE_USER_FAIL");
    }
}