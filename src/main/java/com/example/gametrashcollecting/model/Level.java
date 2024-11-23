package com.example.gametrashcollecting.model;

public enum Level {
    EASY("EASY"),
    MEDIUM("MEDIUM"),
    HARD("HARD");

    private String levelName;

    Level(String levelName) {
        this.levelName = levelName;
    }

    @Override
    public String toString() {
        return this.levelName;
    }
}
