package com.example.restapi;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerAdapter_PhoneBook extends RecyclerView.Adapter<RecyclerAdapter_PhoneBook.ViewHolder> {
    private ArrayList<PhoneBookInfo> phoneBook_array;
    private int Layout_num;

    public RecyclerAdapter_PhoneBook(ArrayList<PhoneBookInfo> phoneBook_array, int Layout_num){
        this.phoneBook_array = phoneBook_array;
        this.Layout_num = Layout_num;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView1.setText(phoneBook_array.get(position).user_name);
        holder.textView2.setText(phoneBook_array.get(position).user_number);
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
        private TextView textView1;
        private TextView textView2;

        public ViewHolder(View view){
            super(view); // ??
            textView1 = (TextView) view.findViewById(R.id.textView1);
            textView2 = (TextView) view.findViewById(R.id.textView2);
        }
    }
}
