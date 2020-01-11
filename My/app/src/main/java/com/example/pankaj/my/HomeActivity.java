package com.example.pankaj.my;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {


    Button atten,result,learn;
    String language,roll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        atten = findViewById(R.id.attendence);
        result = findViewById(R.id.result);
        learn = findViewById(R.id.learn);

        Intent intent=getIntent();
        roll = intent.getStringExtra("roll");
        language= intent.getStringExtra("language");

        Log.i("MSGNM",roll+language);

        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("roll",roll);
                intent.putExtra("language",language);
                startActivity(intent);
            }
        });
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),StudentResult.class);
                intent.putExtra("roll",roll);

                startActivity(intent);
            }
        });
        atten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),StuAttendence.class);
                intent.putExtra("roll",roll);

                startActivity(intent);
            }
        });
    }
}
