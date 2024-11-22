package com.example.wikamay;

import android.os.Bundle;
import android.content.Intent;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ChallengesAndQuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges_and_quiz);
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        setupButtonListener(R.id.aslSignGuessingButton, ASLSignGuessingGameActivity.class);
        setupButtonListener(R.id.aslFingerSpellingButton, ASLFingerSpellingActivity.class);
        setupButtonListener(R.id.aslSignRaceButton, ASLSignRaceActivity.class);
        setupButtonListener(R.id.aslVocabularyMatchingButton, ASLVocabularyMatchingActivity.class);
        setupButtonListener(R.id.aslSignRecognitionQuiz, ASLSignRecognitionActivity.class);

        // Set up the back button
        FrameLayout backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChallengesAndQuizActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // This will close the current activity
        });
    }

    private void setupButtonListener(int buttonId, Class<?> activityClass) {
        ImageButton button = findViewById(buttonId);
        button.setOnClickListener(v -> launchActivity(activityClass));
    }

    private void launchActivity(Class<?> activityClass) {
        try {
            Intent intent = new Intent(ChallengesAndQuizActivity.this, activityClass);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Error launching activity", Toast.LENGTH_SHORT).show();
        }
    }
}