package com.example.wikamay;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ASLFingerSpellingActivity extends AppCompatActivity {

    private ImageView image1, image2;
    private TextView wordText, underscore1, underscore2, underscore3, timerText;
    private PreviewView cameraPreview;
    private View prevButton, nextButton;
    private CountDownTimer timer;
    private TextView detectedWordText;

    private static final int REQUEST_CODE_PERMISSION = 101;
    private static final String[] REQUIRED_PERMISSION = new String[]{"android.permission.CAMERA"};

    private TorchModelHandler torchModelHandler;
    private CameraManager cameraManager;

    private List<ASLSign> signs;
    private int currentSignIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asl_finger_spelling);

        initializeViews();
        initializeSigns();
        Collections.shuffle(signs);
        setupButtons();
        loadSign(currentSignIndex);
        setupCloseButton();
        setupTimer();

        torchModelHandler = new TorchModelHandler(this);

        if (checkPermissions()) {
            initializeCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, REQUEST_CODE_PERMISSION);
        }
    }

    private void initializeViews() {
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        wordText = findViewById(R.id.wordText);
        underscore1 = findViewById(R.id.underscore1);
        underscore2 = findViewById(R.id.underscore2);
        underscore3 = findViewById(R.id.underscore3);
        timerText = findViewById(R.id.timerText);
        cameraPreview = findViewById(R.id.cameraPreview);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        detectedWordText = findViewById(R.id.detectedWordText);
    }

    private void setupCloseButton() {
        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> finish());
    }

    private void initializeSigns() {
        signs = new ArrayList<>();
        signs.add(new ASLSign("ONE", R.drawable.number_1, R.drawable.number_sign_1));
        // Add more signs here...
    }

    private void setupButtons() {
        prevButton.setOnClickListener(v -> moveToPrevious());
        nextButton.setOnClickListener(v -> moveToNext());
    }

    private void setupTimer() {
        timer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText(millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                timerText.setText("Time's up!");
                // Handle end of time logic here
            }
        }.start();
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
        cameraManager = new CameraManager(this, cameraPreview, new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(ImageProxy image) {
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);

                String result = torchModelHandler.analyzeImage(bytes, image.getWidth(), image.getHeight());

                runOnUiThread(() -> {
                    detectedWordText.setText("Detected: " + result);
                    if (result.equalsIgnoreCase(signs.get(currentSignIndex).getWord())) {
                        onSignCorrect();
                    }
                });

                image.close();
            }
        });
        cameraManager.startCamera();
    }

    private void loadSign(int index) {
        ASLSign currentSign = signs.get(index);
        image1.setImageResource(currentSign.getQuestionImageResourceId());
        image2.setImageResource(currentSign.getAnswerImageResourceId());
        wordText.setText(currentSign.getWord());
        resetUnderscores();
    }

    private void resetUnderscores() {
        underscore1.setText("_");
        underscore2.setText("_");
        underscore3.setText("_");
    }

    private void onSignCorrect() {
        // Handle correct sign logic
        moveToNext();
    }

    private void moveToNext() {
        if (currentSignIndex < signs.size() - 1) {
            currentSignIndex++;
            loadSign(currentSignIndex);
        } else {
            // End of signs
            // Add end game logic here
        }
    }

    private void moveToPrevious() {
        if (currentSignIndex > 0) {
            currentSignIndex--;
            loadSign(currentSignIndex);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeCamera();
            } else {
                // Handle permission denied
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        if (cameraManager != null) {
            cameraManager = null;
        }
    }

    private static class ASLSign {
        private String word;
        private int questionImageResourceId;
        private int answerImageResourceId;

        public ASLSign(String word, int questionImageResourceId, int answerImageResourceId) {
            this.word = word;
            this.questionImageResourceId = questionImageResourceId;
            this.answerImageResourceId = answerImageResourceId;
        }

        public String getWord() {
            return word;
        }

        public int getQuestionImageResourceId() {
            return questionImageResourceId;
        }

        public int getAnswerImageResourceId() {
            return answerImageResourceId;
        }
    }
}