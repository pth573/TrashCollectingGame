package com.example.gametrashcollecting.client.model;

import java.io.Serializable;

public class HistoryRecord implements Serializable {
    private final String sessionId;
    private final int me;
    private final int opponent;
    private final String roundName;
    private final String timeStart;
    private final String timeEnd;
    private final String result;

    public HistoryRecord(String sessionId, int me, int opponent, String roundName, String timeStart, String timeEnd, String result) {
        this.sessionId = sessionId;
        this.me = me;
        this.opponent = opponent;
        this.roundName = roundName;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.result = result;
    }

    public String getSessionId() { return sessionId; }
    public int getMe() { return me; }
    public int getOpponent() { return opponent; }
    public String getRoundName() { return roundName; }
    public String getTimeStart() { return timeStart; }
    public String getTimeEnd() { return timeEnd; }
    public String getResult() { return result; }
}
