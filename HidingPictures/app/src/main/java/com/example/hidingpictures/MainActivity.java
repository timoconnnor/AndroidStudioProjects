package com.example.hidingpictures;


import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView opinionTextView;
    private ImageView neutralImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        opinionTextView = findViewById(R.id.opiniontextview);
        neutralImageView = findViewById(R.id.neutral);

        // Register the ImageView for the context menu
        registerForContextMenu(neutralImageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.HappyOpinion:
                opinionTextView.setText("I Like it");
                return true;
            case R.id.NeutralOpinion:
                opinionTextView.setText("I am still neutral");
                return true;
            case R.id.SadOpinion:
                opinionTextView.setText("I don't like it");
                return true;
            case R.id.exit:
                finish();
                return true;
            case R.id.myButton:
                opinionTextView.setText("I have no opinion");
                neutralImageView.setVisibility(View.VISIBLE);  // Make the neutral face visible again
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.InvisibleNeutral:
                // Make the neutral face invisible
                neutralImageView.setVisibility(View.INVISIBLE);
                return true;
            case R.id.GoneNeutral:
                // Make the neutral face gone (not taking up any space)
                neutralImageView.setVisibility(View.GONE);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
