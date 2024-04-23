package com.example.phloggingfinal;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DisplayEdit extends AppCompatActivity {
    //Initializing variables
    private PhloggingEntity entryWithTimestamp;
    private float latitude;
    private float longitude;
    public Uri savedUri = null;
    private String timestamp;
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private ActivityResultLauncher<Uri> runCameraPhotoApp = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
        public void onActivityResult(Boolean result) {
            if (result.booleanValue()) {
                ((ImageView) DisplayEdit.this.findViewById(R.id.edit_image)).setImageURI((Uri) null);
                ((ImageView) DisplayEdit.this.findViewById(R.id.edit_image)).setImageURI(DisplayEdit.this.savedUri);
            }
        }
    });
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    ActivityResultLauncher<String> startGallery = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        public void onActivityResult(Uri resultUri) {
            if (resultUri != null) {
                DisplayEdit displayEdit = DisplayEdit.this;
                StringBuilder sb = new StringBuilder();
                sb.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());
                sb.append("/");
                String string = DisplayEdit.this.getResources().getString(R.string.camera_photo_name);
                sb.append(string.replaceAll("XXX", "" + System.currentTimeMillis()));
                Uri unused = displayEdit.savedUri = Uri.fromFile(new File(sb.toString()));
                try {
                    InputStream inputFromGallery = DisplayEdit.this.getContentResolver().openInputStream(resultUri);
                    OutputStream outputToApp = DisplayEdit.this.getContentResolver().openOutputStream(DisplayEdit.this.savedUri);
                    byte[] buffer = new byte[8192];
                    while (true) {
                        int read = inputFromGallery.read(buffer);
                        int bytesRead = read;
                        if (read <= 0) {
                            break;
                        }
                        outputToApp.write(buffer, 0, bytesRead);
                    }
                    outputToApp.close();
                    inputFromGallery.close();
                } catch (Exception e) {
                    Toast.makeText(DisplayEdit.this.getApplicationContext(), "Error saving gallery image", Toast.LENGTH_SHORT).show();
                }
                ((ImageView) DisplayEdit.this.findViewById(R.id.edit_image)).setImageURI(DisplayEdit.this.savedUri);
            }
        }
    });
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_edit);
        this.timestamp = getIntent().getStringExtra("edu.miami.cs.tem.phlogging.timestamp");
        ((TextView) findViewById(R.id.edit_timestamp)).setText(this.timestamp);
        PhloggingEntity phlogByTimestamp = MainActivity.phloggingDB.daoAccess().getPhlogByTimestamp(this.timestamp);
        this.entryWithTimestamp = phlogByTimestamp;

        if (phlogByTimestamp != null) {
            ((TextView) findViewById(R.id.edit_title)).setText(this.entryWithTimestamp.getTitle());
            ((TextView) findViewById(R.id.edit_text)).setText(this.entryWithTimestamp.getText());
            this.savedUri = Uri.parse(this.entryWithTimestamp.getUri());
            ((ImageView) findViewById(R.id.edit_image)).setImageURI(this.savedUri);
            this.latitude = this.entryWithTimestamp.getLatitude();
            this.longitude = this.entryWithTimestamp.getLongitude();
            ((TextView) findViewById(R.id.edit_latlon_location)).setText(String.format("Lat: %5.1f Lon: %5.1f", new Object[]{Float.valueOf(this.latitude), Float.valueOf(this.longitude)}));
            ((TextView) findViewById(R.id.edit_text_location)).setText(this.entryWithTimestamp.getTextLocation());
            return;
        }
        ((TextView) findViewById(R.id.edit_title)).setHint(getResources().getString(R.string.no_title));
        ((TextView) findViewById(R.id.edit_text)).setHint(getResources().getString(R.string.no_text));

        if (MainActivity.currentLocation != null) {
            this.latitude = (float) MainActivity.currentLocation.getLatitude();
            this.longitude = (float) MainActivity.currentLocation.getLongitude();
            ((TextView) findViewById(R.id.edit_latlon_location)).setText(String.format("Lat: %5.1f Lon: %5.1f", new Object[]{Float.valueOf(this.latitude), Float.valueOf(this.longitude)}));
            new SensorLocatorDecode(getApplicationContext(), this).execute(new Location[]{MainActivity.currentLocation});
            return;
        }
        ((TextView) findViewById(R.id.edit_latlon_location)).setText(getResources().getString(R.string.no_location));
        ((TextView) findViewById(R.id.edit_text_location)).setText(getResources().getString(R.string.no_location));
    }
    //edit, delete, camera, gallery buttons
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void editClickHandler(View view) throws IOException {
        switch (view.getId()) {
            case R.id.edit_camera:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = createImageFile();
                    if (photoFile != null) {
                        savedUri = FileProvider.getUriForFile(this,
                                getApplicationContext().getPackageName() + ".fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, savedUri);
                        runCameraPhotoApp.launch(savedUri);
                    }
                }
                return;
            case R.id.edit_delete:
                if (this.entryWithTimestamp != null) {
                    MainActivity.phloggingDB.daoAccess().deletePhlog(this.entryWithTimestamp);
                } else {
                    Toast.makeText(this, "No Phlog to delete", Toast.LENGTH_SHORT).show();
                }
                setResult(-1, new Intent());
                finish();
                return;
            case R.id.edit_gallery:
                this.startGallery.launch("image/*");
                return;
            case R.id.edit_save:
                PhloggingEntity newEntry = new PhloggingEntity();
                newEntry.setTitle(((TextView) findViewById(R.id.edit_title)).getText().toString());
                newEntry.setText(((TextView) findViewById(R.id.edit_text)).getText().toString());
                newEntry.setUri(this.savedUri.toString());
                newEntry.setTimestamp(((TextView) findViewById(R.id.edit_timestamp)).getText().toString());
                newEntry.setLatitude(this.latitude);
                newEntry.setLongitude(this.longitude);
                newEntry.setTextLocation(((TextView) findViewById(R.id.edit_text_location)).getText().toString());
                PhloggingEntity phloggingEntity = this.entryWithTimestamp;
                if (phloggingEntity != null) {
                    newEntry.setId(phloggingEntity.getId());
                    MainActivity.phloggingDB.daoAccess().updatePhlog(newEntry);
                } else {
                    MainActivity.phloggingDB.daoAccess().addPhlog(newEntry);
                }
                setResult(-1, new Intent());
                finish();
                return;
            default:
                return;
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        String currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}