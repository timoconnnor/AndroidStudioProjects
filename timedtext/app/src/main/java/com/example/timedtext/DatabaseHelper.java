package com.example.timedtext;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mylist.db";
    public static final String TABLE_NAME = "mylist_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "ITEM1";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2); // Increment version to trigger the upgrade
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " ITEM1 TEXT, " +
                " TIME INTEGER, " +
                " FORMATTED_TIME TEXT)";
        db.execSQL(createTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String item1) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Get the current time in milliseconds
        long currentTimeMillis = System.currentTimeMillis();

        // Format the current time using SimpleDateFormat
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedTime = sdf.format(new Date(currentTimeMillis));

        contentValues.put(COL2, item1);
        contentValues.put("TIME", currentTimeMillis); // Save time in milliseconds
        contentValues.put("FORMATTED_TIME", formattedTime); // Save formatted time string

        long result = db.insert(TABLE_NAME, null, contentValues);

        // if data was inserted incorrectly it will return -1
        return result != -1;
    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
}