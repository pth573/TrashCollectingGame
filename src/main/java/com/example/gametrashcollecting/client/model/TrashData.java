package com.example.gametrashcollecting.client.model;

import java.io.Serializable;

public class TrashData implements Serializable {
    private int id;
    private double layoutX;
    private double layoutY;
    private String imagePath;

    public TrashData(double layoutX, double layoutY, String imagePath) {
        this.layoutX = layoutX;
        this.layoutY = layoutY;
        this.imagePath = imagePath;
    }

    public TrashData(int id, double layoutX, double layoutY, String imagePath) {
        this.id = id;
        this.layoutX = layoutX;
        this.layoutY = layoutY;
        this.imagePath = imagePath;
    }

    public double getLayoutX() {
        return layoutX;
    }

    public double getLayoutY() {
        return layoutY;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setLayoutX(double layoutX) {
        this.layoutX = layoutX;
    }

    public void setLayoutY(double layoutY) {
        this.layoutY = layoutY;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TrashData{" +
                "id=" + id +
                ", layoutX=" + layoutX +
                ", layoutY=" + layoutY +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
