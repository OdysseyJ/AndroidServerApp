package com.example.restapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TouchGameActivity extends AppCompatActivity {

    TextView count;
    Button touchButton;
    int countvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_game);

        count = (TextView) findViewById(R.id.count);
        touchButton = (Button) findViewById(R.id.touchbutton);
        countvalue = 0;
        count.setText(countvalue);

        touchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countvalue++;
                count.setText(countvalue);
            }
        });
    }
}
