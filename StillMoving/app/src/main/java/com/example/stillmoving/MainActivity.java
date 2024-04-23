package com.example.stillmoving;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_VIDEO_CAPTURE = 2;
    private Bitmap imageBitmap;

    private ImageView imageView;
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.ImageView);
        videoView = findViewById(R.id.videoView);



        // Check camera permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        } else {
            launchCameraForPhoto();
        }
    }

    private void launchCameraForPhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            // Handle if the device doesn't have a camera app
            finish();
        }
    }

    private void launchCameraForVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        } else {
            // Handle if the device doesn't have a video recording app
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                // Photo capture successful, set the image as the background of ImageView
                imageBitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(imageBitmap);

                // Continue with your logic
                Toast.makeText(this, "Image capture successful", Toast.LENGTH_SHORT).show();
                launchCameraForVideo();
            } else {
                // Photo capture failed or canceled, finish the app
                finish();
            }
        } else if (requestCode == REQUEST_VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Uri videoUri = data.getData();

                // Check if imageBitmap is not null before proceeding
                Uri photoUri = getImageUri(imageBitmap);
                if (photoUri != null) {
                    // Send both image and video URIs to DisplayMediaActivity
                    Intent displayMediaIntent = new Intent(this, DisplayMediaActivity.class);
                    displayMediaIntent.putExtra("photoUri", photoUri);
                    displayMediaIntent.putExtra("videoUri", videoUri);
                    startActivity(displayMediaIntent);
                } else {
                    // Handle the case where imageBitmap is null
                    Toast.makeText(this, "Image is null", Toast.LENGTH_SHORT).show();
                    finish();
                }

                // Video capture successful, do further processing if needed
                Toast.makeText(this, "Video capture successful", Toast.LENGTH_SHORT).show();
            } else {
                // Video capture failed or canceled, finish the app
                finish();
            }
        }
    }
    private Uri getImageUri(Bitmap bitmap) {
        if (imageBitmap != null) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, "Title", null);
            return Uri.parse(path);
        } else {
            // Handle the case where imageBitmap is null
            return null;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_IMAGE_CAPTURE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Camera permission granted, launch camera for photo
            launchCameraForPhoto();
        } else {
            // Camera permission denied, finish the app
            finish();
        }
    }
}





