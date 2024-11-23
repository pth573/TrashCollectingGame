package com.example.gametrashcollecting.server.request;

import java.io.Serializable;

public enum RequestStatus implements Serializable {
    REGISTER("Register"),
    LOGIN("Login"),
    LIST_FRIEND("Get list friend of user"),
    BACK_MAIN_SCREEN("Back main screen"),
    CREATEROOM("Create new room"),
    FIND_ROOM("Find room"),
    FIND_ALL_ROOM("Find all room"),
    JOIN_ROOM("Join room"),
    OUT_ROOM("Out room"),
    FIND_ALL_ROUND("Find all map"),
    CHOOSE_MAP("Choose map"),
    START_GAME("Start game"),
    UPDATE_ROOM_UI_OTHER("Update room UI other"),
    CHOOSE_MAP_AND_CREATE_SESSION("Choose map and create session"),
    START_GAME_UPDATE_CLIENT_2("Start game update client 2"),
    REMOVE_TRASH("Remove trash"),
    UPDATE_COLOR_UI("Update color UI"),
    UPDATE_TRASH_UI("Update trash UI"),
    UPDATE_SCORE_UI("Update score UI"),
    SHOW_SCORE_USER("Show score user"),
    GET_LIST_HISTORY_SESSION("Get list history session"),
    ;

    private String requestStatus;

    RequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
    @Override
    public String toString() {
        return this.requestStatus;
    }
}

