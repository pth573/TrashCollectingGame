package com.example.gametrashcollecting.server.utils;

import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    private String dbClass = "com.mysql.cj.jdbc.Driver";
    private String dbUrl = "jdbc:mysql://localhost:3306/gametrashcollecting";
    private String dbUser = "root";
    private String dbPass = "hang572003";

    public DatabaseConnectionManager() {
    }

    public Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            Class.forName(dbClass);
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
        } catch (Exception e) {
            System.out.println("Khong ket noi thanh cong");
            e.printStackTrace();
        }
        return conn;
    }
}
