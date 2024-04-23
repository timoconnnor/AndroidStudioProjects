package com.example.talkingpicturesfinal;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PictureAccess {

    @Query("SELECT * FROM Picture ORDER BY imageID ASC")
    List<PictureEntity> fetchAllImages();

    @Query("SELECT * FROM Picture WHERE imageID LIKE :id")
    PictureEntity getImageByImageMediaId(long id);

    @Query("SELECT * FROM Picture WHERE imageUri LIKE :uri")
    PictureEntity getImageByImageUri(String uri);

    @Insert
    void addImage(PictureEntity newPicture);

    @Delete
    void deleteImage(PictureEntity oldImage);

    @Update
    void updateImage(PictureEntity newImage);
}

