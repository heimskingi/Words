package com.example.words;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.words.Constants.*;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "game.db";
    public static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
//+ " FOREIGN KEY ("+ BOOKStore +") REFERENCES "+STORE_TABLE+"("+STORE_ID+"));";
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        db = sqLiteDatabase;

        final String CREATE_USERS_TABLE = "CREATE TABLE " + UserTable.TABLE_NAME + " (" +
                    UserTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    UserTable.COLUMN_USERNAME + " TEXT NOT NULL," +
                    UserTable.COLUMN_GAMES_PLAYED + " INTEGER NOT NULL );";
        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);

        final String CREATE_POINTS_TABLE = "CREATE TABLE " + PointsTable.TABLE_NAME + " (" +
                    PointsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PointsTable.COLUMN_HIGHSCORE + " INTEGER NOT NULL, " +
                    PointsTable.COLUMN_SHOW_ON_LEADERBOARD + " BOOL, " +
                    " FOREIGN KEY (" + PointsTable.COLUMN_USER_ID + ") REFERENCES "
                    + UserTable.TABLE_NAME + "(" + UserTable._ID + "));";
        sqLiteDatabase.execSQL(CREATE_POINTS_TABLE);

        final String CREATE_LEVELS_TABLE = "CREATE TABLE " + LevelsTable.TABLE_NAME + " (" +
                    LevelsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LevelsTable.COLUMN_LEVEL_NAME + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(CREATE_LEVELS_TABLE);

        final String CREATE_WORDS_TABLE = "CREATE TABLE " + WordsTable.TABLE_NAME + " (" +
                    WordsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    WordsTable.COLUMN_SERBIAN_WORD + " TEXT NOT NULL, " +
                    WordsTable.COLUMN_ENGLISH_WORD + " TEXT NOT NULL, " +
                    WordsTable.COLUMN_WORD_POINTS + " INTEGER NOT NULL, " +
                    " FOREIGN KEY (" +WordsTable.COLUMN_LEVEL_ID + ") REFERENCES "
                    + LevelsTable.TABLE_NAME + "(" + LevelsTable._ID + "));";
        sqLiteDatabase.execSQL(CREATE_WORDS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
}
