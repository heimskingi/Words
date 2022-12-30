package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game extends AppCompatActivity {

    TextView wordTextView, engTextView;
    ListView lettersListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        wordTextView = findViewById(R.id.word);
        engTextView = findViewById(R.id.wordEng);
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
        lettersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Character selectedItem = (Character) parent.getItemAtPosition(position);
                String idk = selectedItem.toString();
                update(idk, neededLetters.length, word.getEnglishWord().toUpperCase());

            }
        });



    }
    String New="";
    int tries = 0;

    public void update(String string, int NumOfLetters, String two){
        New+=string;
        engTextView.setText(New);
        tries++;
        testTry(tries, NumOfLetters, New, two);
    }

    public void newWord(){
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
        tries = 0;
        New="";
    }

    public void testTry(int trying, int NumOfLetters, String one, String two){
        if(trying>NumOfLetters){
            Toast.makeText(Game.this,
                    "Maximum letters reached", Toast.LENGTH_LONG).show();
            engTextView.setText("Game Over");
        }else if(trying==NumOfLetters){
            compare(one,two);
        }
    }
    int Score = 0;
    public void compare(String one, String two){
        if(one.equals(two)){
            Score++;
            Toast.makeText(Game.this,
                    "Nice", Toast.LENGTH_LONG).show();
            newWord();
        }

    }

    public void openMenu(View view) {
        Intent i = new Intent(Game.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }



}