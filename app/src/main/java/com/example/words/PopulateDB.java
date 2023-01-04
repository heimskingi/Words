package com.example.words;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class PopulateDB {

    Context context;

    public PopulateDB(Context context) {
        this.context = context;
    }

    public boolean populateDatabaseWithWords(String jsonData) {
        Database db = new Database(context);

        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            int arrLength = jsonArray.length();
            if (arrLength == 0) {
                return false;
            }
            for (int i = 0; i < arrLength; i++) {
                JSONObject one = jsonArray.getJSONObject(i);
                String sr = one.getString("sr");
                String eng = one.getString("eng");
                int level = one.getInt("nivo");
                int points = one.getInt("bodovi");
                Word w = new Word(sr, eng, points, level);
                boolean suc = db.addNewWord(w);
                if (!suc) {
                    Toast.makeText(context, "Failed to insert word data in db.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean populateDatabaseWithUsers(String jsonData) {
        Database db = new Database(context);

        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            int arrLength = jsonArray.length();
            if (arrLength == 0) {
                return false;
            }
            for (int i = 0; i < arrLength; i++) {
                JSONObject one = jsonArray.getJSONObject(i);
                String username = one.getString("username");
                int gamesPlayed = one.getInt("gamesPlayed");
                int highscore = one.getInt("highscore");
                boolean showonLB = one.getBoolean("showOnLeaderboard");
                String minTime = one.getString("minTime");
                int maxWords = one.getInt("maxWords");
                User u = new User(username, gamesPlayed, highscore, showonLB, minTime, maxWords);
                boolean suc = db.addUser(u);
                if (!suc) {
                    Toast.makeText(context, "Failed to insert user data in db.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public String getJsonDataFromResource(int resource){
        InputStream is = context.getResources().openRawResource(resource);
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
        if (jsonString.equals("")) {
            Toast.makeText(context, "Unable to read json.", Toast.LENGTH_LONG).show();
        }
        return jsonString;
    }
}
