package com.example.talkingbuttons2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private Button farmerButton;
    private Button dogButton;
    private Button sailorButton;
    private Button bitbutton;
    private Button killedbutton;
    private Button lovesButton;
    private Button exitButton;
    private Timer timer;
    private Map<Button, Queue<String>> buttonPhraseQueues = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get references to all you rb
        farmerButton = findViewById(R.id.farmer);
        dogButton = findViewById(R.id.dog);
        sailorButton = findViewById(R.id.sailor);
        bitbutton = findViewById(R.id.bit);
        lovesButton = findViewById(R.id.loves);
        killedbutton = findViewById(R.id.killed);
        exitButton = findViewById(R.id.exit);

        buttonPhraseQueues.put(farmerButton, new LinkedList<>());
        buttonPhraseQueues.put(dogButton, new LinkedList<>());
        buttonPhraseQueues.put(sailorButton, new LinkedList<>());
        buttonPhraseQueues.put(bitbutton, new LinkedList<>());
        buttonPhraseQueues.put(lovesButton, new LinkedList<>());
        buttonPhraseQueues.put(killedbutton, new LinkedList<>());



        // Initialize the TextToSpeech engine
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });



        // Set up a timer to say "I have nothing to say" every 5 seconds
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                boolean anyButtonClicked = false;

                for (Map.Entry<Button, Queue<String>> entry : buttonPhraseQueues.entrySet()) {
                    Button button = entry.getKey();
                    Queue<String> phraseQueue = entry.getValue();

                    // Check if the queue is not empty
                    if (!phraseQueue.isEmpty()) {
                        // Dequeue and speak each phrase in the queue
                        while (!phraseQueue.isEmpty()) {
                            String phrase = phraseQueue.poll();
                            textToSpeech.speak(phrase, TextToSpeech.QUEUE_ADD, null, null);
                            anyButtonClicked = true;
                        }
                    }
                }

                if (!anyButtonClicked) {
                    // If no button is clicked, speak "I have nothing to say"
                    textToSpeech.speak("I have nothing to say", TextToSpeech.QUEUE_ADD, null, null);
                }
            }
        }, 5000, 5000);

        // Set up click listeners for the Man and Dog buttons
        farmerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enqueue the phrase for the farmer button
                buttonPhraseQueues.get(farmerButton).offer("I am a farmer!");

                // When the Man button is clicked, say something like a man
                textToSpeech.speak("I am a farmer!", TextToSpeech.QUEUE_ADD, null, null);
                resetTimer();
            }
        });

        dogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enqueue the phrase for the dog button
                buttonPhraseQueues.get(dogButton).offer("Woof, woof! I am a dog");

                // When the Dog button is clicked, say something like a dog
                textToSpeech.speak("Woof, woof! I am a dog", TextToSpeech.QUEUE_ADD, null, null);
                resetTimer();
            }
        });
        sailorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPhraseQueues.get(sailorButton).offer("I am a sailor. All Aboard!");

                // When the sailor button is clicked, do something
                textToSpeech.speak("I am a sailor. All Aboard!", TextToSpeech.QUEUE_ADD, null, null);
                resetTimer();
            }
        });

        bitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPhraseQueues.get(bitbutton).offer("I just bit you! Grr!");
                // When the bit button is clicked, do something
                textToSpeech.speak("I just bit you! Grrr!", TextToSpeech.QUEUE_ADD, null, null);
                resetTimer();
            }
        });
        lovesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPhraseQueues.get(lovesButton).offer("I love you so much");
                // whe the loves button is clicked, it does something
                textToSpeech.speak("I love you so much", TextToSpeech.QUEUE_ADD, null, null);
                resetTimer();
            }
        });
        killedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPhraseQueues.get(sailorButton).offer("They were killed");
                // When the killed button is pressed, do something
                textToSpeech.speak("They were killed!", TextToSpeech.QUEUE_ADD, null, null);
                resetTimer();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the exit button is clicked, do something
                onDestroy();
                finish();
            }
        });
    }
    private void resetTimer() {
        // Cancel the current timer task and schedule a new one
        timer.cancel();
        timer.purge();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!farmerButton.isPressed() && !dogButton.isPressed() && !sailorButton.isPressed() && !bitbutton.isPressed() && !lovesButton.isPressed() && !killedbutton.isPressed() && !exitButton.isPressed()) {
                    // If none of these buttons are pressed"
                    textToSpeech.speak("I have nothing to say", TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        }, 5000, 5000);
    }

    @Override
    protected void onDestroy() {
        // Shut down the TextToSpeech engine when the app is closed
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}

