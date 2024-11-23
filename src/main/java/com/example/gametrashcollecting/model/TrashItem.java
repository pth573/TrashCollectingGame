package com.example.gametrashcollecting.model;

import java.io.Serializable;

public class TrashItem implements Serializable {
    int id;
    ItemType itemType;
    String img;
    GameRound round;
    double x, y, vx, vy;

    public TrashItem(){}

    public TrashItem(int id, ItemType itemType, GameRound round, String img, double x, double y, double vx, double vy) {
        this.id = id;
        this.itemType = itemType;
        this.round = round;
        this.img = img;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public GameRound getRound() {
        return round;
    }

    public void setRound(GameRound round) {
        this.round = round;
    }

    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getVx() {
        return vx;
    }
    public void setVx(double vx) {
        this.vx = vx;
    }
    public double getVy() {
        return vy;
    }
    public void setVy(double vy) {
        this.vy = vy;
    }

    @Override
    public String toString() {
        return "TrashItem{" +
                "id=" + id +
                ", itemType=" + itemType +
                ", img='" + img + '\'' +
//                ", round=" + round +
                ", x=" + x +
                ", y=" + y +
                ", vx=" + vx +
                ", vy=" + vy +
                '}';
    }
}
