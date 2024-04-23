package com.example.talkingpicturesmidterm;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface pictureaccess {

    @Query("SELECT * FROM Picture ORDER BY imageID ASC")
    List<PictureEntity> fetchAllImages();

    @Query("SELECT * FROM Picture where imageID LIKE :id")
    PictureEntity getImageByImageMediaId(long id);

    @Insert
    void addImage(PictureEntity newPicture);

    @Delete
    void deleteImage(PictureEntity oldImage);

    @Update
    void updateImage(PictureEntity newImage);
}
