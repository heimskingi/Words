package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Locale;

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

    public void openAdmin(View view) {
        Intent i = new Intent(MainActivity.this, AdminDashboard.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}