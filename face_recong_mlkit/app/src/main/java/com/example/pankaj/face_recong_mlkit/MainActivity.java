package com.example.pankaj.face_recong_mlkit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.pankaj.face_recong_mlkit.Overlay.GraphicOverlay;
import com.example.pankaj.face_recong_mlkit.Overlay.RectOver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Bitmap bitmap;
    Button detect;
    GraphicOverlay graphic;

    public void pick(){

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
                bitmap = Bitmap.createScaledBitmap(bitmap,imageView.getWidth(),imageView.getHeight(),false);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        detect = (Button) findViewById(R.id.detect);
        graphic = (GraphicOverlay) findViewById(R.id.graphic);

        final Button pick = (Button) findViewById(R.id.pick);

        Button detect = (Button) findViewById(R.id.detect);

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pick();

            }
        });

        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphic.clear();
                facedetect(bitmap);
            }
        });


    }

    public void facedetect(Bitmap bitmap){


        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionFaceDetectorOptions options = new  FirebaseVisionFaceDetectorOptions.Builder().build();

        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector(options);

        detector.detectInImage(firebaseVisionImage)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
            result(firebaseVisionFaces);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void result(List<FirebaseVisionFace> firebaseVisionFaces) {

        for (FirebaseVisionFace face : firebaseVisionFaces)
        {

            Rect bound = face.getBoundingBox();

            RectOver rectOver = new RectOver(graphic,bound);
            graphic.add(rectOver);
        }

    }

}
