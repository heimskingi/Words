package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Game extends AppCompatActivity {

    TextView wordTextView, engTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        wordTextView = findViewById(R.id.word);
        engTextView = findViewById(R.id.wordEng);

        int level = getIntent().getExtras().getInt("level", 1);
        Database db = new Database(Game.this);
        ArrayList<Word> words = db.getWordsByLevel(level);
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        /* length_of_words/level/additional_letters/time
                     3-5          easy       0             20sec
                     6-8         medium      3             16sec
                      9+         hard        5             12sec
            each level has 15 words
        */
        Random random = new Random();
        Word word = words.get(random.nextInt(words.size()));
        wordTextView.setText(word.getSerbianWord());
        String lines = "";
        for(int y=0; y<word.englishWord.length();y++){
            lines+="_";
        }
        engTextView.setText(lines);
        char[] neededLetters = word.getEnglishWord().toUpperCase().toCharArray();
        int numOfAddChars = 0;
        if (level == 2) {
            numOfAddChars = 3;
        } else if (level == 3) {
            numOfAddChars = 5;
        }
        char[] additionalLetters = new char[numOfAddChars];
        for (int x = 0; x < numOfAddChars; x++) {
            additionalLetters[x] = chars[random.nextInt(chars.length)];
        }
        StringBuilder sb = new StringBuilder();
        sb.append(neededLetters);
        sb.append(additionalLetters);
        char[] allChars = sb.toString().toCharArray();

        //listadapter za slova



    }

    public void openMenu(View view) {
        Intent i = new Intent(Game.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}