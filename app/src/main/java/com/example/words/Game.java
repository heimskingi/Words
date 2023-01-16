package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game extends AppCompatActivity {

    TextView wordTextView, engTextView;
    ListView lettersListView;
    TextView timer;
    URL url;
    HttpURLConnection connection;
    ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        wordTextView = findViewById(R.id.word);
        engTextView = findViewById(R.id.wordEng);
        timer = findViewById(R.id.time);
        TextView gameOver = findViewById(R.id.gameOver);
        lettersListView = findViewById(R.id.listViewLetters);
        int level = getIntent().getExtras().getInt("level", 1);
        Database db = new Database(Game.this);
        SharedPreferences sharedPref =  getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", "");
        User user = db.getUser(username);
        int highscore = user.getHighscore();
        ArrayList<Word> words = db.getWordsByLevel(level);
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

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
            timer_length=15000;
        }else if (level==2){
            timer_length=60000;
        }else{
            timer_length=50000;
        }
        CountDownTimer tm = new CountDownTimer(timer_length, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                String zeros;

                    if (millisUntilFinished / 1000 < 10) {
                        zeros = "00:0";
                    } else {
                        zeros = "00:";
                    }
                    timer.setText(zeros + millisUntilFinished / 1000);

                }




            public void onFinish() {
                int newHighScore = 0;
                if(newHighScore(Score, highscore)){
                    newHighScore=Score;
                    if(level ==2){
                        newHighScore = newHighScore*2;
                    }
                    if(level == 3){
                        newHighScore = newHighScore+3;
                    }
                }
                if(user.getHighscore() < newHighScore) {
                    user.setHighscore(newHighScore);
                }
                int gamesPlayed = user.getgamesPlayed();
                gamesPlayed++;
                user.setgamesPlayed(gamesPlayed);
                if(user.getMaxWords() < Words) {
                    user.setMaxWords(Words);
                }
                if(db.updateUser(user)){
                    UpdateUserToApi updateUser = new UpdateUserToApi();
                    updateUser.execute();
                    engTextView.setText("Score: "+Score+" "+ "Words: "+ Words);
                    gameOver.setText("Nice job");
                    timer.setText("");
                    wordTextView.setText("");
                }
            }
        };
        tm.start();
        lettersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Character selectedItem = (Character) parent.getItemAtPosition(position);
                String idk = selectedItem.toString().toUpperCase();
                int length = word.englishWord.length();
                update(idk, length, word.getEnglishWord().toUpperCase(), view);
                String omg = (String) engTextView.getText();
                if(omg=="Game Over!"){
                    tm.cancel();
                    timer.setText("");
                    wordTextView.setText("");
                }
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
            engTextView.setText("Game Over!"); //TODO display better

            Score=0;
        }
    }

    public void compare(String one, String two,View view){
        if(one.equals(two)){
            Words++;
            newWord(view);
        }
        else{
            //TODO stop the game
            engTextView.setText("Game Over!");
        }
    }
    public boolean JesusChrist(){
        String sharedFact = engTextView.getText().toString();
        boolean value=false;
        if(sharedFact.contains("Game Over!")){
            value=true;
        }
        return value;
    }

    public void newWord(View view){
        int level = getIntent().getExtras().getInt("level", 1);
        Database db = new Database(Game.this);
        ArrayList<Word> words = db.getWordsByLevel(level);
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

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


    public boolean newHighScore(int older, int newer){
        if(older>newer) {
            return true;
        }else{
            return false;
        }
    }

    public void openMenu(View view) {
        Intent i = new Intent(Game.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private class UpdateUserToApi extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                url = new URL(Constants.ApiKeys.usersApiUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("X-Master-Key", Constants.ApiKeys.X_MASTER_KEY);
                connection.setRequestProperty("X-Access-Key", Constants.ApiKeys.X_ACCESS_KEY);
                connection.setRequestProperty("Content-Type", "application/json");

                Database db = new Database(Game.this);
                users = db.getUsers();
                String payload =  "[" ;
                for (int i = 0; i < users.size(); i++){
                    payload += users.get(i).toJSON();
                    if(i != users.size() - 1 ){
                        payload += ",";
                    }
                    else{
                        payload += "]";
                    }
                }

                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                byte[] requestBody = payload.getBytes();
                outputStream.write(requestBody);
                outputStream.close();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                    return "Server response:" + connection.getResponseMessage();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Success.";
        }

        @Override
        protected void onPostExecute(String s) {
            if (!s.equals("Success.")) {
                try {
                    Toast.makeText(Game.this, "Couldn't send data! " + connection.getResponseMessage(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}