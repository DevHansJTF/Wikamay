package com.example.wikamay;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class ASLVocabularyMatchingActivity extends AppCompatActivity {

    private ImageView[] images = new ImageView[6];
    private Button prevButton, nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asl_vocabulary_matching);

        initializeViews();
        setupButtons();
        loadImages();
    }

    private void initializeViews() {
        images[0] = findViewById(R.id.image1);
        images[1] = findViewById(R.id.image2);
        images[2] = findViewById(R.id.image3);
        images[3] = findViewById(R.id.image4);
        images[4] = findViewById(R.id.image5);
        images[5] = findViewById(R.id.image6);

        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
    }

    private void setupButtons() {
        prevButton.setOnClickListener(v -> {
            // TODO: Handle previous set of images
        });

        nextButton.setOnClickListener(v -> {
            // TODO: Handle next set of images
        });
    }

    private void loadImages() {
        // TODO: Load images for the game
        // This is where you'll set the images for each ImageView
        // For example:
        // images[0].setImageResource(R.drawable.hello_image);
        // images[1].setImageResource(R.drawable.hello_sign);
        // ... and so on for the other images
    }

    // Add more methods here for game logic, scoring, etc.
}