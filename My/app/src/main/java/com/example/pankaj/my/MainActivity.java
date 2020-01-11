package com.example.pankaj.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button button;
    Button button2;
    //TextView textView;
    Bitmap bitmap;
    String language,roll;
    String temp;
    FirebaseTranslator englishGermanTranslator;
    FirebaseTranslator englishGermanTranslator2;
    ArrayAdapter adapter;
    ArrayList<String> mobileArray = new ArrayList<>();
    ArrayList<Integer> fla = new ArrayList<>();
    int j=0;
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


      Intent intent = getIntent();
        roll = intent.getStringExtra("roll");
        language= intent.getStringExtra("language");

        getLangCode(language);

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new CallbackTask().execute(dictionaryEntries(mobileArray.get(i)));

                return false;
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



//


        }




    private String dictionaryEntries(String wordd) {
        final String language = "en-gb";
        final String word = wordd;
        final String fields = "definitions";
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
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

    private void  getLangCode(String language){
        int langCode;
        switch (language){
            case "Hindi":
               langCode =FirebaseTranslateLanguage.HI;break;
            case "English":
                langCode =FirebaseTranslateLanguage.EN;break;
            case "Tamil":
                langCode =FirebaseTranslateLanguage.TA;break;

            default:
                langCode=0;
        }
        trans(langCode);
    }
    private void  trans(int langCode){

        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(FirebaseTranslateLanguage.EN)
                        .setTargetLanguage(langCode)
                        .build();
        englishGermanTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);

        FirebaseTranslatorOptions options2 =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(langCode)
                        .setTargetLanguage(FirebaseTranslateLanguage.EN)
                        .build();
        englishGermanTranslator2 = FirebaseNaturalLanguage.getInstance().getTranslator(options2);

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
    }


    private class CallbackTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            //TODO: replace with your own app id and app key
            final String app_id = "15dba7db";
            final String app_key = "347cfed8a5af33817ff5ec88203b8c66";
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setRequestProperty("app_id",app_id);
                urlConnection.setRequestProperty("app_key",app_key);

                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                return stringBuilder.toString();

            }
            catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Intent intent = new Intent(getApplicationContext(),DictActivity.class);
            intent.putExtra("definition",result);
            intent.putExtra("roll",roll);
            intent.putExtra("language",language);
            startActivity(intent);
            System.out.println(result);
        }
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
