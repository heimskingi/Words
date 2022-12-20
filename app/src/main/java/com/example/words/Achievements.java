package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Achievements extends AppCompatActivity {

    TextView myPointsText, myUsernameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        myPointsText = findViewById(R.id.myPoints);
        myUsernameText = findViewById(R.id.myusername);

        SharedPreferences sharedPref =  getSharedPreferences("preferences",Context.MODE_PRIVATE);
        if(!sharedPref.contains("username")){
            Intent i = new Intent(Achievements.this, UserLogin.class);
            startActivity(i);
        }
        String username = sharedPref.getString("username", "");
        myUsernameText.setText(username);

        Database db = new Database(Achievements.this);
        int score = db.getUserHighscore(username);
        myPointsText.setText(String.valueOf(score));

    }

    public void openMenu(View view) {
        Intent i = new Intent(Achievements.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void openDifficulty(View view) {
        Intent i = new Intent(Achievements.this, Difficulty.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}