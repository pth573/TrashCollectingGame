package com.example.gametrashcollecting.client.model;

import com.example.gametrashcollecting.client.controller.*;
import com.example.gametrashcollecting.model.*;
import com.example.gametrashcollecting.server.request.Request;
import com.example.gametrashcollecting.server.statusReponse.ResponseStatus;
import com.example.gametrashcollecting.server.statusReponse.Status;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Client implements Serializable {
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket socket;
    private User user;
    private Stage stage;
    private StartGame2Controller startGame2Controller;
    private StartGameController startGameController;
    private ResultController resultController;
    private HistoryController historyController;
    private RoomController roomController;
     private RoomController roomController2;
     private UserController userController;


    public Client(String serverAddress, int port, Stage stage) {
        this.stage = stage;
        try{
            socket = new Socket(serverAddress, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            Thread listenerThread = new Thread(new ServerListenter());
            listenerThread.start();


        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendToServer(Request request) throws IOException {
        try{
            if(socket.isConnected()){
//                System.out.println("Socket is connected");
            }
            oos.writeObject(request);
            oos.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    private class ServerListenter implements Runnable {
        @Override
        public void run() {
            while(socket.isConnected()){
                ResponseStatus responseFromServer;
                try {
                    Object response = ois.readObject();
                    if(response instanceof ResponseStatus) {
                        responseFromServer = (ResponseStatus) response;
                        Status statusResponse = responseFromServer.getStatus();
                        System.out.println(statusResponse);
                        if (statusResponse.equals(Status.LOGIN_SUCCESS)) {
                            Object data = responseFromServer.getDataToClient();

                            List<User> friendList = new ArrayList<>();
                            List<User> top3UserPoint = new ArrayList<>();

                            if (data instanceof List) {
                                List<?> outerList = (List<?>) data;
                                System.out.println("List size: " + outerList.size());
                                Object firstElement = outerList.get(0);
                                if (firstElement instanceof User) {
                                    user = (User) firstElement;
                                }
                                Object secondElement = outerList.get(1);
                                if (secondElement instanceof List<?>) {
                                    List<?> innerList = (List<?>) secondElement;
                                    System.out.println("List size: " + innerList.size());
                                    for (Object userFriend : innerList) {
                                        if (userFriend instanceof User) {
                                            friendList.add((User) userFriend);
                                        }
                                    }
                                }
                                Object thirdElement = outerList.get(2);
                                if (thirdElement instanceof List<?>) {
                                    List<?> innerList = (List<?>) thirdElement;
                                    System.out.println("List size: " + innerList.size());
                                    for (Object userTop3 : innerList) {
                                        if (userTop3 instanceof User) {
                                            top3UserPoint.add((User) userTop3);
                                        }
                                    }
                                }
                            }

                            for(User user : friendList){
                                System.out.println(user.getUsername());
                            }
                            Platform.runLater(() -> {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/main-screen.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                    MainScreenController mainScreenController = loader.getController();
                                    mainScreenController.setClient(Client.this);
                                    mainScreenController.setThisUser(Client.this.user);
                                    mainScreenController.updateUI(friendList, top3UserPoint);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                Scene gameScene = new Scene(root);
                                stage.setScene(gameScene);
                                stage.show();
                            });
                        } else if (statusResponse.equals(Status.LOGIN_FAIL)) {
                        } else if (responseFromServer.getStatus().equals(Status.REGISTER_SUCCESS)) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/login-view.fxml"));
                            Platform.runLater(() -> {
                                Parent root = null;
                                try {
                                    root = loader.load();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                LoginController loginController = loader.getController();
                                loginController.setClient(Client.this);
                                Scene gameScene = new Scene(root);
                                stage.setScene(gameScene);
                                stage.show();
                            });
                        } else if (statusResponse.equals(Status.REGISTER_FAIL)) {
                            System.out.println("Register Failed");
                        }else if(statusResponse.equals(Status.CREATE_ROOM_SUCCESS)){
                            Object data = responseFromServer.getDataToClient();

                            List<User> friendList = new ArrayList<>();
                            GameRoom gameRoom;

                            if (data instanceof List) {
                                List<?> outerList = (List<?>) data;
                                System.out.println("List size: " + outerList.size());
                                Object firstElement = outerList.get(1);
                                if (firstElement instanceof GameRoom) {
                                    gameRoom = (GameRoom) firstElement;
                                } else {
                                    gameRoom = new GameRoom();
                                }
                                Object secondElement = outerList.get(0);
                                if (secondElement instanceof List<?>) {
                                    List<?> innerList = (List<?>) secondElement;
                                    System.out.println("List size: " + innerList.size());
                                    for (Object userFriend : innerList) {
                                        if (userFriend instanceof User) {
                                            friendList.add((User) userFriend);
                                        }
                                    }
                                }
                            } else {
                                gameRoom = null;
                            }
                            System.out.println("Game room client:" + gameRoom);
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/room-view.fxml"));
                            Platform.runLater(() -> {
                                Parent root = null;
                                try {
                                    root = loader.load();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                roomController = loader.getController();
                                roomController.setClient(Client.this);
                                roomController.setThisUser(Client.this.user);
                                roomController.updateUI(friendList, gameRoom, Client.this.user);
                                roomController.setGameRoom(gameRoom);
                                roomController.setUserCreateRoom(user);

                                Scene gameScene = new Scene(root);
                                stage.setScene(gameScene);
                                stage.show();
                            });
                        }
                        else if(statusResponse.equals(Status.CREATE_ROOM_FAIL)){
//                            System.out.println(Status.CREATE_ROOM_FAIL.toString());
                        }
                        else if(statusResponse.equals(Status.FIND_ALL_ROOM_SUCCESS)){
                            User user = null;
                            List<GameRoom> gameRoomList;
                            if(responseFromServer.getDataToClient() instanceof List){
                                List<?> outerList = (List<?>) responseFromServer.getDataToClient();
//                                System.out.println("List size: " + outerList.size());
                                Object firstElement = outerList.get(0);
                                if (firstElement instanceof User) {
                                    user = (User) firstElement;
                                } else {
                                    user = new User();
                                }
                                Object secondElement = outerList.get(1);
                                if (secondElement instanceof List<?>) {
                                    gameRoomList = (List<GameRoom>) secondElement;
                                } else {
                                    gameRoomList = new ArrayList<>();
                                }
                            } else {
                                gameRoomList = new ArrayList<>();
                            }


//                            if(responseFromServer.getDataToClient() instanceof List){
//                                gameRoomList = (List<GameRoom>) responseFromServer.getDataToClient();
//                            } else {
//                                gameRoomList = new ArrayList<>();
//                            }

                            Platform.runLater(() -> {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/room-list-view.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                    RoomListViewController roomListViewController = loader.getController();
                                    roomListViewController.setClient(Client.this);
                                    roomListViewController.setThisUser(Client.this.user);
                                    roomListViewController.loadRoomList(gameRoomList);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

//                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                                Scene gameScene = new Scene(root);
                                stage.setScene(gameScene);
                                stage.show();
                            });
                        }
                        else if(statusResponse.equals(Status.BACK_MAIN_SCREEN_SUCCESS)){
                            User user = null;
                            Object data = responseFromServer.getDataToClient();
                            List<User> friendList = new ArrayList<>();
                            List<User> top3UserPoint = new ArrayList<>();

                            if (data instanceof List) {
                                List<?> outerList = (List<?>) data;
                                System.out.println("List size: " + outerList.size());
                                Object firstElement = outerList.get(0);
                                if (firstElement instanceof User) {
                                    user = (User) firstElement;
                                }
                                Object secondElement = outerList.get(1);
                                if (secondElement instanceof List<?>) {
                                    List<?> innerList = (List<?>) secondElement;
                                    System.out.println("List size: " + innerList.size());
                                    for (Object userFriend : innerList) {
                                        if (userFriend instanceof User) {
                                            friendList.add((User) userFriend);
                                        }
                                    }
                                }
                                Object thirdElement = outerList.get(2);
                                if (thirdElement instanceof List<?>) {
                                    List<?> innerList = (List<?>) thirdElement;
                                    System.out.println("List size: " + innerList.size());
                                    for (Object userTop3 : innerList) {
                                        if (userTop3 instanceof User) {
                                            top3UserPoint.add((User) userTop3);
                                        }
                                    }
                                }
                            }

                            for(User user2 : friendList){
                                System.out.println(user2.getUsername());
                            }
                            Platform.runLater(() -> {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/main-screen.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                    MainScreenController mainScreenController = loader.getController();
                                    mainScreenController.setClient(Client.this);
                                    mainScreenController.setThisUser(Client.this.user);
                                    mainScreenController.updateUI(friendList, top3UserPoint);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                Scene gameScene = new Scene(root);
                                stage.setScene(gameScene);
                                stage.show();
                            });
                        }
                        else if(statusResponse.equals(Status.FIND_ROOM_SUCCESS)){
                            GameRoom gameRoom;
                            if(responseFromServer.getDataToClient() instanceof GameRoom){
                                gameRoom = (GameRoom) responseFromServer.getDataToClient();
                            } else {
                                gameRoom = null;
                            }

                            Platform.runLater(() -> {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/room-list-view.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                    RoomListViewController roomListViewController = loader.getController();
                                    roomListViewController.setClient(Client.this);
                                    roomListViewController.loadRoom(gameRoom);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                Scene gameScene = new Scene(root);
                                stage.setScene(gameScene);
                                stage.show();
                            });
                        }
                        else if(statusResponse.equals(Status.FIND_ROOM_FAIL)){

                        }
                        else if(statusResponse.equals(Status.UPDATE_ROOM_SUCCESS)){
                            Object data = responseFromServer.getDataToClient();

                            List<User> friendList = new ArrayList<>();
                            List<User> userList = new ArrayList<>();
                            GameRoom gameRoom;
                            User userCreateRoom;
                            User userJoinRoom;

                            if (data instanceof List) {
                                List<?> outerList = (List<?>) data;
                                Object secondElement = outerList.get(0);
                                if (secondElement instanceof List<?>) {
                                    List<?> innerList = (List<?>) secondElement;
                                    for (Object userFriend : innerList) {
                                        if (userFriend instanceof User) {
                                            friendList.add((User) userFriend);
                                        }
                                    }
                                }
                                Object thirdElement = outerList.get(1);
                                if (thirdElement instanceof List<?>) {
                                    List<?> innerList = (List<?>) thirdElement;
                                    System.out.println("List size: " + innerList.size());
                                    for (Object user : innerList) {
                                        if (user instanceof User) {
                                            userList.add((User) user);
                                        }
                                    }
                                }

                                Object firstElement = outerList.get(2);
                                if (firstElement instanceof GameRoom) {
                                    gameRoom = (GameRoom) firstElement;
                                } else {
                                    gameRoom = new GameRoom();
                                }

                                Object forthElement = outerList.get(3);
                                if (forthElement instanceof User) {
                                    userCreateRoom = (User) forthElement;
                                } else {
                                    userCreateRoom = new User();
                                }

                                Object fifthElement = outerList.get(4);
                                if (fifthElement instanceof User) {
                                    userJoinRoom = (User) fifthElement;
                                } else {
                                    userJoinRoom = new User();
                                }

                            } else {
                                userJoinRoom = null;
                                userCreateRoom = null;
                                gameRoom = new GameRoom();
                            }
                            User otherUser = null;
                            for(User userSub : userList){
                                if(!userSub.getUsername().equals(user.getUsername())){
                                    otherUser = userSub;
                                }
                            }
                            System.out.println("User1: " + user.getUsername());
                            System.out.println("User2: " + otherUser.getUsername());

                            User finalOtherUser = otherUser;
                            Platform.runLater(() -> {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/room-view.fxml"));
                                Parent root = null;
                                try {

                                    root = loader.load();
                                    roomController2 = loader.getController();
                                    // roomController2 = loader.getController();
                                    roomController2.setClient(Client.this);
                                    roomController2.updateUIUser2(userJoinRoom);
                                    roomController2.updateUIUser1(userCreateRoom);
                                    roomController2.updateUIJoin(friendList, gameRoom);
                                    roomController2.setUserCreateRoom(userCreateRoom);
                                    roomController2.setUserJoinRoom(userJoinRoom);
                                    roomController2.setGameRoom(gameRoom);
                                }catch (Exception e){

                                }
                                Scene gameScene = new Scene(root);
                                stage.setScene(gameScene);
                                stage.show();
                            });
                        }
                        else if(statusResponse.equals(Status.UPDATE_ROOM_UI_SUCCESS)){
                            Object data = responseFromServer.getDataToClient();

                            List<User> friendList = new ArrayList<>();
                            GameRoom gameRoom;
                            User thisUser1;
                            User otherUser2;

                            if (data instanceof List) {
                                List<?> outerList = (List<?>) data;
                                System.out.println("List size: " + outerList.size());
                                Object firstElement = outerList.get(2);
                                if (firstElement instanceof GameRoom) {
                                    gameRoom = (GameRoom) firstElement;
                                } else {
                                    gameRoom = new GameRoom();
                                }
                                Object secondElement = outerList.get(0);
                                if (secondElement instanceof List<?>) {
                                    List<?> innerList = (List<?>) secondElement;
                                    System.out.println("List size: " + innerList.size());
                                    for (Object userFriend : innerList) {
                                        if (userFriend instanceof User) {
                                            friendList.add((User) userFriend);
                                        }
                                    }
                                }
//                                Object thirdElement = outerList.get(1);
//                                if (thirdElement instanceof List<?>) {
//                                    List<?> innerList = (List<?>) thirdElement;
//                                    System.out.println("List size: " + innerList.size());
//                                    for (Object user : innerList) {
//                                        if (user instanceof User) {
//                                            userList.add((User) user);
//                                        }
//                                    }
//                                }

                                Object thirdElement = outerList.get(3);
                                if (thirdElement instanceof User) {
                                    thisUser1 = (User) thirdElement;
                                } else {
                                    thisUser1 = new User();
                                }
                                Object forthElement = outerList.get(4);
                                if (forthElement instanceof User) {
                                    otherUser2 = (User) forthElement;
                                } else {
                                    otherUser2 = new User();
                                }
                            } else {
                                thisUser1 = null;
                                gameRoom = null;
                                otherUser2 = new User();
                            }
                            System.out.println("HIIIIIIIIII4444444");
                            System.out.println(thisUser1);
                            System.out.println(otherUser2);
                            System.out.println(gameRoom);


                            System.out.println("Update UI user:" + otherUser2.getUsername());
                            Platform.runLater(() -> {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/room-view.fxml"));
                                Parent root = null;
                                try {

                                    root = loader.load();
                                    RoomController roomController = loader.getController();
                                    roomController.setClient(Client.this);
                                    roomController.updateUI(friendList, gameRoom, Client.this.user, otherUser2);
                                    roomController.setUserCreateRoom(thisUser1);
                                    roomController.setUserJoinRoom(otherUser2);
                                    roomController.setGameRoom(gameRoom);
                                    System.out.println("HIIIIIIIIII33333");
                                    System.out.println(thisUser1);
                                    System.out.println(gameRoom);
                                    System.out.println(otherUser2);

//                                    root = loader.load();
//                                    RoomController roomController = loader.getController();
//                                    roomController.setClient(Client.this);
//                                    roomController.updateUIUser2(thisUser1);
//                                    roomController.updateUIUser1(otherUser2);
//                                    roomController.updateUIJoin(friendList, gameRoom);
//                                    roomController.setUserCreateRoom(otherUser2);
//                                    roomController.setUserJoinRoom(thisUser1);
//                                    roomController.setGameRoom(gameRoom);

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                Scene gameScene = new Scene(root);
                                stage.setScene(gameScene);
                                stage.show();
                            });
                        }
                        else if(statusResponse.equals(Status.OUT_ROOM_SUCCESS)){
                            System.out.println("OUT_ROOM_SUCCESS");
                            Object data = responseFromServer.getDataToClient();
                            List<User> friendList = new ArrayList<>();
                            List<User> top3UserPoint = new ArrayList<>();

                            if (data instanceof List) {
                                List<?> outerList = (List<?>) data;
                                System.out.println("List size: " + outerList.size());
                                Object firstElement = outerList.get(0);
                                if (firstElement instanceof User) {
                                    user = (User) firstElement;
                                }
                                Object secondElement = outerList.get(1);
                                if (secondElement instanceof List<?>) {
                                    List<?> innerList = (List<?>) secondElement;
                                    System.out.println("List size: " + innerList.size());
                                    for (Object userFriend : innerList) {
                                        if (userFriend instanceof User) {
                                            friendList.add((User) userFriend);
                                        }
                                    }
                                }
                                Object thirdElement = outerList.get(2);
                                if (thirdElement instanceof List<?>) {
                                    List<?> innerList = (List<?>) thirdElement;
                                    System.out.println("List size: " + innerList.size());
                                    for (Object userTop3 : innerList) {
                                        if (userTop3 instanceof User) {
                                            top3UserPoint.add((User) userTop3);
                                        }
                                    }
                                }
                            }

                            for(User user : friendList){
                                System.out.println(user.getUsername());
                            }
                            Platform.runLater(() -> {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/main-screen.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                    MainScreenController mainScreenController = loader.getController();
                                    mainScreenController.setClient(Client.this);
                                    mainScreenController.setThisUser(Client.this.user);
                                    mainScreenController.updateUI(friendList, top3UserPoint);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                Scene gameScene = new Scene(root);
                                stage.setScene(gameScene);
                                stage.show();
                            });
                        }
                        if (statusResponse.equals(Status.LOGIN_SUCCESS)) {
                            Object data = responseFromServer.getDataToClient();

                            List<User> friendList = new ArrayList<>();
                            List<User> top3UserPoint = new ArrayList<>();

                            if (data instanceof List) {
                                List<?> outerList = (List<?>) data;
                                System.out.println("List size: " + outerList.size());
                                Object firstElement = outerList.get(0);
                                if (firstElement instanceof User) {
                                    user = (User) firstElement;
                                }
                                Object secondElement = outerList.get(1);
                                if (secondElement instanceof List<?>) {
                                    List<?> innerList = (List<?>) secondElement;
                                    System.out.println("List size: " + innerList.size());
                                    for (Object userFriend : innerList) {
                                        if (userFriend instanceof User) {
                                            friendList.add((User) userFriend);
                                        }
                                    }
                                }
                                Object thirdElement = outerList.get(2);
                                if (thirdElement instanceof List<?>) {
                                    List<?> innerList = (List<?>) thirdElement;
                                    System.out.println("List size: " + innerList.size());
                                    for (Object userTop3 : innerList) {
                                        if (userTop3 instanceof User) {
                                            top3UserPoint.add((User) userTop3);
                                        }
                                    }
                                }
                            }

                            for(User user : friendList){
                                System.out.println(user.getUsername());
                            }
                            Platform.runLater(() -> {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/main-screen.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                    MainScreenController mainScreenController = loader.getController();
                                    mainScreenController.setClient(Client.this);
                                    mainScreenController.setThisUser(Client.this.user);
                                    mainScreenController.updateUI(friendList, top3UserPoint);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                Scene gameScene = new Scene(root);
                                stage.setScene(gameScene);
                                stage.show();
                            });
                        }
                        else if(statusResponse.equals(Status.FIND_ALL_ROUND_SUCCESS)){
                            User userCreateRoom;
                            User userJoinRoom;
                            GameRoom gameRoom;
                            List<GameRound> gameRoundList = new ArrayList<>();
                            if (responseFromServer.getDataToClient() instanceof List) {
                                List<?> outerList = (List<?>) responseFromServer.getDataToClient();
                                System.out.println("List size: " + outerList.size());
                                Object firstElement = outerList.get(0);
                                if (firstElement instanceof User) {
                                    userCreateRoom = (User) firstElement;
                                } else {
                                    userCreateRoom = new User();
                                }
                                Object secondElement = outerList.get(1);
                                if (secondElement instanceof User) {
                                    userJoinRoom = (User) secondElement;
                                } else {
                                    userJoinRoom = new User();
                                }
                                Object thirdElement = outerList.get(2);
                                if (thirdElement instanceof GameRoom) {
                                    gameRoom = (GameRoom) thirdElement;
                                } else {
                                    gameRoom = new GameRoom();
                                }
                                Object forthElement = outerList.get(3);
                                if (forthElement instanceof List<?>) {
                                    List<?> innerList = (List<?>) forthElement;
                                    for (Object gameRound : innerList) {
                                        if (gameRound instanceof GameRound) {
                                            gameRoundList.add((GameRound) gameRound);
                                        }
                                    }
                                }

                            } else {
                                userJoinRoom = new User();
                                gameRoom = new GameRoom();
                                userCreateRoom = new User();
                            }
                            System.out.println(userCreateRoom);
                            System.out.println(userJoinRoom);
                            System.out.println(gameRoom);
                            System.out.println(gameRoundList.size());
                            System.out.println("HIIIIIIIIIIIIIIIII");
                            for(GameRound gameRound : gameRoundList){
                                System.out.println(gameRound);
                            }

                            Platform.runLater(() -> {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/select-map-1.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                    MapController mapController = loader.getController();
                                    mapController.setClient(Client.this);
                                    mapController.loadRounds(gameRoundList);
                                    mapController.setGameRoom(gameRoom);
                                    mapController.setUserCreateRoom(userCreateRoom);
                                    mapController.setUserJoinRoom(userJoinRoom);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

//                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                                Scene gameScene = new Scene(root);
                                stage.setScene(gameScene);
                                stage.show();
                            });
                        }
                        else if(statusResponse.equals(Status.CHOOSE_ROUND_SUCCESS)) {
                            List<Object> list;

                            if (responseFromServer.getDataToClient() instanceof List<?>) {
                                list = (List<Object>) responseFromServer.getDataToClient();
                                GameSession gameSession = (GameSession) list.get(0);
                                User userCreateRoom = (User) list.get(1);
                                User userJoinRoom = (User) list.get(2);

                                Platform.runLater(() -> {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/room-view.fxml"));
                                    Parent root = null;
                                    try {
                                        root = loader.load();
//                                        RoomController roomController = loader.getController();
                                        roomController.setClient(Client.this);
                                        roomController.updateUIUser2(userJoinRoom);
                                        roomController.updateUIUser1(userCreateRoom);
//                                        roomController.updateUIJoin(friendList, gameSession.getRoom());
                                        roomController.setUserCreateRoom(userCreateRoom);
                                        roomController.setUserJoinRoom(userJoinRoom);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    Scene gameScene = new Scene(root);
                                    stage.setScene(gameScene);
                                    stage.show();
                                });
                            }
                        }
                        else if(statusResponse.equals(Status.START_GAME_SUCCESS)){
                            System.out.println("START_GAME_SUCCESS");
                            GameSession gameSession;
                            User userCreateRoom;
                            User userJoinRoom;
                            GameSessionPlayer gameSessionPlayerCreate;
                            GameSessionPlayer gameSessionPlayerJoin;
                            List<TrashItem> trashItemList = new ArrayList<>();

                            if (responseFromServer.getDataToClient() instanceof List) {
                                List<?> outerList = (List<?>) responseFromServer.getDataToClient();
                                System.out.println("OuterList Sz:" + outerList.size());
                                List<Object> data = (List<Object>) responseFromServer.getDataToClient();

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

                                System.out.println("CreateRoom: ");
                                System.out.println(userCreateRoom.getUsername());
                                System.out.println("JoinRoom: ");
                                System.out.println(userJoinRoom.getUsername());
                                System.out.println(gameSession.getRound());

                                System.out.println("TrashItem");
                                for (TrashItem trashItem : trashItemList){
                                    System.out.println(trashItem);
                                }

                                if(gameSession.getRound().getId() == 1){
                                    Platform.runLater(() -> {
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/game-round-map1.fxml"));
                                        Parent root = null;
                                        try {
                                            root = loader.load();
                                            startGameController = loader.getController();
//                                        startGameController.initialize();

                                            startGameController.setClient(Client.this);
                                            startGameController.setUserCreateRoom(userCreateRoom);
                                            startGameController.setUserJoinRoom(userJoinRoom);
                                            startGameController.setSession(gameSession);
                                            startGameController.setGameSessionPlayer(gameSessionPlayerCreate);
                                            startGameController.setGameSessionPlayerJoin(gameSessionPlayerJoin);
                                            startGameController.setTrashItemList(trashItemList);
                                            startGameController.updateUIStartGame();
                                            if(userCreateRoom.getUsername().equals(user.getUsername())){
                                                data.add(userCreateRoom);
                                            }
                                            else {
                                                data.add(userJoinRoom);
                                            }
                                            startGameController.setDataToClient2(data);
                                            Scene gameScene = new Scene(root);
                                            startGameController.setGameScene(gameScene);
                                            stage.setScene(gameScene);
                                            stage.show();

                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                                }
                                else if(gameSession.getRound().getId() == 2){
                                    Platform.runLater(() -> {
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/game-round-map2.fxml"));
                                        Parent root = null;
                                        try {
                                            root = loader.load();
                                            startGame2Controller = loader.getController();
                                            startGame2Controller.setClient(Client.this);
                                            startGame2Controller.setUserCreateRoom(userCreateRoom);
                                            startGame2Controller.setUserJoinRoom(userJoinRoom);
                                            startGame2Controller.setSession(gameSession);
                                            startGame2Controller.setGameSessionPlayer(gameSessionPlayerCreate);
                                            startGame2Controller.setGameSessionPlayerJoin(gameSessionPlayerJoin);
                                            startGame2Controller.setTrashItemList(trashItemList);
                                            startGame2Controller.updateUIStartGame();
                                            if(userCreateRoom.getUsername().equals(user.getUsername())){
//                                            startGameController.setThisUser(userCreateRoom);
                                                data.add(userCreateRoom);
                                            }
                                            else {
//                                            startGameController.setThisUser(userJoinRoom);
                                                data.add(userJoinRoom);
                                            }

                                            startGame2Controller.setDataToClient2(data);

//                                        startGameController.initialize();


                                            Scene gameScene = new Scene(root);
                                            startGame2Controller.setGameScene(gameScene);
                                            stage.setScene(gameScene);
                                            stage.show();

                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
//                                    Scene gameScene = new Scene(root);
//                                    stage.setScene(gameScene);
//                                    stage.show();
                                    });
                                }
//
//
//                                Platform.runLater(() -> {
//                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/game-round-map1.fxml"));
//                                    Parent root = null;
//                                    try {
//                                        root = loader.load();
//                                        startGame2Controller = loader.getController();
////                                        startGameController.initialize();
//
//                                        startGame2Controller.setClient(Client.this);
//                                        startGame2Controller.setUserCreateRoom(userCreateRoom);
//                                        startGame2Controller.setUserJoinRoom(userJoinRoom);
//                                        startGame2Controller.setSession(gameSession);
//                                        startGame2Controller.setGameSessionPlayer(gameSessionPlayerCreate);
//                                        startGame2Controller.setGameSessionPlayerJoin(gameSessionPlayerJoin);
//                                        startGame2Controller.setTrashItemList(trashItemList);
//                                        startGame2Controller.updateUIStartGame();
//                                        if(userCreateRoom.getUsername().equals(user.getUsername())){
////                                            startGameController.setThisUser(userCreateRoom);
//                                            data.add(userCreateRoom);
//                                        }
//                                        else {
////                                            startGameController.setThisUser(userJoinRoom);
//                                            data.add(userJoinRoom);
//                                        }
//
//                                        startGame2Controller.setDataToClient2(data);
//
////                                        startGameController.initialize();
//
//
//                                        Scene gameScene = new Scene(root);
//                                        startGame2Controller.setGameScene(gameScene);
//                                        stage.setScene(gameScene);
//                                        stage.show();
//
//                                    } catch (IOException e) {
//                                        throw new RuntimeException(e);
//                                    }
////                                    Scene gameScene = new Scene(root);
////                                    stage.setScene(gameScene);
////                                    stage.show();
//                                });
                            } else {
                                gameSessionPlayerJoin = null;
                                gameSessionPlayerCreate = null;
                                gameSession = null;
                                userJoinRoom = null;
                                userCreateRoom = null;
                            }
                        }
                        else if(statusResponse.equals(Status.START_GAME_UPDATE_CLIENT_2_SUCCESS)){
                            System.out.println("START_GAME_UPDATE_CLIENT_2_SUCCESS");
                            GameSession gameSession;
                            User userCreateRoom;
                            User userJoinRoom;
                            GameSessionPlayer gameSessionPlayerCreate;
                            GameSessionPlayer gameSessionPlayerJoin;
                            List<TrashItem> trashItemList = new ArrayList<>();
                            User thisUser;
                            int score1;
                            int score2;

                            if (responseFromServer.getDataToClient() instanceof List) {
                                List<?> outerList = (List<?>) responseFromServer.getDataToClient();
                                System.out.println("OuterList Sz:" + outerList.size());

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
                                Object score = outerList.get(7);
                                if (score instanceof Integer) {
                                    score1 = (Integer) score;
                                } else {
                                    score1 = 0;
                                }
                                Object scoree = outerList.get(8);
                                if (scoree instanceof Integer) {
                                    score2 = (Integer) scoree;
                                } else {
                                    score2 = 0;
                                }

                                System.out.println("SCORE1 FROM SERVER: " + score1);
                                System.out.println("SCORE2 FROM SERVER: " + score2);

                                Platform.runLater(() -> {
                                    System.out.println("HELLLLiiiiiii");
                                    if(thisUser.getUsername().equals(userCreateRoom.getUsername())){
                                        System.out.println("HELLLLiiiiiii111111");
                                        System.out.println("UPDATE UI SCORE OTHER (2):" + score2);
                                        startGame2Controller.setScoreUser2(score2);
                                        startGame2Controller.updatePointUser2(score2);

                                    }
                                    else if(thisUser.getUsername().equals(userJoinRoom.getUsername())){
                                        System.out.println("HELLLLiiiiiii222222");
                                        System.out.println("UPDATE UI SCORE OTHER (1):" + score1);
                                        startGame2Controller.setScoreUser2(score1);
                                        startGame2Controller.updatePointUser2(score1);
                                    }
                                });
                            } else {
                                thisUser = null;
                                score1 = 0;
                                score2 = 0;
                                gameSessionPlayerJoin = null;
                                gameSessionPlayerCreate = null;
                                gameSession = null;
                                userJoinRoom = null;
                                userCreateRoom = null;
                            }
                        }

                        else if(statusResponse.equals(Status.UPDATE_SCORE_UI_SUCCESS)){
                            System.out.println("UPDATE_SCORE_UI_SUCCESS");
                            User userCreateRoom;
                            User userJoinRoom;
                            User thisUser;
                            int score;

                            if (responseFromServer.getDataToClient() instanceof List) {
                                List<?> outerList = (List<?>) responseFromServer.getDataToClient();

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
                                } else {
                                    score = 0;
                                }
                                System.out.println("SCORE FROM SERVER: " + score);
                                Platform.runLater(() -> {
                                    System.out.println("HELLLLiiiiiii");
                                    if(user.getUsername().equals(userCreateRoom.getUsername())){
                                        System.out.println("HELLLLiiiiiii111111");
                                        System.out.println("UPDATE UI SCORE OTHER (2):" + score);
                                        startGame2Controller.setScoreUser2(score);
                                        startGame2Controller.updatePointUser2(score);
                                    }
                                    else if(user.getUsername().equals(userJoinRoom.getUsername())){
                                        System.out.println("HELLLLiiiiiii222222");
                                        System.out.println("UPDATE UI SCORE OTHER (1):" + score);
                                        startGame2Controller.setScoreUser1(score);
                                        startGame2Controller.updatePointUser1(score);
                                    }
                                });
                            } else {
                                thisUser = null;
                                score = 0;
                                userJoinRoom = null;
                                userCreateRoom = null;
                            }
                        }
                        else if(statusResponse.equals(Status.START_GAME_UPDATE_CLIENT_2_SUCCESS)){
                            System.out.println("START_GAME_UPDATE_CLIENT_2_SUCCESS");
                            GameSession gameSession;
                            User userCreateRoom;
                            User userJoinRoom;
                            GameSessionPlayer gameSessionPlayerCreate;
                            GameSessionPlayer gameSessionPlayerJoin;
                            List<TrashItem> trashItemList = new ArrayList<>();
                            User thisUser;
                            int score1;
                            int score2;

                            if (responseFromServer.getDataToClient() instanceof List) {
                                List<?> outerList = (List<?>) responseFromServer.getDataToClient();
                                System.out.println("OuterList Sz:" + outerList.size());

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
                                Object score = outerList.get(7);
                                if (score instanceof Integer) {
                                    score1 = (Integer) score;
                                } else {
                                    score1 = 0;
                                }
                                Object scoree = outerList.get(8);
                                if (scoree instanceof Integer) {
                                    score2 = (Integer) scoree;
                                } else {
                                    score2 = 0;
                                }

                                System.out.println("SCORE1 FROM SERVER: " + score1);
                                System.out.println("SCORE2 FROM SERVER: " + score2);
//                                System.out.println("CreateRoom: ");
//                                System.out.println(userCreateRoom.getUsername());
//                                System.out.println("JoinRoom: ");
//                                System.out.println(userJoinRoom.getUsername());
//                                System.out.println(gameSession.getRound());

                                Platform.runLater(() -> {
                                    System.out.println("HELLLLiiiiiii");
                                    if(thisUser.getUsername().equals(userCreateRoom.getUsername())){
                                        System.out.println("HELLLLiiiiiii111111");
                                        System.out.println("UPDATE UI SCORE OTHER (2):" + score2);
                                        startGameController.setScoreUser2(score2);
                                        startGameController.updatePointUser2(score2);
                                    }
                                    else if(thisUser.getUsername().equals(userJoinRoom.getUsername())){
                                        System.out.println("HELLLLiiiiiii222222");
                                        System.out.println("UPDATE UI SCORE OTHER (1):" + score1);
                                        startGameController.setScoreUser2(score1);
                                        startGameController.updatePointUser2(score1);
                                    }

//                                    startGameController.setScoreUser1(score1);
//                                    startGameController.setScoreUser2(score2);
//                                    System.out.println("Score1 first: " + score1);
//                                    System.out.println("Score2 first: " + score2);
//                                    System.out.println("HELLLLiiiiiii");
//                                    if(thisUser.getUsername().equals(userCreateRoom.getUsername())){
//                                        System.out.println("HELLLLiiiiiii111111");
//                                        startGameController.updatePointUser1(score1);
//
//                                    }
//                                    else if(thisUser.getUsername().equals(userJoinRoom.getUsername())){
//                                        System.out.println("HELLLLiiiiiii222222");
//                                        startGameController.updatePointUser2(score2);
//                                    }
                                });
                            } else {
                                thisUser = null;
                                score1 = 0;
                                score2 = 0;
                                gameSessionPlayerJoin = null;
                                gameSessionPlayerCreate = null;
                                gameSession = null;
                                userJoinRoom = null;
                                userCreateRoom = null;
                            }
                        }
                        else if(statusResponse.equals(Status.UPDATE_SCORE_UI_1_SUCCESS)){
                            System.out.println("UPDATE_SCORE_UI_1_SUCCESS");
                            User userCreateRoom;
                            User userJoinRoom;
                            User thisUser;
                            int score;

                            if (responseFromServer.getDataToClient() instanceof List) {
                                List<?> outerList = (List<?>) responseFromServer.getDataToClient();

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
                                } else {
                                    score = 0;
                                }
                                System.out.println("SCORE FROM SERVER: " + score);
                                Platform.runLater(() -> {
                                    System.out.println("HELLLLiiiiiii");
                                    if(user.getUsername().equals(userCreateRoom.getUsername())){
                                        System.out.println("HELLLLiiiiiii111111");
                                        System.out.println("UPDATE UI SCORE OTHER (2):" + score);

                                        startGameController.setScoreUser2(score);
                                        startGameController.updatePointUser2(score);
                                    }
                                    else if(user.getUsername().equals(userJoinRoom.getUsername())){
                                        System.out.println("HELLLLiiiiiii222222");
                                        System.out.println("UPDATE UI SCORE OTHER (1):" + score);

                                        startGameController.setScoreUser1(score);
                                        startGameController.updatePointUser1(score);
                                    }
                                });
                            } else {
                                thisUser = null;
                                score = 0;
                                userJoinRoom = null;
                                userCreateRoom = null;
                            }
                        }
                        else if(statusResponse.equals(Status.UPDATE_TRASH_UI_SUCCESS)){
//                            System.out.println("UPDATE_TRASH_UI_SUCCESS");

                            User userCreateRoom = null;
                            User userJoinRoom = null;
                            User thisUser = null;
                            TrashItem trashItem;
                            int order;

                            if (responseFromServer.getDataToClient() instanceof List) {
                                List<?> outerList = (List<?>) responseFromServer.getDataToClient();

                                Object secondElement = outerList.get(0);
                                if (secondElement instanceof User) {
                                    userCreateRoom = (User) secondElement;
                                } else {
                                    userCreateRoom = new User();
                                }
//                                System.out.println("1: " + userCreateRoom);

                                Object thirdElement = outerList.get(1);
                                if (thirdElement instanceof User) {
                                    userJoinRoom = (User) thirdElement;
                                } else {
                                    userJoinRoom = new User();
                                }
//                                System.out.println("2: " + userJoinRoom);

                                Object seventh = outerList.get(2);
                                if (seventh instanceof User) {
                                    thisUser = (User) seventh;
                                } else {
                                    thisUser = new User();
                                }

                                Object nineElement = outerList.get(3);
                                if (nineElement instanceof Integer) {
                                    order = (Integer) nineElement;
                                } else {
                                    order = 0;
                                }

//                                System.out.println("5: " + thisUser);
                                Object eighthElement = outerList.get(4);
                                if (eighthElement instanceof TrashItem) {
                                    trashItem = (TrashItem) eighthElement;
                                } else {
                                    trashItem = null;
                                }
//                                System.out.println("NHAN YEU CAU UPDATE UI, USER NHAN:" + Client.this.getUser().getUsername());
                                Platform.runLater(() -> {

                                    if (trashItem != null){
                                        if(trashItem.getImg() != null){
                                            startGameController.setCurrentFallingTrash(trashItem);
                                            startGameController.setLayOut(trashItem);
                                        }
                                        else {
                                            System.out.println("DATA trashItem path null");
                                        }
                                    }
                                    else {
                                        System.out.println("DATA trashItem null");
                                    }
                                });
                            } else {
                                trashItem = null;
                            }
                        }
                        else if(statusResponse.equals(Status.REMOVE_TRASH_SUCCESS)){
                            System.out.println("REMOVE_TRASH_SUCCESS");

                            User userCreateRoom = null;
                            User userJoinRoom = null;
                            User thisUser = null;
                            TrashItem trashItem;
                            int order;

                            if (responseFromServer.getDataToClient() instanceof List) {
                                List<?> outerList = (List<?>) responseFromServer.getDataToClient();

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

                                Object nineElement = outerList.get(3);
                                if (nineElement instanceof Integer) {
                                    order = (Integer) nineElement;
                                } else {
                                    order = 0;
                                }

                                System.out.println("5: " + thisUser);
                                Object eighthElement = outerList.get(4);
                                if (eighthElement instanceof TrashItem) {
                                    trashItem = (TrashItem) eighthElement;
                                } else {
                                    trashItem = null;
                                }


                                System.out.println("NHAN YEU CAU XOA RAC, USER NHAN:" + Client.this.getUser().getUsername());
                                Platform.runLater(() -> {

                                    if (trashItem != null){
                                        if(trashItem.getImg() != null){
                                            startGameController.removeTrashFromPane();
                                            startGameController.setCurrentFallingTrash(trashItem);
                                            startGameController.setLayOut(trashItem);
//                                            startGameController.removeTrashFromPane(String.valueOf(trashItem.getId()));
                                            startGameController.setOrder(order);
                                            startGameController.spawnNextTrash();
                                        }
                                        else {
                                            System.out.println("DATA trashItem path null");
                                        }
                                    }
                                    else {
                                        System.out.println("DATA trashItem null");
                                    }
                                });
                            } else {
                                trashItem = null;
                            }
                        }
                        else if(statusResponse.equals(Status.REMOVE_TRASH_FAIL)){
                            System.out.println("REMOVE_TRASH_FAIL");

                        }
                        else if(statusResponse.equals(Status.UPDATE_COLOR_UI_SUCCESS)){
                            User userCreateRoom = null;
                            User userJoinRoom = null;
                            User thisUser = null;

                            TrashItem trashItem;
                            String colorId;
                            int order;

                            if (responseFromServer.getDataToClient() instanceof List) {
                                List<?> outerList = (List<?>) responseFromServer.getDataToClient();

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
                                startGameController.updateColorUIById(colorId);
                            }
                        }
                        else if(statusResponse.equals(Status.SHOW_SCORE_USER_SUCCESS)){
                            System.out.println("SHOW_SCORE_USER_SUCCESS");
                            GameSession session;
                            GameSessionPlayer gameSessionPlayerCreate;
                            GameSessionPlayer gameSessionPlayerJoin;
                            int scoreMaxRoundUserCreate;
                            int scoreMaxRoundUserJoin;

                            if (responseFromServer.getDataToClient() instanceof List) {
                                List<?> outerList = (List<?>) responseFromServer.getDataToClient();
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

                                Object forthElement = outerList.get(3);
                                if (forthElement instanceof Integer) {
                                    scoreMaxRoundUserCreate = (Integer) forthElement;
                                } else {
                                    scoreMaxRoundUserCreate = 0;
                                }
                                System.out.println("4: " + scoreMaxRoundUserCreate);

                                Object fifthElement = outerList.get(3);
                                if (fifthElement instanceof Integer) {
                                    scoreMaxRoundUserJoin = (Integer) fifthElement;
                                } else {
                                    scoreMaxRoundUserJoin = 0;
                                }
                                System.out.println("5: " + scoreMaxRoundUserJoin);
                                Platform.runLater(() -> {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/match-result-view-tmp.fxml"));
                                    Parent root = null;
                                    try {
                                        root = loader.load();
                                        resultController = loader.getController();
                                        resultController.setClient(Client.this);
                                        resultController.setSession(session);
                                        resultController.setThisUser(Client.this.user);
                                        resultController.setGameSessionPlayer(gameSessionPlayerCreate);
                                        resultController.setGameSessionPlayerJoin(gameSessionPlayerJoin);
                                        resultController.setMinuteContinue(5);
                                        resultController.initial();
                                        System.out.println("SESSION: " + session);
                                        System.out.println("ROUND: " + session.getRound());
                                        resultController.showScore(session, gameSessionPlayerCreate, gameSessionPlayerJoin, scoreMaxRoundUserCreate, scoreMaxRoundUserJoin);
                                        Scene gameScene = new Scene(root);
                                        stage.setScene(gameScene);
                                        stage.show();

                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    });
                            } else {
                                scoreMaxRoundUserJoin = 0;
                                scoreMaxRoundUserCreate = 0;
                                gameSessionPlayerJoin = null;
                                gameSessionPlayerCreate = null;
                                session = null;
                            }
                        }else if(statusResponse.equals(Status.SHOW_SCORE_USER_FAIL)) {
                            System.out.println("SHOW_SCORE_USER_FAIL");
                        }
                        else if(statusResponse.equals(Status.GET_LIST_HISTORY_SESSION_SUCCESS)){
                            List<HistoryRecord> historyRecordList;
                            if (responseFromServer.getDataToClient() instanceof List) {
                                historyRecordList = (List<HistoryRecord>) responseFromServer.getDataToClient();
                            } else {
                                historyRecordList = new ArrayList<>();
                            }


                            Platform.runLater(() -> {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/match-history.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                    historyController = loader.getController();
                                    historyController.setHistoryData(historyRecordList);
                                    historyController.setClient(Client.this);
                                    historyController.setThisUser(Client.this.user);

                                    Scene gameScene = new Scene(root);
                                    stage.setScene(gameScene);
                                    stage.show();

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                        else if(statusResponse.equals(Status.SHOW_USER_LIST_SUCCESS)){
                            List<User> userList;
                            if (responseFromServer.getDataToClient() instanceof List) {
                                userList = (List<User>) responseFromServer.getDataToClient();
                            } else {
                                userList = new ArrayList<>();
                            }


                            Platform.runLater(() -> {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gametrashcollecting/user-list.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                    userController = loader.getController();
                                    userController.setUserData(userList);
                                    userController.setClient(Client.this);
                                    userController.setThisUser(Client.this.user);

                                    Scene gameScene = new Scene(root);
                                    stage.setScene(gameScene);
                                    stage.show();

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                        else {
                            System.out.println("Case khac");
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
