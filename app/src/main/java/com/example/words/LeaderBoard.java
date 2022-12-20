package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class LeaderBoard extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        listView = findViewById(R.id.leaderboardListView);
        PopulateListView();
    }

    public void PopulateListView(){
        Database db = new Database(LeaderBoard.this);
        ArrayList<User> list = db.getTop5();
        LeaderboardListAdapter listAdapter = new LeaderboardListAdapter(LeaderBoard.this, list);
        listView.setAdapter(listAdapter);
    }

    public void openMenu(View view) {
        Intent i = new Intent(LeaderBoard.this, MainActivity.class);
        startActivity(i);
    }

    public void openDifficulty(View view) {
        Intent i = new Intent(LeaderBoard.this, Difficulty.class);
        startActivity(i);
    }
}