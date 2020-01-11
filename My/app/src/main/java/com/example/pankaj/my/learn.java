package com.example.pankaj.my;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class learn extends AppCompatActivity {
TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        textView = findViewById(R.id.textView9);
        Intent intent= getIntent();
        String v=intent.getStringExtra("learn");
        textView.setText(v);

    }
}
