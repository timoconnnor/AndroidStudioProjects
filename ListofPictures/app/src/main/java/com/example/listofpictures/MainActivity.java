package com.example.listofpictures;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView countryList = findViewById(R.id.countrylist);

        // Sample data with images
        List<Map<String, Object>> countries = new ArrayList<>();
        for (String country : getResources().getStringArray(R.array.countries)) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", country);
            map.put("image", R.drawable.happy); // Default image
            countries.add(map);
        }

        // Define the keys for the adapter
        String[] from = {"name", "image"};
        int[] to = {R.id.countryname, R.id.happysadface};

        // Create the custom adapter
        CustomAdapter customAdapter = new CustomAdapter(this, countries, R.layout.list_item_layout, from, to);

        // Set the adapter to the ListView
        countryList.setAdapter(customAdapter);
    }
}