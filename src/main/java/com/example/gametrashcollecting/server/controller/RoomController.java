package com.example.gametrashcollecting.server.controller;

import com.example.gametrashcollecting.model.*;
import com.example.gametrashcollecting.server.dao.FriendshipDAO;
import com.example.gametrashcollecting.server.dao.GameRoomDAO;
import com.example.gametrashcollecting.server.dao.GameRoomPlayerDAO;
import com.example.gametrashcollecting.server.dao.UserDAO;
import com.example.gametrashcollecting.server.statusReponse.ResponseStatus;
import com.example.gametrashcollecting.server.statusReponse.Status;
import com.example.gametrashcollecting.server.utils.ClientManager;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomController {
    private static GameRoomDAO gameRoomDAO;
    private static UserDAO userDAO;
    private static GameRoomPlayerDAO gameRoomPlayerDAO;
    private static FriendshipDAO friendshipDAO;

    public RoomController(GameRoomDAO gameRoomDAO, GameRoomPlayerDAO gameRoomPlayerDAO, UserDAO userDAO, FriendshipDAO friendshipDAO) {
        this.gameRoomDAO = gameRoomDAO;
        this.userDAO = userDAO;
        this.gameRoomPlayerDAO = gameRoomPlayerDAO;
        this.friendshipDAO = friendshipDAO;
    }

    public static ResponseStatus backRoom(Object dataFromClient, Client client, Map<String, Client> clientMap) throws SQLException {
        User user = new User();
        if(dataFromClient instanceof User){
            user = (User) dataFromClient;
        }
        if (user != null) {
            List<Object> dataList = new ArrayList<>();
            dataList.add(user);

            FriendshipDAO friendshipDAO = new FriendshipDAO();
            List<User> friendList = friendshipDAO.findFriendOfUser(user);
            dataList.add(friendList);

            UserDAO userDAO = new UserDAO();
            List<User> userTop3Point = userDAO.getTop3UserPoint();
            dataList.add(userTop3Point);

            client.setUser(user);
            ClientManager.getInstance().addClient(user.getUsername(), client);
            userDAO.updateUser(user);

            return new ResponseStatus(Status.BACK_MAIN_SCREEN_SUCCESS, dataList);
//            return new ResponseStatus(Status.OUT_ROOM_SUCCESS, dataList);
        }
        return new ResponseStatus(Status.OTHER, "");
    }


    public static ResponseStatus outRoom(Object dataFromClient, Client client, Map<String, Client> clientMap) throws SQLException, IOException {
        List<User> userList = new ArrayList<>();
        if(dataFromClient instanceof List<?>){
            userList = (List<User>) dataFromClient;
        }

        User userCreateRoom = userList.get(0);
        User userJoinRoom = userList.get(1);

        if (userCreateRoom != null) {
            Client client1 = ClientManager.getInstance().getClient(userCreateRoom.getUsername());
            List<Object> dataList = new ArrayList<>();
            dataList.add(userCreateRoom);

            FriendshipDAO friendshipDAO = new FriendshipDAO();
            List<User> friendList = friendshipDAO.findFriendOfUser(userCreateRoom);
            dataList.add(friendList);

            UserDAO userDAO = new UserDAO();
            List<User> userTop3Point = userDAO.getTop3UserPoint();
            dataList.add(userTop3Point);

            client.setUser(userCreateRoom);
            ClientManager.getInstance().addClient(userCreateRoom.getUsername(), client);
            userDAO.updateUser(userCreateRoom);

            userDAO.updateUserRoom(userCreateRoom.getId(), null);

            client1.getOos().writeObject(new ResponseStatus(Status.OUT_ROOM_SUCCESS, dataList));
//            return new ResponseStatus(Status.OUT_ROOM_SUCCESS, dataList);
        }
        if (userJoinRoom != null) {
            Client client2 = ClientManager.getInstance().getClient(userJoinRoom.getUsername());
            List<Object> dataList = new ArrayList<>();
            dataList.add(userJoinRoom);

            FriendshipDAO friendshipDAO = new FriendshipDAO();
            List<User> friendList = friendshipDAO.findFriendOfUser(userJoinRoom);
            dataList.add(friendList);

            UserDAO userDAO = new UserDAO();
            List<User> userTop3Point = userDAO.getTop3UserPoint();
            dataList.add(userTop3Point);

            client.setUser(userJoinRoom);
            ClientManager.getInstance().addClient(userJoinRoom.getUsername(), client);
            userDAO.updateUser(userJoinRoom);
            userDAO.updateUserRoom(userJoinRoom.getId(), null);
            client2.getOos().writeObject(new ResponseStatus(Status.OUT_ROOM_SUCCESS, dataList));
        }
        return new ResponseStatus(Status.OTHER, "");
    }

    public static ResponseStatus createGameRoom(Object dataFromClient, Client client, Map<String, Client> clientMap) throws SQLException {
        GameRoomDAO gameRoomDAO = new GameRoomDAO();
        User user = null;
        GameRoom newRoom = null;

        if (dataFromClient instanceof List){
            List<?> outerList = (List<?>) dataFromClient;
            if(outerList.size() == 1){
                Object firstElement = outerList.get(0);
                if (firstElement instanceof User) {
                    user = (User) firstElement;
                } else {
                    user = new User();
                }
            }
            else if (outerList.size() == 2){
                Object firstElement = outerList.get(0);
                if (firstElement instanceof User) {
                    user = (User) firstElement;
                } else {
                    user = new User();
                }

                Object secondElement = outerList.get(1);
                if (secondElement instanceof GameRoom) {
                    newRoom = (GameRoom) secondElement;
                } else {
                    newRoom = new GameRoom();
                }
            }
        }

        if(dataFromClient instanceof User) {
            user = (User) dataFromClient;
        }
        if(newRoom == null) {
            newRoom = gameRoomDAO.createGameRoom();
        }
        User newUser = null;
        UserDAO userDAO = new UserDAO();
        newUser = userDAO.getUserById(user.getId());
        GameRoomPlayerDAO gameRoomPlayerDAO = new GameRoomPlayerDAO();
        GameRoomPlayer gameRoomPlayer = new GameRoomPlayer(newUser, newRoom, true);
        FriendshipDAO friendshipDAO = new FriendshipDAO();
        List<User> friendList = friendshipDAO.findFriendOfUser(newUser);

        userDAO.updateUserRoom(newUser.getId(), newRoom.getId());

        List<Object> list = new ArrayList<>();
        list.add(friendList);
        list.add(newRoom);
        System.out.println("New room:" + newRoom);

        if(newRoom != null && gameRoomPlayerDAO.saveGameRoomPlayer(gameRoomPlayer)) {
            return new ResponseStatus(Status.CREATE_ROOM_SUCCESS, list);
        }
        else return new ResponseStatus(Status.CREATE_ROOM_FAIL, "Create room failed");
    }

    public static ResponseStatus joinAndUpdateGameRoom(Object dataFromClient, Client client, Map<String, Client> clientMap) throws SQLException, IOException {
        Map<String, Integer> data = (Map<String, Integer>) dataFromClient;
        Integer idRoom = data.get("roomId");
        Integer idUser = data.get("userId");

        UserDAO userDAO = new UserDAO();
        userDAO.updateUserRoom(idUser, idRoom);

        User newUser = null;
//        UserDAO userDAO = new UserDAO();
        newUser = userDAO.getUserById(idUser);
        System.out.println("USER JOIN ROOM: " + newUser.getUsername());
        GameRoomDAO gameRoomDAO = new GameRoomDAO();
        GameRoom gameRoom = gameRoomDAO.findGameRoomById(idRoom);

        if(gameRoomDAO.joinAndUpdateGameRoom(gameRoom)){
            gameRoom = gameRoomDAO.findGameRoomById(idRoom);
            GameRoomPlayerDAO gameRoomPlayerDAO = new GameRoomPlayerDAO();
            GameRoomPlayer gameRoomPlayer = new GameRoomPlayer(newUser, gameRoom, true);
            gameRoomPlayerDAO.saveGameRoomPlayer(gameRoomPlayer);

            List<Object> list2 = new ArrayList<>();
            List<User> userList = gameRoomPlayerDAO.findListUserByGameRoom(gameRoom);
            FriendshipDAO friendshipDAO = new FriendshipDAO();
            List<User> friendList = friendshipDAO.findFriendOfUser(newUser);

            list2.add(friendList);
            list2.add(userList);
            list2.add(gameRoom);

            User userDB2 = userDAO.getUserById(idUser);
            User userDB1 = null;
            for (User user : userList){
                if(user.getId() != idUser){
                    userDB1 = user;
                }
            }
            List list1 = new ArrayList<>();
            System.out.println("User1: " + userDB1.getUsername());
            Client client1 = ClientManager.getInstance().getClient(userDB1.getUsername());
            System.out.println("User2: " + userDB2.getUsername());
            Client client2 = ClientManager.getInstance().getClient(userDB2.getUsername());

            List<User> friendList1 = friendshipDAO.findFriendOfUser(userDB1);

            // client1
            // ng tao phong
            // list1: thong tin ng t1
            list1.add(friendList1);
            list1.add(userList);
            list1.add(gameRoom);
            list1.add(userDB1);
            list1.add(userDB2); // DB2: them vao 1



            // client2
            // ng join phong
            // this user
            // list2: thong tin ng t2
            list2.add(userDB1);
            list2.add(userDB2);


            // update UI ng tao phong
            ResponseStatus responseStatus = new ResponseStatus(Status.UPDATE_ROOM_UI_SUCCESS, list2);
            client1.getOos().writeObject(responseStatus);
            client1.getOos().flush();

            return new ResponseStatus(Status.UPDATE_ROOM_SUCCESS, list1);
        } else
        {
            return new ResponseStatus(Status.UPDATE_ROOM_FAIL, "Update room failed");
        }
    }

    public static ResponseStatus findGameRoomById(Object dataFromClient, Client client, Map<String, Client> clientMap) throws SQLException {
        if(dataFromClient instanceof String) {
            String idRoom = (String) dataFromClient;
            GameRoomDAO gameRoomDAO = new GameRoomDAO();
            GameRoom gameRoom = gameRoomDAO.findGameRoomById(Integer.parseInt(idRoom));
            if(gameRoom != null) {
                return new ResponseStatus(Status.FIND_ROOM_SUCCESS, gameRoom);
            }
            else{
                return new ResponseStatus(Status.FIND_ROOM_FAIL, "Find room failed");
            }
        }
        else return new ResponseStatus(Status.FIND_ROOM_FAIL, "Find room failed");
    }

    public static ResponseStatus findAllRoom(Object dataFromClient, Client client, Map<String, Client> clientMap) throws SQLException {
        List<Object> data = new ArrayList<>();
        User user = null;
        if(dataFromClient instanceof User){
            user = (User) dataFromClient;
        }
        data.add(user);
        GameRoomDAO gameRoomDAO = new GameRoomDAO();
        List<GameRoom> gameRoomList = gameRoomDAO.findAllGameRoom();
        data.add(gameRoomList);
        if(gameRoomList != null) {
            return new ResponseStatus(Status.FIND_ALL_ROOM_SUCCESS, data);
        }
        else return new ResponseStatus(Status.FIND_ALL_ROOM_FAIL, "Find all room failed");
    }



}
