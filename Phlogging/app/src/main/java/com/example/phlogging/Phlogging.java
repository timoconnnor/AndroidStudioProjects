package com.example.phlogging;


import android.os.Bundle;

import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.net.Uri;
import android.text.format.Time;
import android.view.View;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.Toast;
import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;


import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class Phlogging extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String DATABASE_NAME = "PhlogEntry.db";
    private DataRoomDB phlogEntryDB;
    private List<DataRoomEntity> dbEntities;
    private ListView theList;
    SimpleAdapter listAdapter;
    private final int ACTIVITY_EDIT_PHLOG_ENTRY = 1;
    private final int ACTIVITY_VIEW_PHLOG_ENTRY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phlogging);

        // Initialize your database here
        phlogEntryDB = Room.databaseBuilder(getApplicationContext(), DataRoomDB.class, DATABASE_NAME).build();

        try {
            fillList();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception, log the error, or display a message to the user.
        }
    }

    public void fillList() {
        String[] displayFields = {
                "phlog_photo",
                "phlog_title",
                "phlog_description",
                "phlog_time"
        };

        int[] displayViews = {
                R.id.phlog_photo,
                R.id.phlog_title,
                R.id.phlog_description,
                R.id.phlog_time
        };

        theList = findViewById(R.id.the_list);

        // Create a new SimpleAdapter with the updated data
        listAdapter = new SimpleAdapter(this, fetchAllPhlogEntries(), R.layout.list_item, displayFields, displayViews);

        // Set the new adapter to the ListView
        theList.setAdapter(listAdapter);
    }

    // Mapping the DB to the ListItems ArrayList
    private ArrayList<HashMap<String,Object>> fetchAllPhlogEntries(){
        HashMap<String,Object> oneItem;
        ArrayList<HashMap<String,Object>> listItems;
        Time theTime;
        long unixTime;

        dbEntities = phlogEntryDB.daoAccess().fetchAllPhlogs(); //line 72
        listItems = new ArrayList<>();

        theTime = new Time();

        for (DataRoomEntity onePhlog: dbEntities) {
            oneItem = new HashMap<>();
            if (onePhlog.getCameraPhotoUriString() != null) {
                oneItem.put("phlog_photo", Uri.parse(onePhlog.getCameraPhotoUriString()));
            }
            oneItem.put("phlog_title", onePhlog.getPhlogTitle());
            oneItem.put("phlog_description", onePhlog.getText());
            unixTime = onePhlog.getUnixTime();
            theTime.set(unixTime);
            oneItem.put("phlog_time", theTime.format("%A %D %T"));
            listItems.add(oneItem);
        }

        return (listItems);
    }

    public void myClickHandler(View view){

        Intent editPhlogEntry;

        switch(view.getId()) {

            // Starting the EditPhlogEntry activity:
            case R.id.add_entry_button:
                editPhlogEntry = new Intent();
                editPhlogEntry.setClassName("com.example.phlogging", "com.example.phlogging.EditPhlogEntry");
                startActivityForResult(editPhlogEntry, ACTIVITY_EDIT_PHLOG_ENTRY);
                break;
            default:
                break;
        }
    }

    // View a phlog entry and enables
    public void onItemClick (AdapterView<?> parent, View view, int position, long rowId){
        long unixTime;
        Intent viewPhlogEntry = new Intent();

        unixTime = dbEntities.get(position).getUnixTime();
        viewPhlogEntry.setClassName("com.example.phlogging", "com.example.phlogging.ViewPhlogEntry");
        viewPhlogEntry.putExtra("unix_time", unixTime);
        startActivityForResult(viewPhlogEntry, ACTIVITY_VIEW_PHLOG_ENTRY);

    }

    // Edit Phlog Entry activ
    public boolean onItemLongClick (AdapterView<?> parent, View view, int position, long rowId) {
        long unixTime;
        Intent editPhlogEntry;

        unixTime = dbEntities.get(position).getUnixTime();
        editPhlogEntry = new Intent();
        editPhlogEntry.setClassName("com.example.phlogging.phlogging", "com.example.phlogging.EditPhlogEntry");
        editPhlogEntry.putExtra("unix_time", unixTime);
        startActivityForResult(editPhlogEntry, ACTIVITY_EDIT_PHLOG_ENTRY);

        return true;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        long deleteUnixTime;
        DataRoomEntity deletePhlogEntry;

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_EDIT_PHLOG_ENTRY:
                if (resultCode == Activity.RESULT_OK) {
                    fillList();
                } else {
                    Toast.makeText(this, "Okay, return to main activity",
                            Toast.LENGTH_LONG).show();
                }
                break;
            case ACTIVITY_VIEW_PHLOG_ENTRY:
                if (resultCode == Activity.RESULT_OK){
                    deleteUnixTime = data.getLongExtra("com.example.phlogging.delete_unix_time", -1);
                    if (deleteUnixTime != -1){
                        deletePhlogEntry = phlogEntryDB.daoAccess().getPhlogByUnixTime(deleteUnixTime);
                        phlogEntryDB.daoAccess().deletePhlog(deletePhlogEntry);
                        fillList();
                    }
                }
                break;
            default:
                break;

        }

    }
    private void initializeList() {
        try {
            fillList();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception, log the error, or display a message to the user.
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        initializeList();
    }


}