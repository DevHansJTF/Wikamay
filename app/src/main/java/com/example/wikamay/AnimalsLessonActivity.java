package com.example.wikamay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.Gravity;
import android.widget.FrameLayout;

public class AnimalsLessonActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private static final String TAG = "AnimalsLessonActivity";
    private TextView animalNameText, text2, resultIndicator, congratsMessage;
    private ImageView animalImage, signImage;
    private VideoView videoLessonView;
    private ProgressBar videoLoadingProgress;
    private MediaController mediaController;
    private int currentLessonIndex = 0;
    private List<AnimalLesson> lessons;
    private GestureDetector gestureDetector;
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    private static final int REQUEST_CODE_PERMISSION = 101;
    private static final String[] REQUIRED_PERMISSION = new String[]{"android.permission.CAMERA"};

    private PreviewView previewView;
    private TorchModelHandler torchModelHandler;
    private CameraManager cameraManager;
    private TextView detectedWordText;

    private int currentStage = 0;
    private Button nextButton;

    private boolean isFinishing = false;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            initializeLessons();
            gestureDetector = new GestureDetector(this, this);
            showCurrentStage();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate", e);
            Toast.makeText(this, "Error initializing lesson", Toast.LENGTH_SHORT).show();
            finishSafely();
        }
    }

    private void showCurrentStage() {
        Log.d(TAG, "showCurrentStage: " + currentStage);
        try {
            switch (currentStage) {
                case 0:
                    setContentView(R.layout.animals_lesson_1);
                    setupLesson1View();
                    break;
                case 1:
                    setContentView(R.layout.animals_hand_sign_tutorial);
                    setupHandSignTutorialView();
                    break;
                case 2:
                    setContentView(R.layout.animals_lesson_layout);
                    setupResultView();
                    break;
                default:
                    Log.e(TAG, "Invalid stage: " + currentStage);
                    Toast.makeText(this, "Invalid lesson stage", Toast.LENGTH_SHORT).show();
                    finishSafely();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in showCurrentStage", e);
            Toast.makeText(this, "Error loading lesson stage", Toast.LENGTH_SHORT).show();
            finishSafely();
        }
    }

    private void setupLesson1View() {
        Log.d(TAG, "setupLesson1View");
        try {
            animalImage = findViewById(R.id.animalImage);
            videoLessonView = findViewById(R.id.videoLessonView);
            videoLoadingProgress = findViewById(R.id.videoLoadingProgress);
            nextButton = findViewById(R.id.nextButton);

            if (animalImage == null || videoLessonView == null || nextButton == null) {
                throw new NullPointerException("One or more views not found in layout");
            }

            AnimalLesson currentLesson = lessons.get(currentLessonIndex);
            animalImage.setImageResource(currentLesson.getAnimalImageResource());

            setupVideo(currentLesson.getAnimalName().toLowerCase());

            nextButton.setOnClickListener(v -> {
                if (videoLessonView != null && videoLessonView.isPlaying()) {
                    videoLessonView.stopPlayback();
                }
                currentStage = 1;
                showCurrentStage();
            });

            setupCloseButton();
        } catch (Exception e) {
            Log.e(TAG, "Error in setupLesson1View", e);
            Toast.makeText(this, "Error setting up lesson view", Toast.LENGTH_SHORT).show();
            finishSafely();
        }
    }

    private void setupVideo(String animal) {
        try {
            videoLoadingProgress.setVisibility(View.VISIBLE);

            mediaController = new MediaController(this);
            mediaController.setAnchorView(videoLessonView);
            videoLessonView.setMediaController(mediaController);

            String videoPath = "android.resource://" + getPackageName() + "/raw/animal_" +
                    animal.toLowerCase() + "_video";
            Uri videoUri = Uri.parse(videoPath);

            videoLessonView.setVideoURI(videoUri);

            videoLessonView.setOnPreparedListener(mp -> {
                videoLoadingProgress.setVisibility(View.GONE);
                mp.setLooping(true);

                // Get the VideoView's container dimensions
                int containerWidth = ((View)videoLessonView.getParent()).getWidth();
                int containerHeight = ((View)videoLessonView.getParent()).getHeight();

                // Get video dimensions
                int videoWidth = mp.getVideoWidth();
                int videoHeight = mp.getVideoHeight();

                float scaleX = (float) containerWidth / videoWidth;
                float scaleY = (float) containerHeight / videoHeight;

                // Use the larger scale factor to ensure the video fills the container
                float scale = Math.max(scaleX, scaleY);

                int scaledWidth = (int) (videoWidth * scale);
                int scaledHeight = (int) (videoHeight * scale);

                // Center the video
                int leftOffset = (containerWidth - scaledWidth) / 2;
                int topOffset = (containerHeight - scaledHeight) / 2;

                // Create layout parameters that will center and scale the video
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        scaledWidth,
                        scaledHeight
                );
                layoutParams.leftMargin = leftOffset;
                layoutParams.topMargin = topOffset;
                layoutParams.gravity = Gravity.CENTER;

                videoLessonView.setLayoutParams(layoutParams);

                videoLessonView.start();
            });

            videoLessonView.setOnErrorListener((mp, what, extra) -> {
                videoLoadingProgress.setVisibility(View.GONE);
                Toast.makeText(this, "Error loading video", Toast.LENGTH_SHORT).show();
                return true;
            });
        } catch (Exception e) {
            Log.e(TAG, "Error setting up video", e);
            videoLoadingProgress.setVisibility(View.GONE);
            Toast.makeText(this, "Error setting up video", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupHandSignTutorialView() {
        try {
            animalNameText = findViewById(R.id.animalNameText);
            text2 = findViewById(R.id.text2);
            nextButton = findViewById(R.id.nextButton);
            previewView = findViewById(R.id.cameraPreview);
            detectedWordText = findViewById(R.id.detectedWordText);

            AnimalLesson currentLesson = lessons.get(currentLessonIndex);
            animalNameText.setText(currentLesson.getAnimalName());

            nextButton.setOnClickListener(v -> {
                currentStage = 2;
                showCurrentStage();
            });

            setupCloseButton();

            torchModelHandler = new TorchModelHandler(this);

            if (checkPermissions()) {
                initializeCamera();
            } else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, REQUEST_CODE_PERMISSION);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in setupHandSignTutorialView", e);
            Toast.makeText(this, "Error setting up hand sign tutorial view", Toast.LENGTH_SHORT).show();
            finishSafely();
        }
    }

    private void setupResultView() {
        initializeViews();
        loadLesson(currentLessonIndex);
        torchModelHandler = new TorchModelHandler(this);

        if (checkPermissions()) {
            initializeCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, REQUEST_CODE_PERMISSION);
        }

        setupCloseButton();
    }

    private void initializeViews() {
        animalNameText = findViewById(R.id.animalNameText);
        text2 = findViewById(R.id.text2);
        animalImage = findViewById(R.id.animalImage);
        signImage = findViewById(R.id.signImage);
        resultIndicator = findViewById(R.id.resultIndicator);
        congratsMessage = findViewById(R.id.congratsmessage);
        previewView = findViewById(R.id.cameraPreview);
        detectedWordText = findViewById(R.id.detectedWordText);
    }

    private void setupCloseButton() {
        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> finishSafely());
    }

    private boolean checkPermissions() {
        for (String permission : REQUIRED_PERMISSION) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void initializeCamera() {
        cameraManager = new CameraManager(this, previewView, new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(ImageProxy image) {
                if (isFinishing) {
                    image.close();
                    return;
                }
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);

                String result = torchModelHandler.analyzeImage(bytes, image.getWidth(), image.getHeight());

                mainHandler.post(() -> {
                    if (detectedWordText != null) {
                        detectedWordText.setText("Detected: " + result);
                        if (result.equalsIgnoreCase(lessons.get(currentLessonIndex).getAnimalName())) {
                            onLessonComplete();
                        }
                    }
                });

                image.close();
            }
        });
        cameraManager.startCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeCamera();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
                finishSafely();
            }
        }
    }

    private void initializeLessons() {
        Log.d(TAG, "initializeLessons");
        lessons = new ArrayList<>();
        lessons.add(new AnimalLesson("Cow", R.drawable.animal_cow, R.drawable.animal_sign_cow));
        lessons.add(new AnimalLesson("Cat", R.drawable.animal_cat, R.drawable.animal_sign_cat));
        lessons.add(new AnimalLesson("Bear", R.drawable.animal_bear, R.drawable.animal_sign_bear));
        lessons.add(new AnimalLesson("Rabbit", R.drawable.animal_rabbit, R.drawable.animal_sign_rabbit));
        lessons.add(new AnimalLesson("Bird", R.drawable.animal_bird, R.drawable.animal_sign_bird));
    }

    private void loadLesson(int index) {
        Log.d(TAG, "loadLesson: " + index);
        try {
            AnimalLesson lesson = lessons.get(index);
            animalNameText.setText(lesson.getAnimalName());

            Bitmap animalBitmap = BitmapFactory.decodeResource(getResources(), lesson.getAnimalImageResource());
            Bitmap signBitmap = BitmapFactory.decodeResource(getResources(), lesson.getSignImageResource());

            if (animalBitmap == null || signBitmap == null) {
                throw new NullPointerException("Failed to load image resources");
            }

            animalImage.setImageBitmap(scaleBitmapToFill(animalBitmap, animalImage.getWidth(), animalImage.getHeight()));
            signImage.setImageBitmap(scaleBitmapToFill(signBitmap, signImage.getWidth(), signImage.getHeight()));

            resultIndicator.setVisibility(View.INVISIBLE);
            congratsMessage.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            Log.e(TAG, "Error in loadLesson", e);
            Toast.makeText(this, "Error loading lesson", Toast.LENGTH_SHORT).show();
            finishSafely();
        }
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
        if (isFinishing) return;
        mainHandler.post(() -> {
            if (resultIndicator != null && congratsMessage != null) {
                resultIndicator.setText("Great Job!");
                congratsMessage.setText("The sign you did was correct.");
                resultIndicator.setVisibility(View.VISIBLE);
                congratsMessage.setVisibility(View.VISIBLE);
            }
        });

        mainHandler.postDelayed(this::moveToNextLesson, 2000);
    }

    private void moveToNextLesson() {
        if (isFinishing) {
            return;
        }

        currentLessonIndex++;
        if (currentLessonIndex < lessons.size()) {
            currentStage = 0;
            mainHandler.post(this::showCurrentStage);
        } else {
            isFinishing = true;
            mainHandler.post(() -> {
                Toast.makeText(this, "Congratulations! You've completed all lessons.", Toast.LENGTH_LONG).show();
                mainHandler.postDelayed(() -> {
                    if (!isFinishing()) {
                        try {
                            Intent intent = new Intent(AnimalsLessonActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } catch (Exception e) {
                            Log.e(TAG, "Error returning to MainActivity", e);
                        } finally {
                            finishSafely();
                        }
                    }
                }, 2000);
            });
        }
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
                        onSwipePrevious();
                    } else {
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
        if (currentStage == 2 && !isFinishing) {
            moveToNextLesson();
        }
    }

    private void onSwipePrevious() {
        // Optionally implement swipe to previous lesson if needed
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoLessonView != null && videoLessonView.isPlaying()) {
            videoLessonView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        if (videoLessonView != null) {
            videoLessonView.stopPlayback();
        }
        super.onDestroy();
        finishSafely();
        Log.d(TAG, "onDestroy");
    }

    private void finishSafely() {
        if (isFinishing) return;
        isFinishing = true;
        if (videoLessonView != null) {
            videoLessonView.stopPlayback();
        }
        if (cameraManager != null) {
            cameraManager.shutdownCamera();
        }
        mainHandler.removeCallbacksAndMessages(null);
        finish();
    }

    // Inner class for AnimalLesson
    class AnimalLesson {
        private String animalName;
        private int animalImageResource;
        private int signImageResource;

        public AnimalLesson(String animalName, int animalImageResource, int signImageResource) {
            this.animalName = animalName;
            this.animalImageResource = animalImageResource;
            this.signImageResource = signImageResource;
        }

        public String getAnimalName() {
            return animalName;
        }

        public int getAnimalImageResource() {
            return animalImageResource;
        }

        public int getSignImageResource() {
            return signImageResource;
        }
    }
}