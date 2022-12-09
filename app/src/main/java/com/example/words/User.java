package com.example.words;

public class User {

    public String username;
    public int games_played = 0;

    public User(String username, int games_played) {
        this.username = username;
        this.games_played = games_played;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGames_played() {
        return games_played;
    }

    public void setGames_played(int games_played) {
        this.games_played = games_played;
    }
}
