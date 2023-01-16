package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ProcessNewUser extends AppCompatActivity {

    ArrayList<User> usersList;
    URL url;
    HttpURLConnection connection;
    User newUser;
    String usersData = "", data = "", eTag = "";
    InputStream inputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_new_user);

        Database db = new Database(ProcessNewUser.this);
        SharedPreferences sharedPref =  getSharedPreferences("preferences", Context.MODE_PRIVATE);
        boolean internetExists = InternetCheck.isInternetAvailable(ProcessNewUser.this);
        if (internetExists) {
            newUser = db.getUser(sharedPref.getString("newUser", ""));
            DownloaddUsers task2 = new DownloaddUsers();
            task2.execute();
        }
        Intent i = new Intent(ProcessNewUser.this, Difficulty.class);
        startActivity(i);
    }

    public void insertUsers(String users){
        Database db = new Database(ProcessNewUser.this);
        db.dropPreviousData(Constants.UserTable.TABLE_NAME);
        PopulateDB populateDB = new PopulateDB(ProcessNewUser.this);
        populateDB.populateDatabaseWithUsers(users);
        usersList = db.getUsers();
        Toast.makeText(ProcessNewUser.this, String.valueOf(usersList.size()), Toast.LENGTH_LONG).show();
        usersList.add(newUser);
        sendData();
    }

    public void sendData(){
        SendNewUser sendNewUser = new SendNewUser();
        sendNewUser.execute();
    }

    private class SendNewUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                url = new URL(Constants.ApiKeys.usersApiUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("X-Master-Key", Constants.ApiKeys.X_MASTER_KEY);
                connection.setRequestProperty("X-Access-Key", Constants.ApiKeys.X_ACCESS_KEY);
                connection.setRequestProperty("Content-Type", "application/json");

                String payload =  "[" ;
                for (int i = 0; i < usersList.size(); i++){
                    payload += usersList.get(i).toJSON();
                    if(i != usersList.size() - 1 ){
                        payload += ",";
                    }
                    else{
                        payload += "]";
                    }
                }

                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                byte[] requestBody = payload.getBytes();
                outputStream.write(requestBody);
                outputStream.close();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                    return "Server response:" + connection.getResponseMessage();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Success.";
        }

        @Override
        protected void onPostExecute(String s) {
            if (!s.equals("Success.")) {
                try {
                    Toast.makeText(ProcessNewUser.this, "Couldn't send data! " + connection.getResponseMessage(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                SharedPreferences sharedPref =  getSharedPreferences("preferences", Context.MODE_PRIVATE);
                sharedPref.edit().remove("newUser").apply();
            }
        }

    }

    private class DownloaddUsers extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
            data = "";
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
                Toast.makeText(ProcessNewUser.this, "No data", Toast.LENGTH_SHORT).show();
            }else{
                insertUsers(s);
            }
        }

    }

}