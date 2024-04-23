package com.example.talkingpicturesmidterm;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PictureEntity.class}, version = 1, exportSchema = false)
public abstract class PictureDB extends RoomDatabase {

    public abstract PictureAccess daoAccess();
}
