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
import android.util.Log;

public class GreetingsLessonActivity extends AppCompatActivity {
    private TextView greetingText, resultIndicator, congratsMessage;
    private ImageView greetingImage, signImage;
    private int currentLessonIndex = 0;
    private List<GreetingLesson> lessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("GreetingsLessonActivity", "onCreate started");
        setContentView(R.layout.greetings_lesson_layout);

        // Initialize views
        greetingText = findViewById(R.id.greetingText);
        greetingImage = findViewById(R.id.greetingImage);
        signImage = findViewById(R.id.signImage);
        resultIndicator = findViewById(R.id.resultIndicator);
        congratsMessage = findViewById(R.id.congratsmessage);

        Log.d("GreetingsLessonActivity", "Views initialized");

        // Initialize lessons
        initializeLessons();
        Log.d("GreetingsLessonActivity", "Lessons initialized");

        // Load first lesson
        loadLesson(currentLessonIndex);
        Log.d("GreetingsLessonActivity", "First lesson loaded");
    }

    private void initializeLessons() {
        lessons = new ArrayList<>();
        lessons.add(new GreetingLesson("Hello", R.drawable.greeting_hello, R.drawable.sign_hello));
        lessons.add(new GreetingLesson("Goodbye", R.drawable.greeting_goodbye, R.drawable.sign_goodbye));
        // ... add more greeting lessons
    }

    private void loadLesson(int index) {
        GreetingLesson lesson = lessons.get(index);
        greetingText.setText(lesson.getGreeting());
        Bitmap greetingBitmap = BitmapFactory.decodeResource(getResources(), lesson.getGreetingImageResource());
        Bitmap signBitmap = BitmapFactory.decodeResource(getResources(), lesson.getSignImageResource());
        greetingImage.setImageBitmap(scaleBitmapToFill(greetingBitmap, greetingImage.getWidth(), greetingImage.getHeight()));
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

class GreetingLesson {
    private String greeting;
    private int greetingImageResource;
    private int signImageResource;

    // Constructor
    public GreetingLesson(String greeting, int greetingImageResource, int signImageResource) {
        this.greeting = greeting;
        this.greetingImageResource = greetingImageResource;
        this.signImageResource = signImageResource;
    }

    // Getters
    public String getGreeting() {
        return greeting;
    }

    public int getGreetingImageResource() {
        return greetingImageResource;
    }

    public int getSignImageResource() {
        return signImageResource;
    }
}