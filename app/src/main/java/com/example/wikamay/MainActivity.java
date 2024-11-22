package com.example.wikamay;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

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
        if (progressButton != null) {
            progressButton.setOnClickListener(v -> launchActivity(ProgressActivity.class));
        } else {
            Log.e(TAG, "progressImageButton not found");
        }

        Button challengesAndQuizButton = findViewById(R.id.challengesAndQuizButton);
        if (challengesAndQuizButton != null) {
            challengesAndQuizButton.setOnClickListener(v -> launchActivity(ChallengesAndQuizActivity.class));
        } else {
            Log.e(TAG, "challengesAndQuizButton not found");
        }

        Button masteryPlantButton = findViewById(R.id.masteryPlantButton);
        if (masteryPlantButton != null) {
            masteryPlantButton.setOnClickListener(v -> launchActivity(MasteryPlantActivity.class));
        } else {
            Log.e(TAG, "masteryPlantButton not found");
        }
    }

    private void setupButtonListener(int buttonId, Class<?> activityClass) {
        ImageButton button = findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(v -> launchActivity(activityClass));
        } else {
            Log.e(TAG, "Button not found: " + getResources().getResourceEntryName(buttonId));
        }
    }

    private void launchActivity(Class<?> activityClass) {
        try {
            Log.d(TAG, "Launching activity: " + activityClass.getSimpleName());
            Intent intent = new Intent(MainActivity.this, activityClass);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error launching activity: " + activityClass.getSimpleName(), e);
            Toast.makeText(this, "Error launching activity", Toast.LENGTH_SHORT).show();
        }
    }
}