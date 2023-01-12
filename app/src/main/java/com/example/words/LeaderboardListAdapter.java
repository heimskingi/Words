package com.example.words;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class LeaderboardListAdapter  extends ArrayAdapter<User> {

    public LeaderboardListAdapter(Context context, ArrayList<User> arrayList){
        super(context, R.layout.leaderboard_list_item, arrayList);
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {

        User u = (User) getItem(position);
        if(convertView ==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.leaderboard_list_item, parent,false);
        }
        TextView username = convertView.findViewById(R.id.usernameLeaderboard);
        TextView words = convertView.findViewById(R.id.wordsLeaderboard);
        TextView highscore = convertView.findViewById(R.id.highsoreLeaderboard);
        username.setText(u.getUsername());
        words.setText(u.getMaxWords() + "\nwords");
        highscore.setText(u.getHighscore() + "\npoints");

        return convertView;
    }
}
