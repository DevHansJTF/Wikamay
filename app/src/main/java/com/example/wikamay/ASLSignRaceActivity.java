package com.example.wikamay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ASLSignRaceActivity extends AppCompatActivity {

    private ImageView objectImage;
    private TextView timerText, timeLeftText;
    private PreviewView previewView;
    private View prevButton, nextButton;
    private CountDownTimer gameTimer, roundTimer;

    private List<ASLSign> signs;
    private int currentSignIndex = 0;

    private static final int REQUEST_CODE_PERMISSION = 101;
    private static final String[] REQUIRED_PERMISSION = new String[]{"android.permission.CAMERA"};

    private TorchModelHandler torchModelHandler;
    private CameraManager cameraManager;
    private TextView detectedWordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asl_sign_race);

        initializeViews();
        initializeSigns();
        Collections.shuffle(signs);
        setupButtons();
        loadSign(currentSignIndex);
        setupTimer();

        torchModelHandler = new TorchModelHandler(this);

        if (checkPermissions()) {
            initializeCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, REQUEST_CODE_PERMISSION);
        }

        setupCloseButton();
    }

    private void initializeViews() {
        objectImage = findViewById(R.id.objectImage);
        timerText = findViewById(R.id.timerText);
        timeLeftText = findViewById(R.id.timeLeftText);
        previewView = findViewById(R.id.cameraPreview);
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

        // Alphabet signs
        signs.add(new ASLSign("A", R.drawable.letter_a, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_b, "Alphabet"));
        signs.add(new ASLSign("C", R.drawable.letter_c, "Alphabet"));
        signs.add(new ASLSign("D", R.drawable.letter_d, "Alphabet"));
        signs.add(new ASLSign("E", R.drawable.letter_e, "Alphabet"));
        signs.add(new ASLSign("F", R.drawable.letter_f, "Alphabet"));
        signs.add(new ASLSign("G", R.drawable.letter_g, "Alphabet"));
        signs.add(new ASLSign("H", R.drawable.letter_h, "Alphabet"));
        signs.add(new ASLSign("I", R.drawable.letter_i, "Alphabet"));
        signs.add(new ASLSign("J", R.drawable.letter_j, "Alphabet"));
        signs.add(new ASLSign("K", R.drawable.letter_k, "Alphabet"));
        signs.add(new ASLSign("L", R.drawable.letter_l, "Alphabet"));
        signs.add(new ASLSign("M", R.drawable.letter_m, "Alphabet"));
        signs.add(new ASLSign("N", R.drawable.letter_n, "Alphabet"));
        signs.add(new ASLSign("O", R.drawable.letter_o, "Alphabet"));
        signs.add(new ASLSign("P", R.drawable.letter_p, "Alphabet"));
        signs.add(new ASLSign("Q", R.drawable.letter_q, "Alphabet"));
        signs.add(new ASLSign("R", R.drawable.letter_r, "Alphabet"));
        signs.add(new ASLSign("S", R.drawable.letter_s, "Alphabet"));
        signs.add(new ASLSign("T", R.drawable.letter_t, "Alphabet"));
        signs.add(new ASLSign("U", R.drawable.letter_u, "Alphabet"));
        signs.add(new ASLSign("V", R.drawable.letter_v, "Alphabet"));
        signs.add(new ASLSign("W", R.drawable.letter_w, "Alphabet"));
        signs.add(new ASLSign("X", R.drawable.letter_x, "Alphabet"));
        signs.add(new ASLSign("Y", R.drawable.letter_y, "Alphabet"));
        signs.add(new ASLSign("Z", R.drawable.letter_z, "Alphabet"));

        // Number signs
        signs.add(new ASLSign("0", R.drawable.number_0, "Numbers"));
        signs.add(new ASLSign("1", R.drawable.number_1, "Numbers"));
        signs.add(new ASLSign("2", R.drawable.number_2, "Numbers"));
        signs.add(new ASLSign("3", R.drawable.number_3, "Numbers"));
        signs.add(new ASLSign("4", R.drawable.number_4, "Numbers"));
        signs.add(new ASLSign("5", R.drawable.number_5, "Numbers"));
        signs.add(new ASLSign("6", R.drawable.number_6, "Numbers"));
        signs.add(new ASLSign("7", R.drawable.number_7, "Numbers"));
        signs.add(new ASLSign("8", R.drawable.number_8, "Numbers"));
        signs.add(new ASLSign("9", R.drawable.number_9, "Numbers"));
        signs.add(new ASLSign("10", R.drawable.number_10, "Numbers"));

        // Greetings
        signs.add(new ASLSign("Hello", R.drawable.greeting_hello, "Greetings"));
        // Add more greeting signs here

        // Emotions
        signs.add(new ASLSign("Happy", R.drawable.emotion_happy, "Emotions"));
        // Add more emotion signs here

        // Animals
        signs.add(new ASLSign("Dog", R.drawable.animal_cow, "Animals"));
        // Add more animal signs here
    }

    private void setupButtons() {
        prevButton.setOnClickListener(v -> {
            if (currentSignIndex > 0) {
                currentSignIndex--;
                loadSign(currentSignIndex);
            }
        });

        nextButton.setOnClickListener(v -> {
            if (currentSignIndex < signs.size() - 1) {
                currentSignIndex++;
                loadSign(currentSignIndex);
            }
        });
    }

    private void loadSign(int index) {
        ASLSign sign = signs.get(index);
        objectImage.setImageResource(sign.getImageResourceId());
        startRoundTimer();
    }

    private void setupTimer() {
        gameTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftText.setText(millisUntilFinished / 1000 + " seconds left");
            }

            public void onFinish() {
                timeLeftText.setText("Time's up!");
                // Handle game over
            }
        }.start();

        startRoundTimer();
    }

    private void startRoundTimer() {
        if (roundTimer != null) {
            roundTimer.cancel();
        }
        roundTimer = new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText(millisUntilFinished / 1000 + " seconds");
            }

            public void onFinish() {
                timerText.setText("Next!");
                // Move to next sign
                if (currentSignIndex < signs.size() - 1) {
                    currentSignIndex++;
                    loadSign(currentSignIndex);
                } else {
                    // Game over
                    gameTimer.cancel();
                    timeLeftText.setText("Game Over!");
                }
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
        cameraManager = new CameraManager(this, previewView, new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(ImageProxy image) {
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);

                String result = torchModelHandler.analyzeImage(bytes, image.getWidth(), image.getHeight());

                runOnUiThread(() -> {
                    detectedWordText.setText("Detected: " + result);
                    if (result.equalsIgnoreCase(signs.get(currentSignIndex).getName())) {
                        // Correct sign detected
                        roundTimer.cancel();
                        startRoundTimer();
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
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        if (roundTimer != null) {
            roundTimer.cancel();
        }
        if (cameraManager != null) {
            cameraManager = null;
        }
    }
}