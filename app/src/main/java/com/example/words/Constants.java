package com.example.words;

import android.provider.BaseColumns;

public final class Constants {

    private Constants() {
    }

    public static class UserTable implements BaseColumns {
        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_GAMES_PLAYED = "games_played";
    }

    public static class PointsTable implements BaseColumns {
        public static final String TABLE_NAME = "Points";
        public static final String COLUMN_HIGHSCORE = "highscore";
        public static final String COLUMN_SHOW_ON_LEADERBOARD = "show_on_leaderboard";
        public static final String COLUMN_USER_ID = "user_id";
    }

    public static class LevelsTable implements BaseColumns {
        public static final String TABLE_NAME = "Levels";
        public static final String COLUMN_LEVEL_NAME = "level_name"; //easy medium hard
    }

    public static class WordsTable implements BaseColumns{
        public static final String TABLE_NAME = "Words";
        public static final String COLUMN_SERBIAN_WORD = "serbian_word";
        public static final String COLUMN_ENGLISH_WORD = "english_word";
        public static final String COLUMN_WORD_POINTS = "word_points";
        public static final String COLUMN_LEVEL_ID = "level_id";
    }

}
