package com.example.wikamay;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ASLSignRecognitionActivity extends AppCompatActivity {

    private ImageView signImage;
    private TextView questionText;
    private ImageButton[] answerButtons = new ImageButton[4];
    private View prevButton, nextButton;

    private List<ASLSign> signs;
    private int currentSignIndex = 0;
    private List<Integer> answeredCorrectly = new ArrayList<>();
    private List<Integer> visitedQuestions = new ArrayList<>();
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asl_sign_recognition);

        initializeViews();
        initializeSigns();
        Collections.shuffle(signs);
        setupButtons();
        loadQuestion();
        setupCloseButton();
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

    private void setupCloseButton() {
        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> finish());
    }

    private void initializeSigns() {
        signs = new ArrayList<>();
        signs.add(new ASLSign("animal_cow", R.drawable.animal_cow, R.drawable.animal_sign_cow));
        signs.add(new ASLSign("emotion_happy", R.drawable.emotion_happy, R.drawable.emotion_sign_happy));
        signs.add(new ASLSign("greeting_hello", R.drawable.greeting_hello, R.drawable.greetings_sign_hello));
        signs.add(new ASLSign("letter_a", R.drawable.letter_a, R.drawable.letter_sign_a));

        signs.add(new ASLSign("number_0", R.drawable.number_0, R.drawable.number_sign_0));
        signs.add(new ASLSign("number_1", R.drawable.number_1, R.drawable.number_sign_1));
        signs.add(new ASLSign("number_2", R.drawable.number_2, R.drawable.number_sign_2));
        signs.add(new ASLSign("number_3", R.drawable.number_3, R.drawable.number_sign_3));
        signs.add(new ASLSign("number_4", R.drawable.number_4, R.drawable.number_sign_4));
        signs.add(new ASLSign("number_5", R.drawable.number_5, R.drawable.number_sign_5));
        signs.add(new ASLSign("number_6", R.drawable.number_6, R.drawable.number_sign_6));
        signs.add(new ASLSign("number_7", R.drawable.number_7, R.drawable.number_sign_7));
        signs.add(new ASLSign("number_8", R.drawable.number_8, R.drawable.number_sign_8));
        signs.add(new ASLSign("number_9", R.drawable.number_9, R.drawable.number_sign_9));
        signs.add(new ASLSign("number_10", R.drawable.number_10, R.drawable.number_sign_10));
    }

    private void setupButtons() {
        for (int i = 0; i < answerButtons.length; i++) {
            final int index = i;
            answerButtons[i].setOnClickListener(v -> checkAnswer(index));
        }

        prevButton.setOnClickListener(v -> moveToPrevious());
        nextButton.setOnClickListener(v -> moveToNext());
    }

    private void loadQuestion() {
        if (!visitedQuestions.contains(currentSignIndex)) {
            visitedQuestions.add(currentSignIndex);
        }

        ASLSign currentSign = signs.get(currentSignIndex);
        signImage.setImageResource(currentSign.getQuestionImageResourceId());
        questionText.setText("What is the sign for this image?");

        List<Integer> answerIndices = new ArrayList<>();
        answerIndices.add(currentSign.getAnswerImageResourceId());

        List<ASLSign> otherSigns = new ArrayList<>(signs);
        otherSigns.remove(currentSignIndex);
        Collections.shuffle(otherSigns);

        for (int i = 0; answerIndices.size() < 4 && i < otherSigns.size(); i++) {
            int wrongAnswerResourceId = otherSigns.get(i).getAnswerImageResourceId();
            if (!answerIndices.contains(wrongAnswerResourceId)) {
                answerIndices.add(wrongAnswerResourceId);
            }
        }

        Collections.shuffle(answerIndices);

        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setImageResource(answerIndices.get(i));
            answerButtons[i].setTag(answerIndices.get(i));
        }

        updateNavigationButtons();
    }

    private void checkAnswer(int selectedIndex) {
        ASLSign currentSign = signs.get(currentSignIndex);
        int selectedAnswerResourceId = (Integer) answerButtons[selectedIndex].getTag();

        if (selectedAnswerResourceId == currentSign.getAnswerImageResourceId()) {
            // Correct answer
            if (!answeredCorrectly.contains(currentSignIndex)) {
                answeredCorrectly.add(currentSignIndex);
            }
            moveToNext();
        } else {
            // Incorrect answer
            // You can add feedback for incorrect answers here
        }
    }

    private void moveToNext() {
        if (currentSignIndex < signs.size() - 1) {
            currentSignIndex++;
            loadQuestion();
        } else {
            // End of questions
            // You can add end-of-game logic here
        }
    }

    private void moveToPrevious() {
        if (currentSignIndex > 0) {
            do {
                currentSignIndex--;
            } while (currentSignIndex > 0 && answeredCorrectly.contains(currentSignIndex));

            if (!answeredCorrectly.contains(currentSignIndex)) {
                loadQuestion();
            } else {
                // If we've reached the beginning and all previous questions are answered
                // we can either disable the previous button or show a message
                updateNavigationButtons();
            }
        }
    }

    private void updateNavigationButtons() {
        prevButton.setEnabled(canMoveToPrevious());
        nextButton.setEnabled(currentSignIndex < signs.size() - 1);
    }

    private boolean canMoveToPrevious() {
        for (int i = currentSignIndex - 1; i >= 0; i--) {
            if (!answeredCorrectly.contains(i)) {
                return true;
            }
        }
        return false;
    }

    private class ASLSign {
        private String name;
        private int questionImageResourceId;
        private int answerImageResourceId;

        public ASLSign(String name, int questionImageResourceId, int answerImageResourceId) {
            this.name = name;
            this.questionImageResourceId = questionImageResourceId;
            this.answerImageResourceId = answerImageResourceId;
        }

        public String getName() {
            return name;
        }

        public int getQuestionImageResourceId() {
            return questionImageResourceId;
        }

        public int getAnswerImageResourceId() {
            return answerImageResourceId;
        }
    }
}