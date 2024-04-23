package com.example.talkingbuttons;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private Date lastButtonClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textToSpeech = new TextToSpeech(this, this);

        // Set up click listeners for subject/object buttons
        findViewById(R.id.btnMan).setOnClickListener(view -> speakText("the man"));
        findViewById(R.id.btnDog).setOnClickListener(view -> speakText("the dog"));

        // Set up click listeners for verb buttons
        findViewById(R.id.btnLoves).setOnClickListener(view -> speakText("loves"));
        findViewById(R.id.btnBit).setOnClickListener(view -> speakText("bit"));
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // TextToSpeech is initialized successfully
            lastButtonClickTime = new Date();
        } else {
            // Show a message if TextToSpeech initialization fails
            System.out.println("TextToSpeech initialization failed");
        }
    }

    private void speakText(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        lastButtonClickTime = new Date();
    }

    public void onButtonClick(View view) {
        // Handle button clicks
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - lastButtonClickTime.getTime();

        if (timeDifference >= 5000) {
            // If there's no button click for 5 seconds, say "I have nothing to say"
            textToSpeech.speak("I have nothing to say", TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release TextToSpeech resources when the app is closed
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
