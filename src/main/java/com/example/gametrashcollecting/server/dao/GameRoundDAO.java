package com.example.gametrashcollecting.server.dao;

import com.example.gametrashcollecting.model.GameRoom;
import com.example.gametrashcollecting.model.GameRound;
import com.example.gametrashcollecting.model.Level;
import com.example.gametrashcollecting.model.RoomStatus;
import com.example.gametrashcollecting.server.utils.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameRoundDAO {
    private static final String FIND_GAME_ROUND_BY_ID = "SELECT * FROM GameRound WHERE roundId = ?";
    private static final String FIND_ALL_ROUND = "SELECT * FROM GameRound LIMIT 4";
//    private static final String SAVE_GAME_ROUND = "INSERT INTO GameRound(roundId, roundName, timeLimit, difficultyLevel) VALUES(?,?,?)";
//    // Tra ve thong tin round
//    int id;
//    String roundName;
//    int timeLimit;
//    Level difficulLevel;
//    String img;



    private DatabaseConnectionManager dbManager;
    public GameRoundDAO() {
        dbManager = new DatabaseConnectionManager();
    }

    public List<GameRound> findAllGameRound() throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement(FIND_ALL_ROUND);
        List<GameRound> gameRoundList = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            GameRound gameRound = new GameRound();
            gameRound.setId(rs.getInt("roundId"));
            gameRound.setRoundName(rs.getString("roundName"));
            gameRound.setTimeLimit(rs.getInt("timeLimit"));
            gameRound.setDifficulLevel(Level.valueOf(rs.getString("difficultyLevel")));
            gameRound.setImg(rs.getString("img"));
            gameRoundList.add(gameRound);
        }
        return gameRoundList;
    }


    public GameRound findGameRoundById(int id) throws SQLException {
        Connection connection = dbManager.getConnection();
        GameRound gameRound = null;
        try (PreparedStatement stmt = connection.prepareStatement(FIND_GAME_ROUND_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int roundId = rs.getInt("roundId");
                String roundName = rs.getString("roundName");
                int timeLimit = rs.getInt("timeLimit");
                Level difficultyLevel = Level.valueOf(rs.getString("difficultyLevel"));
                String img = rs.getString("img");
                gameRound = new GameRound(roundId, roundName, timeLimit, difficultyLevel, img);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gameRound;
    }

    // update do kho trong vong
    public void updateRoundLevel(GameRound gameRound){
        Connection connection = null;
        String sql = "UPDATE gameround SET difficultyLevel = ? WHERE roundId = ?";

        try {
            connection = dbManager.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, gameRound.getDifficulLevel().name());
                stmt.setInt(2, gameRound.getId());

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Cập nhật độ khó thành công cho vòng: " + gameRound.getId());
                } else {
                    System.out.println("Không tìm thấy vòng để cập nhật.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
