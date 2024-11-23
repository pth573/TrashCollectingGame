package com.example.gametrashcollecting.server.controller;

import com.example.gametrashcollecting.model.Client;
import com.example.gametrashcollecting.model.User;
import com.example.gametrashcollecting.server.dao.FriendshipDAO;
import com.example.gametrashcollecting.server.dao.UserDAO;
import com.example.gametrashcollecting.server.statusReponse.ResponseStatus;
import com.example.gametrashcollecting.server.statusReponse.Status;
import com.example.gametrashcollecting.server.utils.DatabaseConnectionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FriendshipController {
    private static FriendshipDAO friendshipDAO;
//    private static DatabaseConnectionManager dbManager;

    public FriendshipController(FriendshipDAO friendshipDAO) {
        this.friendshipDAO = friendshipDAO;
    }

    public static ResponseStatus findFriendOfUser(Object dataFromClient, Client client, Map<String, Client> clientMap) throws SQLException {
        if(dataFromClient instanceof User) {
            User user = (User) dataFromClient;
            System.out.println("Find friend of user:" + user.getUsername() + " " + user.getPassword());
            FriendshipDAO friendshipDAO = new FriendshipDAO();
            List<User> listFriendUser = friendshipDAO.findFriendOfUser(user);
            if(listFriendUser.size() > 0) {
                return new ResponseStatus(Status.FIND_FRIEND_OF_USER_SUCCESS, listFriendUser);
            }
            else {
                return new ResponseStatus(Status.FIND_FRIEND_OF_USER_FAIL, "User dont have any friend");
            }
        }
        else {
            return new ResponseStatus(Status.FIND_FRIEND_OF_USER_FAIL, "Data from client is not a User");
        }
    }

//    public ResponseStatus addFriendOfUser(Object dataFromClient, Client client) throws SQLException {
//        if(dataFromClient instanceof User) {
//            User user = (User) dataFromClient;
//            FriendshipDAO friendshipDAO = new FriendshipDAO();
//        }
//    }

}
