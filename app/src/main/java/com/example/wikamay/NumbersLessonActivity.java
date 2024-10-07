package com.example.wikamay;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class NumbersLessonActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private TextView lessonText, numberText, resultIndicator, congratsMessage;
    private ImageView numberImage, signImage;
    private int currentLessonIndex = 0;
    private List<NumberLesson> lessons;
    private GestureDetector gestureDetector;
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

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

        // Initialize GestureDetector
        gestureDetector = new GestureDetector(this, this);
    }

    private void initializeLessons() {
        lessons = new ArrayList<>();
        lessons.add(new NumberLesson("0", R.drawable.number_0, R.drawable.number_sign_0));
        lessons.add(new NumberLesson("1", R.drawable.number_1, R.drawable.number_sign_1));
        lessons.add(new NumberLesson("2", R.drawable.number_2, R.drawable.number_sign_2));
        lessons.add(new NumberLesson("3", R.drawable.number_3, R.drawable.number_sign_3));
        lessons.add(new NumberLesson("4", R.drawable.number_4, R.drawable.number_sign_4));
        lessons.add(new NumberLesson("5", R.drawable.number_5, R.drawable.number_sign_5));
        lessons.add(new NumberLesson("6", R.drawable.number_6, R.drawable.number_sign_6));
        lessons.add(new NumberLesson("7", R.drawable.number_7, R.drawable.number_sign_7));
        lessons.add(new NumberLesson("8", R.drawable.number_8, R.drawable.number_sign_8));
        lessons.add(new NumberLesson("9", R.drawable.number_9, R.drawable.number_sign_9));
        lessons.add(new NumberLesson("10", R.drawable.number_10, R.drawable.number_sign_10));
        // ... add more lessons if needed
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {}

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {}

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        // Swipe from left to right
                        onSwipePrevious();
                    } else {
                        // Swipe from right to left
                        onSwipeNext();
                    }
                    result = true;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    private void onSwipeNext() {
        if (currentLessonIndex < lessons.size() - 1) {
            currentLessonIndex++;
            loadLesson(currentLessonIndex);
        }
    }

    private void onSwipePrevious() {
        if (currentLessonIndex > 0) {
            currentLessonIndex--;
            loadLesson(currentLessonIndex);
        }
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