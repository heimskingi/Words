package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AdminDashboard extends AppCompatActivity {
    EditText serbianWordEditText;
    EditText englishWordEditText;
    EditText levelEditText;
    EditText wordPointsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        serbianWordEditText = findViewById(R.id.serbianWord);
        englishWordEditText = findViewById(R.id.englishWord);
        levelEditText = findViewById(R.id.level);
        wordPointsEditText = findViewById(R.id.wordPoints);
    }

    public void openMenu(View view) {
        Intent i = new Intent(AdminDashboard.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void addWord(View view) {
        String serbianWord = String.valueOf(serbianWordEditText.getText());
        String englishWord = String.valueOf(englishWordEditText.getText());
        int level = Integer.parseInt(String.valueOf(levelEditText.getText()));
        int wordPoints = Integer.parseInt(String.valueOf(wordPointsEditText.getText()));
        Database db = new Database(AdminDashboard.this);

        Word word = new Word(serbianWord, englishWord, wordPoints, level);
        boolean success = db.addNewWord(word);
        if (!success) {
            Toast.makeText(AdminDashboard.this, "Could not add new word. Please try again!", Toast.LENGTH_LONG).show();
        } else {
            SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("update", "true");
            editor.apply();
            Toast.makeText(AdminDashboard.this, "Successfully added a new word!", Toast.LENGTH_SHORT).show();
        }

    }
}