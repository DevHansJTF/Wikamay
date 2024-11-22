package com.example.wikamay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import org.pytorch.IValue;
import org.pytorch.LiteModuleLoader;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TorchModelHandler {
    private static final String TAG = "TORCH_MODEL_HANDLER";
    private final Context context;
    private Module module;
    private List<String> classes;

    public TorchModelHandler(Context context) {
        this.context = context;
        loadClasses();
        loadTorchModule();
    }

    private void loadTorchModule() {
        String fileName = "2_CLASSES_MODEL.ptl";
        File modelFile = new File(context.getFilesDir(), fileName);
        try {
            if (!modelFile.exists()) {
                InputStream inputStream = context.getAssets().open(fileName);
                FileOutputStream outputStream = new FileOutputStream(modelFile);
                byte[] buffer = new byte[2048];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.close();
            }
            module = LiteModuleLoader.load(modelFile.getAbsolutePath());
            Log.d(TAG, "Model successfully loaded");
        } catch (IOException e) {
            Log.e(TAG, "Error loading model", e);
        }
    }

    private void loadClasses() {
        classes = new ArrayList<>();
        try (InputStream is = context.getAssets().open("classes.txt");
             java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                classes.add(line);
            }
            Log.d(TAG, "Number of classes loaded: " + classes.size());
        } catch (IOException e) {
            Log.e(TAG, "Error loading classes", e);
        }
    }

    public String analyzeImage(byte[] imageBytes, int width, int height) {
        try {
            int cropSize = 224;
            float[] mean = {0.485f, 0.456f, 0.406f};
            float[] std = {0.229f, 0.224f, 0.225f};

            Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
                    android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length),
                    mean, std
            );

            Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();
            float[] scores = outputTensor.getDataAsFloatArray();

            int maxScoreIdx = 0;
            float maxScore = scores[0];
            for (int i = 1; i < scores.length; i++) {
                if (scores[i] > maxScore) {
                    maxScore = scores[i];
                    maxScoreIdx = i;
                }
            }

            if (maxScoreIdx >= 0 && maxScoreIdx < classes.size()) {
                return classes.get(maxScoreIdx);
            } else {
                Log.e(TAG, "Index out of bounds for class labels");
                return "Unknown";
            }
        } catch (Exception e) {
            Log.e(TAG, "Error analyzing image", e);
            return "Error";
        }
    }
}