package com.example.phlogging;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

@Dao
public interface DataRoomAccess {
    @Query("SELECT * FROM PhlogEntry ORDER BY unix_time DESC")
    List<DataRoomEntity> fetchAllPhlogs();

    @Query("SELECT * FROM PhlogEntry WHERE unix_time LIKE :time")
    DataRoomEntity getPhlogByUnixTime(long time);

    @Query("SELECT * FROM PhlogEntry WHERE phlog_title LIKE :title")
    DataRoomEntity getPhlogByTitle(String title);

    @Insert
    void addPhlog(DataRoomEntity newPhlog);

    @Delete
    void deletePhlog(DataRoomEntity oldPhlog);

    @Update
    void updatePhlog(DataRoomEntity oldPhlog);
}
