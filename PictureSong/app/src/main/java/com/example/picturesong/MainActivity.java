package com.example.picturesong;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private ImageView imageView;
    private MediaPlayer mediaPlayer;
    private List<Long> imageIds = new ArrayList<>();
    private List<String> audioFilePaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPermissions();
    }

    private void getPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            goOnCreating();
        }
    }

    private void goOnCreating() {
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);

        queryAndDisplayRandomImage();
        queryAndPlayRandomAudio();
    }

    private void queryAndDisplayRandomImage() {
        String[] projection = {MediaStore.Images.Media._ID};
        ContentResolver contentResolver = getContentResolver();
        Cursor imageCursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);

        if (imageCursor != null && imageCursor.moveToFirst()) {
            do {
                long imageId = imageCursor.getLong(imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                imageIds.add(imageId);
            } while (imageCursor.moveToNext());
            imageCursor.close();

            long randomImageId = imageIds.get(new Random().nextInt(imageIds.size()));
            Uri imageUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, randomImageId);
            imageView.setImageURI(imageUri);
        }
    }

    private void queryAndPlayRandomAudio() {
        // Release the previous MediaPlayer to avoid issues
        releasePlayer();

        String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DATA};
        ContentResolver contentResolver = getContentResolver();
        Cursor audioCursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);

        if (audioCursor != null && audioCursor.moveToFirst()) {
            do {
                String audioFilePath = audioCursor.getString(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                audioFilePaths.add(audioFilePath);
                // Log the path to check which audio files are being added
                Log.d("AudioPath", "Audio File Path: " + audioFilePath);
            } while (audioCursor.moveToNext());
            audioCursor.close();

            String randomAudioFilePath = audioFilePaths.get(new Random().nextInt(audioFilePaths.size()));
            playAudio(randomAudioFilePath);
        }
    }

    private void playAudio(String audioFilePath) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.setOnCompletionListener(mp -> onCompletion());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error playing audio", Toast.LENGTH_SHORT).show();
        }
    }

    private void onCompletion() {
        // Delay finishing the activity to allow time for selecting a new audio file
        new android.os.Handler().postDelayed(
                this::finish,
                1000 // 1 second delay, adjust as needed
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}





