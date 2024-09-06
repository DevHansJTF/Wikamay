package com.example.wikamay;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;


public class AlphabetLessonActivity extends AppCompatActivity {
    private TextView lettertext, text2, wordtext, resultIndicator, congratsMessage;
    private ImageView letterImage, signImage;
    private int currentLessonIndex = 0;
    private List<AlphabetLesson> lessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alphabet_lesson_layout);

        // Initialize views
        lettertext = findViewById(R.id.lettertext);
        text2 = findViewById(R.id.text2);
        wordtext = findViewById(R.id.wordtext);
        letterImage = findViewById(R.id.letterImage);
        signImage = findViewById(R.id.signImage);
        resultIndicator = findViewById(R.id.resultIndicator);
        congratsMessage = findViewById(R.id.congratsmessage);

        // Initialize lessons
        initializeLessons();

        // Load first lesson
        loadLesson(currentLessonIndex);
    }

    private void initializeLessons() {
        lessons = new ArrayList<>();
        lessons.add(new AlphabetLesson("Aa", "Apple", R.drawable.letter_a, R.drawable.sign_a));
        lessons.add(new AlphabetLesson("Bb", "Banana", R.drawable.letter_b, R.drawable.sign_b));
        // ... add more lessons
    }

    private void loadLesson(int index) {
        AlphabetLesson lesson = lessons.get(index);
        lettertext.setText(lesson.getLetter());
        wordtext.setText(lesson.getWord());
        Bitmap letterBitmap = BitmapFactory.decodeResource(getResources(), lesson.getLetterImageResource());
        Bitmap signBitmap = BitmapFactory.decodeResource(getResources(), lesson.getSignImageResource());
        letterImage.setImageBitmap(scaleBitmapToFill(letterBitmap, letterImage.getWidth(), letterImage.getHeight()));
        signImage.setImageBitmap(scaleBitmapToFill(signBitmap, signImage.getWidth(), signImage.getHeight()));


        // Reset result indicators
        resultIndicator.setVisibility(View.INVISIBLE);
        congratsMessage.setVisibility(View.INVISIBLE);
    }

    private Bitmap scaleBitmapToFill(Bitmap bitmap, int targetWidth, int targetHeight) {
        if (targetWidth == 0 || targetHeight == 0 || bitmap == null) {
            return bitmap;
        }

        Matrix matrix = new Matrix();
        float scaleX = (float) targetWidth / bitmap.getWidth();
        float scaleY = (float) targetHeight / bitmap.getHeight();
        float scale = Math.max(scaleX, scaleY);
        matrix.postScale(scale, scale);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public void onLessonComplete() {
        // Show success result
        resultIndicator.setText("Great Job!");
        congratsMessage.setText("The sign you did was correct.");
        resultIndicator.setVisibility(View.VISIBLE);
        congratsMessage.setVisibility(View.VISIBLE);

        // Move to next lesson
        currentLessonIndex++;
        if (currentLessonIndex < lessons.size()) {
            // Load next lesson after a delay
            new Handler().postDelayed(() -> loadLesson(currentLessonIndex), 2000);
        } else {
            // All lessons complete
            // Show completion message or return to menu
        }
    }

    public void onLessonFail() {
        // Show fail result
        resultIndicator.setText("Let's do it again!");
        congratsMessage.setText("Practice makes perfect.");
        resultIndicator.setVisibility(View.VISIBLE);
        congratsMessage.setVisibility(View.VISIBLE);

        // Optionally, you can add a delay before allowing the user to try again
        new Handler().postDelayed(() -> {
            resultIndicator.setVisibility(View.INVISIBLE);
            congratsMessage.setVisibility(View.INVISIBLE);
            // You might want to reset any UI elements or flags here
        }, 2000);
    }
}

class AlphabetLesson {
    private String letter;
    private String word;
    private int letterImageResource;
    private int signImageResource;

    // Constructor
    public AlphabetLesson(String letter, String word, int letterImageResource, int signImageResource) {
        this.letter = letter;
        this.word = word;
        this.letterImageResource = letterImageResource;
        this.signImageResource = signImageResource;
    }

    // Getters
    public String getLetter() {
        return letter;
    }

    public String getWord() {
        return word;
    }

    public int getLetterImageResource() {
        return letterImageResource;
    }

    public int getSignImageResource() {
        return signImageResource;
    }
}