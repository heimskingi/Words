package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class Game extends AppCompatActivity {

    TextView wordTextView, engTextView;
    ListView lettersListView;
    TextView timer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        wordTextView = findViewById(R.id.word);
        engTextView = findViewById(R.id.wordEng);
        timer = findViewById(R.id.time);
        lettersListView = findViewById(R.id.listViewLetters);
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
        //num of lines is num of letters of eng word
        String lines = "";
        for(int y=0; y<word.englishWord.length();y++){
            lines+="_";
        }
        engTextView.setText(lines);
        //list of chars needed for completing the word +  additional chars based on level
        ArrayList<Character> characters = new ArrayList<>();
        char[] neededLetters = word.getEnglishWord().toUpperCase().toCharArray();
        for(int i= 0 ; i < neededLetters.length; i++){
            characters.add(neededLetters[i]);
        }
        int numOfAddChars = 0;
        if (level == 2) {
            numOfAddChars = 3;
        } else if (level == 3) {
            numOfAddChars = 5;
        }
        for (int x = 0; x < numOfAddChars; x++) {
            char ch = chars[random.nextInt(chars.length)];
            characters.add(ch);
        }
        Collections.shuffle(characters);
        //listAdapter to show chars
        GameLetterListAdapter listAdapter = new GameLetterListAdapter(Game.this, characters);
        lettersListView.setAdapter(listAdapter);
        int timer_length;
        if (level==1){
            timer_length=20000;
        }else if (level==2){
            timer_length=16000;
        }else{
            timer_length=12000;
        }
        new CountDownTimer(timer_length, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);
                // logic to set the EditText could go here
            }

            public void onFinish() {
                displayScore();
            }

        }.start();
        lettersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Character selectedItem = (Character) parent.getItemAtPosition(position);
                String idk = selectedItem.toString().toUpperCase();
                int length = word.englishWord.length();
                update(idk, length, word.getEnglishWord().toUpperCase(), view);

            }

        });




    }
    String New="";
    int tries = 0;
    int Words = 0;
    int Score = 0;
    public void update(String one, int NumOfLetters, String two,View view){
        New+=one;
        engTextView.setText(New);
        tries++;
        Score++;
        testTry(tries, NumOfLetters, New, two, view);
    }

    public void testTry(int trying, int NumOfLetters, String one, String two,View view){
        if(trying==NumOfLetters){
            compare(one,two, view);
        }
        if(trying>NumOfLetters){
            Toast.makeText(Game.this,
                    "Maximum letters reached", Toast.LENGTH_LONG).show();
            engTextView.setText("Game Over");
            Score=0;
        }
    }
    public void compare(String one, String two,View view){
        if(one.equals(two)){
            Words++;
            Toast.makeText(Game.this,
                    "Nice", Toast.LENGTH_LONG).show();
            newWord(view);
        }

    }

    public void newWord(View view){
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
        //num of lines is num of letters of eng word
        String lines = "";
        for(int y=0; y<word.englishWord.length();y++){
            lines+="_";
        }
        engTextView.setText(lines);
        //list of chars needed for completing the word +  additional chars based on level
        ArrayList<Character> characters = new ArrayList<>();
        char[] neededLetters = word.getEnglishWord().toUpperCase().toCharArray();
        for(int i= 0 ; i < neededLetters.length; i++){
            characters.add(neededLetters[i]);
        }
        int numOfAddChars = 0;
        if (level == 2) {
            numOfAddChars = 3;
        } else if (level == 3) {
            numOfAddChars = 5;
        }
        for (int x = 0; x < numOfAddChars; x++) {
            char ch = chars[random.nextInt(chars.length)];
            characters.add(ch);
        }
        Collections.shuffle(characters);
        //listAdapter to show chars
        GameLetterListAdapter listAdapter = new GameLetterListAdapter(Game.this, characters);
        lettersListView.setAdapter(listAdapter);
        lettersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Character selectedItem = (Character) parent.getItemAtPosition(position);
                String idk = selectedItem.toString().toUpperCase();
                int length = word.englishWord.length();
                update(idk, length, word.getEnglishWord().toUpperCase(), view);
            }

        });
        tries = 0;
        New="";
    }

    public void displayScore(){
        Toast.makeText(Game.this,
                "Words: "+Words+"  " + "Score: "+Score, Toast.LENGTH_LONG).show();
    }





    public void openMenu(View view) {
        Intent i = new Intent(Game.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }





}