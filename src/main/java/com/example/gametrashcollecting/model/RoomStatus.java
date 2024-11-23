package com.example.gametrashcollecting.model;

public enum RoomStatus {
    WAITING("WAITING"),
    IN_PROGRESS("IN_PROGRESS"),
    FULL("FULL");

    private String roomStatus;

    RoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    @Override
    public String toString() {
        return this.roomStatus;
    }
}
