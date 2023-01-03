package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Locale;

@SuppressLint("CustomSplashScreen")
public class Splashscreen extends AppCompatActivity {

    String wordsData = "";
    String usersData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        boolean internetExists = InternetCheck.isInternetAvailable(Splashscreen.this);
        if(internetExists){
            Toast.makeText(Splashscreen.this, "connected to internet", Toast.LENGTH_SHORT).show();
            //update local db with api data
        }else{
            Toast.makeText(Splashscreen.this, "no internet", Toast.LENGTH_SHORT).show();
            //load local json
        }

        Database db = new Database(Splashscreen.this);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(db.tableEmpty(Constants.WordsTable.TABLE_NAME)){
            wordsData = getJsonDataFromResource(R.raw.words);
            boolean wordsDone = populateDatabaseWithWords(wordsData);
            if(!wordsDone){
                Toast.makeText(Splashscreen.this,"Unable to load resources.", Toast.LENGTH_SHORT).show();
            }
        }
        if(db.tableEmpty(Constants.UserTable.TABLE_NAME)) {
            usersData = getJsonDataFromResource(R.raw.users);
            boolean usersDone = populateDatabaseWithUsers(usersData);
            if(!usersDone){
                Toast.makeText(Splashscreen.this,"Unable to load resources.", Toast.LENGTH_SHORT).show();
            }
        }

        SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);

        if(sharedPref.contains("language")){
            Locale myLocale = new Locale(sharedPref.getString("language", ""));
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }

        if(!sharedPref.contains("username")){
            Intent i = new Intent(Splashscreen.this, UserLogin.class);
            startActivity(i);
        }else {
            Intent i = new Intent(Splashscreen.this, Difficulty.class);
            startActivity(i);
        }
    }

    private boolean populateDatabaseWithWords(String jsonData){
        Database db = new Database(Splashscreen.this);

        try{
            JSONArray jsonArray=new JSONArray(jsonData);
            int arrLength = jsonArray.length();
            if(arrLength == 0){
                return false;
            }
            for(int i =0 ; i<arrLength; i++) {
                JSONObject one = jsonArray.getJSONObject(i);
                String sr = one.getString("sr");
                String eng = one.getString("eng");
                int level = one.getInt("nivo");
                int points = one.getInt("bodovi");
                Word w = new Word(sr,eng,points,level);
                boolean suc = db.addNewWord(w);
                if(!suc){
                    Toast.makeText(Splashscreen.this, "Failed to insert word data in db.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean populateDatabaseWithUsers(String jsonData){
        Database db = new Database(Splashscreen.this);

        try{
            JSONArray jsonArray=new JSONArray(jsonData);
            int arrLength = jsonArray.length();
            if(arrLength == 0){
                return false;
            }
            for(int i =0 ; i<arrLength; i++) {
                JSONObject one = jsonArray.getJSONObject(i);
                String username = one.getString("username");
                int gamesPlayed = one.getInt("gamesPlayed");
                int highscore = one.getInt("highscore");
                boolean showonLB = one.getBoolean("showOnLeaderboard");
                String minTime = one.getString("minTime");
                int maxWords = one.getInt("maxWords");
                User u = new User(username,gamesPlayed,highscore, showonLB,minTime, maxWords);
                boolean suc = db.addUser(u);
                if(!suc){
                    Toast.makeText(Splashscreen.this, "Failed to insert user data in db.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }


    private String getJsonDataFromResource(int resource){
        InputStream is = getResources().openRawResource(resource);
        Writer writer = new StringWriter();
        int size = 0;
        try {
            size = is.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        char[] buffer = new char[size];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String jsonString = writer.toString();
        if(jsonString.equals("")) {
            Toast.makeText(Splashscreen.this, "Unable to read json.", Toast.LENGTH_LONG).show();
        }
        return jsonString;
    }

}