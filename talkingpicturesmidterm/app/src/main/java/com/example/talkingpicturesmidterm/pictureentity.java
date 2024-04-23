package com.example.talkingpicturesmidterm;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Picture")
public class PictureEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "imageID")
    private long imageID;
    @ColumnInfo(name = "description")
    private String imageDescription;

    public PictureEntity() {}

    public int getId() {
        return(id);
    }

    public long getImageID() {
        return(imageID);
    }

    public String getImageDescription() {
        return(imageDescription);
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public void setImageID(long newImageID) {
        this.imageID = newImageID;
    }

    public void setImageDescription(String newDescription) {
        this.imageDescription = newDescription;
    }
}
