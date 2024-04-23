package com.example.phloggingfinal;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SimpleAdapter.ViewBinder {
    //Initializing variables
    private static final String DATABASE_NAME = "PhloggingRoom.db";
    public static Location currentLocation = null;
    public static PhloggingDB phloggingDB;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    //getting permissions
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private ActivityResultLauncher<String[]> getPermissions = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        public void onActivityResult(Map<String, Boolean> results) {
            for (String key : results.keySet()) {
                if (!results.get(key).booleanValue()) {
                    MainActivity.this.goOnCreating(false);
                }
            }
            MainActivity.this.goOnCreating(true);
        }
    });
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    LocationCallback myLocationCallback = new LocationCallback() {
        public void onLocationResult(LocationResult locationResult) {
            MainActivity.currentLocation = locationResult.getLastLocation();
        }
    };
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    ActivityResultLauncher<Intent> startEditDisplay = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == -1) {
                MainActivity.this.fillList();
            }
        }
    });

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getPermissions.launch(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"});
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void goOnCreating(boolean havePermission) {
        String[] strArr = {"_id", "title", "_data"};
        if (havePermission) {
            setContentView( R.layout.activity_main);
            phloggingDB = Room.databaseBuilder(getApplicationContext(), PhloggingDB.class, DATABASE_NAME).allowMainThreadQueries().build();
            fillList();
            this.fusedLocationClient = LocationServices.getFusedLocationProviderClient((Activity) this);
            LocationRequest locationRequest2 = new LocationRequest();
            this.locationRequest = locationRequest2;
            locationRequest2.setInterval( getResources().getInteger(R.integer.time_between_location_updates_ms));
            this.locationRequest.setFastestInterval( getResources().getInteger(R.integer.time_between_location_updates_ms));
            this.locationRequest.setPriority(100);
            try {
                this.fusedLocationClient.requestLocationUpdates(this.locationRequest, this.myLocationCallback, Looper.myLooper());
            } catch (SecurityException e) {
                Toast.makeText(this, "Permission denied for location", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Need permission", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    //filling list
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void fillList() {
        String[] displayFields = {"list_thumbnail", "list_title", "list_timestamp"};
        int[] displayViews = {R.id.list_thumbnail, R.id.list_title, R.id.list_timestamp};
        ListView theList = findViewById(R.id.phlog_list);
        List<PhloggingEntity> allPhlogEntities = phloggingDB.daoAccess().fetchAllPhlogs();
        ArrayList<HashMap<String, Object>> arrayListOfPhlogHashes = new ArrayList<>();
        for (PhloggingEntity onePhlog : allPhlogEntities) {
            HashMap<String, Object> onePhlogHash = new HashMap<>();
            onePhlogHash.put("list_thumbnail", onePhlog.getUri());
            onePhlogHash.put("list_title", onePhlog.getTitle());
            onePhlogHash.put("list_timestamp", onePhlog.getTimestamp());
            arrayListOfPhlogHashes.add(onePhlogHash);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, arrayListOfPhlogHashes, R.layout.phlog_list_entry, displayFields, displayViews);
        simpleAdapter.setViewBinder(this);
        theList.setOnItemClickListener(this);
        theList.setAdapter(simpleAdapter);
    }
    //setting values for the view
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean setViewValue(View view, Object data, String asText) {
        switch (view.getId()) {
            case R.id.list_thumbnail:
                Log.i("DEBUG", "The saved uri to show in main is ===" + ((String) data) + "===");
                ((ImageView) view).setImageURI(Uri.parse((String) data));
                return true;
            case R.id.list_timestamp:
                ((TextView) view).setText((String) data);
                return true;
            case R.id.list_title:
                if (data == null || ((String) data).isEmpty()) {
                    ((TextView) view).setText(getResources().getString(R.string.no_title));
                    return true;
                }
                ((TextView) view).setText((String) data);
                return true;
            default:
                return true;
        }
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void myClickHandler(View view) {
        switch (view.getId()) {
            case R.id.make_phlog :
                Intent displayEditIntent = new Intent(this, DisplayEdit.class);
                displayEditIntent.putExtra("edu.miami.cs.tem.phlogging.timestamp", timeToString(System.currentTimeMillis()));
                this.startEditDisplay.launch(displayEditIntent);
                return;
            default:
                return;
        }
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {
        Intent displayEditIntent = new Intent(this, DisplayEdit.class);
        displayEditIntent.putExtra("edu.miami.cs.tem.phlogging.timestamp", ((TextView) view.findViewById(R.id.list_timestamp)).getText());
        this.startEditDisplay.launch(displayEditIntent);
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private static String timeToString(long time) {
        GregorianCalendar lastRunTime = new GregorianCalendar();
        lastRunTime.setTimeInMillis(time);
        return new SimpleDateFormat("HH:mm:ss EEEE, MMMM dd, yyyy z").format(lastRunTime.getTime());
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onDestroy() {
        super.onDestroy();
        phloggingDB.close();
        this.fusedLocationClient.removeLocationUpdates(this.myLocationCallback);
    }
}