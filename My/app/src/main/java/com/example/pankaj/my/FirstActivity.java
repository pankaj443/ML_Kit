package com.example.pankaj.my;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    Button button;
    String lan;
    EditText editText;
    String[] country = { "English", "Hindi", "Tamil", "Marathi"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        button = findViewById(R.id.button3);
        editText = findViewById(R.id.editText);

        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),HomeActivity.class);
                intent.putExtra("roll",editText.getText().toString());
                intent.putExtra("language",lan);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
      //  Toast.makeText(getApplicationContext(),country[i] , Toast.LENGTH_LONG).show();
        lan = country[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
