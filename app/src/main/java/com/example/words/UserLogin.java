package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UserLogin extends AppCompatActivity {

    EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        usernameEditText = findViewById(R.id.editTextUsername);
    }

    public void openDifficulty(View view) {
        Intent i = new Intent(UserLogin.this, Difficulty.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void openMenu(View view) {
        Intent i = new Intent(UserLogin.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void insertUser(View view) {
        String username = String.valueOf(usernameEditText.getText());
        Database db = new Database(UserLogin.this);
        if(db.doesUserExists(username)){
            Toast.makeText(UserLogin.this,"User with same username already exists. Please try something else.", Toast.LENGTH_LONG).show();
            usernameEditText.setText("");
        }else {
            User user = new User(username);
            boolean success = db.addUser(user);
            if (!success) {
                Toast.makeText(UserLogin.this, "Could not make your profile. Please try again!", Toast.LENGTH_LONG).show();
            } else {
                SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("username", username);
                editor.apply();
                Toast.makeText(UserLogin.this, "Successfully created account!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}