package com.example.gametrashcollecting.model;

import java.io.Serializable;
import java.util.Date;

public class GameResult implements Serializable {
    int id;
    int pointsScored;
    Date playTime;
    User userGame;
    GameRound round;

    public GameResult(){}

    public GameResult(int id, int pointsScored, Date playTime, User userGame, GameRound round) {
        this.id = id;
        this.pointsScored = pointsScored;
        this.playTime = playTime;
        this.userGame = userGame;
        this.round = round;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPointsScored() {
        return pointsScored;
    }

    public void setPointsScored(int pointsScored) {
        this.pointsScored = pointsScored;
    }

    public Date getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Date playTime) {
        this.playTime = playTime;
    }

    public User getUserGame() {
        return userGame;
    }

    public void setUserGame(User userGame) {
        this.userGame = userGame;
    }

    public GameRound getRound() {
        return round;
    }

    public void setRound(GameRound round) {
        this.round = round;
    }
}
