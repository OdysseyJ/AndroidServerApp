package com.example.restapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import com.example.restapi.databinding.ActivityOnetofiftyBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class OneToFiftyActivity extends AppCompatActivity {

    RewardRecyclerAdapter rewardRecyclerAdapter;

    // for firebase database
    static DatabaseReference myRef;
    static ArrayList<RewardData> users = new ArrayList<>();
    String userName_ref;
    String userSec_ref;
    String userMil_ref;

    Vector<Integer> _1to25, _26to50;
    ActivityOnetofiftyBinding binding;
    ItemAdapter adapter;
    Observable<Long> duration;
    Disposable disposable;
    int now;
    Handler handler;

    // 유저 게임 결과
    long second, msecond;
    long milisecond, mmilisecond;
    String userName;

    int timeset = 0;

    public OneToFiftyActivity() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.child("Rank").child("OneToFifty").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot temp : dataSnapshot.getChildren()){
                    RewardData user = temp.getValue(RewardData.class);
                    user.setName(user.getName());
                    user.setSec(user.getSec());
                    user.setMil(user.getMil());
                    users.add(user);
                    System.out.println("##################snapshot############");
                    System.out.println(user.getName());
                    System.out.println(user.getSec());
                    System.out.println(user.getMil());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        binding = DataBindingUtil.setContentView(this, R.layout.activity_onetofifty);
        handler = new Handler();
        timer();
        init();
        binding.retryBtn.setOnClickListener(view -> {
            stop();
            msecond = second;
            mmilisecond = milisecond;
            timeset = 1;
            timer();
            init();
        });
    }

    private void timer() {
        duration = Observable.interval(10, TimeUnit.MILLISECONDS)
                .map(milli -> milli += 1L);
        disposable = duration.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    long sec = s / 100;
                    long milli = s % 100;
                    if (timeset ==0) {
                        second = sec;
                        milisecond = milli;
                    }
                    runOnUiThread(() -> binding.timeTxtView.setText(sec + " : " + milli));
                });
    }

    private void stop() {
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(this.disposable);
        disposable.dispose();
    }

    // 다른 액티비티 다녀온후 결과 처리
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // 이름 받아오는 액티비티 무사히 갔다올 경우
        if (requestCode==1){
            if(resultCode==1111){
                //이름값 인텐트에서 뽑아서 저장시키기, 이후 RewardActivity로 이동시킴.
                userName = data.getStringExtra("name");

                // firebase 저장
                RewardData user = new  RewardData(userName, Long.toString(msecond), Long.toString(mmilisecond));
                myRef.child("Rank").child("OneToFifty").child(userName).setValue(user);



                // firebase에서 db 호출.
                moveRewardActivity();
            }
        }
        // ranking 보는 액티비티 무사히 다녀올 경우
        else if (requestCode==2){
            if(resultCode==2222){
                String topUserName = data.getStringExtra("name");
                String topUserSec = data.getStringExtra("sec");
                String topUserMil = data.getStringExtra("mil");
                TextView rank1 = (TextView) findViewById(R.id.rank1Text);
                String temp = topUserName+" "+topUserSec + "초 " + topUserMil;
                rank1.setText(temp);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }
    private void init() {
        binding.gridView.removeOnItemTouchListener(select);
        now = 1;
        timeset = 0;
        _1to25 = new Vector<>();
        _26to50 = new Vector<>();
        binding.gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                int length = binding.gridView.getWidth() / 5 - 10;
                adapter.setLength(length, length);
                adapter.notifyDataSetChanged();

                binding.gridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        for (int i = 1; i <= 25; i++) {
            _1to25.add(i);
            _26to50.add(i + 25);
        }
        binding.gridView.setLayoutManager(new GridLayoutManager(this, 5));
        adapter = new ItemAdapter(this);
        binding.gridView.setAdapter(adapter);
        for (int i = 1; i <= 25; i++) {
            int rand = (int) (Math.random() * _1to25.size());
            adapter.init1to25(_1to25.get(rand));
            _1to25.remove(rand);
            adapter.notifyDataSetChanged();
        }
        binding.gridView.addOnItemTouchListener(select);
    }

    private RecyclerView.OnItemTouchListener select = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView parent, @NonNull MotionEvent evt) {
            try {
                switch (evt.getAction()) {
                    case MotionEvent.ACTION_UP:
                        Button child = (Button) parent.findChildViewUnder(evt.getX(), evt.getY());
                        if (child != null) {
                            int selected = Integer.parseInt(child.getText().toString());
                            if (selected == now) {
                                int position = parent.getChildAdapterPosition(child);
                                Log.e("position", " => " + selected);
                                if (selected >= 26 && selected == now)
                                    adapter.setUpVisible(position);
                                now++;
                                if (_26to50 != null) {
                                    int rand = (int) (Math.random() * _26to50.size());
                                    adapter.update26to50(position, _26to50.get(rand));
                                    _26to50.remove(rand);
                                    if (_26to50.size() == 0) _26to50 = null;
                                }
                                adapter.notifyItemChanged(position);
                            } else {
                                Toast.makeText(getApplicationContext(), "순서대로 선택해주세요.", Toast.LENGTH_SHORT).show();
                            }
                            if (now == 51) {

                                stop();
                                msecond = second;
                                mmilisecond = milisecond;
                                timeset = 1;
                                moveNameInputActivity();
                            }
                        }
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }

    };

    // 이름값 받아오기
    public void moveNameInputActivity(){
        //완료되면 명예의전당 팝업창 띄우기
        Intent intent = new Intent(this, NamePopupActivity.class);
        startActivityForResult(intent,1);
    }

    // 이름값 받아온거 + 시간초까지 같이 넘겨서 저장시키고
    // TOP 플레이어 정보를 받아온다.
    public void moveRewardActivity(){
        Intent intent = new Intent(this, PopupActivity.class);

        intent.putExtra("sec", Double.toString(second));
        intent.putExtra("mil", Double.toString(milisecond));
        intent.putExtra("name", userName);
        startActivityForResult(intent,2);
    }



}