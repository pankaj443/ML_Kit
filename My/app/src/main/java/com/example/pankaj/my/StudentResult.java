package com.example.pankaj.my;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentResult extends AppCompatActivity {

    Button learneng,learnhin,learnmat;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView hindi,english,maths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result);

        final Intent intent = getIntent();
        String roll = intent.getStringExtra("roll");
        learneng = findViewById(R.id.learneng);
        learnhin= findViewById(R.id.learnhin);
        learnmat = findViewById(R.id.learnmat);

        learneng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),learn.class);
              //  String b =  R.string.learneng;
              //  Log.i("MLOP",);
                intent1.putExtra("learn",getResources().getString(R.string.learneng));
                startActivity(intent1);
            }
        });
        learnmat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        learnhin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),learn.class);
                //  String b =  R.string.learneng;
                //  Log.i("MLOP",);
                intent1.putExtra("learn",getResources().getString(R.string.learnhin));
                startActivity(intent1);
            }
        });

        hindi = findViewById(R.id.hin);
        maths = findViewById(R.id.mat);
        english = findViewById(R.id.eng);

        db.collection("StudentResult").document(roll)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            String h = (String) document.get("Hindi");
                            String e = (String) document.get("English");
                            String m = (String) document.get("Maths");

                            hindi.setText(h);
                            english.setText(e);
                            maths.setText(m);

                        }
                    }
                });

    }
}
