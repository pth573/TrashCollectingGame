package com.example.gametrashcollecting.server.statusReponse;

public enum Status {
    LOGIN_SUCCESS("Login Success"),
    LOGIN_FAIL("Login Fail"),
    REGISTER_SUCCESS("Register Success"),
    REGISTER_FAIL("Register Fail"),
    BACK_MAIN_SCREEN_SUCCESS("Back Main Screen"),
    BACK_MAIN_SCREEN_FAIL("Back Main Screen Fail"),
    FIND_FRIEND_OF_USER_SUCCESS("Find friend of user success"),
    FIND_FRIEND_OF_USER_FAIL("Find friend of user fail"),
    CREATE_ROOM_SUCCESS("Create Room Success"),
    CREATE_ROOM_FAIL("Create Room Fail"),
    FIND_ROOM_SUCCESS("Find Room Success"),
    FIND_ROOM_FAIL("Find Room Fail"),
    FIND_ALL_ROOM_SUCCESS("Find All Room Success"),
    FIND_ALL_ROOM_FAIL("Find All Room Fail"),
    UPDATE_ROOM_SUCCESS("Update Room Success"),
    UPDATE_ROOM_FAIL("Update Room Fail"),
    JOIN_ROOM_SUCCESS("Join Room Success"),
    JOIN_ROOM_FAIL("Join Room Fail"),
    UPDATE_ROOM_UI_SUCCESS("Update Room UI Success"),
    UPDATE_ROOM_UI_FAIL("Update Room UI Fail"),
    OUT_ROOM_SUCCESS("Out Room Success"),
    OUT_ROOM_FAIL("Out Room Fail"),
    CREATE_SESSION_SUCCESSFUL("Create Session Successful"),
    CREATE_SESSION_FAIL("Create Session Fail"),
    CREATE_SESSION_PLAYER_SUCCESSFUL("CREATE SESSION PLAYER SUCCESSFUL"),
    CREATE_SESSION_PLAYER_FAIL("CREATE SESSION PLAYER FAIL"),
    FIND_ALL_ROUND_SUCCESS("Find All Round Success"),
    FIND_ALL_ROUND_FAIL("Find All Round Fail"),
    CHOOSE_ROUND_SUCCESS("Choose Round Success"),
    CHOOSE_ROUND_FAIL("Choose Round Fail"),
    START_GAME_SUCCESS("Start Game Success"),
    START_GAME_FAIL("Start Game Fail"),
    START_GAME_UPDATE_CLIENT_2_SUCCESS("Start Game Update Client 2 success"),
    START_GAME_UPDATE_CLIENT_2_FAIL("Start Game Update Client 2 fail"),
    REMOVE_TRASH_SUCCESS("Remove Trash Success"),
    REMOVE_TRASH_FAIL("Remove Trash Fail"),
    UPDATE_SCORE_UI_SUCCESS("Update Score UI Success"),
    UPDATE_SCORE_UI_FAIL("Update Score UI Fail"),
    UPDATE_COLOR_UI_SUCCESS("Update Color UI Success"),
    UPDATE_COLOR_UI_FAIL("Update Color UI Fail"),
    UPDATE_TRASH_UI_SUCCESS("Update Trash UI Success"),
    UPDATE_TRASH_UI_FAIL("Update Trash UI Fail"),
    SHOW_SCORE_USER_SUCCESS("Show Score User Success"),
    SHOW_SCORE_USER_FAIL("Show Score User Fail"),
    GET_LIST_HISTORY_SESSION_SUCCESS("Get List History Session Success"),
    GET_LIST_HISTORY_SESSION_FAIL("Get List History Session Fail"),
    SHOW_USER_LIST_SUCCESS("SHOW User List Success"),
    SHOW_USER_LIST_FAIL("SHOW User List Fail"),
    UPDATE_SCORE_UI_1_SUCCESS("Update Score UI 1 Success"),
    UPDATE_SCORE_UI_1_FAIL("Update Score UI 1 Fail"),
    OTHER("Dont do anything")
    ;

    private String statusString;

    Status(String statusString) {
        this.statusString = statusString;
    }
    @Override
    public String toString() {
        return this.statusString;
    }
}
