package com.example.gametrashcollecting.model;

import java.io.Serializable;

public class GameSessionPlayer implements Serializable {
    int id;
    GameSession session;
    User user;
    int score;

    public GameSessionPlayer() {}

    public GameSessionPlayer(int id, GameSession session, User user, int score) {
        this.id = id;
        this.session = session;
        this.user = user;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GameSession getSession() {
        return session;
    }

    public void setSession(GameSession session) {
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "GameSessionPlayer{" +
                "id=" + id +
                ", session=" + session +
                ", user=" + user +
                ", score=" + score +
                '}';
    }
}
