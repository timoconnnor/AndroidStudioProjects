package com.example.talkingpicturesmidterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class editactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Log.i("IN Edit", "onCreate()");

        String selectedDescription = this.getIntent().getStringExtra("imageDescription");
        EditText editText = findViewById(R.id.edit_image_text);
        editText.setText(selectedDescription);

        Uri selectedUri = Uri.parse(this.getIntent().getStringExtra("imageUri"));
        ImageView imageView = findViewById(R.id.edit_image);
        imageView.setImageURI(selectedUri);
    }

    public void onClick(View view) {

        String editedDescription;
        EditText editText = findViewById(R.id.edit_image_text);
        Intent returnIntent;

        switch(view.getId()) {
            case R.id.save_button:
                Log.i("IN Edit", "IN onClick(): case R.id.save_button");
                returnIntent = new Intent();
                editedDescription = editText.getText().toString();
                returnIntent.putExtra("newDescription", editedDescription);
                setResult(RESULT_OK, returnIntent);
                finish();
                break;
            default:
                Log.i("ERROR", "IN onClick(): default case");
                break;
        }
    }
}
