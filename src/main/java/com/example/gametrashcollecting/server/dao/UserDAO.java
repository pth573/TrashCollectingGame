package com.example.gametrashcollecting.server.dao;

import com.example.gametrashcollecting.model.User;
import com.example.gametrashcollecting.model.UserStatus;
import com.example.gametrashcollecting.server.utils.DatabaseConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static DatabaseConnectionManager dbManager;
    private static final String GET_USER_BY_USERNAME_AND_PASSWORD = "SELECT * FROM user WHERE username = ? AND password = ?";
    private static final String GET_USERNAME_BY_USER = "SELECT username FROM user";
    private static final String UPDATE_USER_STATUS = "UPDATE user SET status = ? WHERE id = ?";
    private static final String UPDATE_USER_CURRENT_ROOM = "UPDATE user SET currentRoomId = ? WHERE userId = ?";
    private static final String UPDATE_USER_TOTAL_POINT = "UPDATE user SET totalPoints = ? WHERE userId = ?";
    private static final String FIND_ALL_USERS = "SELECT * FROM user";
    private static final String SAVE_USER = "INSERT INTO user (username, password, status, totalPoints, lastLogin) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_USER = "DELETE FROM user WHERE id = ?";
    private static final String GET_USER_BY_ID = "SELECT * FROM user WHERE userId = ?";
    private static final String ADD_ACCOUNT_USER= "INSERT INTO user (username, password) VALUES (?,?)";
    private static final String GET_USERNAME_BY_USERNAME = "SELECT username FROM user WHERE username = ?";
    private static final String GET_USER_BY_USERNAME = "SELECT * FROM user WHERE username = ?";
