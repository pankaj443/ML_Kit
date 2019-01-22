package com.example.pankaj.face_detect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ImageView imageView ;
    Button button,button2;
    Bitmap   bitmap;
    Bitmap tempbitmap;
    Canvas canvas;
    Frame frame;
    public void upload(View v){

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
                tempbitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.RGB_565);
                canvas = new Canvas(tempbitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);



        final Paint paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .setMode(FaceDetector.FAST_MODE)
                        .build();

                if (!faceDetector.isOperational())
                {
                    Toast.makeText(MainActivity.this, "Cannot detect!", Toast.LENGTH_SHORT).show();
                    return;
                }

                frame = new Frame.Builder().setBitmap(bitmap).build();

                SparseArray<Face> sparseArray = faceDetector.detect(frame);


                for (int i=0;i<sparseArray.size();i++)
                {

                    Face face = sparseArray.valueAt(i);
                    float x = face.getPosition().x;
                    float y = face.getPosition().y;
                    float x1 = x + face.getWidth();
                    float y1 = y + face.getHeight();
                    RectF rectF = new RectF(x,y,x1,y1);

                    canvas.drawRoundRect(rectF,2,2,paint);

                }
                imageView.setImageBitmap(tempbitmap);



            }
        });


    }
}
