package com.example.phloggingfinal;

import androidx.room.RoomDatabase;

public abstract class PhloggingDB extends RoomDatabase {
    public abstract PhloggingAccess daoAccess();
}
