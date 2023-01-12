package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class MainActivity extends AppCompatActivity {

    RelativeLayout settingsLayout;
    ImageView settingsImage;
    URL url;
    InputStream inputStream;
    HttpURLConnection connection;
    SharedPreferences sharedPref;
    PopulateDB populateDB;
    String wordsData = "", data = "", eTag = "";
    boolean newWords = false, emptyWords = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settingsLayout = findViewById(R.id.settingsMenu);
        settingsImage = findViewById(R.id.settingsImage);
    }

    public void openSettings(View view) {
        if(settingsLayout.getVisibility() == View.INVISIBLE){
            settingsLayout.setVisibility(View.VISIBLE);
            settingsImage.setImageResource(R.drawable.up);
        }else{
            settingsLayout.setVisibility(View.INVISIBLE);
            settingsImage.setImageResource(R.drawable.down);
        }
    }

    public void exitMenu(View view) {
        Intent i = new Intent(MainActivity.this, LeaderBoard.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void openDifficulty(View view) {
        Intent i = new Intent(MainActivity.this, Difficulty.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void openLeaderBoard(View view) {
        Intent i = new Intent(MainActivity.this, LeaderBoard.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void openAchievements(View view) {
        Intent i = new Intent(MainActivity.this, Achievements.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void changeToEnglish(View view) {
        Locale myLocale = new Locale("en");
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("language", "en");
        editor.apply();
        Intent refresh = new Intent(this, MainActivity.class);
        finish();
        startActivity(refresh);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void changeToSerbian(View view) {
        Locale myLocale = new Locale("sr");
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("language", "sr");
        editor.apply();
        Intent refresh = new Intent(this, MainActivity.class);
        finish();
        startActivity(refresh);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void checkForRefresh(View view) {
        RefreshWords refreshWords = new RefreshWords();
        refreshWords.execute();
    }

    private class RefreshWords extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
            data = "";

            try {
                url = new URL(Constants.ApiKeys.wordsApiUrl);
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

                if (!eTag.equals(sharedPref.getString("eTagWords", null))) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("eTagWords", eTag);
                    editor.apply();
                    newWords = true;
                    Database db = new Database(MainActivity.this);
                    db.dropPreviousData(Constants.WordsTable.TABLE_NAME);
                }
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                Toast.makeText(MainActivity.this, "No data", Toast.LENGTH_SHORT).show();
            }else{
                if (newWords || emptyWords){
                    populateDB = new PopulateDB(MainActivity.this);
                    populateDB.populateDatabaseWithWords(wordsData);
                    emptyWords = false;
                    newWords = false;
                    Toast.makeText(MainActivity.this, "Words have been updated!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

}