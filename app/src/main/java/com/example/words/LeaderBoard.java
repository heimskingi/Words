package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class LeaderBoard extends AppCompatActivity {

    ListView listView;
    String usersData = "", data = "", eTag = "";
    URL url;
    InputStream inputStream;
    HttpURLConnection connection;
    SharedPreferences sharedPref;
    PopulateDB populateDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        listView = findViewById(R.id.leaderboardListView);

        boolean internetExists = InternetCheck.isInternetAvailable(LeaderBoard.this);
        if (internetExists) {
            DownloadUsers task2 = new DownloadUsers();
            task2.execute();
        }
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
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void openDifficulty(View view) {
        Intent i = new Intent(LeaderBoard.this, Difficulty.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private class DownloadUsers extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
            data = "";
            Database db = new Database(LeaderBoard.this);
            try {
                url = new URL(Constants.ApiKeys.usersApiUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-Master-Key", Constants.ApiKeys.X_MASTER_KEY);
                connection.setRequestProperty("X-Access-Key", Constants.ApiKeys.X_ACCESS_KEY);
                connection.connect();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                    return "Server response:" + connection.getResponseMessage();

                eTag = connection.getHeaderField("ETag");
                if (!sharedPref.contains("eTagUsers")) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("eTagUsers", eTag);
                    editor.apply();
                }

                inputStream = connection.getInputStream();
                if (inputStream == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String oneLine;
                while ((oneLine = reader.readLine()) != null) {
                    data += oneLine;
                }
                if (data.length() == 0) {
                    return null;
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray jsonArray = jsonObject.optJSONArray("record");
                        if (jsonArray != null) {
                           usersData = jsonArray.toString();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (connection != null) {
                    connection.disconnect();
                }

                if (!eTag.equals(sharedPref.getString("eTagUsers", null))) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("eTagUsers" , eTag);
                    editor.apply();
                }
            }
            return usersData;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                Toast.makeText(LeaderBoard.this, "No data", Toast.LENGTH_SHORT).show();
            }else{
                Database db = new Database(LeaderBoard.this);
                db.dropPreviousData(Constants.UserTable.TABLE_NAME);
                populateDB = new PopulateDB(LeaderBoard.this);
                populateDB.populateDatabaseWithUsers(usersData);
                PopulateListView();
            }
        }

    }

}