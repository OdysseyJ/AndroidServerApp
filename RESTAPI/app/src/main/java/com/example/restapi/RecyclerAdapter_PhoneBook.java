package com.example.restapi;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerAdapter_PhoneBook extends RecyclerView.Adapter<RecyclerAdapter_PhoneBook.ViewHolder> {
    private ArrayList<PhoneBookInfo> phoneBook_array = new ArrayList<PhoneBookInfo>();
    private int Layout_num;

    public RecyclerAdapter_PhoneBook(int Layout_num){
        this.Layout_num = Layout_num;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageBitmap(phoneBook_array.get(position).user_photo);
        holder.textView1.setText(phoneBook_array.get(position).user_name);
        holder.textView2.setText(phoneBook_array.get(position).user_phonenumber);
    }

    @Override
    public int getItemCount() {
        return phoneBook_array.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(Layout_num, parent, false); // 뜻 ?? context 개념 ??
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView1;
        private TextView textView2;

        public ViewHolder(View view){
            super(view);
            imageView = (ImageView) view.findViewById(R.id.profile_photo);
            textView1 = (TextView) view.findViewById(R.id.textView1);
            textView2 = (TextView) view.findViewById(R.id.textView2);
        }
    }

    void addItem(PhoneBookInfo data) {
        phoneBook_array.add(data);
    }

    void resetItem(){
        phoneBook_array.clear();
    }
}