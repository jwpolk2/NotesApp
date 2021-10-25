package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    TextView welcomeText;
    public static ArrayList<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        //1. fetch username from shared preferences and display welcome message
        String usernameKey = "username";
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

        welcomeText = (TextView) findViewById(R.id.textViewWelcome);
        //Intent intent = getIntent();
        //String str = intent.getStringExtra("message");
        String userName = sharedPreferences.getString(usernameKey, "");
        welcomeText.setText("Welcome " + userName + "!");

        //2. get SQLiteDatabase instance
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("notes", Context.MODE_PRIVATE,null);

        //3.Initiate the "notes" class variable using readNotes method implemented in DBHelper class.
        //  the username you got from SharedPreferences as a parameter to readNotes method
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        notes = dbHelper.readNotes(userName);

        //4. Create an arrayList<String> object by iterating over notes object.
        ArrayList<String> displayNotes = new ArrayList<>();
        for (Note note : notes) {
            displayNotes.add(String.format("Title:%s\nDate:%s", note.getTitle(), note.getDate()));
            Log.i(note.getTitle() + " date: " + note.getDate(), "notes here");
        }

        //5. Use ListView view to display notes on screen
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, displayNotes);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        //6. Add onItemClickListener for ListView item, a note in our case
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Initialise intent to take user to third party activity (NoteActivity in this case).
                Intent intent = new Intent(getApplicationContext(), ThirdActivity.class);
                //Add the position of the item thats was clicked on as "noteid".
                intent.putExtra("noteid", position);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logoutOption:
                SharedPreferences sharedPreferences = getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

                sharedPreferences.edit().remove("username").apply();
                Intent logoutIntent = new Intent(this, MainActivity.class);
                startActivity(logoutIntent);
                return true;
            case R.id.addNoteOption:
                Intent newNoteIntent = new Intent(this, ThirdActivity.class);
                startActivity(newNoteIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}