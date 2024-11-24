package com.example.gametrashcollecting.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

//// lastLogin : string
public class User implements Serializable {
    int id;
    String username;
    String password;
    int totalPoints;
    UserStatus status;
    String lastLogin;
    int currentRoomId;
    String img;
//    List<User> userListFriend;


    public User(){}

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
//    public User(int id, String username, String password, int totalPoints, UserStatus status, String lastLogin, int currentRoomId) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.totalPoints = totalPoints;
//        this.status = status;
//        this.lastLogin = lastLogin;
//        this.currentRoomId = currentRoomId;
//    }

    public User(int id, String username, String password, int totalPoints, UserStatus status, String lastLogin, int currentRoomId, String img) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.totalPoints = totalPoints;
        this.status = status;
        this.lastLogin = lastLogin;
        this.currentRoomId = currentRoomId;
        this.img = img;
    }


//    public User(int id, String username, String password, int totalPoints, UserStatus status, String lastLogin) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.totalPoints = totalPoints;
//        this.status = status;
//        this.lastLogin = lastLogin;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentRoomId() {
        return currentRoomId;
    }

    public void setCurrentRoomId(int currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", totalPoints=" + totalPoints +
                ", status=" + status +
                ", lastLogin='" + lastLogin + '\'' +
                ", currentRoomId=" + currentRoomId +
                ", img='" + img + '\'' +
                '}';
    }
}
