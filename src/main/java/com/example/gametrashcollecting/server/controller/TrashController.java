package com.example.gametrashcollecting.server.controller;

import com.example.gametrashcollecting.model.*;
import com.example.gametrashcollecting.server.dao.RoundTrashItemDAO;
import com.example.gametrashcollecting.server.statusReponse.ResponseStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TrashController {
    private RoundTrashItemDAO roundTrashItemDAO;

    public TrashController(RoundTrashItemDAO roundTrashItemDAO) {
        this.roundTrashItemDAO = roundTrashItemDAO;
    }

//    public static ResponseStatus findListTrashByRound(Object dataFromClient, Client client, Map<String, Client> clientMap) throws SQLException {
//        // chua xu li object
//        GameRound gameRound = (GameRound) dataFromClient;
//        RoundTrashItemDAO roundTrashItemDAO = new RoundTrashItemDAO();
//        List<TrashItem> trashItemList = roundTrashItemDAO.findListTrashByRound(gameRound);
//        return
//    }
}
