package com.example.wikamay;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ASLFingerSpellingActivity extends AppCompatActivity {

    private ImageView image1, image2;
    private TextView wordText, underscore1, underscore2, underscore3, timerText;
    private CardView cameraCard;
    private Button prevButton, nextButton;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asl_finger_spelling);

        initializeViews();
        setupTimer();
        setupButtons();
    }

    private void initializeViews() {
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        wordText = findViewById(R.id.wordText);
        underscore1 = findViewById(R.id.underscore1);
        underscore2 = findViewById(R.id.underscore2);
        underscore3 = findViewById(R.id.underscore3);
        timerText = findViewById(R.id.timerText);
        cameraCard = findViewById(R.id.cameraCard);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);

        image1.setImageResource(R.drawable.backpack_placeholder);
        image2.setImageResource(R.drawable.handbag_placeholder);
    }

    private void setupTimer() {
        timer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText(millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                timerText.setText("Time's up!");
                // Handle end of time logic here
            }
        }.start();
    }

    private void setupButtons() {
        prevButton.setOnClickListener(v -> {
            // Handle previous button click
            // e.g., load previous word or image
        });

        nextButton.setOnClickListener(v -> {
            // Handle next button click
            // e.g., load next word or image
        });
    }

    private void updateWord(String word) {
        wordText.setText(word);
        underscore1.setText("_");
        underscore2.setText("_");
        underscore3.setText("_");
    }

    private void handleUserInput(char letter) {
        // Update underscores based on user input
        // Check if the input is correct
        // Update game state accordingly
    }

    private void startCameraPreview() {
        // Implement camera preview logic here
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}