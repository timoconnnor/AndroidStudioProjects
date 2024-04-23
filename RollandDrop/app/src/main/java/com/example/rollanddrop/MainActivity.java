package com.example.rollanddrop;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView ballImageView = findViewById(R.id.ballImageView);
        Button rollAndDropButton = findViewById(R.id.rollAndDropButton);


        rollAndDropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRollAndDropAnimation(ballImageView);
            }
        });
    }

    private void startRollAndDropAnimation(ImageView ballImageView) {

        Animation ballAnimation = AnimationUtils.loadAnimation(this, R.anim.ball_animation);
        ballImageView.startAnimation(ballAnimation);
    }
}

