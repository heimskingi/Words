package com.example.words;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.words.Constants.*;

import java.util.ArrayList;

import kotlin.reflect.KFunction;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "game.db";
    public static final int DATABASE_VERSION = 1;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_USERS_TABLE = "CREATE TABLE " + UserTable.TABLE_NAME + " (" +
                    UserTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    UserTable.COLUMN_USERNAME + " TEXT NOT NULL," +
                    UserTable.COLUMN_GAMES_PLAYED + " INTEGER NOT NULL, " +
                    UserTable.COLUMN_HIGHSCORE + " INTEGER NOT NULL, " +
                    UserTable.COLUMN_SHOW_ON_LEADERBOARD + " BOOL NOT NULL);" ;
        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);

        final String CREATE_WORDS_TABLE = "CREATE TABLE " + WordsTable.TABLE_NAME + " (" +
                    WordsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    WordsTable.COLUMN_SERBIAN_WORD + " TEXT NOT NULL, " +
                    WordsTable.COLUMN_ENGLISH_WORD + " TEXT NOT NULL, " +
                    WordsTable.COLUMN_WORD_POINTS + " INTEGER NOT NULL, " +
                    WordsTable.COLUMN_LEVEL + " INTEGER NOT NULL);";
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
                u.setshowOnLeaderboard(Boolean.parseBoolean(c.getString(c.getColumnIndex(UserTable.COLUMN_SHOW_ON_LEADERBOARD))));
                u.setHighscore(c.getInt(c.getColumnIndex(UserTable.COLUMN_HIGHSCORE)));
                list.add(u);
            } while (c.moveToNext());
        }
        c.close();

        return list;
    }

    @SuppressLint("Range")
    public int getUserHighscore(String username){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c=db.rawQuery("SELECT * FROM " + UserTable.TABLE_NAME
                + " WHERE " + UserTable.COLUMN_USERNAME + " = '" + username + "'"
                , null);
        if(c.moveToFirst()){
            return c.getInt(c.getColumnIndex(UserTable.COLUMN_HIGHSCORE));
        }
        return 0;
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

        long insert = db.insert(UserTable.TABLE_NAME, null, values);
        return insert != -1;
    }

    public boolean updateUser(User user, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserTable.COLUMN_GAMES_PLAYED, user.gamesPlayed);
        values.put(UserTable.COLUMN_HIGHSCORE, user.highscore);
        values.put(UserTable.COLUMN_SHOW_ON_LEADERBOARD, user.showOnLeaderboard);
        values.put(UserTable.COLUMN_USERNAME, user.username);

        long update = db.update(UserTable.TABLE_NAME, values, UserTable._ID + " = ?", new String[]{String.valueOf(id)});
        return update != -1;
    }

    public boolean deleteOneUser(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + UserTable.TABLE_NAME + " WHERE " + UserTable._ID + " = " + id;
        Cursor cursor = db.rawQuery(query,null);
        return !cursor.moveToFirst();
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

    public boolean deleteOneWord(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + WordsTable.TABLE_NAME + " WHERE " + WordsTable._ID + " = " + id;
        Cursor cursor = db.rawQuery(query,null);
        return !cursor.moveToFirst();
    }

}
