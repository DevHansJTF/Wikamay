package com.example.wikamay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ASLSignGuessingGameActivity extends AppCompatActivity {

    private ImageView signImage;
    private TextView congratsMessage;
    private View prevButton, nextButton;
    private PreviewView previewView;
    private TextView detectedWordText;

    private List<ASLSign> signs;
    private int currentSignIndex = 0;

    private static final int REQUEST_CODE_PERMISSION = 101;
    private static final String[] REQUIRED_PERMISSION = new String[]{"android.permission.CAMERA"};

    private TorchModelHandler torchModelHandler;
    private CameraManager cameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asl_sign_guessing_game);

        initializeViews();
        initializeSigns();
        Collections.shuffle(signs);
        setupButtons();
        loadSign(currentSignIndex);

        torchModelHandler = new TorchModelHandler(this);

        if (checkPermissions()) {
            initializeCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, REQUEST_CODE_PERMISSION);
        }

        setupCloseButton();
    }

    private void initializeViews() {
        signImage = findViewById(R.id.signImage);
        congratsMessage = findViewById(R.id.congratsmessage);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        previewView = findViewById(R.id.cameraPreview);
        detectedWordText = findViewById(R.id.detectedWordText);

        congratsMessage.setVisibility(View.INVISIBLE);
    }

    private void setupCloseButton() {
        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // This will close the current activity and return to the previous one
            }
        });
    }


    private void initializeSigns() {
        signs = new ArrayList<>();

        // Alphabet signs
        signs.add(new ASLSign("A", R.drawable.letter_a, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_b, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_c, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_d, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_e, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_f, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_g, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_h, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_i, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_j, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_k, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_l, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_m, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_n, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_o, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_p, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_q, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_r, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_s, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_t, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_u, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_v, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_w, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_x, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_y, "Alphabet"));
        signs.add(new ASLSign("B", R.drawable.letter_z, "Alphabet"));
        // Add more alphabet signs...

        // Number signs
        signs.add(new ASLSign("1", R.drawable.number_0, "Numbers"));
        signs.add(new ASLSign("2", R.drawable.number_1, "Numbers"));
        signs.add(new ASLSign("2", R.drawable.number_2, "Numbers"));
        signs.add(new ASLSign("2", R.drawable.number_3, "Numbers"));
        signs.add(new ASLSign("2", R.drawable.number_4, "Numbers"));
        signs.add(new ASLSign("2", R.drawable.number_5, "Numbers"));
        signs.add(new ASLSign("2", R.drawable.number_6, "Numbers"));
        signs.add(new ASLSign("2", R.drawable.number_7, "Numbers"));
        signs.add(new ASLSign("2", R.drawable.number_8, "Numbers"));
        signs.add(new ASLSign("2", R.drawable.number_9, "Numbers"));
        signs.add(new ASLSign("2", R.drawable.number_10, "Numbers"));
        // Add more number signs...

        // Greetings
        signs.add(new ASLSign("Hello", R.drawable.greeting_hello, "Greetings"));
        // Add more greeting signs...

        // Emotions
        signs.add(new ASLSign("Happy", R.drawable.emotion_happy, "Emotions"));
        // Add more emotion signs...

        // Animals
        signs.add(new ASLSign("Dog", R.drawable.animal_cow, "Animals"));
        // Add more animal signs...
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
        signImage.setImageResource(sign.getImageResourceId());
        congratsMessage.setVisibility(View.INVISIBLE);
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
                        showCongratsMessage(true);
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

    private void showCongratsMessage(boolean correct) {
        if (correct) {
            congratsMessage.setText("The sign you did was correct!");
            congratsMessage.setVisibility(View.VISIBLE);
        } else {
            congratsMessage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraManager != null) {
            cameraManager = null;
        }
    }
}

class ASLSign {
    private String name;
    private int imageResourceId;
    private String category;

    public ASLSign(String name, int imageResourceId, String category) {
        this.name = name;
        this.imageResourceId = imageResourceId;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getCategory() {
        return category;
    }
}