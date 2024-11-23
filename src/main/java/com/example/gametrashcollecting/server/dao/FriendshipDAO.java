package com.example.gametrashcollecting.server.dao;

import com.example.gametrashcollecting.model.User;
import com.example.gametrashcollecting.model.UserStatus;
import com.example.gametrashcollecting.server.utils.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendshipDAO {
    private DatabaseConnectionManager dbManager;
    private static final String GET_FRIENDS_BY_USER =
            "SELECT u.* FROM user u JOIN friendship f ON u.userId = f.user2Id WHERE f.user1Id = ? AND f.status = ?";
    private static final String GET_USER_BY_ID = "SELECT * FROM user WHERE userId = ?";
    private static final String UPDATE_FRIEND_STATUS = "UPDATE friendship SET status = ? WHERE user1Id = ? AND user2Id = ?";
//    private static final String GET_USERS_BY_USER_AND_STATUS = "SELECT * FROM user WHERE user1Id = ? AND user2Id = ?";


    public FriendshipDAO() {
        dbManager = new DatabaseConnectionManager();
    }

    public List<User> findFriendOfUser(User user) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement(GET_FRIENDS_BY_USER);
        ps.setInt(1, user.getId());
        ps.setInt(2, 1);
        ResultSet rs = ps.executeQuery();

        List<User> friends = new ArrayList<>();
        while (rs.next()) {
            int userId = rs.getInt("userId");
            String username = rs.getString("userName");
            String password = rs.getString("password");
            int totalPoints = rs.getInt("totalPoints");
            UserStatus userStatus = UserStatus.valueOf(rs.getString("status"));
            String lastLogin = rs.getString("lastLogin");
            int currentRoomId = rs.getInt("currentRoomId");
            String img = rs.getString("img");
            User friend = new User(userId, username, password, totalPoints, userStatus, lastLogin, currentRoomId, img);
            friends.add(friend);
        }
        return friends;
    }

    public void addFriend(User user1, User user2) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement(UPDATE_FRIEND_STATUS);
        ps.setString(1, String.valueOf(1));
        ps.setString(2, String.valueOf(user1.getId()));
        ps.setString(3, String.valueOf(user2.getId()));
        ps.executeUpdate();
    }



//    public void unfriend(User user1, User user2) throws SQLException {
//        Connection connection = dbManager.getConnection();
//        PreparedStatement ps = connection.prepareStatement(UPDATE_FRIEND_STATUS);
//        ps.setString(1, String.valueOf(0));
//        ps.setString(2, String.valueOf(user1.getId()));
//        ps.setString(3, String.valueOf(user2.getId()));
//        ps.executeUpdate();
//    }

    public void unfriend(User user1, User user2) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement(UPDATE_FRIEND_STATUS);
        ps.setString(1, String.valueOf(0));
        ps.setString(2, String.valueOf(user1.getId()));
        ps.setString(3, String.valueOf(user2.getId()));
        ps.executeUpdate();
    }
}
