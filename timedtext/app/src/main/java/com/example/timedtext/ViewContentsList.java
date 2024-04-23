package com.example.timedtext;

import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewContentsList extends AppCompatActivity {

    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contents_list);

        ListView listView = findViewById(R.id.listView);
        myDB = new DatabaseHelper(this);

        // populate an ArrayList<String> from the database and then view it
        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getListContents();
        if (data.getCount() == 0) {
            Toast.makeText(this, "There are no contents in this list!", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                // Get item content, time, and formatted time
                String item = data.getString(1);
                long timeInMillis = data.getLong(2); // Assuming the TIME column is at index 2
                String formattedTime = data.getString(3); // Assuming the FORMATTED_TIME column is at index 3

                // Combine item content and time information
                String listItem = item + "\nTime: " + formattedTime;

                theList.add(listItem);
            }

            // Set the adapter after retrieving all data
            ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
            listView.setAdapter(listAdapter);
        }
    }
}