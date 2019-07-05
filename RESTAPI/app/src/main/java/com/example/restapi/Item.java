package com.example.restapi;

import android.graphics.Bitmap;

public class Item {

    String name;
    Bitmap image;

    public Item(String name, Bitmap image) {
        super();
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public Bitmap getImage(){
        return image;
    }

    public void setImage(Bitmap image){
        this.image = image;
    }

}