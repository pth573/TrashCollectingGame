package com.example.gametrashcollecting.server.dao;

import com.example.gametrashcollecting.model.GameRoom;
import com.example.gametrashcollecting.model.GameRound;
import com.example.gametrashcollecting.model.GameSession;
import com.example.gametrashcollecting.model.RoomStatus;
import com.example.gametrashcollecting.server.utils.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameSessionDAO {
    private DatabaseConnectionManager dbManager;
    private static final String GET_GAME_SESSION_BY_ID = "SELECT * FROM gamesession WHERE sessionId = ?";
    private static final String GET_GAME_ROOM_BY_ID = "SELECT * FROM gameroom WHERE roomId = ?";
    private static final String DELETE_GAME_SESSION = "DELETE FROM gamesseion WHERE sessionId = ?";
    private static final String GET_AVAILABLE_GAME_ROOM = "SELECT * FROM gameroom WHERE status = 'WAITING'";
    private static final String CREATE_GAME_SESSION = "INSERT INTO gamesession (startTime, endTime, roundId, roomId)\n" +
            "VALUES (?,?,?, ?)";

//    private static final String CREATE_GAME_SESSION = "INSERT INTO gamesession (startTime, roundId, roomId)\n" +
//            "VALUES (?,?,?)";
    private static final String GET_GAME_ROUND_BY_ROOM_ID = "SELECT * FROM gameround WHERE roomId = ?";

    public GameSessionDAO(){
        dbManager = new DatabaseConnectionManager();
    }


    // Lay thong tin tran dau
    public GameSession findGameSessionById(GameSession gameSession) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement(GET_GAME_SESSION_BY_ID);
        ps.setInt(1,gameSession.getId());
        ResultSet rs = ps.executeQuery();

        if(rs.next()) {
            int roomId = rs.getInt("roomId");
            PreparedStatement ps1 = connection.prepareStatement(GET_GAME_ROOM_BY_ID);
            ps1.setInt(1, roomId);
            ResultSet rs1 = ps1.executeQuery();


            if(rs1.next()) {
                GameRoom gameRoom = new GameRoom();
                gameRoom.setId(rs1.getInt("roomId"));
                gameRoom.setRoomName(rs1.getString("roomName"));
                gameRoom.setMaxPlayer(rs1.getInt("maxPlayer"));
                gameRoom.setCurrentPlayer(rs1.getInt("currentPlayer"));
                RoomStatus statusDB = RoomStatus.valueOf(rs1.getString("status").toUpperCase());
                gameRoom.setStatus(statusDB);

                GameSession gameSession1 = new GameSession();
                gameSession1.setId(rs.getInt("sessionId"));
                gameSession1.setStartTime(rs.getString("startTime"));
                gameSession1.setEndTime(rs.getString("endTime"));
                gameSession1.setRoom(gameRoom);
                gameSession1.setRound(gameSession.getRound());

                return gameSession1;
            }
        }
        return null;

    }

    public GameSession updateGameSession(GameSession session) throws SQLException {
        Connection connection = dbManager.getConnection();
        String query = "UPDATE GameSession SET startTime = ?, endTime = ?, roundId = ?, roomId = ? WHERE sessionId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, session.getStartTime());
            preparedStatement.setString(2, session.getEndTime());
            preparedStatement.setInt(3, session.getRound().getId());  // Assuming `GameRound` has `getId()`
            preparedStatement.setInt(4, session.getRoom().getId());    // Assuming `GameRoom` has `getId()`
            preparedStatement.setInt(5, session.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                return session;
            } else {
                throw new SQLException("Updating session failed, no rows affected.");
            }
        }
    }




    public GameSession createGameSession(GameRound gameRound, GameRoom gameRoom) throws SQLException {

        System.out.println("GameRoom Id: " + gameRoom.getId());
        System.out.println("GameRound Id: " + gameRound.getId());
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement(CREATE_GAME_SESSION, PreparedStatement.RETURN_GENERATED_KEYS);
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        ps.setString(1, formattedTime);
        ps.setString(2, formattedTime);
        ps.setInt(3,gameRound.getId());
        ps.setInt(4, gameRoom.getId());
        int affectedRows = ps.executeUpdate();
        long generatedKey = -1;

        if (affectedRows > 0) {
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedKey = generatedKeys.getLong(1);
                }
            }
        }
        int id = Integer.parseInt(String.valueOf(generatedKey));
        return new GameSession(id, formattedTime, formattedTime, gameRound, gameRoom);
    }


//    public GameSession createGameSession() throws SQLException {
//        Connection connection = dbManager.getConnection();
//        PreparedStatement ps = connection.prepareStatement(GET_AVAILABLE_GAME_ROOM);
//        ResultSet rs = ps.executeQuery();
//        List<GameRoom> gameRooms = new ArrayList<>();
//        while(rs.next()) {
//            GameRoom gameRoom = new GameRoom();
//            gameRoom.setId(rs.getInt("roomId"));
//            gameRoom.setStatus(RoomStatus.WAITING);
//
//            int roomId = rs.getInt("roomId");
//            Date startTime = new Date();
//            Date endTime = new Date(startTime.getTime() + 1000 * 60 * 60);
//
//            PreparedStatement ps1 = connection.prepareStatement(CREATE_GAME_SESSION);
//            ps1.setString(1, String.valueOf(startTime.getTime()));
//            ps1.setString(2, String.valueOf(endTime.getTime()));
//            ps1.setInt(3,roomId);
//            ps1.setInt(4, );
//
//            int affectedRows = ps1.executeUpdate(); // Correct the query execution for CREATE_GAME_SESSION
//            long generatedKey = -1;
//
//            if(affectedRows > 0) {
//                ResultSet generatedKeys = ps1.getGeneratedKeys();
//                if(generatedKeys.next()) {
//                    generatedKey = generatedKeys.getLong(1);
//                }
//            }
//
//            int id = Integer.parseInt(String.valueOf(generatedKey));
//            return new GameSession(id,startTime,endTime,null,null,gameRoom);
//
//        }
//        return null;
//    }

    public GameSession deleteGameSessionById(int id) throws SQLException {
        Connection connection = dbManager.getConnection();
        GameSession gameSession = new GameSession();
        PreparedStatement ps = connection.prepareStatement(GET_GAME_SESSION_BY_ID);
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();

        if(rs.next()) {
            gameSession.setId(rs.getInt("sessionId"));
            gameSession.setStartTime(rs.getString("startTime"));
            gameSession.setEndTime(rs.getString("endTime"));
        }

        rs.close();
        ps.close();

        if(gameSession==null) {
            return null;
        }

        ps = connection.prepareStatement(DELETE_GAME_SESSION);
        ps.setInt(1,id);

        int rowsAffected = ps.executeUpdate();
        if(rowsAffected > 0) {
            return gameSession;
        } else {
            return null;
        }
    }
}