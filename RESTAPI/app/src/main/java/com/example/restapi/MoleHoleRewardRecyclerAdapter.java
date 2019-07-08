package com.example.restapi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class MoleHoleRewardRecyclerAdapter extends RecyclerView.Adapter<MoleHoleRewardRecyclerAdapter.ItemViewHolder2> {

    // adapter에 들어갈 list 입니다.
    private static ArrayList<MoleHoleRewardData> listData = new ArrayList<>();

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public ItemViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 rewardrecycler_item을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewardrecycler_item, parent, false);
        return new ItemViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder2 holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        final int Position = position;
        holder.onBind(listData.get(position), position+1);
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        if (listData.size()<5) {
            return listData.size();
        }
        else{
            return 5;
        }
    }

    void addItem(MoleHoleRewardData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
        Collections.sort(listData);
    }

    void clearData(){listData.clear();}

    MoleHoleRewardData getFirstItem() {
        if (listData.size()==0){
            MoleHoleRewardData temp = new MoleHoleRewardData("기록없음","0");
            return temp;
        }
        return listData.get(0);
    }

    void resetItem(){
        listData.clear();
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder2 extends RecyclerView.ViewHolder {

        //랭크
        private TextView rewardtextView1;
        //이름
        private TextView rewardtextView2;
        //시간
        private TextView rewardtextView3;

        ItemViewHolder2(View itemView) {
            super(itemView);
            rewardtextView1 = itemView.findViewById(R.id.rewardtextView1);
            rewardtextView2 = itemView.findViewById(R.id.rewardtextView2);
            rewardtextView3 = itemView.findViewById(R.id.rewardtextView3);
        }

        void onBind(MoleHoleRewardData data, int position) {
            rewardtextView1.setText(Integer.toString(position));
            rewardtextView2.setText(data.getName());
            String temp = data.getCount() + "마리";
            rewardtextView3.setText(temp);
        }
    }
}
