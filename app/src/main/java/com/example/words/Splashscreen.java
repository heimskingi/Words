package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

@SuppressLint("CustomSplashScreen")
public class Splashscreen extends AppCompatActivity {

    String wordsData = "", data = "", eTag = "";
    boolean emptyWords = false, sendNotification = false;
    URL url;
    InputStream inputStream;
    HttpURLConnection connection;
    SharedPreferences sharedPref;
    PopulateDB populateDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        Database db = new Database(Splashscreen.this);
        sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        populateDB = new PopulateDB(Splashscreen.this);

        boolean internetExists = InternetCheck.isInternetAvailable(Splashscreen.this);
        if (internetExists) {
            if(db.tableEmpty(Constants.WordsTable.TABLE_NAME)){
                emptyWords = true;
            }
            //update local db with api data
            DownloadWords task1 = new DownloadWords();
            task1.execute(Constants.ApiKeys.wordsApiUrl);

        }
        else {
            if (db.tableEmpty(Constants.WordsTable.TABLE_NAME)) {
                boolean wordsDone = populateDB.populateDatabaseWithWords(populateDB.getJsonDataFromResource(R.raw.words));
                if (!wordsDone) {
                    Toast.makeText(Splashscreen.this, "Unable to load resources.", Toast.LENGTH_SHORT).show();
                }
            }
            if (db.tableEmpty(Constants.UserTable.TABLE_NAME)) {
                boolean usersDone = populateDB.populateDatabaseWithUsers(populateDB.getJsonDataFromResource(R.raw.users));
                if (!usersDone) {
                    Toast.makeText(Splashscreen.this, "Unable to load resources.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (sharedPref.contains("language")) {
            Locale myLocale = new Locale(sharedPref.getString("language", ""));
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }

        if (!sharedPref.contains("username")) {
            Intent i = new Intent(Splashscreen.this, UserLogin.class);
            startActivity(i);
        } else if(sharedPref.contains("newUser")){
            Intent i = new Intent(Splashscreen.this, ProcessNewUser.class);
            startActivity(i);
        }
        else {
            Intent i = new Intent(Splashscreen.this, Difficulty.class);
            i.putExtra("notification", sendNotification);
            startActivity(i);
        }
    }


    private class DownloadWords extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
            data = "";

            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-Master-Key", Constants.ApiKeys.X_MASTER_KEY);
                connection.setRequestProperty("X-Access-Key", Constants.ApiKeys.X_ACCESS_KEY);
                connection.connect();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                    return "Server response:" + connection.getResponseMessage();

                eTag = connection.getHeaderField("ETag");
                if (!sharedPref.contains("eTagWords")) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("eTagWords", eTag);
                    editor.apply();
                }
                String sh = sharedPref.getString("eTagWords", null);
                Log.println(Log.INFO, "tag", eTag);
                Log.println(Log.INFO, "sh", sh);
                if(!eTag.equals(sh)){
                    sendNotification = true;
                }

                if (!eTag.equals(sharedPref.getString("eTagWords", null))) {
                    sendNotification = true;
                }

                inputStream = connection.getInputStream();
                if (inputStream == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String oneLine;
                while ((oneLine = reader.readLine()) != null) {
                    data += oneLine;
                }
                if (data.length() == 0) {
                    return null;
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray jsonArray = jsonObject.optJSONArray("record");
                        if (jsonArray != null) {
                            wordsData = jsonArray.toString();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (connection != null) {
                    connection.disconnect();
                }
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                Toast.makeText(Splashscreen.this, "No data", Toast.LENGTH_SHORT).show();
            }else{
                if (emptyWords){
                    populateDB.populateDatabaseWithWords(wordsData);
                    emptyWords = false;
                }
            }
        }

    }

}