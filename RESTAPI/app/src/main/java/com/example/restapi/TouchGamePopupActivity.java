package com.example.restapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TouchGamePopupActivity extends AppCompatActivity {

    TouchGameRewardRecyclerAdapter rewardRecyclerAdapter;

    Button returnBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.resultaward);

        //UI 객체생성
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.awardrecyclerView);

        //데이터 가져오기
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String score = intent.getStringExtra("score");

        rewardRecyclerAdapter = new TouchGameRewardRecyclerAdapter();
        rewardRecyclerAdapter.clearData();

        for (MoleHoleRewardData tempz : TouchGameActivity.users){
            MoleHoleRewardData f3user = new MoleHoleRewardData();
            f3user.setName(tempz.getName());
            f3user.setCount(tempz.getCount());
            rewardRecyclerAdapter.addItem(f3user);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(rewardRecyclerAdapter);
        rewardRecyclerAdapter.notifyDataSetChanged();


        returnBtn = (Button) findViewById(R.id.button_return);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoleHoleRewardData topUser = rewardRecyclerAdapter.getFirstItem();
                Intent intent = new Intent();
                intent.putExtra("name", topUser.getName());
                intent.putExtra("score", topUser.getCount());
                setResult(2222, intent);
                //끝낸다.
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}