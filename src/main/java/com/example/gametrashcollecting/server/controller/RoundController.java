package com.example.gametrashcollecting.server.controller;

import com.example.gametrashcollecting.model.*;
import com.example.gametrashcollecting.server.dao.GameRoomDAO;
import com.example.gametrashcollecting.server.dao.GameRoundDAO;
import com.example.gametrashcollecting.server.statusReponse.ResponseStatus;
import com.example.gametrashcollecting.server.statusReponse.Status;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoundController {
    private GameRoundDAO gameRoundDAO;

    public RoundController(GameRoundDAO gameRoundDAO) {
        this.gameRoundDAO = gameRoundDAO;
    }

    public static ResponseStatus findAllRound(Object dataFromClient, Client client, Map<String, Client> clientMap) throws SQLException {
        User userCreateRoom = new User();
        User userJoinRoom = new User();
        GameRoom gameRoom = new GameRoom();
        if (dataFromClient instanceof List) {
            List<?> outerList = (List<?>) dataFromClient;
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
                userCreateRoom = new User();
            }
            Object thirdElement = outerList.get(2);
            if (thirdElement instanceof GameRoom) {
                gameRoom = (GameRoom) thirdElement;
            } else {
                gameRoom = new GameRoom();
            }
        }

        System.out.println(userCreateRoom);
        System.out.println(userJoinRoom);
        System.out.println(gameRoom);

        List<Object> list;
        if (dataFromClient instanceof List<?>) {
            list = (List<Object>) dataFromClient;
        } else {
            list = new ArrayList<>();
        }

        GameRoundDAO gameRoomDAO = new GameRoundDAO();
        List<GameRound> gameRoundList = gameRoomDAO.findAllGameRound();
        for (GameRound gameRound : gameRoundList) {
            System.out.println(gameRound);
        }
        list.add(gameRoundList);
        if(gameRoundList != null) {
//            return new ResponseStatus(Status.FIND_ALL_ROUND_SUCCESS, gameRoundList);
            return new ResponseStatus(Status.FIND_ALL_ROUND_SUCCESS, list);
        }
        else return new ResponseStatus(Status.FIND_ALL_ROUND_FAIL, "Find all round failed");
    }

    public static ResponseStatus chooseMap(Object dataFromClient, Client client, Map<String, Client> clientMap) throws SQLException {
        return new ResponseStatus(Status.CHOOSE_ROUND_SUCCESS, dataFromClient);

    }
}

