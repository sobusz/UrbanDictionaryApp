package com.example.urbandictionaryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView resultsTextView;
    private StringBuilder TextToSet;
    private EditText editText;
    private TextView wordTextView;
    private String WordToSet;
    private MediaPlayer mp;
    private Editable textCaptured;
    private ImageView errorCat;


    public void ApiConnection(){
        errorCat.setVisibility(View.INVISIBLE);
        OkHttpClient client = new OkHttpClient();
        String url = "https://mashape-community-urban-dictionary.p.rapidapi.com/define?term="+textCaptured;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "mashape-community-urban-dictionary.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "ce94ee3a7dmsh3a4f7acb7d26343p15a639jsn7e4b97caf03d")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String jsonData = response.body().string();
                    try {
                        JSONObject Jobject = new JSONObject(jsonData);
                        JSONArray Jarray = Jobject.getJSONArray("list");
                        StringBuilder total = new StringBuilder();

                        int limit = Jarray.length();
                        for (int i = 0; i < limit; i++) {
                            JSONObject object = Jarray.getJSONObject(i);
                            String word = object.getString("word");
                            String definition = object.getString("definition");
                            String example = object.getString("example");

                            Log.i("JSON", word+" " +definition);

                            TextToSet = total.append(i+1 + ")  " + definition + "\n _________________________________ \n \n");
                            WordToSet = word;
                        }
                        Log.d("TOTAL", total.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            resultsTextView.setMovementMethod(new ScrollingMovementMethod());
                            if(textCaptured.toString().equals("")){
                                wordTextView.setText("why u do dis");
                                resultsTextView.setText("");
                                errorCat.setVisibility(View.VISIBLE);

                            }
                            else{
                                resultsTextView.setText(TextToSet);
                                wordTextView.setText(WordToSet);
                            }

                        }
                    });
                }
            }
        });
    }

    public void searchWord(View view){
        mp.start();
        ApiConnection();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultsTextView = findViewById(R.id.resultsTextView);
        editText = findViewById(R.id.editText);
        wordTextView = findViewById(R.id.wordTextView);
        mp = MediaPlayer.create(this, R.raw.click);
        textCaptured = editText.getText();
        errorCat = findViewById(R.id.errorCat);
    }
}