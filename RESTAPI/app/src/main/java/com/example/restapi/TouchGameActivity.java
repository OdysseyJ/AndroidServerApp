package com.example.restapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TouchGameActivity extends AppCompatActivity {

    static DatabaseReference myRef;
    static ArrayList<MoleHoleRewardData> users = new ArrayList<>();

    TextView count;
    TextView time;
    Button touchButton;
    Button start;
    int countvalue;

    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_game);

        count = (TextView) findViewById(R.id.count);
        touchButton = (Button) findViewById(R.id.touchbutton);
        time = (TextView) findViewById(R.id.time);
        start = (Button) findViewById(R.id.start);
        countvalue = 0;
        count.setText("0");
        time.setText("10초");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.child("Rank").child("Touch").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot temp : dataSnapshot.getChildren()){
                    MoleHoleRewardData user = temp.getValue(MoleHoleRewardData.class);
                    user.setName(user.getName());
                    user.setCount(user.getCount());
                    users.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        touchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countvalue++;
                count.setText(Integer.toString(countvalue));
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start.setVisibility(View.GONE);
                touchButton.setVisibility(View.VISIBLE);
                new Thread(new timeCheck()).start();
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            time.setText(msg.arg1 + "초");
        }
    };

    public class timeCheck implements Runnable {
        final int MAXTIME = 10;

        @Override
        public void run() {
            for (int i = MAXTIME; i >= 0; i--) {
                Message msg = new Message();
                msg.arg1 = i;
                handler.sendMessage(msg);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Intent intent = new Intent(getBaseContext(), NamePopupActivity.class);
            startActivityForResult(intent,111);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // 이름 받아오는 액티비티 무사히 갔다올 경우
        if (requestCode==111){
            if(resultCode==1111){
                //이름값 인텐트에서 뽑아서 저장시키기, 이후 RewardActivity로 이동시킴.
                userName = data.getStringExtra("name");

                // firebase 저장
                MoleHoleRewardData user = new  MoleHoleRewardData(userName, Integer.toString(countvalue));
                myRef.child("Rank").child("Touch").child(userName).setValue(user);

                // firebase에서 db 호출.
                moveRewardActivity();
            }
        }
        // ranking 보는 액티비티 무사히 다녀올 경우
        else if (requestCode==222){
            if(resultCode==2222){
//                String topUserName = data.getStringExtra("name");
//                String topUserScore = data.getStringExtra("score");
//                TextView rank1 = (TextView) findViewById(R.id.rank1Text);
//                String temp = topUserName+" "+topUserScore + "마리";
//                rank1.setText(temp);
                Intent refresh = new Intent(this, TouchGameActivity.class);
                startActivity(refresh);
                this.finish(); //
            }
        }
    }

    public void moveRewardActivity(){
        Intent intent = new Intent(this, MoleHolePopupActivity.class);
        intent.putExtra("score", countvalue);
        intent.putExtra("name", userName);
        startActivityForResult(intent,222);
    }
}
