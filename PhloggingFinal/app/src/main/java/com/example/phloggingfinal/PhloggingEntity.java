package com.example.phloggingfinal;

public class PhloggingEntity {
    private int id;
    private float latitude;
    private float longitude;
    private String text;
    private String textLocation;
    private String timestamp;
    private String title;
    private String uri;

    public int getId() {
        return this.id;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String newText) {
        this.text = newText;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String newUri) {
        this.uri = newUri;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String newTimestamp) {
        this.timestamp = newTimestamp;
    }

    public float getLatitude() {
        return this.latitude;
    }

    public void setLatitude(float newLatitude) {
        this.latitude = newLatitude;
    }

    public float getLongitude() {
        return this.longitude;
    }

    public void setLongitude(float newLongitude) {
        this.longitude = newLongitude;
    }

    public String getTextLocation() {
        return this.textLocation;
    }

    public void setTextLocation(String newTextLocation) {
        this.textLocation = newTextLocation;
    }
}
