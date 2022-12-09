package com.example.words;

public class User {

    public String username;
    public int gamesPlayed = 0, highscore = 0;
    public boolean showOnLeaderboard = false;

    public User(String username) {
        this.username = username;
    }

    public User(String username, int gamesPlayed, int highscore, boolean showOnLeaderboard) {
        this.username = username;
        this.gamesPlayed = gamesPlayed;
        this.highscore = highscore;
        this.showOnLeaderboard = showOnLeaderboard;
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
