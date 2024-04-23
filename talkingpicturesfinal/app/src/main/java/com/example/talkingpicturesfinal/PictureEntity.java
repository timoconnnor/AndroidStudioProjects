package com.example.talkingpicturesfinal;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Picture")
public class PictureEntity {
    @ColumnInfo(name = "imageUri")
    private String imageUri;

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
    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

}
