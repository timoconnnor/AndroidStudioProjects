package com.example.stillmoving;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayMediaActivity extends AppCompatActivity {

    private ImageView imageView;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_media);

        imageView = findViewById(R.id.ImageView2);
        videoView = findViewById(R.id.videoView2);

        // Retrieve the image and video URIs from the intent
        Intent intent = getIntent();
        Uri photoUri = intent.getParcelableExtra("photoUri");
        Uri videoUri = intent.getParcelableExtra("videoUri");

        // Check if URIs are not null before proceeding
        if (photoUri != null) {
            imageView.setImageURI(photoUri);
            imageView.setVisibility(View.VISIBLE);
        }

        if (videoUri != null) {
            videoView.setVideoURI(videoUri);
            videoView.start();
            videoView.setVisibility(View.VISIBLE);
        }
    }
}
