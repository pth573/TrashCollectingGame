package com.example.gametrashcollecting.server.request;

import com.example.gametrashcollecting.model.Client;

import java.io.Serializable;

public class Request  implements Serializable {
    private RequestStatus status;
    private Object dataFromClient;

    public Request(RequestStatus status) {
        this.status = status;
    }

    public Request(RequestStatus status, Object dataFromClient) {
        this.status = status;
        this.dataFromClient = dataFromClient;
    }


    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public Object getDataFromClient() {
        return dataFromClient;
    }

    public void setDataFromClient(Object dataFromClient) {
        this.dataFromClient = dataFromClient;
    }

    @Override
    public String toString() {
        return "Request{" +
                "status=" + status +
                ", dataFromClient=" + dataFromClient +
                '}';
    }
}
