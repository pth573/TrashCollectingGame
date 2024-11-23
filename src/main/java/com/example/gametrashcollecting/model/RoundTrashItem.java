package com.example.gametrashcollecting.model;

import java.io.Serializable;

public class RoundTrashItem implements Serializable {
    int id;
    TrashItem trash;
    GameRound round;

    public RoundTrashItem(){}

    public RoundTrashItem(int id, TrashItem trash, GameRound round) {
        this.id = id;
        this.trash = trash;
        this.round = round;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TrashItem getTrash() {
        return trash;
    }

    public void setTrash(TrashItem trash) {
        this.trash = trash;
    }

    public GameRound getRound() {
        return round;
    }

    public void setRound(GameRound round) {
        this.round = round;
    }
}
