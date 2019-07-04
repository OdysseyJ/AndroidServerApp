package com.example.bottomnavigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NamePopupActivity extends AppCompatActivity {

    RewardRecyclerAdapter rewardRecyclerAdapter;

    Button okBtn;

    private EditText nameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.namepopup);

        //UI 객체생성
        nameText = (EditText) findViewById(R.id.rankName);
        okBtn = (Button) findViewById(R.id.okButton);

        //버튼 눌렀을때
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 반환하기.
                String data = nameText.getText().toString();
                // 이름에 아무것도 입력 안했을 경우
                if (data.equals("")){
                    Toast.makeText(getApplicationContext(), "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                //이름값이 제대로 들어온 경우
                else {
                    Intent intent = new Intent();
                    intent.putExtra("name", data);
                    setResult(1111, intent);

                    //끝낸다.
                    finish();
                }
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
