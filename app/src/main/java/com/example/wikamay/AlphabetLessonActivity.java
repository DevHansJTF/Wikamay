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
import android.util.TypedValue;
import android.view.ViewGroup.MarginLayoutParams;

public class AlphabetLessonActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private TextView lettertext, text2, wordtext, resultIndicator, congratsMessage;
    private ImageView letterImage, signImage;
    private int currentLessonIndex = 0;
    private List<AlphabetLesson> lessons;
    private GestureDetector gestureDetector;
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

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

        // Initialize GestureDetector
        gestureDetector = new GestureDetector(this, this);
    }

    private void initializeLessons() {
        lessons = new ArrayList<>();
        lessons.add(new AlphabetLesson("Aa", "Apple", R.drawable.letter_a, R.drawable.number_sign_0));
        lessons.add(new AlphabetLesson("Bb", "Balloon", R.drawable.letter_b, R.drawable.number_sign_1));
        lessons.add(new AlphabetLesson("Cc", "Cake", R.drawable.letter_c, R.drawable.number_sign_2));
        lessons.add(new AlphabetLesson("Dd", "Duck", R.drawable.letter_d, R.drawable.number_sign_3));
        lessons.add(new AlphabetLesson("Ee", "Egg", R.drawable.letter_e, R.drawable.number_sign_4));
        lessons.add(new AlphabetLesson("Ff", "Flower", R.drawable.letter_f, R.drawable.number_sign_5));
        lessons.add(new AlphabetLesson("Gg", "Grapes", R.drawable.letter_g, R.drawable.number_sign_6));
        lessons.add(new AlphabetLesson("Hh", "Heart", R.drawable.letter_h, R.drawable.number_sign_7));
        lessons.add(new AlphabetLesson("Ii", "Igloo", R.drawable.letter_i, R.drawable.number_sign_8));
        lessons.add(new AlphabetLesson("Jj", "Jelly", R.drawable.letter_j, R.drawable.number_sign_9));
        lessons.add(new AlphabetLesson("Kk", "Kite", R.drawable.letter_k, R.drawable.number_sign_10));
        lessons.add(new AlphabetLesson("Ll", "Lollipop", R.drawable.letter_l, R.drawable.number_sign_1));
        lessons.add(new AlphabetLesson("Mm", "Mango", R.drawable.letter_m, R.drawable.number_sign_2));
        lessons.add(new AlphabetLesson("Nn", "Nose", R.drawable.letter_n, R.drawable.number_sign_4));
        lessons.add(new AlphabetLesson("Oo", "Orange", R.drawable.letter_o, R.drawable.number_sign_9));
        lessons.add(new AlphabetLesson("Pp", "Pumpkin", R.drawable.letter_p, R.drawable.number_sign_6));
        lessons.add(new AlphabetLesson("Qq", "Queen", R.drawable.letter_q, R.drawable.number_sign_1));
        lessons.add(new AlphabetLesson("Rr", "Rocket", R.drawable.letter_r, R.drawable.number_sign_3));
        lessons.add(new AlphabetLesson("Ss", "Sun", R.drawable.letter_s, R.drawable.number_sign_10));
        lessons.add(new AlphabetLesson("Tt", "Tree", R.drawable.letter_t, R.drawable.number_sign_5));
        lessons.add(new AlphabetLesson("Uu", "Umbrella", R.drawable.letter_u, R.drawable.number_sign_1));
        lessons.add(new AlphabetLesson("Vv", "Vase", R.drawable.letter_v, R.drawable.number_sign_0));
        lessons.add(new AlphabetLesson("Ww", "Window", R.drawable.letter_w, R.drawable.number_sign_5));
        lessons.add(new AlphabetLesson("Xx", "Xylophone", R.drawable.letter_x, R.drawable.number_sign_7));
        lessons.add(new AlphabetLesson("Yy", "Yoyo", R.drawable.letter_y, R.drawable.number_sign_6));
        lessons.add(new AlphabetLesson("Zz", "Zipper", R.drawable.letter_z, R.drawable.number_sign_0));
    }

    private void loadLesson(int index) {
        AlphabetLesson lesson = lessons.get(index);
        lettertext.setText(lesson.getLetter());
        wordtext.setText(lesson.getWord());

        String word = lesson.getWord();
        MarginLayoutParams params = (MarginLayoutParams) wordtext.getLayoutParams();

        if (word.equals("Xylophone") || word.equals("Umbrella") || word.equals("Pumpkin")) {
            wordtext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
            params.topMargin = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 62, getResources().getDisplayMetrics());
        } else {
            wordtext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 48);
            params.topMargin = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 52, getResources().getDisplayMetrics());
        }
        wordtext.setLayoutParams(params);


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