package com.example.facinghome;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final double BIRTHPLACE_LATITUDE = 40.9793;  // Replace with actual birthplace latitude
    private static final double BIRTHPLACE_LONGITUDE = -74.1165;  // Replace with actual birthplace longitude

    private static final double HOME_LATITUDE = 40.9793;  // Replace with actual home latitude
    private static final double HOME_LONGITUDE = -74.1165;  // Replace with actual home longitude
    private static final double PROXIMITY_THRESHOLD = 5;  // Set a threshold for proximity to home

    private static final int ANGLE_THRESHOLD = 1;
    private static final double DISTANCE_THRESHOLD = 100;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private Sensor magnetometer;
    private LocationManager locationManager;

    private float[] accelerometerValues = new float[3];
    private float[] gyroscopeValues = new float[3];
    private float[] magnetometerValues = new float[3];

    private ProgressBar progressBar;
    private double azimuthValue;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;

    private double angleDifference;
    private boolean initialUpdate = true;
    private TextView textViewOrientation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        textViewOrientation = findViewById(R.id.textViewOrientation);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        checkLocationPermission();

        // Initialize progress bar based on initial orientation
        int initialProgress = calculateProgress(angleDifference);
        progressBar.setProgress(initialProgress);

        // Update orientation text
        updateOrientationText(angleDifference);
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        // Check if the app has permission to access location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);

        } else {
            // Handle the case where permission is not granted
            // You may request permission again or inform the user
            // You can also use a default value or handle it according to your app's logic
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void updateOrientationText(double angleDifference) {
        String orientationText;
        if (angleDifference <= ANGLE_THRESHOLD) {
            // Phone is facing birthplace
            orientationText = "Home is " + getBearing() + " degrees, and you are facing home.";
        } else {
            // Phone is not facing birthplace
            orientationText = "Home is "+ getBearing() + "Current orientation: " + angleDifference + " degrees.";
        }

        // Update the TextView
        textViewOrientation.setText(orientationText);
    }

    // In onSensorChanged method
    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerValues = event.values.clone();
                break;
            case Sensor.TYPE_GYROSCOPE:
                gyroscopeValues = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magnetometerValues = event.values.clone();
                break;
        }

        updateOrientation();
    }

    private void updateOrientation() {
        float[] rotationMatrix = new float[9];
        float[] orientationValues = new float[3];

        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerValues, magnetometerValues);
        SensorManager.getOrientation(rotationMatrix, orientationValues);

        double azimuth = Math.toDegrees(orientationValues[0]);
        double angleDifference = Math.abs(azimuth - getBearing());

        Log.d("FacingHome", "Azimuth: " + azimuth); // Add this line
        Log.d("FacingHome", "Angle Difference: " + angleDifference); // Add this line

        if (initialUpdate) {
            initialUpdate = false;  // Ignore the initial update
            return;
        }

        if (angleDifference <= ANGLE_THRESHOLD) {
            // Phone is facing birthplace
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(500);
            }
            updateProgressBar(100);  // Set progress to 100 if facing birthplace
        } else {
            // Gradual reduction of progress
            updateProgressBar(calculateProgress(angleDifference));
        }

        // Update orientation text
        updateOrientationText(angleDifference);
    }


    private double getBearing() {
        // Check if the app has permission to access location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            Location birthplace = new Location("Birthplace");
            birthplace.setLatitude(BIRTHPLACE_LATITUDE);
            birthplace.setLongitude(BIRTHPLACE_LONGITUDE);

            Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (currentLocation != null) {
                return currentLocation.bearingTo(birthplace);
            }
        } else {
            // Handle the case where permission is not granted
            // You may request permission again or inform the user
            // You can also use a default value or handle it according to your app's logic
        }

        return 0;
    }

    private void updateProgressBar(final double distance) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Calculate progress based on the distance
                int progress = calculateProgress(distance);
                progressBar.setProgress(progress);
            }
        });
    }


    private int calculateProgress(double angleDifference) {
        // Linear reduction of progress
        return Math.max(0, 100 - (int) (angleDifference / ANGLE_THRESHOLD));
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }

    @Override
    public void onLocationChanged(Location location) {
        // Location update
        updateOrientation();
        updateDistance(location);

        Log.d("FacingHome", "Distance to Home: " + location.distanceTo(getHomeLocation())); // Add this line

        // Check if the angle difference is less than or equal to ANGLE_THRESHOLD
        if (!initialUpdate && angleDifference <= ANGLE_THRESHOLD) {
            // Beep sound
            playBeepSound();

            // Display "Walk straight home" toast
            showToast("Walk straight home");
        }
    }
    private Location getHomeLocation() {
        Location homeLocation = new Location("Home");
        homeLocation.setLatitude(HOME_LATITUDE);
        homeLocation.setLongitude(HOME_LONGITUDE);
        return homeLocation;
    }
    private void playBeepSound() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.beep); // Create MediaPlayer with your beep sound resource
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // Release the MediaPlayer resources when playback is completed
                    mp.release();
                    mediaPlayer = null;
                }
            });
        }

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void showToast(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void updateDistance(Location location) {
        // Check if the app has permission to access location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            Location birthplace = new Location("Birthplace");
            birthplace.setLatitude(BIRTHPLACE_LATITUDE);
            birthplace.setLongitude(BIRTHPLACE_LONGITUDE);

            float distance = location.distanceTo(birthplace);
            updateProgressBar(distance);
        } else {
            // Handle the case where permission is not granted
            // You may request permission again or inform the user
            // You can also use a default value or handle it according to your app's logic
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
