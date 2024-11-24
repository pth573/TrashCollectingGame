package com.example.gametrashcollecting.server.handlers;

import com.example.gametrashcollecting.model.Client;
import com.example.gametrashcollecting.model.GameSession;
import com.example.gametrashcollecting.server.controller.*;
import com.example.gametrashcollecting.server.request.Request;
import com.example.gametrashcollecting.server.request.RequestStatus;
import com.example.gametrashcollecting.server.statusReponse.ResponseStatus;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Router {
    public ResponseStatus handle(Request request, Client client, Map<String, Client> clientMap) throws Exception {
        switch (request.getStatus()){
            case RequestStatus.LOGIN -> {
                System.out.println("Handling login request");
                return UserController.loginAccount(request.getDataFromClient(), client, clientMap);
            }
            case RequestStatus.REGISTER -> {
                System.out.println("Handling register request");
                return UserController.registerAccount(request.getDataFromClient(), client, clientMap);
            }
            case RequestStatus.LIST_FRIEND -> {
                System.out.println("Handling get list friend request");
                return FriendshipController.findFriendOfUser(request.getDataFromClient(), client, clientMap);
            }
            case RequestStatus.BACK_MAIN_SCREEN -> {
                System.out.println("Handling back main screen request");
                return RoomController.backRoom(request.getDataFromClient(), client, clientMap);
            }
            case RequestStatus.CREATEROOM -> {
                System.out.println("Handling create room request");
                return RoomController.createGameRoom(request.getDataFromClient(), client, clientMap);
            }
            case RequestStatus.FIND_ROOM -> {
                System.out.println("Hanling find room request");
                return RoomController.findGameRoomById(request.getDataFromClient(), client, clientMap);
            }
            case RequestStatus.FIND_ALL_ROOM -> {
                System.out.println("Hanling find all room request");
                return RoomController.findAllRoom(request.getDataFromClient(), client, clientMap);
            }
            case RequestStatus.JOIN_ROOM -> {
                System.out.println("Handling join room request");
                return RoomController.joinAndUpdateGameRoom(request.getDataFromClient(), client, clientMap);
            }
            case RequestStatus.OUT_ROOM -> {
                System.out.println("Handling out room request");
                return RoomController.outRoom(request.getDataFromClient(), client, clientMap);
            }
            case RequestStatus.CHOOSE_MAP -> {
                System.out.println("Handling choose map request");
                return RoundController.findAllRound(request.getDataFromClient(), client, clientMap);
            }
//            case RequestStatus.FIND_ALL_ROUND -> {
//                System.out.println("Handling find all request");
//                return RoundController.findAllRoom(request.getDataFromClient(), client, clientMap);
//            }
            case RequestStatus.START_GAME -> {
                System.out.println("Handling choose map request");
                return GameSessionController.startGame(request.getDataFromClient(), client, clientMap);
            }
            case RequestStatus.START_GAME_UPDATE_CLIENT_2 -> {
                System.out.println("Handling start game update client 2 request");
                return GameSessionController.updateUIPoint(request.getDataFromClient(), client, clientMap);
            }
            case RequestStatus.UPDATE_SCORE_UI -> {
                System.out.println("Handling update score UI request");
                return GameSessionController.updateScoreUI(request.getDataFromClient(), client, clientMap);
            }
            case RequestStatus.UPDATE_SCORE_UI_1 -> {
                System.out.println("Handling update score UI 1 request");
                return GameSessionController.updateScoreUI_1(request.getDataFromClient(), client, clientMap);
            }
            case RequestStatus.UPDATE_TRASH_UI -> {
//                System.out.println("Handing update trash UI request");
                return GameSessionController.updateTrashUI(request.getDataFromClient(), client, clientMap);
            }
            case RequestStatus.REMOVE_TRASH -> {
                System.out.println("Handling remove trash request");
                return GameSessionController.removeTrash(request.getDataFromClient(), client, clientMap);
            }
            case RequestStatus.UPDATE_COLOR_UI -> {
                System.out.println("Handling update color UI request");
                return GameSessionController.updateColorUI(request.getDataFromClient(), client, clientMap);
            }
            case RequestStatus.SHOW_SCORE_USER -> {
                System.out.println("Handling show score user request");
                return GameSessionController.updateSessionAndScoreAfterPlaying(request.getDataFromClient(), client, clientMap);
            }
            case RequestStatus.GET_LIST_HISTORY_SESSION -> {
                System.out.println("Handling get list history session request");
                return HistoryController.getListHistorySession(request.getDataFromClient(), client, clientMap);
            }
            case RequestStatus.SHOW_USER_LIST -> {
                System.out.println("Handling show user list");
                return UserController.getUserList(request.getDataFromClient(), client, clientMap);
            }
            default -> {
                System.out.println("Unknown request status");
                return null;
            }
        }
    }
}
