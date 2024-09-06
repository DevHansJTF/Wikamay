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

public class AnimalsLessonActivity extends AppCompatActivity {
    private TextView animalNameText, text2, wordText, resultIndicator, congratsMessage;
    private ImageView animalImage, signImage;
    private int currentLessonIndex = 0;
    private List<AnimalLesson> lessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animals_lesson_layout);

        // Initialize views
        animalNameText = findViewById(R.id.animalNameText);
        animalImage = findViewById(R.id.animalImage);
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
        lessons.add(new AnimalLesson("Dog", "Woof", R.drawable.animal_dog, R.drawable.sign_dog));
        lessons.add(new AnimalLesson("Cat", "Meow", R.drawable.animal_cat, R.drawable.sign_cat));
        // ... add more animal lessons
    }

    private void loadLesson(int index) {
        AnimalLesson lesson = lessons.get(index);
        animalNameText.setText(lesson.getAnimalName());
        Bitmap animalBitmap = BitmapFactory.decodeResource(getResources(), lesson.getAnimalImageResource());
        Bitmap signBitmap = BitmapFactory.decodeResource(getResources(), lesson.getSignImageResource());
        animalImage.setImageBitmap(scaleBitmapToFill(animalBitmap, animalImage.getWidth(), animalImage.getHeight()));
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

class AnimalLesson {
    private String animalName;
    private String sound;
    private int animalImageResource;
    private int signImageResource;

    // Constructor
    public AnimalLesson(String animalName, String sound, int animalImageResource, int signImageResource) {
        this.animalName = animalName;
        this.sound = sound;
        this.animalImageResource = animalImageResource;
        this.signImageResource = signImageResource;
    }

    // Getters
    public String getAnimalName() {
        return animalName;
    }

    public String getSound() {
        return sound;
    }

    public int getAnimalImageResource() {
        return animalImageResource;
    }

    public int getSignImageResource() {
        return signImageResource;
    }
}