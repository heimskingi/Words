package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Difficulty extends AppCompatActivity {

    TextView notificationTextView;
    ImageButton exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        notificationTextView = findViewById(R.id.textViewNotification);
        exit = findViewById(R.id.exitButtonNotification);

        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            boolean sendNotification = intent.getExtras().getBoolean("notification", false);
            String notification = "New words available! Go to 'menu' -> 'settings' -> 'refresh words' to get them!";
            if (sendNotification) {
                notificationTextView.setText(notification);
                notificationTextView.setVisibility(View.VISIBLE);
                exit.setVisibility(View.VISIBLE);
            }
        }
    }

    public void openGame(View view) {
        int level = 1;
        switch (view.getId()){
            case R.id.easyButton:
                level=1;
                break;
            case R.id.mediumButton:
                level=2;
                break;
            case R.id.hardButton:
                level=3;
                break;
        }
        Intent i = new Intent(Difficulty.this, Game.class);
        i.putExtra("level", level);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void openMenu(View view) {
        Intent i = new Intent(Difficulty.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void exitMenu(View view) {
        exit.setVisibility(View.INVISIBLE);
        notificationTextView.setVisibility(View.INVISIBLE);
    }
}