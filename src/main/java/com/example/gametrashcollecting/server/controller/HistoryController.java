package com.example.gametrashcollecting.server.controller;

import com.example.gametrashcollecting.client.model.HistoryRecord;
import com.example.gametrashcollecting.model.Client;
import com.example.gametrashcollecting.model.GameSession;
import com.example.gametrashcollecting.model.GameSessionPlayer;
import com.example.gametrashcollecting.model.User;
import com.example.gametrashcollecting.server.dao.FriendshipDAO;
import com.example.gametrashcollecting.server.dao.GameSessionDAO;
import com.example.gametrashcollecting.server.dao.GameSessionPlayerDAO;
import com.example.gametrashcollecting.server.statusReponse.ResponseStatus;
import com.example.gametrashcollecting.server.statusReponse.Status;
import com.example.gametrashcollecting.server.utils.DatabaseConnectionManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class HistoryController {

    private static GameSessionDAO gameSessionDAO;
    private static GameSessionPlayerDAO gameSessionPlayerDAO;
    private static DatabaseConnectionManager dbManager;
//    private static GameSessionDAO sessionDAO;


    public HistoryController(GameSessionDAO gameSessionDAO, GameSessionPlayerDAO gameSessionPlayerDAO) {
        this.gameSessionDAO = gameSessionDAO;
        this.gameSessionPlayerDAO = gameSessionPlayerDAO;
        dbManager = new DatabaseConnectionManager();
    }


    public static ResponseStatus getListHistorySession(Object dataFromClient, Client client, Map<String, Client> clientMap) throws SQLException {
        User thisUser = new User();
        if(dataFromClient instanceof User) {
            thisUser = (User) dataFromClient;
        }
        GameSessionPlayerDAO gameSessionPlayerDAO = new GameSessionPlayerDAO();
        List<HistoryRecord> historyRecordList = gameSessionPlayerDAO.getHistoryByUserId(thisUser.getId());
        return new ResponseStatus(Status.GET_LIST_HISTORY_SESSION_SUCCESS, historyRecordList);
    }
}
