package com.example.pankaj.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateRemoteModel;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button button;
    Button button2;
    //TextView textView;
    Bitmap bitmap;

    ArrayAdapter adapter;
    ArrayList<String> mobileArray = new ArrayList<>();
    ArrayList<Integer> fla = new ArrayList<>();
    int j=0;
    FirebaseTranslatorOptions options =
            new FirebaseTranslatorOptions.Builder()
                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                    .setTargetLanguage(FirebaseTranslateLanguage.TA)
                    .build();
    final FirebaseTranslator englishGermanTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);

    FirebaseTranslatorOptions options2 =
            new FirebaseTranslatorOptions.Builder()
                    .setSourceLanguage(FirebaseTranslateLanguage.TA)
                    .setTargetLanguage(FirebaseTranslateLanguage.EN)
                    .build();
    final FirebaseTranslator englishGermanTranslator2 = FirebaseNaturalLanguage.getInstance().getTranslator(options2);
    //String text = "hello";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
    imageView = (ImageView) findViewById(R.id.imageView);
   // textView = (TextView) findViewById(R.id.textView);
    button = (Button) findViewById(R.id.button);
      button2 = (Button) findViewById(R.id.button2);
       // textView.setText(text);
      button2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              pickimage();
          }
      });


          adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, mobileArray);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              //  Toast.makeText(MainActivity.this, , Toast.LENGTH_SHORT).show();
                starttranslate(mobileArray.get(i),i,fla.get(i));
            }
        });
//      textView.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View view) {
//              starttranslate((String) textView.getText());
//          }
//      });
      button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
//              FirebaseVisionLabelDetectorOptions options =
//                      new FirebaseVisionLabelDetectorOptions.Builder()
//                              .setConfidenceThreshold(0.7f)
//                              .build();
              final String[] aalabels = {" "};
              FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

              FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                      .getOnDeviceImageLabeler();

              labeler.processImage(image)
                      .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                          @Override
                          public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                              // Task completed successfully
                              // ...
                              for (FirebaseVisionImageLabel label: labels) {

                                  String text = label.getText();
                                 mobileArray.add(text);
                                 fla.add(0);
                                 // aalabels[0] += text + ",";

                                  String entityId = label.getEntityId();
                                  float confidence = label.getConfidence();
                              }
                              adapter.notifyDataSetChanged();
                              //textView.setText(aalabels[0]);
                          }
                      })
                      .addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              // Task failed with an exception
                              // ...
                          }
                      });



          }
      });

        Log.i("MSGNM","onit");



        FirebaseModelManager modelManager = FirebaseModelManager.getInstance();
        modelManager.getDownloadedModels(FirebaseTranslateRemoteModel.class)
                .addOnSuccessListener(new OnSuccessListener<Set<FirebaseTranslateRemoteModel>>() {
                    @Override
                    public void onSuccess(Set<FirebaseTranslateRemoteModel> models) {
                        // ...
                        //Log.i("MSGNM",models.toArray().toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error.
                    }
                });


        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
              .build();
        englishGermanTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                // Model downloaded successfully. Okay to start translating.
                                // (Set a flag, unhide the translation UI, etc.)
                                //flag[0] = 1;
                                Log.i("MSGNM","Model downloaded successfully. Okay to start translating.");
                                //starttranslate();


                               // Toast.makeText(MainActivity.this, "Model downloaded successfully. Okay to start translating.", Toast.LENGTH_SHORT).show();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be downloaded or other internal error.
                                // ...
                                 Log.i("MSGNM","fail");
                            }
                        });

     //   Log.i("MSGNM", String.valueOf(flag[0]));
//       if(flag[0] == 1){
//


        }

    private void starttranslate(String s, final int i,int flag) {

       if (flag== 0)
       {englishGermanTranslator.translate(s)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {
                                // Translation successful.
                                //textView.setText(translatedText);
                                mobileArray.set(i,translatedText);
                               fla.set(i,1);
                                adapter.notifyDataSetChanged();
                               // Log.i("MSGNM",translatedText);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error.
                                // ...
                                Log.i("MSGNM","Error");
                            }
                        });}
       else{
           englishGermanTranslator2.translate(s)
                   .addOnSuccessListener(
                           new OnSuccessListener<String>() {
                               @Override
                               public void onSuccess(@NonNull String translatedText) {
                                   // Translation successful.
                                   //textView.setText(translatedText);
                                   mobileArray.set(i,translatedText);
                                   fla.set(i,0);
                                   adapter.notifyDataSetChanged();
                                   // Log.i("MSGNM",translatedText);
                               }
                           })
                   .addOnFailureListener(
                           new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   // Error.
                                   // ...
                                   Log.i("MSGNM","Error");
                               }
                           });
       }
    }




    // }
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
