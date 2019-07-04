package com.example.restapi;

import android.content.Intent;
import android.os.AsyncTask;



import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.net.HttpURLConnection;

import java.net.MalformedURLException;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView tvData, facebook;
    private LoginButton loginButton;

    CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        facebook = (TextView) findViewById(R.id.facebook);
        loginButton.setReadPermissions("email");
        Intent intent = new Intent(this, HomeActivity.class);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        startActivityForResult(intent, 1);
                        facebook.setText("completed");
                    }

                    @Override
                    public void onCancel() {
                        startActivityForResult(intent,2 );
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        exception.getStackTrace();
                        //Toast.makeText(this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

        tvData = (TextView)findViewById(R.id.textView);

        Button btn = (Button)findViewById(R.id.httpTest);

        //버튼이 눌리면 아래 리스너가 수행된다.

        btn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                //아래 링크를 파라미터를 넘겨준다는 의미.

                new JSONTask().execute("http://143.248.36.59:3000/users");

            }

        });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            System.out.println("###########################3");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class JSONTask extends AsyncTask<String, String, String>{

        @Override

        protected String doInBackground(String... urls) {

            try {

                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("user_id", "androidTest");

                jsonObject.accumulate("name", "yun");

                HttpURLConnection con = null;

                BufferedReader reader = null;

                try{

                    //URL url = new URL(“http://192.168.25.16:3000/users“);

                    URL url = new URL(urls[0]);//url을 가져온다.

                    con = (HttpURLConnection) url.openConnection();

                    con.connect();//연결 수행

                    //입력 스트림 생성

                    InputStream stream = con.getInputStream();

                    //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.

                    reader = new BufferedReader(new InputStreamReader(stream));

                    //실제 데이터를 받는곳

                    StringBuffer buffer = new StringBuffer();

                    //line별 스트링을 받기 위한 temp 변수

                    String line = "";

                    //아래라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.

                    while((line = reader.readLine()) != null){

                        buffer.append(line);

                    }

                    //다 가져오면 String 형변환을 수행한다. 이유는 protected String doInBackground(String… urls) 니까

                    return buffer.toString();

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

            tvData.setText(result);

        }

    }

}
