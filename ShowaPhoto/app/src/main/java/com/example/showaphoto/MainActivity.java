package com.example.showaphoto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AlertDialog dialog; // Declare dialog as a field

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button chooseATvShowButton = findViewById(R.id.choosetvshow);
        Button exitButton = findViewById(R.id.Exit);

        chooseATvShowButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
    }

    private void showTVListDialog() {
        // Sample list of TV shows
        String[] tvshow = {"Deadliest Catch", "Archer", "Top Gear", "M.A.S.H.", "Two and a Half Men", "Big Cat Diary"};

        // Create a custom adapter for the list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tvshow);

        // Create a ListView to display the movies
        ListView listView = new ListView(this);
        listView.setAdapter(adapter);

        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a TV Show")
                .setView(listView);

        dialog = builder.create(); // Assign dialog to the field

        // Create item click listener for the list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedTvShow = tvshow[position];
                showCustomTvShowDialog(selectedTvShow);
                dialog.dismiss(); // Dismiss the dialog using the field
            }
        });

        dialog.show();
    }

    private void showCustomTvShowDialog(String selectedTvShow) {
        // Create a custom dialog with the selected TV show
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Use a single LayoutInflater instance
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_tv_show_dialog, null);

        // Set up the ImageView and Button in the custom dialog
        ImageView tvShowImageView = dialogView.findViewById(R.id.tvShowImageView);
        Button dismissButton = dialogView.findViewById(R.id.dismissButton);

        // Set the image based on the selected TV show using a switch statement
        int imageResource;
        switch (selectedTvShow) {
            case "Deadliest Catch":
                imageResource = R.drawable.deadliestcatch;
                break;
            case "Archer":
                imageResource = R.drawable.archer;
                break;
            case "Top Gear":
                imageResource = R.drawable.topgear;
                break;
            case "M.A.S.H.":
                imageResource = R.drawable.mash;
                break;
            case "two and a half men":
                imageResource = R.drawable.twoandahalf;
                break;
            case "Big Cat Diary":
                imageResource = R.drawable.bigcatdiary;
                break;
            default:
                // Use a default image or handle unknown TV shows
                imageResource = R.drawable.unknown;
                break;
        }

        tvShowImageView.setImageResource(imageResource);

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.dismissButton:
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                }
            }
        });

        builder.setView(dialogView);
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choosetvshow:
                showTVListDialog();
                break;
            case R.id.Exit:
                finish();
                break;
        }
    }
}



