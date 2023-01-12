package com.example.words;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.words.Constants.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import kotlin.reflect.KFunction;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "game.db";
    public static final int DATABASE_VERSION = 1;

    final String CREATE_USERS_TABLE = "CREATE TABLE " + UserTable.TABLE_NAME + " (" +
            UserTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            UserTable.COLUMN_USERNAME + " TEXT NOT NULL," +
            UserTable.COLUMN_GAMES_PLAYED + " INTEGER NOT NULL, " +
            UserTable.COLUMN_HIGHSCORE + " INTEGER NOT NULL, " +
            UserTable.COLUMN_MAX_WORDS + " INTEGER NOT NULL, " +
            UserTable.COLUMN_MIN_TIME + " TEXT NOT NULL, " +
            UserTable.COLUMN_SHOW_ON_LEADERBOARD + " BOOL NOT NULL);" ;

    final String CREATE_WORDS_TABLE = "CREATE TABLE " + WordsTable.TABLE_NAME + " (" +
            WordsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            WordsTable.COLUMN_SERBIAN_WORD + " TEXT NOT NULL, " +
            WordsTable.COLUMN_ENGLISH_WORD + " TEXT NOT NULL, " +
            WordsTable.COLUMN_WORD_POINTS + " INTEGER NOT NULL, " +
            WordsTable.COLUMN_LEVEL + " INTEGER NOT NULL);";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_WORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+UserTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+WordsTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean tableEmpty(String table){
        SQLiteDatabase database = this.getReadableDatabase();
        long NoOfRows = DatabaseUtils.queryNumEntries(database,table);

        if (NoOfRows == 0){
            return true;
        } else {
            return false;
        }
    }
    public int tableCapacity(String table){
        SQLiteDatabase database = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(database,table);
    }
    public void dropPreviousData(String table){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + table);
        if(table.equals("Words")){
            sqLiteDatabase.execSQL(CREATE_WORDS_TABLE);
        }
        if(table.equals("Users")){
            sqLiteDatabase.execSQL(CREATE_USERS_TABLE);
        }
    }

    @SuppressLint("Range")
    public ArrayList<User> getTop5(){
        ArrayList<User> list =new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c=db.rawQuery("SELECT * FROM " + UserTable.TABLE_NAME
                 + " WHERE " + UserTable.COLUMN_SHOW_ON_LEADERBOARD + " = true "
                + " ORDER BY " + UserTable.COLUMN_HIGHSCORE + " DESC LIMIT 5"
                ,null);

        if(c.moveToFirst()) {
            do {
                User u = new User();
                u.setUsername(c.getString(c.getColumnIndex(UserTable.COLUMN_USERNAME)));
                u.setgamesPlayed(c.getInt(c.getColumnIndex(UserTable.COLUMN_GAMES_PLAYED)));
                boolean show = c.getInt(c.getColumnIndex(UserTable.COLUMN_SHOW_ON_LEADERBOARD)) > 0;
                u.setshowOnLeaderboard(show);
                u.setMaxWords(c.getInt(c.getColumnIndex(UserTable.COLUMN_MAX_WORDS)));
                u.setHighscore(c.getInt(c.getColumnIndex(UserTable.COLUMN_HIGHSCORE)));
                u.setMinTime(c.getString(c.getColumnIndex(UserTable.COLUMN_MIN_TIME)));
                list.add(u);
            } while (c.moveToNext());
        }
        c.close();

        return list;
    }

    @SuppressLint("Range")
    public User getUser(String username){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c=db.rawQuery("SELECT * FROM " + UserTable.TABLE_NAME
                + " WHERE " + UserTable.COLUMN_USERNAME + " = '" + username + "'"
                , null);
        User u = new User();
        if(c.moveToFirst()){
            u.setUsername(c.getString(c.getColumnIndex(UserTable.COLUMN_USERNAME)));
            u.setgamesPlayed(c.getInt(c.getColumnIndex(UserTable.COLUMN_GAMES_PLAYED)));
            boolean show = c.getInt(c.getColumnIndex(UserTable.COLUMN_SHOW_ON_LEADERBOARD)) > 0;
            u.setshowOnLeaderboard(show);
            u.setHighscore(c.getInt(c.getColumnIndex(UserTable.COLUMN_HIGHSCORE)));
            u.setMaxWords(c.getInt(c.getColumnIndex(UserTable.COLUMN_MAX_WORDS)));
            u.setMinTime(c.getString(c.getColumnIndex(UserTable.COLUMN_MIN_TIME)));
        }
        return u;
    }

    public boolean doesUserExists(String username){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c=db.rawQuery("SELECT * FROM " + UserTable.TABLE_NAME
                        + " WHERE " + UserTable.COLUMN_USERNAME + " = '" + username + "'"
                , null);
        if(c.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }

    public boolean addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserTable.COLUMN_USERNAME, user.username);
        values.put(UserTable.COLUMN_GAMES_PLAYED, user.gamesPlayed);
        values.put(UserTable.COLUMN_HIGHSCORE, user.highscore);
        values.put(UserTable.COLUMN_SHOW_ON_LEADERBOARD, user.showOnLeaderboard);
        values.put(UserTable.COLUMN_MAX_WORDS, user.maxWords);
        values.put(UserTable.COLUMN_MIN_TIME, user.minTime);

        long insert = db.insert(UserTable.TABLE_NAME, null, values);
        return insert != -1;
    }

    public boolean updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserTable.COLUMN_GAMES_PLAYED, user.gamesPlayed);
        values.put(UserTable.COLUMN_HIGHSCORE, user.highscore);
        values.put(UserTable.COLUMN_SHOW_ON_LEADERBOARD, user.showOnLeaderboard);
        values.put(UserTable.COLUMN_USERNAME, user.username);
        values.put(UserTable.COLUMN_MAX_WORDS, user.maxWords);
        values.put(UserTable.COLUMN_MIN_TIME, user.minTime);

        long update = db.update(UserTable.TABLE_NAME, values, UserTable.COLUMN_USERNAME + " = ?", new String[]{String.valueOf(user.username)});
        return update != -1;
    }

    public boolean addNewWord(Word word){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WordsTable.COLUMN_SERBIAN_WORD, word.serbianWord);
        values.put(WordsTable.COLUMN_ENGLISH_WORD, word.englishWord);
        values.put(WordsTable.COLUMN_LEVEL, word.level);
        values.put(WordsTable.COLUMN_WORD_POINTS, word.wordPoints);

        long insert = db.insert(WordsTable.TABLE_NAME, null, values);
        return insert != -1;
    }

    @SuppressLint("Range")
    public ArrayList<Word> getWordsByLevel(int level){
        ArrayList<Word> list =new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c=db.rawQuery("SELECT * FROM " + WordsTable.TABLE_NAME
                        + " WHERE " + WordsTable.COLUMN_LEVEL + " = " + level
                ,null);

        if(c.moveToFirst()) {
            do {
               Word w = new Word();
               w.setLevel(c.getInt(c.getColumnIndex(WordsTable.COLUMN_LEVEL)));
               w.setEnglishWord(c.getString(c.getColumnIndex(WordsTable.COLUMN_ENGLISH_WORD)));
               w.setSerbianWord(c.getString(c.getColumnIndex(WordsTable.COLUMN_SERBIAN_WORD)));
               w.setWordPoints(c.getInt(c.getColumnIndex(WordsTable.COLUMN_WORD_POINTS)));
               list.add(w);
            } while (c.moveToNext());
        }
        c.close();

        return list;
    }

    @SuppressLint("Range")
    public ArrayList<User> getUsers(){
        ArrayList<User> list =new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM " + UserTable.TABLE_NAME,null);
        if(c.moveToFirst()) {
            do {
                User u = new User();
                u.setUsername(c.getString(c.getColumnIndex(UserTable.COLUMN_USERNAME)));
                u.setgamesPlayed(c.getInt(c.getColumnIndex(UserTable.COLUMN_GAMES_PLAYED)));
                boolean show = c.getInt(c.getColumnIndex(UserTable.COLUMN_SHOW_ON_LEADERBOARD)) > 0;
                u.setshowOnLeaderboard(show);
                u.setMaxWords(c.getInt(c.getColumnIndex(UserTable.COLUMN_MAX_WORDS)));
                u.setHighscore(c.getInt(c.getColumnIndex(UserTable.COLUMN_HIGHSCORE)));
                u.setMinTime(c.getString(c.getColumnIndex(UserTable.COLUMN_MIN_TIME)));
                list.add(u);
            } while (c.moveToNext());
        }
        c.close();

        return list;
    }

}
