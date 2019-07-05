package com.example.restapi;

public class Item {

    String name;
    int image;

    public Item(String name, int image) {
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
    public int getImage(){
        return image;
    }

    public void setImage(int image){
        this.image = image;
    }

}