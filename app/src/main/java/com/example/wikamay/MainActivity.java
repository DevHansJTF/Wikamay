package com.example.wikamay;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        setupButtonListener(R.id.alphabetsImageButton, AlphabetLessonActivity.class);
        setupButtonListener(R.id.numbersImageButton, NumbersLessonActivity.class);
        setupButtonListener(R.id.animalsImageButton, AnimalsLessonActivity.class);
        setupButtonListener(R.id.emotionsImageButton, EmotionsLessonActivity.class);
        setupButtonListener(R.id.greetingsImageButton, GreetingsLessonActivity.class);

        ImageButton progressButton = findViewById(R.id.progressImageButton);
        progressButton.setOnClickListener(v -> launchActivity(ProgressActivity.class));

        Button challengesAndQuizButton = findViewById(R.id.challengesAndQuizButton);
        challengesAndQuizButton.setOnClickListener(v -> launchActivity(ChallengesAndQuizActivity.class));
    }

    private void setupButtonListener(int buttonId, Class<?> activityClass) {
        ImageButton button = findViewById(buttonId);
        button.setOnClickListener(v -> launchActivity(activityClass));
    }

    private void launchActivity(Class<?> activityClass) {
        try {
            Intent intent = new Intent(MainActivity.this, activityClass);
            startActivity(intent);
        } catch (Exception e) {
            // Handle exception if needed
        }
    }
}