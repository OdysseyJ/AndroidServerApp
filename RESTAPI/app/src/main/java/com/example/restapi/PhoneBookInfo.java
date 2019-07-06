package com.example.restapi;

import android.graphics.Bitmap;

public class PhoneBookInfo {
    public String user_name;
    public String user_phonenumber;
    public Bitmap user_photo;


    public PhoneBookInfo(String user_name, String user_phonenumber, Bitmap user_photo){
        this.user_name = user_name;
        this.user_phonenumber = user_phonenumber;
        this.user_photo = user_photo;
    }
}