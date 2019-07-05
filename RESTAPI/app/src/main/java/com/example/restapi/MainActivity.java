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
        startActivity(intent);
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        intent.putExtra("user_id", loginResult.getAccessToken().getUserId());
                        System.out.println("userId : " + loginResult.getAccessToken().getUserId() + "#########3");
                        startActivity(intent);
                    }

                    @Override
                    public void onCancel() {
                        startActivity(intent);
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

}
