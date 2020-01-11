package com.example.pankaj.my;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class StuAttendence extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView pr,tot,per;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_attendence);
        Intent intent = getIntent();
        String roll = intent.getStringExtra("roll");

        pr = findViewById(R.id.hin);
        per = findViewById(R.id.mat);
        tot = findViewById(R.id.eng);

        db.collection("StudentAttendence").document(roll)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            String percent = (String) document.get("percentage");
                            String total = (String) document.get("total");
                            String present = (String) document.get("present");

                            per.setText(percent);
                            pr.setText(present);
                            tot.setText(total);

                        }
                    }
                });
    }
}
