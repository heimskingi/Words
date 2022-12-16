package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

@SuppressLint("CustomSplashScreen")
public class Splashscreen extends AppCompatActivity {

    String data = "";
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        data = getJsonDataFromResource();
        boolean done = populateDatabaseWithWords(data);
        if(done){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //TODO provera da li je na tom tel-u vec prijavljen korisnik pa da ode na drugu stranicu
            Intent i = new Intent(Splashscreen.this, UserLogin.class);
            startActivity(i);
        }
        else{
            text = findViewById(R.id.splashText);
            text.setText("Unable to load resorces.");
        }
    }

    private boolean populateDatabaseWithWords(String jsonData){
        Database db = new Database(Splashscreen.this);

        try{
            JSONArray jsonArray=new JSONArray(jsonData);
            int arrLength = jsonArray.length();
            if(arrLength == 0){
                return false;
            }
            for(int i =0 ; i<arrLength; i++) {
                JSONObject one = jsonArray.getJSONObject(i);
                String sr = one.getString("sr");
                String eng = one.getString("eng");
                int level = one.getInt("nivo");
                int points = one.getInt("bodovi");
                Word w = new Word(sr,eng,points,level);
                boolean suc = db.addNewWord(w);
                if(!suc){
                    Toast.makeText(Splashscreen.this, "Failed to insert data in db.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    private String getJsonDataFromResource(){
        InputStream is = getResources().openRawResource(R.raw.words);
        Writer writer = new StringWriter();
        int size = 0;
        try {
            size = is.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        char[] buffer = new char[size];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String jsonString = writer.toString();
        if(jsonString.equals("")) {
            Toast.makeText(Splashscreen.this, "Unable to read json.", Toast.LENGTH_LONG).show();
        }
        return jsonString;
    }

}