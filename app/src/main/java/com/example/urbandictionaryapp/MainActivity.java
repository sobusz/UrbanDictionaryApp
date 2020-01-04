package com.example.urbandictionaryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultsTextView = findViewById(R.id.resultsTextView);
        OkHttpClient client = new OkHttpClient();

        String url = "https://mashape-community-urban-dictionary.p.rapidapi.com/define?term=wat";

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
                        Log.i("XD", String.valueOf(Jarray));

                        int limit = Jarray.length();
                        for (int i = 0; i < limit; i++) {
                            JSONObject object = Jarray.getJSONObject(i);
                            String word = object.getString("word");
                            String definition = object.getString("definition");
                            String example = object.getString("example");

                            Log.i("JSON", word+" " +definition);
                        }


                        } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultsTextView.setText(jsonData);
                        }
                    });
                }
            }
        });
    }
}