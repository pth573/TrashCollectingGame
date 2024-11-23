package com.example.gametrashcollecting.server.dao;

import com.example.gametrashcollecting.model.GameRound;
import com.example.gametrashcollecting.model.ItemType;
import com.example.gametrashcollecting.model.TrashItem;
import com.example.gametrashcollecting.server.utils.DatabaseConnectionManager;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
public class RoundTrashItemDAO {
    private static final String FIND_LIST_TRASH_BY_ROUND =
            "SELECT * FROM roundTrashItem rt " +
                    "INNER JOIN trashItem ti ON rt.trashId = ti.itemId " +
                    "WHERE rt.roundId = ?";
    // Danh sach cac rac trong 1 vong
    private DatabaseConnectionManager dbManager;
    public RoundTrashItemDAO() {
        dbManager = new DatabaseConnectionManager();
    }

    public List<TrashItem> findListTrashByRound(GameRound gameRound) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement ps = connection.prepareStatement(FIND_LIST_TRASH_BY_ROUND);
        ps.setInt(1, gameRound.getId());
        ResultSet rs = ps.executeQuery();
        List<TrashItem> trashItems = new ArrayList<>();

        try {
            while (rs.next()) {
                int itemId = rs.getInt("itemId");
                ItemType itemType = ItemType.valueOf(rs.getString("type"));
                String img = rs.getString("img");
                int roundId = rs.getInt("roundId");
                TrashItem trashItem = new TrashItem(itemId, itemType, gameRound, img, 0, 0, 0 ,0);
                trashItems.add(trashItem);
            }
            return trashItems;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trashItems;
    }
}
