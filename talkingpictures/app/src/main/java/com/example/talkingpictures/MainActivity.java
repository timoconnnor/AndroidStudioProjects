package com.example.talkingpictures;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private boolean isRecording = false;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);

        openGalleryAndStartRecording();
    }

    private void openGalleryAndStartRecording() {
        // Open gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_CAPTURE);


        startRecording();
    }

    private void startRecording() {
        if (!isRecording) {

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


            String outputFile = getExternalCacheDir().getAbsolutePath() + "/recording.3gp";
            mediaRecorder.setOutputFile(outputFile);

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                isRecording = true;
                Toast.makeText(this, "Recording started", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error starting recording: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void stopRecording() {
        if (isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            isRecording = false;
            Toast.makeText(this, "Recording stopped", Toast.LENGTH_LONG).show();


            playRecording();
        }

    }

    private void playRecording() {
        mediaPlayer = new MediaPlayer();
        try {
            String outputFile = getExternalCacheDir().getAbsolutePath() + "/recording.3gp";
            mediaPlayer.setDataSource(outputFile);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    finish();
                }
            });
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error playing recording: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void displayImageAndRecording(Uri imageUri) {

        imageView.setVisibility(View.VISIBLE);
        imageView.setImageURI(imageUri);


        playRecording();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {

            Uri selectedImageUri = data.getData();
            displayImageAndRecording(selectedImageUri);
        } else {

            finish();
        }


        stopRecording();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        stopRecording();
    }
}