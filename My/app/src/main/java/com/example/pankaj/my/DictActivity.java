package com.example.pankaj.my;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class DictActivity extends AppCompatActivity {
    TextView word,definition,typee;
    int dflag=0;
    String definiti = "";
    int tflag =0;
    String heading="";
    String language,roll;
    TextToSpeech textToSpeech;
    FirebaseTranslator englishGermanTranslator;
    FirebaseTranslator englishGermanTranslator2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dict);
        typee = findViewById(R.id.type);
        definition = findViewById(R.id.definition);

        textToSpeech = new TextToSpeech(DictActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                if (i!=TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        Button speak = findViewById(R.id.speakword);
        Button speakdef = findViewById(R.id.speakdefinition);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tex= heading;
                textToSpeech.speak(tex,TextToSpeech.QUEUE_FLUSH,null);
            }
        });

        speakdef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tex= definiti;
                textToSpeech.speak(tex,TextToSpeech.QUEUE_FLUSH,null);
            }
        });
        Intent intent = getIntent();
        roll = intent.getStringExtra("roll");
        language= intent.getStringExtra("language");
        String s = intent.getStringExtra("definition");

        getLangCode(language);

        definition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starttranslate(String.valueOf(definition.getText()),dflag);
            }
        });
        typee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starttranslatety(String.valueOf(typee.getText()),tflag);
            }
        });

        //textView = findViewById(R.id.textView);
        word = findViewById(R.id.word);


        String type="";
       // Intent intent = getIntent();

      Log.i("MSGNM",s);

        try {
          //  Log.i("MSGNM","pankaj");
            JSONObject jsonObject = new JSONObject(s);
            heading = jsonObject.getString("id");

            word.setText(heading);
            //JSONObject object = jsonObject.getJSONObject("results");
            JSONArray JA = jsonObject.getJSONArray("results");

            for(int i =0 ;i <JA.length(); i++){

                JSONObject JO = (JSONObject) JA.get(i);
                String id = (String) JO.get("id");
                String lan = (String) JO.get("language");
            //    Log.i("MSGNM",id+lan);


                JSONArray JAa = JO.getJSONArray("lexicalEntries");
                for(int j =0 ;j <JAa.length(); j++){
                    JSONObject JOo = (JSONObject) JAa.get(j);

                    JSONArray JAaa = JOo.getJSONArray("entries");
                    for(int k =0 ;k<JAaa.length(); k++){
                        JSONObject JOoo = (JSONObject) JAaa.get(k);
                        JSONArray JAaaa = JOoo.getJSONArray("senses");
                        for(int l =0 ;l<JAaaa.length(); l++){
                            JSONObject JOooo = (JSONObject) JAaaa.get(l);
                            JSONArray JAaaaa = JOooo.getJSONArray("definitions");
                             definiti += JAaaaa.get(0).toString() +"\n";

                        }
                    }
                }

            }

            for(int i =0 ;i <JA.length(); i++){

                JSONObject JO = (JSONObject) JA.get(i);


                JSONArray JAa = JO.getJSONArray("lexicalEntries");
                for(int j =0 ;j <JAa.length(); j++){
                    JSONObject JOo = (JSONObject) JAa.get(j);

                    if (JOo.has("lexicalCategory")){
                        JSONObject subObject = JOo.getJSONObject("lexicalCategory");
                        type += subObject.getString("text")+"\n";
                        Log.i("MSGNM",type);
                    }


                }

            }


            definition.setText(definiti);
            typee.setText(type);
          // Log.i("MSGNM",subArray.getJSONObject(1).getString("id"));
        } catch (JSONException e) {
            definition.setText(definiti);
            typee.setText(type);
            e.printStackTrace();
        }

        // new CallbackTask().execute(dictionaryEntries());
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

    private void starttranslate(String s, int flag) {

        if (flag== 0)
        {englishGermanTranslator.translate(s)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {
                                // Translation successful.
                                //textView.setText(translatedText);
                                definition.setText(translatedText);
                                dflag =1;
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
                                    definition.setText(translatedText);
                                    dflag =0;
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

    private void starttranslatety(String s, int flag) {

        if (flag== 0)
        {englishGermanTranslator.translate(s)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {
                                // Translation successful.
                                //textView.setText(translatedText);
                                typee.setText(translatedText);
                                tflag =1;
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
                                    typee.setText(translatedText);
                                    tflag =0;
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


    //in android calling network requests on the main thread forbidden by default
    //create class to do async job


    @Override
    protected void onPause() {
        if (textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }
}
