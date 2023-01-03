package com.example.words;

import android.provider.BaseColumns;

public final class Constants {

    private Constants() {
    }

    public static class UserTable implements BaseColumns {
        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_GAMES_PLAYED = "games_played";
        public static final String COLUMN_HIGHSCORE = "highscore";
        public static final String COLUMN_MAX_WORDS = "max_words";
        public static final String COLUMN_MIN_TIME = "min_time";
        public static final String COLUMN_SHOW_ON_LEADERBOARD = "show_on_leaderboard";
    }

    public static class WordsTable implements BaseColumns{
        public static final String TABLE_NAME = "Words";
        public static final String COLUMN_SERBIAN_WORD = "serbian_word";
        public static final String COLUMN_ENGLISH_WORD = "english_word";
        public static final String COLUMN_WORD_POINTS = "word_points";
        public static final String COLUMN_LEVEL = "level";
    }

    public static class ApiKeys{
        public static final String X_MASTER_KEY = "$2b$10$Qfpc/cG24evUBJxRcmzxvuO8jhbxzCJF74mGiXKJhIGR9L3TRrtnm";
        public static final String X_ACCESS_KEY = "$2b$10$Q9hhj/cGfjIhZuvXob8A4uDibduDL/3831kUFxMby2IniEb6h968.";
        public static final String usersApiUrl = "https://api.jsonbin.io/v3/b/63ae943301a72b59f23c3d28";
        public static final String wordsApiUrl = "https://api.jsonbin.io/v3/b/63ae941015ab31599e27f5fd";
    }

}
