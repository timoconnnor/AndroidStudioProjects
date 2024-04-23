package com.example.listofpictures;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

public class CustomAdapter extends SimpleAdapter {
    private Context context;
    private List<Map<String, Object>> data;

    public CustomAdapter(Context context, List<Map<String, Object>> data, int resource, String[] from, int[] to ){
        super(context, data, resource, from, to);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        ImageView smileyface = view.findViewById(R.id.happysadface);

        // Set click listener for the list item
        view.setOnClickListener(v -> {
            // Flip the image when the item is clicked
            int currentImage = (int) data.get(position).get("image");
            int newImage = (currentImage == R.drawable.happy) ? R.drawable.unhappy : R.drawable.happy;

            // Update the image in the data
            data.get(position).put("image", newImage);

            // Update the image in the ImageView
            smileyface.setImageResource(newImage);

            // Show the resultant image at the top of the screen
            ImageView topImageView = ((Activity) context).findViewById(R.id.face);
            topImageView.setImageResource(newImage);
        });

        return view;
    }

}
