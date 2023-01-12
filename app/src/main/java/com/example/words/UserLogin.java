package com.example.words;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class UserLogin extends AppCompatActivity {

    EditText usernameEditText;
    User user;
    ArrayList<User> users;
    URL url;
    HttpURLConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        usernameEditText = findViewById(R.id.editTextUsername);
        DownloadUsersFromApi downloadUsers = new DownloadUsersFromApi();
        downloadUsers.execute();
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
            user = new User(username);
            boolean success = db.addUser(user);
            users = db.getUsers();
            SendNewUserToApi sendNewUserToApi = new SendNewUserToApi();
            sendNewUserToApi.execute();
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

    private class SendNewUserToApi extends AsyncTask<String, Void, String> {

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
                for (int i = 0; i < users.size(); i++){
                    payload += users.get(i).toJSON();
                    if(i != users.size() - 1 ){
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
                    Toast.makeText(UserLogin.this, "Couldn't send data! " + connection.getResponseMessage(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private class DownloadUsersFromApi extends AsyncTask<String, Void, String> {

        String data = "", usersData = "";
        InputStream inputStream;
        PopulateDB populateDB = new PopulateDB(UserLogin.this);

        @Override
        protected String doInBackground(String... strings) {
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
            }
            return usersData;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                Toast.makeText(UserLogin.this, "No data", Toast.LENGTH_SHORT).show();
            }else{
                populateDB.populateDatabaseWithUsers(usersData);
            }
        }

    }

}