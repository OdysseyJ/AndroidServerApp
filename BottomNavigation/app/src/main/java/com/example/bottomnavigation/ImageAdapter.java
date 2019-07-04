package com.example.bottomnavigation;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    static Integer[] imageIDs = {
            R.drawable.sample_1,
            R.drawable.sample_2,
            R.drawable.sample_3,
            R.drawable.sample_4
    };

    ArrayList<String> myArray = new ArrayList<String>();

    private Context context;
    private int itemBackground;
    public ImageAdapter(Context c, ArrayList<String> array){
        context = c;
        for (int i = 0; i < array.size(); i++) {
            myArray.add(array.get(i));
            System.out.println(array.get(i));
        }
    }

    public int getCount() {
        return myArray.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    public void addItem(String content){
        myArray.add(content);
    }

    public void removeItem(String content){
        myArray.remove(content);
    }



    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ImageView imageView = new ImageView(context);
        imageView.setImageURI(Uri.parse(myArray.get(position)));
        imageView.setLayoutParams(new Gallery.LayoutParams(500,500));
        return imageView;
    }
}