package com.example.countmein;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String EXECUTION_COUNT_KEY = "executionCount";

    private int executionCount;
    private TextView countTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countTextView = findViewById(R.id.number);

        // Load the execution count from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        executionCount = prefs.getInt(EXECUTION_COUNT_KEY, 1);

        // Display the execution count in the UI
        countTextView.setText(getString(R.string.execution_count, executionCount));

        // Increment the execution count
        executionCount++;

        // Save the updated count to SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(EXECUTION_COUNT_KEY, executionCount);
        editor.apply();
    }
}


