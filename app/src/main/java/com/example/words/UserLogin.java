package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
    }

    public void openDifficulty(View view) {
        Intent i = new Intent(UserLogin.this, Difficulty.class);
        startActivity(i);
    }

    public void openMenu(View view) {
        Intent i = new Intent(UserLogin.this, MainActivity.class);
        startActivity(i);
    }
}