//    private static final String UPDATE_USER = "UPDATE user\n" +
//            "SET username = ?, password = ?, totalPoints = ?, status = ?, lastLogin = ?, currentRoomId = ?\n" +
//            "WHERE userId = ?";
//    private static final String UPDATE_USER_WITHOUT_CURRENT_ROOM = "UPDATE user\n" +
//        "SET username = ?, password = ?, totalPoints = ?, status = ?, lastLogin = ?\n" +
//        "WHERE userId = ?";
    private static final String UPDATE_USER_WITHOUT_CURRENT_ROOM = "UPDATE user\n" +
            "SET username = ?, password = ?, totalPoints = ?, status = ?, lastLogin = ?, img = ?\n" +
            "WHERE userId = ?";
    private static final String GET_TOP_3_USER = "SELECT * FROM user ORDER BY totalPoints DESC LIMIT 3";



    public UserDAO() {
        dbManager = new DatabaseConnectionManager();
    }


    public static User getUserById(int userId) throws SQLException {
        Connection conn = dbManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(GET_USER_BY_ID);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        User user = new User();
        while (rs.next()) {
            user.setId(rs.getInt("userId"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setTotalPoints(rs.getInt("totalPoints"));
            user.setStatus(UserStatus.valueOf(rs.getString("status")));
            user.setLastLogin(rs.getString("lastLogin"));
            user.setCurrentRoomId(rs.getInt("userId"));
            user.setImg(rs.getString("img"));
        }
        return user;
    }

    public static User getUserByUsername(String username) throws SQLException {
        Connection conn = dbManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(GET_USER_BY_USERNAME);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        User user = new User();
        while (rs.next()) {
            user.setId(rs.getInt("userId"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setTotalPoints(rs.getInt("totalPoints"));
            user.setStatus(UserStatus.valueOf(rs.getString("status")));
            user.setLastLogin(rs.getString("lastLogin"));
            user.setCurrentRoomId(rs.getInt("userId"));
            user.setImg(rs.getString("img"));
        }
        return user;
    }

//
//    public static void updateUserRoom(int userId, int roomId) throws SQLException {
//        Connection conn = dbManager.getConnection();
//        PreparedStatement ps = conn.prepareStatement(UPDATE_USER_CURRENT_ROOM);
//        ps.setInt(1, roomId);
//        ps.setInt(2, userId);
//        ps.executeUpdate();
//    }

    public static void updateUserRoom(int userId, Integer roomId) throws SQLException {
        Connection conn = dbManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(UPDATE_USER_CURRENT_ROOM);

        // Kiểm tra roomId có phải null không
        if (roomId == null) {
            ps.setNull(1, java.sql.Types.INTEGER);
        } else {
            ps.setInt(1, roomId);
        }

        ps.setInt(2, userId);
        ps.executeUpdate();
    }


    public static void updateUserTotalPoint(int userId, int totalPoint) throws SQLException {
        Connection conn = dbManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(UPDATE_USER_TOTAL_POINT);
        ps.setInt(1, totalPoint);
        ps.setInt(2, userId);
        ps.executeUpdate();
    }


    public List<User> getTop3UserPoint() throws SQLException {
        Connection conn = dbManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(GET_TOP_3_USER);
        ResultSet rs = ps.executeQuery();
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setTotalPoints(rs.getInt("totalPoints"));
            user.setStatus(UserStatus.valueOf(rs.getString("status")));
            user.setLastLogin(rs.getString("lastLogin"));
            user.setCurrentRoomId(rs.getInt("userId"));
            users.add(user);
        }
        return users;
    }

    public List<User> findAll() throws SQLException {
        Connection conn = dbManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(FIND_ALL_USERS);
        ResultSet rs = ps.executeQuery();
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("userId"));
            user.setUsername(rs.getString("username"));
//            user.setPassword(rs.getString("password"));
            user.setTotalPoints(rs.getInt("totalPoints"));
            user.setStatus(UserStatus.valueOf(rs.getString("status")));
            user.setLastLogin(rs.getString("lastLogin"));
            user.setCurrentRoomId(rs.getInt("currentRoomId"));
            users.add(user);
        }
        return users;
    }

    public User checkLogin(User user) throws SQLException {
        String username = user.getUsername();
        String password = user.getPassword();
        Connection conn = dbManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(GET_USER_BY_USERNAME_AND_PASSWORD);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        User userDB = null;
        while (rs.next()) {
            int id = rs.getInt("userId");
            String usernameDB = rs.getString("username");
            String passwordDB = rs.getString("password");
            int totalPointsDB = rs.getInt("totalPoints");
            UserStatus statusDB = UserStatus.OFFLINE;
            if(rs.getString("status") != null){
                statusDB = UserStatus.valueOf(rs.getString("status").toUpperCase());
            }
            String lastLoginDB = rs.getString("lastLogin");
            int currentRoomIdDB = rs.getInt("currentRoomId");
            String img = rs.getString("img");
            userDB = new User(id, usernameDB, passwordDB, totalPointsDB, statusDB, lastLoginDB, currentRoomIdDB, img);

            System.out.println(id + " " + usernameDB + " " + passwordDB + " " + totalPointsDB + " " + statusDB + " " + lastLoginDB);
        }
        return userDB;
    }

    public void updateUser(User user) throws SQLException {
        Connection conn = dbManager.getConnection();
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = currentDateTime.format(formatter);
        PreparedStatement ps = conn.prepareStatement(UPDATE_USER_WITHOUT_CURRENT_ROOM);
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ps.setInt(3, user.getTotalPoints());
        ps.setString(4, String.valueOf(UserStatus.ONLINE));
        ps.setString(5, currentTime);
        ps.setString(6, user.getImg());
        ps.setString(7, String.valueOf(user.getId()));
        int rs = ps.executeUpdate();
    }

    public void updateUserStatus(User user) throws SQLException {
        Connection conn = dbManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(UPDATE_USER_STATUS);
        ps.setString(1, UserStatus.ONLINE.toString());
    }

    public void insertUser(User user) throws SQLException {}

    private ArrayList<String> getUsers() throws SQLException {
        Connection conn = dbManager.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(GET_USERNAME_BY_USER);

        ArrayList<String> users = new ArrayList<>();
        while (rs.next()) {
            users.add(rs.getString("username"));
        }
        return users;
    }

    public boolean addUser(User user) throws SQLException {
        Connection conn = dbManager.getConnection();
        PreparedStatement psFindUser = conn.prepareStatement(GET_USERNAME_BY_USERNAME);
        psFindUser.setString(1, user.getUsername());
        ResultSet rsFindUser = psFindUser.executeQuery();
        if (!rsFindUser.next()) {
            PreparedStatement ps = conn.prepareStatement(ADD_ACCOUNT_USER);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.executeUpdate();
            return true;
        }
        else {
            return false;
        }

    }

}
