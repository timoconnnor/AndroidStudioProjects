package com.example.phloggingfinal;


import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.TextView;
import java.util.List;

public class SensorLocatorDecode extends AsyncTask<Location, Void, String> {
    private Activity theActivity;
    private Context theContext;
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public SensorLocatorDecode(Context context, Activity activity) {
        this.theContext = context;
        this.theActivity = activity;
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public String doInBackground(Location... location) {
        return androidGeodecode(location[0]);
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void onPostExecute(String result) {
        ((TextView) this.theActivity.findViewById(R.id.edit_text_location)).setText(result);
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private String androidGeodecode(Location thisLocation) {
        if (!Geocoder.isPresent()) {
            return "ERROR: No Geocoder available";
        }
        try {
            List<Address> addresses = new Geocoder(this.theContext).getFromLocation(thisLocation.getLatitude(), thisLocation.getLongitude(), 1);
            if (addresses.isEmpty()) {
                return "ERROR: Unkown location";
            }
            Address firstAddress = addresses.get(0);
            String locationName = "";
            int index = 0;
            while (true) {
                String addressLine = firstAddress.getAddressLine(index);
                String addressLine2 = addressLine;
                if (addressLine == null) {
                    return locationName;
                }
                locationName = locationName + addressLine2 + ", ";
                index++;
            }
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}
