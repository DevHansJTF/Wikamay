package com.example.wikamay;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ASLSignRecognitionActivity extends AppCompatActivity {

    private ImageView signImage;
    private TextView questionText;
    private ImageButton[] answerButtons = new ImageButton[4];
    private Button prevButton, nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asl_sign_recognition);

        initializeViews();
        setupButtons();
        loadQuestion();
    }

    private void initializeViews() {
        signImage = findViewById(R.id.signImage);
        questionText = findViewById(R.id.questionText);
        answerButtons[0] = findViewById(R.id.answerButton1);
        answerButtons[1] = findViewById(R.id.answerButton2);
        answerButtons[2] = findViewById(R.id.answerButton3);
        answerButtons[3] = findViewById(R.id.answerButton4);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
    }

    private void setupButtons() {
        for (int i = 0; i < answerButtons.length; i++) {
            final int index = i;
            answerButtons[i].setOnClickListener(v -> checkAnswer(index));
        }

        prevButton.setOnClickListener(v -> loadPreviousQuestion());
        nextButton.setOnClickListener(v -> loadNextQuestion());
    }

    private void loadQuestion() {
        // TODO: Load question data
        // This is where you'll set the sign image and answer options
        // For example:
        // signImage.setImageResource(R.drawable.sign_hello);
        // answerButtons[0].setImageResource(R.drawable.answer_hello);
        // answerButtons[1].setImageResource(R.drawable.answer_goodbye);
        // ... and so on for the other answer options
    }

    private void checkAnswer(int selectedIndex) {
        // TODO: Implement answer checking logic
    }

    private void loadPreviousQuestion() {
        // TODO: Load the previous question
    }

    private void loadNextQuestion() {
        // TODO: Load the next question
    }
}