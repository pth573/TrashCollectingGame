package com.example.gametrashcollecting.model;

import java.io.Serializable;

public class GameRound implements Serializable {
    int id;
    String roundName;
    int timeLimit;
    Level difficulLevel;
    String img;

    public GameRound(){}

    public GameRound(int id, String roundName, int timeLimit, Level difficulLevel, String img) {
        this.id = id;
        this.roundName = roundName;
        this.timeLimit = timeLimit;
        this.difficulLevel = difficulLevel;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Level getDifficulLevel() {
        return difficulLevel;
    }

    public void setDifficulLevel(Level difficulLevel) {
        this.difficulLevel = difficulLevel;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "GameRound{" +
                "id=" + id +
                ", roundName='" + roundName + '\'' +
                ", timeLimit=" + timeLimit +
                ", difficulLevel=" + difficulLevel +
                ", img='" + img + '\'' +
                '}';
    }
}
