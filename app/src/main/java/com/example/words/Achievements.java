package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Achievements extends AppCompatActivity {

    TextView myUsernameText, myGamesPlayed, myMaxWords, myHighscore;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchShowOnLB;
    URL url;
    HttpURLConnection connection;
    ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        myUsernameText = findViewById(R.id.myusername);
        myGamesPlayed = findViewById(R.id.myGamesPlayed);
        myMaxWords = findViewById(R.id.myMaxWords);
        myHighscore = findViewById(R.id.myHighscore);
        switchShowOnLB = findViewById(R.id.switchLB);

        SharedPreferences sharedPref =  getSharedPreferences("preferences",Context.MODE_PRIVATE);
        if(!sharedPref.contains("username")){
            Intent i = new Intent(Achievements.this, UserLogin.class);
            startActivity(i);
        }
        String username = sharedPref.getString("username", "");
        Database db = new Database(Achievements.this);
        User user = db.getUser(username);
        myUsernameText.setText(user.getUsername());
        myGamesPlayed.setText(String.valueOf(user.getgamesPlayed()));
        myMaxWords.setText(String.valueOf(user.getMaxWords()));
        myHighscore.setText(String.valueOf(user.getHighscore()));
        switchShowOnLB.setChecked(user.isshowOnLeaderboard());

        switchShowOnLB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                user.setshowOnLeaderboard(true);
                boolean s = db.updateUser(user);
                if (!s) {
                    Toast.makeText(Achievements.this, "Could not save changes to your profile. Please try again!", Toast.LENGTH_LONG).show();
                }
            } else {
                user.setshowOnLeaderboard(false);
                boolean s = db.updateUser(user);
                if (!s) {
                    Toast.makeText(Achievements.this, "Could not save changes to your profile. Please try again!", Toast.LENGTH_LONG).show();
                }
            }
            UpdateUser updateUser = new UpdateUser(); // TODO why ne radi??????
            updateUser.execute();
        });
    }

    public void openMenu(View view) {
        Intent i = new Intent(Achievements.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private class UpdateUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                url = new URL(Constants.ApiKeys.usersApiUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("X-Master-Key", Constants.ApiKeys.X_MASTER_KEY);
                connection.setRequestProperty("X-Access-Key", Constants.ApiKeys.X_ACCESS_KEY);
                connection.setRequestProperty("Content-Type", "application/json");

                Database db = new Database(Achievements.this);
                users = db.getUsers();
                String payload =  "[" ;
                for (int i = 0; i < users.size(); i++){
                    payload += users.get(i).toJSON();
                    if(i != users.size() - 1 ){
                        payload += ",";
                    }
                    else{
                        payload += "]";
                    }
                }

                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                byte[] requestBody = payload.getBytes();
                outputStream.write(requestBody);
                outputStream.close();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                    return "Server response:" + connection.getResponseMessage();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Success.";
        }

        @Override
        protected void onPostExecute(String s) {
            if (!s.equals("Success.")) {
                try {
                    Toast.makeText(Achievements.this, "Couldn't send data! " + connection.getResponseMessage(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }



}