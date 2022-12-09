package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    RelativeLayout settingsLayout;
    ImageView settingsImage;

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
    }

    public void openDifficulty(View view) {
        Intent i = new Intent(MainActivity.this, Difficulty.class);
        startActivity(i);
    }

    public void openLeaderBoard(View view) {
        Intent i = new Intent(MainActivity.this, LeaderBoard.class);
        startActivity(i);
    }

    public void openAchievements(View view) {
        Intent i = new Intent(MainActivity.this, Achievements.class);
        startActivity(i);
    }
}