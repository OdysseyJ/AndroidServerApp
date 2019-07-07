package com.example.restapi;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class RecyclerAdapter_PhoneBook extends RecyclerView.Adapter<RecyclerAdapter_PhoneBook.ViewHolder> {
    private ArrayList<PhoneBookInfo> phoneBook_array = new ArrayList<PhoneBookInfo>();
    private OnItemClickListener onItemClickListener;

    public RecyclerAdapter_PhoneBook(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageBitmap(phoneBook_array.get(position).user_photo);
        holder.textView1.setText(phoneBook_array.get(position).user_name);
        holder.textView2.setText(phoneBook_array.get(position).user_phonenumber);
        holder.remove_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.v("태그", "연락처 삭제 버튼 눌림");
                onItemClickListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return phoneBook_array.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_phonebook, parent, false); // 뜻 ?? context 개념 ??
//        return new ViewHolder(view);
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.recyclerview_phonebook, parent, false) ;
        RecyclerAdapter_PhoneBook.ViewHolder vh = new RecyclerAdapter_PhoneBook.ViewHolder(view) ;

        return vh ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView1;
        private TextView textView2;
        private ImageButton remove_button;

        public ViewHolder(View view){
            super(view);
            imageView = (ImageView) view.findViewById(R.id.profile_photo);
            textView1 = (TextView) view.findViewById(R.id.textView1);
            textView2 = (TextView) view.findViewById(R.id.textView2);
            remove_button = (ImageButton) view.findViewById(R.id.remove_button);
        }
    }


    void addItem(PhoneBookInfo data) {
        phoneBook_array.add(data);
    }

    void resetItem(){
        phoneBook_array.clear();
    }
}