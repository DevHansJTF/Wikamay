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

public class EmotionsLessonActivity extends AppCompatActivity {
    private TextView emotionText, descriptionText, resultIndicator, congratsMessage;
    private ImageView emotionImage, signImage;
    private int currentLessonIndex = 0;
    private List<EmotionLesson> lessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("EmotionsLessonActivity", "onCreate started");
        setContentView(R.layout.emotions_lesson_layout);

        // Initialize views
        emotionText = findViewById(R.id.emotionText);
        emotionImage = findViewById(R.id.emotionImage);
        signImage = findViewById(R.id.signImage);
        resultIndicator = findViewById(R.id.resultIndicator);
        congratsMessage = findViewById(R.id.congratsmessage);

        Log.d("EmotionsLessonActivity", "Views initialized");

        // Initialize lessons
        initializeLessons();
        Log.d("EmotionsLessonActivity", "Lessons initialized");

        // Load first lesson
        loadLesson(currentLessonIndex);
        Log.d("EmotionsLessonActivity", "First lesson loaded");
    }

    private void initializeLessons() {
        lessons = new ArrayList<>();
        lessons.add(new EmotionLesson("Happy", "Feeling joy or pleasure", R.drawable.emotion_happy, R.drawable.sign_happy));
        lessons.add(new EmotionLesson("Sad", "Feeling sorrow or unhappiness", R.drawable.emotion_sad, R.drawable.sign_sad));
        // ... add more emotion lessons
    }

    private void loadLesson(int index) {
        EmotionLesson lesson = lessons.get(index);
        emotionText.setText(lesson.getEmotion());
        Bitmap emotionBitmap = BitmapFactory.decodeResource(getResources(), lesson.getEmotionImageResource());
        Bitmap signBitmap = BitmapFactory.decodeResource(getResources(), lesson.getSignImageResource());
        emotionImage.setImageBitmap(scaleBitmapToFill(emotionBitmap, emotionImage.getWidth(), emotionImage.getHeight()));
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

class EmotionLesson {
    private String emotion;
    private String description;
    private int emotionImageResource;
    private int signImageResource;

    // Constructor
    public EmotionLesson(String emotion, String description, int emotionImageResource, int signImageResource) {
        this.emotion = emotion;
        this.description = description;
        this.emotionImageResource = emotionImageResource;
        this.signImageResource = signImageResource;
    }

    // Getters
    public String getEmotion() {
        return emotion;
    }

    public String getDescription() {
        return description;
    }

    public int getEmotionImageResource() {
        return emotionImageResource;
    }

    public int getSignImageResource() {
        return signImageResource;
    }
}