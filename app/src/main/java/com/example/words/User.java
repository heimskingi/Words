package com.example.words;

public class User {

    public String username, minTime;
    public int gamesPlayed, maxWords, highscore;
    public boolean showOnLeaderboard ;

    public User(){}

    public User(String username) {
        this.username = username;
        this.gamesPlayed = 0;
        this.highscore = 0;
        this.showOnLeaderboard = false;
        this.minTime = "00:00";
        this.maxWords = 0;
    }

    public User(String username, int gamesPlayed, int highscore, boolean showOnLeaderboard, String minTime, int maxWords) {
        this.username = username;
        this.gamesPlayed = gamesPlayed;
        this.highscore = highscore;
        this.showOnLeaderboard = showOnLeaderboard;
        this.maxWords = maxWords;
        this.minTime = minTime;
    }

    public String toJSON(){
        return "{" +
                "\"username\": \"" + this.getUsername() + "\", " +
                "\"gamesPlayed\": " + this.getgamesPlayed() + ", " +
                "\"highscore\": " + this.getHighscore() + ", " +
                "\"showOnLeaderboard\": " + this.isshowOnLeaderboard() + ", " +
                "\"maxWords\": " + this.getMaxWords() + ", " +
                "\"minTime\": \"" + this.getMinTime() + "\"" +
                "}";
    }

    public String getMinTime() {
        return minTime;
    }

    public void setMinTime(String minTime) {
        this.minTime = minTime;
    }

    public int getMaxWords() {
        return maxWords;
    }

    public void setMaxWords(int maxWords) {
        this.maxWords = maxWords;
    }

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    public boolean isshowOnLeaderboard() {
        return showOnLeaderboard;
    }

    public void setshowOnLeaderboard(boolean showOnLeaderboard) {
        this.showOnLeaderboard = showOnLeaderboard;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getgamesPlayed() {
        return gamesPlayed;
    }

    public void setgamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }
}
