//package com.example.gametrashcollecting.server.controller;
//
//import com.example.gametrashcollecting.model.Client;
//import com.example.gametrashcollecting.model.GameRoom;
//import com.example.gametrashcollecting.server.dao.GameRoomDAO;
//import com.example.gametrashcollecting.server.dao.UserDAO;
//import com.example.gametrashcollecting.server.statusReponse.ResponseStatus;
//import com.example.gametrashcollecting.server.statusReponse.Status;
//
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Map;
//
//public class GameRoomController {
//    private GameRoomDAO gameRoomDAO;
//    private UserDAO userDAO;
//
//    public GameRoomController(GameRoomDAO gameRoomDAO, UserDAO userDAO, Map<String, Client> clientMap) {
//        this.gameRoomDAO = gameRoomDAO;
//        this.userDAO = userDAO;
//    }
//
//    public ResponseStatus createGameRoom(Object dataFromClient, Client client, Map<String, Client> clientMap) throws SQLException {
//        GameRoomDAO gameRoomDAO = new GameRoomDAO();
//        GameRoom gameRoom = gameRoomDAO.createGameRoom();
//        if(gameRoom != null) {
//            return new ResponseStatus(Status.CREATE_ROOM_SUCCESS, gameRoom);
//        }
//        else {
//            return new ResponseStatus(Status.CREATE_ROOM_FAIL, "Create room failed");
//        }
//    }
//
//    public ResponseStatus findGameRoomById(Object dataFromClient, Client client, Map<String, Client> clientMap) throws SQLException {
//        int idData = (int) dataFromClient;
//        GameRoomDAO gameRoomDAO = new GameRoomDAO();
//        GameRoom gameRoom = gameRoomDAO.findGameRoomById(idData);
//        if(gameRoom != null) {
//            return new ResponseStatus(Status.FIND_ROOM_SUCCESS, gameRoom);
//        }
//        else return new ResponseStatus(Status.FIND_ROOM_FAIL, "Find room failed");
//    }
//
//    public ResponseStatus joinAndUpdateGameRoom(Object dataFromClient, Client client, Map<String, Client> clientMap) throws SQLException {
//        GameRoomDAO gameRoomDAO = new GameRoomDAO();
//        String response = "FAIL UPDATE ROOM";
//        if(dataFromClient instanceof GameRoom) {
//            GameRoom gameRoom = (GameRoom) dataFromClient;
//            if(gameRoomDAO.joinAndUpdateGameRoom(gameRoom)){
//                return new ResponseStatus(Status.UPDATE_ROOM_SUCCESS, gameRoom);
//            }
//            else{
//                return new ResponseStatus(Status.UPDATE_ROOM_FAIL, "Update room failed");
//            }
//        }
//        return new ResponseStatus(Status.UPDATE_ROOM_FAIL, "Data is not a GameRoom");
//    }
//}
