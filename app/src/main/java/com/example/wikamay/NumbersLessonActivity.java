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

public class NumbersLessonActivity extends AppCompatActivity {
    private TextView lessonText, numberText, resultIndicator, congratsMessage;
    private ImageView numberImage, signImage;
    private int currentLessonIndex = 0;
    private List<NumberLesson> lessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numbers_lesson_layout);

        // Initialize views
        lessonText = findViewById(R.id.lessonText);
        numberText = findViewById(R.id.numberText);
        numberImage = findViewById(R.id.numberImage);
        signImage = findViewById(R.id.signImage);
        resultIndicator = findViewById(R.id.resultIndicator);
        congratsMessage = findViewById(R.id.congratsMessage);

        // Set the lesson text
        lessonText.setText("Let's learn the\nsign of number");

        // Initialize lessons
        initializeLessons();

        // Load first lesson
        loadLesson(currentLessonIndex);
    }

    private void initializeLessons() {
        lessons = new ArrayList<>();
        lessons.add(new NumberLesson("1", R.drawable.number_1, R.drawable.sign_1));
        lessons.add(new NumberLesson("2", R.drawable.number_2, R.drawable.sign_2));
        // ... add more lessons
    }

    private void loadLesson(int index) {
        NumberLesson lesson = lessons.get(index);
        numberText.setText(lesson.getNumber());
        Bitmap numberBitmap = BitmapFactory.decodeResource(getResources(), lesson.getNumberImageResource());
        Bitmap signBitmap = BitmapFactory.decodeResource(getResources(), lesson.getSignImageResource());
        numberImage.setImageBitmap(scaleBitmapToFill(numberBitmap, numberImage.getWidth(), numberImage.getHeight()));
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
        resultIndicator.setText("Let's try again!");
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

class NumberLesson {
    private String number;
    private int numberImageResource;
    private int signImageResource;

    // Constructor
    public NumberLesson(String number, int numberImageResource, int signImageResource) {
        this.number = number;
        this.numberImageResource = numberImageResource;
        this.signImageResource = signImageResource;
    }

    // Getters
    public String getNumber() {
        return number;
    }

    public int getNumberImageResource() {
        return numberImageResource;
    }

    public int getSignImageResource() {
        return signImageResource;
    }
}