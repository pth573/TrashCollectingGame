package com.example.gametrashcollecting.model;

import java.io.Serializable;

public class GameRoom implements Serializable {
    private int id;
    private String roomName;
    private int maxPlayer;
    private int currentPlayer;
    private RoomStatus status;

    public GameRoom(){}

    public GameRoom(int id, String roomName, int maxPlayer, int currentPlayer, RoomStatus status) {
        this.id = id;
        this.roomName = roomName;
        this.maxPlayer = maxPlayer;
        this.currentPlayer = currentPlayer;
        this.status = status;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "GameRoom{" +
                "id=" + id +
                ", roomName='" + roomName + '\'' +
                ", maxPlayer=" + maxPlayer +
                ", currentPlayer=" + currentPlayer +
                ", status=" + status +
                '}';
    }
}
