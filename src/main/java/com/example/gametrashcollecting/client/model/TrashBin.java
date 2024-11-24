package com.example.gametrashcollecting.client.model;


import com.example.gametrashcollecting.model.ItemType;
import javafx.scene.image.ImageView;

public class TrashBin {
    private ItemType type;
    private ImageView imageView;

    public TrashBin() {
    }


    public TrashBin(ItemType type, ImageView imageView) {
        this.type = type;
        this.imageView = imageView;
    }

    public ItemType getType() {
        return type;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
