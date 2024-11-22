package com.example.wikamay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.content.Intent;
import android.util.Log;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class ProgressActivity extends AppCompatActivity {

    private static final String TAG = "ProgressActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        try {
            setupCloseButton();
            setupProgressItems();
            setupNextButton();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: ", e);
        }
    }

    private void setupCloseButton() {
        ImageButton closeButton = findViewById(R.id.closeButton);
        if (closeButton != null) {
            closeButton.setOnClickListener(v -> finish());
        } else {
            Log.e(TAG, "closeButton not found in layout");
        }
    }

    private void setupProgressItems() {
        try {
            setupProgressItem(R.id.alphabetsProgress, "Memorized Alphabets", R.drawable.alphabet_lesson_logo, 60, R.color.progress_purple);
            setupProgressItem(R.id.numbersProgress, "Mastered Numbers", R.drawable.numbers_lesson_logo, 50, R.color.progress_blue);
            setupProgressItem(R.id.animalsProgress, "Familiarized Animals", R.drawable.animals_lesson_logo, 40, R.color.progress_green);
            setupProgressItem(R.id.emotionsProgress, "Known Emotions", R.drawable.emotions_lesson_logo, 60, R.color.progress_pink);
            setupProgressItem(R.id.greetingsProgress, "Greetings Fluency", R.drawable.greetings_lesson_logo, 60, R.color.black);
            setupProgressItem(R.id.overallProgress, "OVERALL MASTERY", R.drawable.progress_lesson_logo, 60, R.color.progress_red);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up progress items: ", e);
        }
    }

    private void setupProgressItem(int layoutId, String itemName, int iconResId, int progress, int colorResId) {
        try {
            View itemView = findViewById(layoutId);
            if (itemView == null) {
                Log.e(TAG, "itemView not found for layoutId: " + layoutId);
                return;
            }

            ImageView itemIcon = itemView.findViewById(R.id.itemIcon);
            TextView itemNameText = itemView.findViewById(R.id.itemName);
            CircularProgressIndicator progressBar = itemView.findViewById(R.id.itemProgressBar);
            TextView percentageText = itemView.findViewById(R.id.itemPercentage);

            if (itemIcon != null) itemIcon.setImageResource(iconResId);
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

    private void setupNextButton() {
        View nextButton = findViewById(R.id.nextButton);
        if (nextButton != null) {
            nextButton.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(ProgressActivity.this, QuizzesGamesMasteryActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, "Error starting QuizzesGamesMasteryActivity", e);
                }
            });
        } else {
            Log.e(TAG, "nextButton not found in layout");
        }
    }
}