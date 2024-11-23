package com.example.gametrashcollecting.server.dao;

import com.example.gametrashcollecting.model.GameRoom;
import com.example.gametrashcollecting.model.RoomStatus;
import com.example.gametrashcollecting.server.utils.DatabaseConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameRoomDAO {
    private DatabaseConnectionManager dbManager;
    private static final String FIND_ROOM_BY_ID = "SELECT * FROM GameRoom WHERE roomId = ?";
    private static final String FIND_ALL_ROOM = "SELECT * FROM GameRoom LIMIT 3";

    private static final String CREATE_NEW_ROOM = "INSERT INTO GameRoom (roomName, maxPlayer, currentPlayer, status) VALUES (?,?,?,?)";
    private static final String UPDATE_ROOM_AFTER_JOIN_ROOM = "UPDATE GameRoom SET currentPlayer = ?, status = ? WHERE roomId = ?";
    // Lay thong tin phong

    public GameRoomDAO() {
        this.dbManager = new DatabaseConnectionManager();
    }

    public GameRoom findGameRoomById(int roomId) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement(FIND_ROOM_BY_ID);
        ps.setInt(1, roomId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            GameRoom gameRoom = new GameRoom();
            gameRoom.setId(rs.getInt("roomId"));
            gameRoom.setRoomName(rs.getString("roomName"));
            gameRoom.setMaxPlayer(rs.getInt("maxPlayer"));
            gameRoom.setCurrentPlayer(rs.getInt("currentPlayer"));
            gameRoom.setStatus(RoomStatus.valueOf(rs.getString("status")));
            return gameRoom;
        }
        return null;
    }


    public List<GameRoom> findAllGameRoom() throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement(FIND_ALL_ROOM);
        List<GameRoom> gameRoomList = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            GameRoom gameRoom = new GameRoom();
            gameRoom.setId(rs.getInt("roomId"));
            gameRoom.setRoomName(rs.getString("roomName"));
            gameRoom.setMaxPlayer(rs.getInt("maxPlayer"));
            gameRoom.setCurrentPlayer(rs.getInt("currentPlayer"));
            gameRoom.setStatus(RoomStatus.valueOf(rs.getString("status")));
            gameRoomList.add(gameRoom);
        }
        return gameRoomList;
    }

    // Tao phong moi
    public GameRoom createGameRoom() throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement(CREATE_NEW_ROOM, Statement.RETURN_GENERATED_KEYS);

        // Tạo tên phòng mới
        String roomName = "Room-" + UUID.randomUUID().toString().substring(0, 8);

        // Thiết lập tham số cho PreparedStatement
        ps.setString(1, roomName);
        ps.setInt(2, 2);  // maxPlayer
        ps.setInt(3, 1);  // currentPlayer
        String roomStatusStr = RoomStatus.WAITING.toString();
        ps.setString(4, roomStatusStr);  // status

        System.out.println("Status:" + roomStatusStr);


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

        // Tạo đối tượng GameRoom và trả về
        return new GameRoom(id, roomName, 2, 1, RoomStatus.WAITING);
    }


    // Cap nhat so nguoi trong phong khi co nguoi vao phong
    public boolean joinAndUpdateGameRoom(GameRoom gameRoom) throws SQLException {
        Connection connection = dbManager.getConnection();
        RoomStatus roomStatus = null;
        if(gameRoom.getCurrentPlayer() + 1 < gameRoom.getMaxPlayer()) {
            roomStatus = RoomStatus.WAITING;
        }
        else{
            roomStatus = RoomStatus.FULL;
        }
        PreparedStatement ps = connection.prepareStatement(UPDATE_ROOM_AFTER_JOIN_ROOM);
        ps.setInt(1, gameRoom.getCurrentPlayer() + 1);
        ps.setString(2, String.valueOf(roomStatus));
        ps.setInt(3, gameRoom.getId());
        return ps.executeUpdate() >= 1;
    }
}
