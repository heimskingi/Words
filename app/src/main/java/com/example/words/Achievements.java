package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Achievements extends AppCompatActivity {

    TextView myPointsText, myUsernameText, myGamesPlayed;
    Switch switchShowOnLB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        myPointsText = findViewById(R.id.myPoints);
        myUsernameText = findViewById(R.id.myusername);
        myGamesPlayed = findViewById(R.id.myGamesPlayed);
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
        myPointsText.setText(String.valueOf(user.getHighscore()));
        myGamesPlayed.setText(String.valueOf(user.getgamesPlayed()));
        switchShowOnLB.setChecked(user.isshowOnLeaderboard());

        switchShowOnLB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
            }
        });
    }

    public void openMenu(View view) {
        Intent i = new Intent(Achievements.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}