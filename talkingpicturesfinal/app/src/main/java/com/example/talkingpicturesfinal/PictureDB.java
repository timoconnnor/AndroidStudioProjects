package com.example.talkingpicturesfinal;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PictureEntity.class}, version = 2, exportSchema = false)
public abstract class PictureDB extends RoomDatabase {

    public abstract PictureAccess daoAccess();

    // Add this part for migration
    public static PictureDB getInstance(Context context) {
        return Room.databaseBuilder(context, PictureDB.class, "PictureRoom.db")
                .fallbackToDestructiveMigration()
                .build();
    }
}

