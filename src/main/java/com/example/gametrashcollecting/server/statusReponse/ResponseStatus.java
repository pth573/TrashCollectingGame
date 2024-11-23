package com.example.gametrashcollecting.server.statusReponse;//package com.example.gametrashcollecting.server.statusReponse;
//
//import java.io.Serializable;
//
//public class ResponseStatus implements Serializable {
//    private Status status;
//    private Object dataToClient;
//
//    public ResponseStatus(Status status, Object dataToClient) {
//        this.status = status;
//        this.dataToClient = dataToClient;
//    }
//
//    public ResponseStatus(Status status) {
//        this.status = status;
//    }
//
//    public Status getStatus() {
//        return status;
//    }
//
//    public void setStatus(Status status) {
//        this.status = status;
//    }
//
//    public Object getDataToClient() {
//        return dataToClient;
//    }
//
//    public void setDataToClient(Object dataToClient) {
//        this.dataToClient = dataToClient;
//    }
//
//    @Override
//    public String toString() {
//        return "ResponseStatus{" +
//                "status=" + status +
//                ", dataToClient=" + dataToClient +
//                '}';
//    }
//}


import com.example.gametrashcollecting.server.statusReponse.Status;

import java.io.Serializable;
import java.util.List;

public class ResponseStatus implements Serializable {
    private Status status;
    private Object dataToClient;

    // Constructor
    public ResponseStatus(Status status, Object dataToClient) {
        this.status = status;
        this.dataToClient = dataToClient;
    }

    public ResponseStatus(Status status) {
        this.status = status;
    }

    // Getter and Setter
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Object getDataToClient() {
        return dataToClient;
    }

    public void setDataToClient(List<Object> dataToClient) {
        this.dataToClient = dataToClient;
    }

    @Override
    public String toString() {
        return "ResponseStatus{" +
                "status=" + status +
                ", dataToClient=" + dataToClient +
                '}';
    }
}
