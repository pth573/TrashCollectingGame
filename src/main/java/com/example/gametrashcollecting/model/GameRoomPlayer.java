package com.example.gametrashcollecting.model;

import java.io.Serializable;

public class GameRoomPlayer implements Serializable {
    private int roomPlayerId;
    private User user;
    private GameRoom room;
    private boolean isActive;

    public GameRoomPlayer(int roomPlayerId, User user, GameRoom room, boolean isActive) {
        this.roomPlayerId = roomPlayerId;
        this.user = user;
        this.room = room;
        this.isActive = isActive;
    }

    public GameRoomPlayer(User user, GameRoom room, boolean isActive) {
        this.user = user;
        this.room = room;
        this.isActive = isActive;
    }


    public int getRoomPlayerId() {
        return roomPlayerId;
    }

    public void setRoomPlayerId(int roomPlayerId) {
        this.roomPlayerId = roomPlayerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GameRoom getRoom() {
        return room;
    }

    public void setRoom(GameRoom room) {
        this.room = room;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
