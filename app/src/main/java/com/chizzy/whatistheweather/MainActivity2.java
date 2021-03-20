package com.chizzy.whatistheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity2 extends AppCompatActivity {

    EditText editText;
    TextView resultTextView;
    Button   button;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);

            editText = findViewById(R .id .editTextTextPersonName);
           resultTextView = findViewById(R .id .resultTextVie);
            button = findViewById(R.id.button);
        }
        public void  weather (View view) {
            try {
                DownloadTask task = new DownloadTask();
                String encodedCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");

                task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=cb2852c010413e6dcba45f339dffc674");

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        }
        public class DownloadTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground (String...urls){
                String result = "";
                URL url;
                HttpURLConnection urlConnection = null;
                try {

                    url = new URL(urls[0]);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    int data = reader.read();
                    while (data != -1) {
                        char current = (char) data;
                        result += current;
                        data = reader.read();
                    }
                    return result;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            @Override
            protected void onPostExecute (String s){
                super.onPostExecute(s);
                try {

                    JSONObject jsonObject = new JSONObject(s);

                    Log.i("Info",jsonObject + "Api");

                    String weatherInfo = jsonObject.getString("weather");
                    Log.i("Weather content", weatherInfo);
                    JSONArray array = new JSONArray(weatherInfo);
                    String  message  ="";
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonPart = array.getJSONObject(i);

                        String main = jsonPart.getString("main");
                        String description = jsonPart.getString("description");

                        if (!main.equals("") && !description.equals("")){
                            message += main + " : " + description + "\r\n";
                        }
                    }
                    if (!message.equals((""))){
                       resultTextView.setText(message);
                    }else {
                        Toast toast = Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();                    }

                   } catch (Exception e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();                }
            }
        }

    }
