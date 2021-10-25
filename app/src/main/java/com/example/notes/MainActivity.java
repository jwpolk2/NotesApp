package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {



    public void clickFunction(View view){
        //Get username and password via EditText view
        EditText userNameText = (EditText) findViewById(R.id.editTextTextPersonName);
        String userName = userNameText.getText().toString();

        //Add username to shared preference object
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("username", userName).apply();

        //start the second activity
        goToNotes(userName);
    }

    public void goToNotes(String s) {
        Intent intent = new Intent(this, NotesActivity.class);
        //intent.putExtra("message", s);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String usernameKey = "username";
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

        if(!sharedPreferences.getString(usernameKey, "").equals("")){
            //"username" key exists in SharedPrefernces object which means that a user was logged in
            //Get the name of that user from SharedPreferences
            String userName = sharedPreferences.getString(usernameKey, "");
            goToNotes(userName);
        } else {
            //sharedpreferences object has no username key set
            //start screen 1, that is the main activity
            setContentView(R.layout.activity_main);
        }

    }
}