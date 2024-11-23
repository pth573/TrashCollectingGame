package com.example.gametrashcollecting.server.dao;

import com.example.gametrashcollecting.model.*;
import com.example.gametrashcollecting.server.utils.DatabaseConnectionManager;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class TrashItemDAO {
    private DatabaseConnectionManager dbManager;
    // Thong tin tung loai rac
    public TrashItemDAO() {
        dbManager = new DatabaseConnectionManager();
    }
    public TrashItem findTrashItemById(int id) throws SQLException {
        Connection connection = dbManager.getConnection();
        TrashItem trashItem = null;
        ResultSet resultSet = null;
        String sql = "SELECT * FROM trashitem WHERE itemId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int itemId = resultSet.getInt("itemId");
                ItemType itemType = ItemType.valueOf(resultSet.getString("itemType")); // Assuming an item_type column exists
                GameRound round = null;
//                trashItem = new TrashItem(itemId, itemType, round);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trashItem;
    }

    // List cac loai rac
    public List<TrashItem> findAllTrashItems() throws SQLException {
        Connection connection = dbManager.getConnection();
        ResultSet resultSet = null;
        List<TrashItem> trashItems = new ArrayList<>();
        String sql = "SELECT * FROM trashitem";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int itemId = resultSet.getInt("itemId");
                ItemType itemType = ItemType.valueOf(resultSet.getString("itemType"));
                GameRound round = null;
//                TrashItem trashItem = new TrashItem(itemId, itemType, round);
//                trashItems.add(trashItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trashItems;

    }
}
