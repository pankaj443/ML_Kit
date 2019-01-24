package com.example.pankaj.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button button;
    Button button2;
    TextView textView;
    Bitmap bitmap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    imageView = (ImageView) findViewById(R.id.imageView);
    textView = (TextView) findViewById(R.id.textView);
    button = (Button) findViewById(R.id.button);
      button2 = (Button) findViewById(R.id.button2);

      button2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              pickimage();
          }
      });
      button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              FirebaseVisionLabelDetectorOptions options =
                      new FirebaseVisionLabelDetectorOptions.Builder()
                              .setConfidenceThreshold(0.7f)
                              .build();
              final String[] aalabels = {" "};
              FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

              FirebaseVisionLabelDetector detector = FirebaseVision.getInstance()
                      .getVisionLabelDetector(options);

              Task<List<FirebaseVisionLabel>> result =
                      detector.detectInImage(image)
                              .addOnSuccessListener(
                                      new OnSuccessListener<List<FirebaseVisionLabel>>() {
                                          @Override
                                          public void onSuccess(List<FirebaseVisionLabel> labels) {
                                              for (FirebaseVisionLabel label: labels) {

                                                  String text = label.getLabel();
                                                  aalabels[0] += text + ",";

                                                  String entityId = label.getEntityId();
                                                  float confidence = label.getConfidence();
                                              }
                                              textView.setText(aalabels[0]);
                                          }
                                      })
                              .addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {

                                  }
                              });


          }
      });

    }
    public void pickimage(){

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK){

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);

                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
