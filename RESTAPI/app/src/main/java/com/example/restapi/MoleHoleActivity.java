package com.example.restapi;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class MoleHoleActivity extends AppCompatActivity {

    //for firebase
    static DatabaseReference myRef;
    static ArrayList<MoleHoleRewardData> users = new ArrayList<>();

    TextView time;
    TextView count;
    Button start;
    String userName;

    ImageView[] img_array = new ImageView[9];
    int[] imageID = {R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6, R.id.imageView7, R.id.imageView8, R.id.imageView9};

    final String TAG_ON = "on"; //태그용
    final String TAG_OFF = "off";
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_molehole);

        time = (TextView)findViewById(R.id.time);
        count = (TextView)findViewById(R.id.count);
        start = (Button)findViewById(R.id.start);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.child("Rank").child("MoleHole").addValueEventListener(new ValueEventListener() {
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

        for(int i = 0; i<img_array.length; i++){
            /*int img_id = getResources().getIdentifier("imageView"+i+1, "id", "com.example.pc_20.molegame");*/
            img_array[i] = (ImageView)findViewById(imageID[i]);
            img_array[i].setImageResource(R.drawable.moledown);
            img_array[i].setTag(TAG_OFF);

            img_array[i].setOnClickListener(new View.OnClickListener() { //두더지이미지에 온클릭리스너
                @Override
                public void onClick(View v) {
                    if(((ImageView)v).getTag().toString().equals(TAG_ON)){
                        Toast.makeText(getApplicationContext(), "good", Toast.LENGTH_LONG).show();
                        count.setText(String.valueOf(score++));
                        ((ImageView) v).setImageResource(R.drawable.moledown);
                        v.setTag(TAG_OFF);
                    }else{
                        Toast.makeText(getApplicationContext(), "bad", Toast.LENGTH_LONG).show();
                        if(score<=0){
                            score=0;
                            count.setText(String.valueOf(score));
                        }else{
                            count.setText(String.valueOf(score--));
                        }
                        ((ImageView) v).setImageResource(R.drawable.moleup);
                        v.setTag(TAG_ON);
                    }
                }
            });
        }

        time.setText("30초");
        count.setText("0마리");

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                start.setVisibility(View.GONE);
                count.setVisibility(View.VISIBLE);

                new Thread(new timeCheck()).start();

                for(int i = 0; i<img_array.length; i++){
                    new Thread(new DThread(i)).start();
                }
            }
        });
    }

    Handler onHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            img_array[msg.arg1].setImageResource(R.drawable.moleup);
            img_array[msg.arg1].setTag(TAG_ON); //올라오면 ON태그 달아줌
        }
    };

    Handler offHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            img_array[msg.arg1].setImageResource(R.drawable.moledown);
            img_array[msg.arg1].setTag(TAG_OFF); //내려오면 OFF태그 달아줌

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // 이름 받아오는 액티비티 무사히 갔다올 경우
        if (requestCode==111){
            if(resultCode==1111){
                //이름값 인텐트에서 뽑아서 저장시키기, 이후 RewardActivity로 이동시킴.
                userName = data.getStringExtra("name");
                System.out.println("name : "+userName);
                System.out.println(score);

                // firebase 저장
                MoleHoleRewardData user = new  MoleHoleRewardData(userName, Integer.toString(score));
                myRef.child("Rank").child("MoleHole").child(userName).setValue(user);

                // firebase에서 db 호출.
                moveRewardActivity();
            }
        }
        // ranking 보는 액티비티 무사히 다녀올 경우
        else if (requestCode==222){
            if(resultCode==2222){
                String topUserName = data.getStringExtra("name");
                String topUserScore = data.getStringExtra("score");
                TextView rank1 = (TextView) findViewById(R.id.rank1Text);
                String temp = topUserName+" "+topUserScore + "마리";
                rank1.setText(temp);
                Intent refresh = new Intent(this, MoleHoleActivity.class);
                startActivity(refresh);
                this.finish(); //
            }
        }
    }

    public void moveRewardActivity(){
        Intent intent = new Intent(this, MoleHolePopupActivity.class);
        intent.putExtra("score", Double.toString(score));
        intent.putExtra("name", userName);
        startActivityForResult(intent,222);
    }

    public class DThread implements Runnable{ //두더지를 올라갔다 내려갔다 해줌
        int index = 0; //두더지 번호

        DThread(int index){
            this.index=index;
        }

        @Override
        public void run() {
            while(true){
                try {
                    Message msg1 = new Message();
                    int offtime = new Random().nextInt(5000) + 500 ;
                    Thread.sleep(offtime); //두더지가 내려가있는 시간

                    msg1.arg1 = index;
                    onHandler.sendMessage(msg1);

                    int ontime = new Random().nextInt(1000)+500;
                    Thread.sleep(ontime); //두더지가 올라가있는 시간
                    Message msg2 = new Message();
                    msg2.arg1= index;
                    offHandler.sendMessage(msg2);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            time.setText(msg.arg1 + "초");
        }
    };

    public class timeCheck implements Runnable {
        final int MAXTIME = 30;

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
}
