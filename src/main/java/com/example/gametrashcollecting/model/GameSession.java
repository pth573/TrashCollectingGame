package com.example.gametrashcollecting.model;

import java.io.Serializable;
import java.util.Date;

public class GameSession implements Serializable {
    int id;
    String startTime;
    String endTime;
    GameRound round;
//    User userGame;
    GameRoom room;

    public GameSession(){}


public GameSession(int id, String startTime, String endTime, GameRound round, GameRoom room) {
    this.id = id;
    this.startTime = startTime;
    this.endTime = endTime;
    this.round = round;
    this.room = room;
}

    public GameSession(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public GameRound getRound() {
        return round;
    }

    public void setRound(GameRound round) {
        this.round = round;
    }

//    public User getUserGame() {
//        return userGame;
//    }
//
//    public void setUserGame(User userGame) {
//        this.userGame = userGame;
//    }

    public GameRoom getRoom() {
        return room;
    }

    public void setRoom(GameRoom room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "GameSession{" +
                "id=" + id +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", round=" + round.getId() +
                ", room=" + room.getId() +
                '}';
    }
}
