package com.example.words;

public class Point {

    public String highscore, userID;
    public boolean showOnLeaderboard;

    public Point(String highscore, String userID, boolean showOnLeaderboard) {
        this.highscore = highscore;
        this.userID = userID;
        this.showOnLeaderboard = showOnLeaderboard;
    }

    public String getHighscore() {
        return highscore;
    }

    public void setHighscore(String highscore) {
        this.highscore = highscore;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isShowOnLeaderboard() {
        return showOnLeaderboard;
    }

    public void setShowOnLeaderboard(boolean showOnLeaderboard) {
        this.showOnLeaderboard = showOnLeaderboard;
    }
}
