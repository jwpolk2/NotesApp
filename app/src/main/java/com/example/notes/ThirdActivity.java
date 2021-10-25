package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThirdActivity extends AppCompatActivity {
    int noteid = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        //1. Get EditText view.
        EditText editTextBox = (EditText) findViewById(R.id.editTextTextMultiLine);

        //2. Get Intent.
        Intent intent = getIntent();

        //3. Get the value of the integer "noteid from intent"
        noteid = intent.getIntExtra("noteid", -1);

        //4. Initialise class variable "noteid" with the value from intent

        if (noteid != -1) {
            //Display content of ntoe by retrieving ""notes Arraylist in SecondActivity.
            Note note = NotesActivity.notes.get(noteid);
            String noteContent = note.getContent();
            // Use editText.setText() to display contents of notes on the screen
            editTextBox.setText(noteContent);
        }
    }

    public void saveMethod(View view){
        String usernameKey = "username";

        // 1. Get editText view and the content that the user entered.
        EditText contentText = (EditText) findViewById(R.id.editTextTextMultiLine);
        String content = contentText.getText().toString();
        // 2. Initialise SQLiteDatabse instance.
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("notes", Context.MODE_PRIVATE,null);
        // 3. Initialise DBHelper class.
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        //4. Set username in the following variable by fetching it from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(usernameKey, "");

        String title;
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String date = dateFormat.format(new Date());

        if (noteid == -1){ //Add note.
            title = "NOTE_" + (NotesActivity.notes.size() + 1);
            dbHelper.saveNotes(username, title, content, date);
        } else {
            title = "NOTE_" + (noteid +1);
            dbHelper.updateNote(title, date, content, username);
        }

        // 6. Go to the second activity using intents
        Intent intent = new Intent(this, NotesActivity.class);
        //intent.putExtra("message", s);
        startActivity(intent);

    }
}