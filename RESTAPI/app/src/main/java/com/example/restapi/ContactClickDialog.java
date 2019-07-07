package com.example.restapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ContactClickDialog extends Activity implements View.OnClickListener {

    private Button cancel_contact_delete;
    private Button confirm_contact_delete;

    private ImageView profile_photo_delete;
    private TextView name_delete;
    private TextView phonenum_delete;

    private String user_name;
    private String user_phonenum;
    private Bitmap user_photo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v("디버그", "ContactClickDialog onCreate");

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.contactclick_dialog);

        cancel_contact_delete = findViewById(R.id.cancel_contact_delete);
        confirm_contact_delete = findViewById(R.id.confirm_contact_delete);

        profile_photo_delete = findViewById(R.id.profile_photo_delete);
        name_delete = findViewById(R.id.name_delete);
        phonenum_delete = findViewById(R.id.phonenum_delete);

        Intent intent = getIntent();
        user_name = intent.getStringExtra("name");
        user_phonenum = intent.getStringExtra("phonenum");
        byte[] arr = getIntent().getByteArrayExtra("photo");
        user_photo = BitmapFactory.decodeByteArray(arr, 0, arr.length);

        profile_photo_delete.setImageBitmap(user_photo);
        name_delete.setText(user_name);
        phonenum_delete.setText(user_phonenum);

        cancel_contact_delete.setOnClickListener(this);
        confirm_contact_delete.setOnClickListener(this);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.confirm_contact_delete:
                Log.v("디버그", user_name);

                String http = "http://143.248.36.59:8080/api/contact/"+user_name;
                new JSONTaskDeleteObj().execute(http);
                Intent intent = new Intent();
                intent.putExtra("name", user_name);
                setResult(RESULT_OK, intent);
                Toast.makeText(this, "연락처가 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                this.finish();
                break;
            case R.id.cancel_contact_delete:
                this.finish();
                break;

        }
    }

    public class JSONTaskDeleteObj extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String urls[]) {
            try {
                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("DELETE");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
}
