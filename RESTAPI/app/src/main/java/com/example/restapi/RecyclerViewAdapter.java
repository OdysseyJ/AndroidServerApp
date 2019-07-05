package com.example.restapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Item> mData = new ArrayList<>() ;

    private ImageView imageview;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1 ;
        ImageView image;
        ViewHolder(View itemView) {
            super(itemView) ;
            textView1 = itemView.findViewById(R.id.textview_recyclerview_title);
            image = itemView.findViewById(R.id.imageview_recyclerview_image);
        }

        public ImageView getImageView() {
            return image;
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    RecyclerViewAdapter(ArrayList<Item> list) {
        mData = list ;
    }

    RecyclerViewAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.squarelayout, parent, false) ;
        RecyclerViewAdapter.ViewHolder vh = new RecyclerViewAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        Item text = mData.get(position) ;
        holder.textView1.setText(text.getName()) ;
        Bitmap image = mData.get(position).getImage();
        holder.image.setImageBitmap(image);

        RecyclerViewAdapter.ViewHolder vholder = (RecyclerViewAdapter.ViewHolder)holder;

        vholder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position);
            }
        });
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    void addItem(Item data) {
        mData.add(data);
    }

    void resetItem(){
        mData.clear();
    }
}
