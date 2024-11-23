package com.example.gametrashcollecting.server.dao;

import com.example.gametrashcollecting.model.*;
import com.example.gametrashcollecting.server.statusReponse.Status;
import com.example.gametrashcollecting.server.utils.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameRoomPlayerDAO {
    private  static DatabaseConnectionManager dbManager;
    private static final String SAVE_GAME_ROOM_PLAYER = "INSERT INTO GameRoomPlayer (userId, roomId, isActive) VALUES (?, ?, ?)";
    private static final String FIND_LIST_USER_OF_ROOM = "SELECT u.*\n" +
            "FROM GameRoomPlayer grp \n" +
            "JOIN User u ON grp.userId = u.userId\n" +
            "WHERE grp.roomId = ?";

    private static final String FIND_GAME_ROOM_BY_USER = "SELECT gr.*\n" +
            "FROM GameRoom gr \n" +
            "JOIN user u ON u.currentRoomId = gr.roomId\n" +
            "WHERE u.userId = ?";
    public GameRoomPlayerDAO() {
        dbManager = new DatabaseConnectionManager();
    }

//    public static boolean createGameRoomPlayer(GameRoom gameRoom, User user) throws SQLException {
//        Connection conn = dbManager.getConnection();
//        PreparedStatement ps = conn.prepareStatement(SAVE_GAME_ROOM_PLAYER);
//        ps.setString(1, String.valueOf(gameRoomPlayer.getUser().getId()));
//        ps.setString(2, String.valueOf(gameRoomPlayer.getRoom().getId()));
//        int rs = ps.executeUpdate();
//        return rs > 0;
//    }

    public static boolean saveGameRoomPlayer(GameRoomPlayer gameRoomPlayer) throws SQLException {
        Connection conn = dbManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(SAVE_GAME_ROOM_PLAYER);
        ps.setString(1, String.valueOf(gameRoomPlayer.getUser().getId()));
        ps.setString(2, String.valueOf(gameRoomPlayer.getRoom().getId()));
        ps.setBoolean(3, gameRoomPlayer.isActive());
        int rs = ps.executeUpdate();
        return rs > 0;
    }

    // Tim cac user trong 1 phong
    public List<User> findListUserByGameRoom(GameRoom gameRoom) throws SQLException {
        List<User> users = new ArrayList<>();
        Connection conn = dbManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(FIND_LIST_USER_OF_ROOM);
        ps.setInt(1, gameRoom.getId());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int userId = Integer.parseInt(rs.getString("userId"));
            String userName = rs.getString("username");
            String password = rs.getString("password");
            int totalPoints = rs.getInt("totalPoints");
            UserStatus userStatus = UserStatus.valueOf(rs.getString("status"));
            String lastLogin = rs.getString("lastLogin");
            int currentRoomId = rs.getInt("currentRoomId");
            String img = rs.getString("img");
            User user = new User(userId, userName, password, totalPoints, userStatus, lastLogin, currentRoomId, img);
            users.add(user);
        }
        return users;
    }

    // Tim phong cua 1 user
    public GameRoom findGameRoomByUser(User user) throws SQLException {
        Connection conn = dbManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(FIND_GAME_ROOM_BY_USER);
        ps.setInt(1, user.getId());
        ResultSet rs = ps.executeQuery();
        GameRoom gameRoom = null;
        while (rs.next()) {
            int roomId = rs.getInt("roomId");
            String roomName = rs.getString("roomName");
            int maxPlayer = rs.getInt("maxPlayer");
            int currentPlayer = rs.getInt("currentPlayer");
            RoomStatus status = RoomStatus.valueOf(rs.getString("status"));
            gameRoom = new GameRoom(roomId, roomName, maxPlayer, currentPlayer, status);
        }
        return gameRoom;
    }

}
