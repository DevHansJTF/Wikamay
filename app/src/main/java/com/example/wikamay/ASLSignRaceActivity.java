package com.example.wikamay;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ASLSignRaceActivity extends AppCompatActivity {

    private ImageView objectImage;
    private TextView timerText, timeLeftText;
    private CardView cameraCard;
    private Button prevButton, nextButton;
    private CountDownTimer gameTimer, roundTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asl_sign_race);

        initializeViews();
        setupTimer();
        setupButtons();
    }

    private void initializeViews() {
        objectImage = findViewById(R.id.objectImage);
        timerText = findViewById(R.id.timerText);
        timeLeftText = findViewById(R.id.timeLeftText);
        cameraCard = findViewById(R.id.cameraCard);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);

        // TODO: Set initial image for objectImage
        // objectImage.setImageResource(R.drawable.initial_object);
    }

    private void setupTimer() {
        gameTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftText.setText(millisUntilFinished / 1000 + " seconds left");
            }

            public void onFinish() {
                timeLeftText.setText("Time's up!");
                // TODO: Handle game over
            }
        }.start();

        startRoundTimer();
    }

    private void startRoundTimer() {
        if (roundTimer != null) {
            roundTimer.cancel();
        }
        roundTimer = new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText(millisUntilFinished / 1000 + " seconds");
            }

            public void onFinish() {
                timerText.setText("Next!");
                // TODO: Move to next object
            }
        }.start();
    }

    private void setupButtons() {
        prevButton.setOnClickListener(v -> {
            // TODO: Handle previous object
        });

        nextButton.setOnClickListener(v -> {
            // TODO: Handle next object
        });
    }

    private void startCameraPreview() {
        // TODO: Implement camera preview logic here
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        if (roundTimer != null) {
            roundTimer.cancel();
        }
    }
}