package com.example.gametrashcollecting.server.controller;

import com.example.gametrashcollecting.model.Client;
import com.example.gametrashcollecting.model.User;
import com.example.gametrashcollecting.model.UserStatus;
import com.example.gametrashcollecting.server.dao.FriendshipDAO;
import com.example.gametrashcollecting.server.dao.UserDAO;
import com.example.gametrashcollecting.server.statusReponse.ResponseStatus;
import com.example.gametrashcollecting.server.statusReponse.Status;
import com.example.gametrashcollecting.server.utils.ClientManager;
import com.example.gametrashcollecting.server.utils.DatabaseConnectionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserController {
    private static UserDAO userDAO;
    private static FriendshipDAO friendshipDAO;
//    private static DatabaseConnectionManager dbManager;

    public UserController(UserDAO userDAO, FriendshipDAO friendshipDAO) {
        this.userDAO = userDAO;
        this.friendshipDAO = friendshipDAO;
    }

    // Login
    public static ResponseStatus loginAccount(Object dataFromClient, Client client, Map<String, Client> clientMap) throws SQLException {
        Map<String, String> userInfo = (Map<String, String>) dataFromClient;
        String username = userInfo.get("username");
        String password = userInfo.get("password");
        User userFromClient = new User(username, password);
        System.out.println(username + " " + password);
        UserDAO userDAO = new UserDAO();
        User user = userDAO.checkLogin(userFromClient);
        if (user != null) {
            List<Object> dataList = new ArrayList<>();
            dataList.add(user);

            FriendshipDAO friendshipDAO = new FriendshipDAO();
            List<User> friendList = friendshipDAO.findFriendOfUser(user);
            dataList.add(friendList);

            List<User> userTop3Point = userDAO.getTop3UserPoint();
            dataList.add(userTop3Point);

            System.out.println("User logged in");
            client.setUser(user);
            ClientManager.getInstance().addClient(user.getUsername(), client);
            userDAO.updateUser(user);
            return new ResponseStatus(Status.LOGIN_SUCCESS, dataList);
        }
        else{
            System.out.println("User is null");
            return new ResponseStatus(Status.LOGIN_FAIL, "User dont exist");
        }
    }

    // Register
    public static ResponseStatus registerAccount(Object dataFromClient, Client client, Map<String, Client> clientMap) throws SQLException {
        Map<String, String> userInfo = (Map<String, String>) dataFromClient;
        String username = userInfo.get("username");
        String password = userInfo.get("password");
        User userFromClient = new User(username, password);
        UserDAO userDAO = new UserDAO();
        if (userDAO.addUser(userFromClient)) {
            System.out.println("User register successful");
            return new ResponseStatus(Status.REGISTER_SUCCESS, "User register successful");
        }
        else {
            System.out.println("User register failed");
            return new ResponseStatus(Status.REGISTER_FAIL, "User register successful");
        }
    }

    public static ResponseStatus getUserList(Object dataFromClient, Client client, Map<String, Client> clientMap) throws SQLException {
        User thisUser = new User();
        if(dataFromClient instanceof User) {
            thisUser = (User) dataFromClient;
        }
        UserDAO userDAO1 = new UserDAO();
        List<User> userList = userDAO1.findAll();
        return new ResponseStatus(Status.SHOW_USER_LIST_SUCCESS, userList);
    }
}
