package com.example.words;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GameLetterListAdapter extends ArrayAdapter<Character> {

    public GameLetterListAdapter(Context context, ArrayList<Character> arrayList){
        super(context, R.layout.game_one_letter, arrayList);
    }

    public View getView(int position, @Nullable View convertView, ViewGroup parent) {

        Character letter = getItem(position);
        if(convertView ==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.game_one_letter, parent,false);
        }
        TextView letterTextView = convertView.findViewById(R.id.letter);
        letterTextView.setText(String.valueOf(letter));

        return convertView;
    }
}
