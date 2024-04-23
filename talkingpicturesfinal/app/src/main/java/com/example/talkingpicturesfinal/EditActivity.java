package com.example.talkingpicturesfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Log.i("IN Edit", "onCreate()");

        String selectedDescription = getIntent().getStringExtra("imageDescription");

        EditText editText = findViewById(R.id.edit_image_text);
        editText.setText(selectedDescription);

        String imageUriString = getIntent().getStringExtra("imageUri");

        if (imageUriString != null) {
            Uri selectedUri = Uri.parse(imageUriString);

            ImageView imageView = findViewById(R.id.edit_image);
            imageView.setImageURI(selectedUri);
        }


        findViewById(R.id.save_button).setOnClickListener(v -> saveAndFinish());
    }


    @Override
    public void onBackPressed() {
        // Handle the back button press
        super.onBackPressed();
        saveAndFinish();
    }
    private void saveAndFinish() {
        EditText editText = findViewById(R.id.edit_image_text);

        if (editText != null) {
            String editedDescription = editText.getText().toString();

            Intent returnIntent = new Intent();
            returnIntent.putExtra("newDescription", editedDescription);
            setResult(RESULT_OK, returnIntent);
        } else {
            Log.e("ERROR", "EditText is null in saveAndFinish()");
            setResult(RESULT_CANCELED);
        }

        finish();
    }

}
