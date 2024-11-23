package com.example.gametrashcollecting.server.dao;

import com.example.gametrashcollecting.client.model.HistoryRecord;
import com.example.gametrashcollecting.model.GameRound;
import com.example.gametrashcollecting.model.GameSession;
import com.example.gametrashcollecting.model.GameSessionPlayer;
import com.example.gametrashcollecting.model.User;
import com.example.gametrashcollecting.model.UserStatus;
import com.example.gametrashcollecting.server.utils.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameSessionPlayerDAO {
    private GameSessionDAO gameSessionDAO;
    private UserDAO userDAO;
    private DatabaseConnectionManager dbManager;
    private static final String UPDATE_GAME_SESSION_PLAYER = "UPDATE GameSessionPlayer SET sessionId = ?, userId = ?, score = ? WHERE sessionPlayerId = ?";
    private static final String FIND_GAME_SESSION_PLAYER_LIST_BY_USER_ID = "SELECT * FROM GameSessionPlayer WHERE userId = ?";
    private static final String FIND_GAME_SESSION_PLAYER_BY_ID = "SELECT * FROM gamesessionplayer where sessionPlayerId = ?";
    private static final String GET_USER_BY_SESSION = "SELECT * FROM gamesessionplayer where sessionId = ?";
    private static final String GET_HISTORY_QUERY =
            "SELECT " +
            "    gs.sessionId AS sessionId, " +
            "    gsp1.score AS myScore, " +
            "    COALESCE(gsp2.score, 0) AS opponentScore, " +
            "    gr.roundName AS roundName, " +
            "    gs.startTime AS timeStart, " +
            "    gs.endTime AS timeEnd, " +
            "    CASE " +
            "        WHEN gsp1.score > COALESCE(gsp2.score, 0) THEN 'Win' " +
            "        WHEN gsp1.score < COALESCE(gsp2.score, 0) THEN 'Lose' " +
            "        ELSE 'Draw' " +
            "    END AS result " +
            "FROM " +
            "    gamesession gs " +
            "JOIN " +
            "    gamesessionplayer gsp1 ON gs.sessionId = gsp1.sessionId " +
            "LEFT JOIN " +
            "    gamesessionplayer gsp2 ON gs.sessionId = gsp2.sessionId AND gsp2.userId != gsp1.userId " +
            "JOIN " +
            "    gameround gr ON gs.roundId = gr.roundId " +
            "WHERE " +
            "    gsp1.userId = ?";
    private static final String GET_USER_BY_ID = "SELECT * FROM user WHERE userId = ?";
    private static final String CREATE_GAME_SESSION_PLAYER = "INSERT INTO gamesessionplayer (sessionId, userId, score) VALUES (?,?,?)";
    private static final String SAVE_GAMESESSION_PLAYER = "INSERT INTO gamesessionplayer (sessionId, userId, score) VALUES (?, ?, ?)";
//    private static final String FIND_SCORE_MAX_ROUND =  "SELECT MAX(gsp.score) AS max_score FROM gamesessionplayer gsp WHERE gsp.userId = ? AND gsp.roundId = ?";
private static final String FIND_SCORE_MAX_ROUND =
                "SELECT MAX(gsp.score) AS max_score " +
                "FROM gamesessionplayer gsp " +
                "JOIN gamesession gs ON gsp.sessionId = gs.sessionId " +
                "WHERE gsp.userId = ? AND gs.roundId = ?";

    private static final String GET_USER_TOTAL_SCORE = """
            SELECT 
                SUM(max_score) AS total_score
            FROM (
                SELECT 
                    gs.roundId,
                    MAX(gsp.score) AS max_score
                FROM 
                    gamesessionplayer AS gsp
                INNER JOIN 
                    gamesession AS gs ON gsp.sessionId = gs.sessionId
                WHERE 
                    gsp.userId = ?
                GROUP BY 
                    gs.roundId
            ) AS round_scores;
        """;


    public GameSessionPlayerDAO() {
        dbManager = new DatabaseConnectionManager();
    }

    public List<HistoryRecord> getHistoryByUserId(int userId) {
        List<HistoryRecord> historyList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Lấy kết nối tới cơ sở dữ liệu
            connection = dbManager.getConnection(); // Đảm bảo bạn có lớp DatabaseManager để quản lý kết nối

            // Chuẩn bị câu truy vấn
            preparedStatement = connection.prepareStatement(GET_HISTORY_QUERY);
            preparedStatement.setInt(1, userId);

            // Thực thi truy vấn
            resultSet = preparedStatement.executeQuery();

            // Lấy dữ liệu từ ResultSet
            while (resultSet.next()) {
                String sessionId = resultSet.getString("sessionId");
                int myScore = resultSet.getInt("myScore");
                int opponentScore = resultSet.getInt("opponentScore");
                String roundName = resultSet.getString("roundName");
                String timeStart = resultSet.getString("timeStart");
                String timeEnd = resultSet.getString("timeEnd");
                String result = resultSet.getString("result");

                // Tạo đối tượng HistoryRecord
                HistoryRecord record = new HistoryRecord(
                        sessionId, myScore, opponentScore, roundName, timeStart, timeEnd, result
                );

                // Thêm vào danh sách
                historyList.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý ngoại lệ
        } finally {
            // Đóng tài nguyên
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return historyList;
    }

//    public GameSessionPlayer findGameSessionPlayerByUser(int userId) throws SQLException {
//        Connection connection = dbManager.getConnection();
//        PreparedStatement preparedStatement = connection.prepareStatement(FIND_GAME_SESSION_PLAYER_LIST_BY_USER_ID);
//        preparedStatement.setInt(1, userId);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        List<GameSessionPlayer> gameSessionPlayerList = new ArrayList<>();
//
//        while (resultSet.next()) {
//            int id = resultSet.getInt("id");
//            int sessionId = resultSet.getInt("sessionId");
//            int userIdFromDb = resultSet.getInt("userId");
//            int score = resultSet.getInt("score");
//
//            GameSession session = gameSessionDAO.findGameSessionById(new GameSession(sessionId));
//            User user = userDAO.getUserById(userIdFromDb);
//
//            GameSessionPlayer gameSessionPlayer = new GameSessionPlayer(id, session, user, score);
//            gameSessionPlayerList.add(gameSessionPlayer);
//
//
//            GameSessionPlayer  gameSessionPlayer = new GameSessionPlayer();
//
//
//
//            GameSession gameSession = new GameSession();
//            gameSessionDAO = new GameSessionDAO();
//            gameSession.setId(resultSet.getInt("sessionId"));
//            gameSession = gameSessionDAO.findGameSessionById(gameSession);
//
//            UserDAO userDAO = new UserDAO();
//            User user = new User();
////                user.setId(resultSet.getInt("userId"));
//            user = userDAO.getUserById(resultSet.getInt("userId"));
//
//            gameSessionPlayer.setUser(user);
//            gameSessionPlayer.setSession(gameSession);
//            gameSessionPlayer.setScore(resultSet.getInt("score"));
//            gameSessionPlayer.setId(resultSet.getInt("sessionPlayerId"));
//        }
//    }

    public GameSessionPlayer updateGameSessionPlayer(GameSessionPlayer sessionPlayer) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_GAME_SESSION_PLAYER);
            // Set parameters in the query
        preparedStatement.setInt(1, sessionPlayer.getSession().getId());  // Assuming `GameSession` has `getId()`
        preparedStatement.setInt(2, sessionPlayer.getUser().getId());      // Assuming `User` has `getId()`
        preparedStatement.setInt(3, sessionPlayer.getScore());
        preparedStatement.setInt(4, sessionPlayer.getId());

        // Execute the update
        int affectedRows = preparedStatement.executeUpdate();

        if (affectedRows > 0) {
            // Update was successful, return the updated sessionPlayer object
            return sessionPlayer;
        }
        return sessionPlayer;
    }


    public GameSessionPlayer findGameSessionPlayerById(int sessionPlayerId) throws SQLException {
        GameSessionPlayer gameSessionPlayer = null;

        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_GAME_SESSION_PLAYER_BY_ID)) {

            preparedStatement.setInt(1, sessionPlayerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                gameSessionPlayer = new GameSessionPlayer();
                GameSession gameSession = new GameSession();
                gameSessionDAO = new GameSessionDAO();
                gameSession.setId(resultSet.getInt("sessionId"));
                gameSession = gameSessionDAO.findGameSessionById(gameSession);

                UserDAO userDAO = new UserDAO();
                User user = new User();
//                user.setId(resultSet.getInt("userId"));
                user = userDAO.getUserById(resultSet.getInt("userId"));

                gameSessionPlayer.setUser(user);
                gameSessionPlayer.setSession(gameSession);
                gameSessionPlayer.setScore(resultSet.getInt("score"));
                gameSessionPlayer.setId(resultSet.getInt("sessionPlayerId"));
            }
        }

        return gameSessionPlayer;
    }

    public GameSessionPlayer createGameSessionPlayer(GameSession gameSession, User user) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement(CREATE_GAME_SESSION_PLAYER, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setInt(1, gameSession.getId());
        ps.setInt(2, user.getId());
        ps.setInt(3, 0);
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
        return new GameSessionPlayer(id, gameSession, user, 0);
    }


    // Tra ve cac user trong 1 tran dau
    public List<User> findListUserBySession(GameSession gameSession) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement(GET_USER_BY_SESSION);

        ps.setString(1, String.valueOf(gameSession.getId()));
        ResultSet rs = ps.executeQuery();
        List<User> users = new ArrayList<User>();
        while (rs.next()) {
            int id = rs.getInt("userId");
            PreparedStatement ps1 = connection.prepareStatement(GET_USER_BY_ID);
            ps1.setString(1, String.valueOf(id));
            ResultSet rs1 = ps.executeQuery();
            while (rs1.next()) {
                int userId = rs1.getInt("userId");
                String username = rs1.getString("username");
                String password = rs1.getString("password");
                int totalPoint = rs1.getInt("totalPoints");
                UserStatus userStatus = UserStatus.valueOf(rs1.getString("status"));
                String lastLogin = rs1.getString("lastLogin");
                int currentRoomId = rs1.getInt("currentRoomId");
                String img = rs1.getString("img");
                User u = new User(userId, username, password, totalPoint, userStatus, lastLogin, currentRoomId, img);
                users.add(u);
            }
        }
        return users;
    }

    public int findUserTotalPoint(User user) {
        int totalScore = 0;
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(GET_USER_TOTAL_SCORE);
            ps.setInt(1,user.getId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                totalScore = rs.getInt("total_score");
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        
        
        return totalScore;
    }

    public User uppateUserScore(User user, int totalScore) {
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(GET_USER_TOTAL_SCORE);
            ps.setInt(1,user.getId());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                totalScore = rs.getInt("total_score");
            }
            user.setTotalPoints(totalScore);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return user;
    }
    public GameSessionPlayer saveGameSessionPlayer(GameSessionPlayer gameSessionPlayer, User user, int score) {  
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SAVE_GAMESESSION_PLAYER, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            // Thiết lập các giá trị tham số
            stmt.setInt(1, gameSessionPlayer.getSession().getId());
            stmt.setInt(2, user.getId());
            stmt.setInt(3, score);

            // Thực thi lệnh SQL
            int affectedRows = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi lưu GameSessionPlayer.");
        }

        return gameSessionPlayer;
    }
    public int findScoreMaxRound(GameSessionPlayer gameSessionPlayer, GameRound round) {
        int maxScore = 0;
        try (Connection connection = dbManager.getConnection(); // Giả định bạn có một phương thức để lấy kết nối
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_SCORE_MAX_ROUND)) {

            preparedStatement.setInt(1, gameSessionPlayer.getUser().getId());
            preparedStatement.setInt(2, round.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    maxScore = resultSet.getInt("max_score");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return maxScore;
    }
}


