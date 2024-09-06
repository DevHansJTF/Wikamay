package com.example.wikamay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.Intent;
import android.util.Log;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class QuizzesGamesMasteryActivity extends AppCompatActivity {

    private static final String TAG = "QuizzesGamesMasteryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzes_games_mastery);

        try {
            setupProgressItems();
            setupBackButton();
            setupCloseButton();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: ", e);
        }
    }

    private void setupProgressItems() {
        try {
            setupProgressItem(R.id.signGuessingProgress, "ASL Sign Guessing Game", 80, R.color.progress_purple);
            setupProgressItem(R.id.fingerSpellingProgress, "ASL Finger-Spelling Challenge", 60, R.color.progress_blue);
            setupProgressItem(R.id.signRaceProgress, "ASL Sign Race", 100, R.color.progress_green);
            setupProgressItem(R.id.vocabularyMatchingProgress, "ASL Vocabulary Matching Game", 90, R.color.progress_pink);
            setupProgressItem(R.id.signRecognitionProgress, "ASL Sign Recognition Quiz", 40, R.color.black);
            setupProgressItem(R.id.overallProgress, "OVERALL MASTERY", 74, R.color.progress_red);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up progress items: ", e);
        }
    }

    private void setupProgressItem(int layoutId, String itemName, int progress, int colorResId) {
        try {
            View itemView = findViewById(layoutId);
            if (itemView == null) {
                Log.e(TAG, "itemView not found for layoutId: " + layoutId);
                return;
            }

            TextView itemNameText = itemView.findViewById(R.id.itemName);
            CircularProgressIndicator progressBar = itemView.findViewById(R.id.itemProgressBar);
            TextView percentageText = itemView.findViewById(R.id.itemPercentage);

            if (itemNameText != null) itemNameText.setText(itemName);
            if (progressBar != null) {
                progressBar.setProgress(progress);
                progressBar.setIndicatorColor(ContextCompat.getColor(this, colorResId));
            }
            if (percentageText != null) percentageText.setText(progress + "%");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up progress item: " + itemName, e);
        }
    }

    private void setupBackButton() {
        Button backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        } else {
            Log.e(TAG, "backButton not found in layout");
        }
    }

    private void setupCloseButton() {
        ImageButton closeButton = findViewById(R.id.closeButton);
        if (closeButton != null) {
            closeButton.setOnClickListener(v -> {
                Intent intent = new Intent(QuizzesGamesMasteryActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        } else {
            Log.e(TAG, "closeButton not found in layout");
        }
    }
}