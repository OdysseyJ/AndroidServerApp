package com.example.restapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageClickDialog extends Activity implements View.OnClickListener {

    private Button deleteButton;
    private Button exitButton;
    private ImageView image;

    private String name;
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.imageclick_dialog);

        //init
        deleteButton = (Button)findViewById(R.id.delete);
        exitButton = (Button)findViewById(R.id.exit);
        image = (ImageView)findViewById(R.id.imageView);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        byte[] arr = getIntent().getByteArrayExtra("photo");
        photo = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        image.setImageBitmap(photo);

        System.out.println("############################333@@@@@@@@@@@@@@@@@");
        System.out.println("name:" + name);
        System.out.println("photo:" + photo);

        deleteButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.delete:
                String http = "http://143.248.36.59:8080/api/gallery/"+name;
                new JSONTaskDeleteObj().execute(http);
                Intent intent = new Intent();
                intent.putExtra("name", name);
                setResult(RESULT_OK, intent);
                Toast.makeText(this, "이미지가 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                this.finish();
                break;
            case R.id.exit:
                this.finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

        }
    }

    //new JSONTask().execute("http://143.248.36.59:8080/api/gallery/add");로 쓴다. (바디필요)
    public class JSONDeleteTask extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {
                HttpURLConnection con = null;

                BufferedReader reader = null;

                try{

                    // URL url = new URL("http://192.168.25.16:8080/api/gallery/add");

                    URL url = new URL(urls[0]);//url을 가져온다.

                    Log.v("태그", url.toString());

                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//set request method
                    connection.setRequestMethod("DELETE");
                    connection.setRequestProperty("Content-Type", "application/xml");
                    connection.setDoOutput(true);
                    con.setUseCaches (false);

                    con.connect();//연결 수행


                    return "";
                    //아래는 예외처리 부분이다.

                } catch (MalformedURLException e){

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();

                } finally {

                    //종료가 되면 disconnect메소드를 호출한다.

                    if(con != null){

                        con.disconnect();

                    }

                    try {

                        //버퍼를 닫아준다.

                        if(reader != null){

                            reader.close();

                        }

                    } catch (IOException e) {

                        e.printStackTrace();

                    }

                }//finally 부분

            } catch (Exception e) {

                e.printStackTrace();

            }

            return null;

        }

        //doInBackground메소드가 끝나면 여기로 와서 텍스트뷰의 값을 바꿔준다.

        @Override

        protected void onPostExecute(String result) {

            super.onPostExecute(result);

        }

    }


    //하나의 contact 삭제
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