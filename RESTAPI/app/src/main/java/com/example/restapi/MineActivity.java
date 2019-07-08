package com.example.restapi;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MineActivity extends AppCompatActivity implements OnClickListener, OnGlobalLayoutListener  {

    private int mMineCount = 0;
    private int mTick = -1;
    private TextView mMineCounter;
    private TextView mTimerView;
    private Timer mTimer = new Timer();
    private TimerTask mTimerTask;
    private View mRootView;
    private static int mCellSize=80;
    private float mMineRatio = 0.2f;

    private static MineActivity sMainActivity;

    private enum GAME_MODE {NOT_STARTED, GAMING, ENDED};
    private GAME_MODE mGameMode = GAME_MODE.NOT_STARTED;

    private static boolean mInitialized = false;
    public enum DIFFICULTY { EASY, MEDIUM, HARD };
    public enum CELL_SIZE { SMALL, MEDIUM, LARGE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        sMainActivity = this;

        if (savedInstanceState == null) { //�ʱ� ������Ʈ ������ ��Ƽ��Ƽ�� ���� ���尪�� NULL
            //ID �̸��� container(Frame Layout)�� ���̾ƿ� �ȿ�  �����׸�Ʈ�� ���̱� ���� �Ʒ� �ڵ� ���
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();

        }

    }

    //PlaceholderFragment Ŭ������ ���� ���� Ŭ����(Static Nested Class) �� �����Ͽ�, �ܺο����� �����Ҽ� �ֵ��� �����Ѵ�.
    public static class PlaceholderFragment extends Fragment {

        //������
        public PlaceholderFragment() {
        }

        //�����׸�Ʈ�� ȭ�鿡 ������ View �� �����Ѵ�.
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.mines_main, container, false);

            v.getViewTreeObserver().addOnGlobalLayoutListener(sMainActivity);
            return v;
        }
    }

    public void setMarkerMode(View v) {
        Drawable d;
        MineButton.BombMode bombMode = MineButton.toggleBombMode();
        if (bombMode == MineButton.BombMode.MARK)
            d = getApplication().getResources().getDrawable(R.drawable.marker);
        else
            d = getApplication().getResources().getDrawable(R.drawable.bomb);

        ((ImageButton)v).setImageDrawable(d);
    }

    public void openSetup(View v) {

        DIFFICULTY df = mMineRatio <= 0.15f ? DIFFICULTY.EASY : mMineRatio <= 0.20f ? DIFFICULTY.MEDIUM : DIFFICULTY.HARD;
        CELL_SIZE sz = mCellSize <= 70 ? CELL_SIZE.SMALL : mCellSize <= 80 ? CELL_SIZE.MEDIUM : CELL_SIZE.LARGE;
        SetupDialog dlg = new SetupDialog(this, df, sz);
        dlg.setTitle(v.getResources().getString(R.string.setup));
        dlg.setOwnerActivity(this);
        dlg.show();

    }

    /** Called when the user clicks the Send button */
    public void startNewGame(View view) {
        mGameMode = GAME_MODE.GAMING;

        // Do something in response to button
        if (mTimer != null) mTimer.cancel();
        if (mTimerTask != null) mTimerTask.cancel();

        MineButton.initAllMines(mMineCount);

        mTick = 0;

        startTimer();

        setMineCounter(mMineCount);
        setTimer();
    }

    private void startTimer() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        mTick++;
                        mTimerView.setText(String.format("%03d", mTick));
                    }
                });
            }

        };
        mTimer.scheduleAtFixedRate (mTimerTask, 1000, 1000);
    }

    public void stopGame() {
        stopTimer();

        mGameMode = GAME_MODE.ENDED;

        if (MineButton.getFoundCount() == mMineCount) {  // found all
            AlertDialog.Builder ad = new AlertDialog.Builder(mRootView.getContext());
            ad.setTitle(mRootView.getContext().getResources().getString(R.string.result_title));
            ad.setPositiveButton("OK", null);
            ad.setCancelable(false);
            ad.setMessage(String.format("%d " + mRootView.getContext().getResources().getString(R.string.sec), mTick));
            ad.create().show();
        }

    }

    private void setTimer() {
        mTimerView.setText(String.format("%03d", mTick));
    }

    private void setMineCounter(int count) {
        mMineCounter.setText(String.format("%03d", count));
    }

    private void stopTimer() {
        if (mTimerTask != null) mTimerTask.cancel();
        mTimer.cancel();
        mTimer.purge();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        stopTimer();
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        stopTimer();
        mInitialized = false;
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        stopTimer();
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (mTick >= 0)
            startTimer();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    public void onClick(View v) {

        if (mGameMode == GAME_MODE.NOT_STARTED)  // if not started, start new game
            startNewGame(v);

        if (mGameMode != GAME_MODE.GAMING) // if not gaming, just ignore
            return;

        MineButton btn = (MineButton)v;
        boolean bRet = btn.clickMine();  // MineButton class handles click

        setMineCounter(mMineCount - btn.getMarkedCount()); // display remaining mines count
        if (!bRet)   // if game ends with success or failure, stop the game
            stopGame();

    }

    public void setup(DIFFICULTY df, CELL_SIZE sz) {

        mCellSize = sz == CELL_SIZE.SMALL ? 70 : sz == CELL_SIZE.MEDIUM ? 80 : 90;
        mMineRatio = (df == DIFFICULTY.EASY ? 0.15f : df == DIFFICULTY.MEDIUM ? 0.20f : 0.25f);

        initGame();

    }

    private void initGame() {// �������� ���� �����. (ũ��, ��/������ ���ϰ� ���ڸ� ���ܵд�

        LinearLayout myLayout = (LinearLayout)mRootView.findViewById(R.id.panel);
        int w = myLayout.getWidth();
        int h = myLayout.getHeight();

        int cols = w / mCellSize;
        int rows = (h - 40) / mCellSize;
        mMineCount = (int)((cols * rows) * mMineRatio);// ���ڰ���

        myLayout.removeAllViews();

        MineButton.resetAllMines();

        for(int i=0; i<rows; i++) {// �� �࿡ �ϳ��� LinearLayout�� �����
            LinearLayout layout = new LinearLayout(mRootView.getContext());
            layout.setPadding(0, 0, 0, 0);
            layout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            myLayout.addView(layout);
            for(int j=0; j<cols; j++) {// �� ���� �ϳ��� ��ư�� �����
                MineButton myButton = new MineButton(mRootView.getContext(), i, j);
                myButton.setLayoutParams(new RelativeLayout.LayoutParams(mCellSize, mCellSize));
                myButton.setPadding(0, 0, 0, 0);
                myButton.setOnClickListener(this); // ��ư�� OnClickListener�� this(MainActivity)�� ����Ѵ�
                layout.addView(myButton);
            }

        }
        MineButton.initAllMines(mMineCount);// ���� ���ڸ� ���� �����

    }

    @Override
    public void onGlobalLayout() {

        if (mInitialized) return;

        mInitialized = true;

        mRootView = this.findViewById(R.id.mines_main);
        mMineCounter = (TextView)mRootView.findViewById(R.id.mine_counter);
//        Typeface tf = Typeface.createFromAsset(mRootView.getContext().getAssets(),"fonts/digital-7.ttf");
//        mMineCounter.setTypeface(tf);
        mTimerView = (TextView)mRootView.findViewById(R.id.timer);
//        mTimerView.setTypeface(tf);

        initGame();

    }

}