package com.example.bottomnavigation;

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

public class PopupActivity extends AppCompatActivity {

    RewardRecyclerAdapter rewardRecyclerAdapter;

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
        String sec = intent.getStringExtra("sec");
        String mil = intent.getStringExtra("mil");

        rewardRecyclerAdapter = new RewardRecyclerAdapter();
        rewardRecyclerAdapter.clearData();

        for (RewardData tempz : PageThree.users){
            RewardData f3user = new RewardData();
            f3user.setName(tempz.getName());
            f3user.setSec(tempz.getSec());
            f3user.setMil(tempz.getMil());
            System.out.println("#######################");
            System.out.println(tempz.getName());
            System.out.println(tempz.getSec());
            System.out.println(tempz.getMil());
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
                RewardData topUser = rewardRecyclerAdapter.getFirstItem();
                Intent intent = new Intent();
                intent.putExtra("name", topUser.getName());
                intent.putExtra("sec", topUser.getSec());
                intent.putExtra("mil", topUser.getMil());
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
