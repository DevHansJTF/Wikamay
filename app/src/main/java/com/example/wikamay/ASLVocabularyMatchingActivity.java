package com.example.wikamay;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ASLVocabularyMatchingActivity extends AppCompatActivity {

    private ImageView[] images = new ImageView[6];
    private View prevButton, nextButton;
    private List<ASLPair> allPairs;
    private List<ASLPair> currentPagePairs;
    private Map<Integer, List<Integer>> pageProgress = new HashMap<>();
    private List<Integer> completedPages = new ArrayList<>();
    private int currentPage = 0;
    private int selectedImageIndex = -1;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asl_vocabulary_matching);

        initializeViews();
        initializePairs();
        setupButtons();
        loadPage();
        setupCloseButton();
    }

    private void initializeViews() {
        images[0] = findViewById(R.id.image1);
        images[1] = findViewById(R.id.image2);
        images[2] = findViewById(R.id.image3);
        images[3] = findViewById(R.id.image4);
        images[4] = findViewById(R.id.image5);
        images[5] = findViewById(R.id.image6);

        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);

        for (int i = 0; i < images.length; i++) {
            final int index = i;
            images[i].setOnClickListener(v -> onImageClick(index));
        }
    }

    private void setupCloseButton() {
        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> finish());
    }

    private void initializePairs() {
        allPairs = new ArrayList<>();
        allPairs.add(new ASLPair(R.drawable.animal_cow, R.drawable.animal_sign_cow));
        allPairs.add(new ASLPair(R.drawable.emotion_happy, R.drawable.emotion_sign_happy));
        allPairs.add(new ASLPair(R.drawable.greeting_hello, R.drawable.greetings_sign_hello));
        allPairs.add(new ASLPair(R.drawable.letter_a, R.drawable.letter_sign_a));
        for (int i = 0; i <= 10; i++) {
            int numberResId = getResources().getIdentifier("number_" + i, "drawable", getPackageName());
            int signResId = getResources().getIdentifier("number_sign_" + i, "drawable", getPackageName());
            allPairs.add(new ASLPair(numberResId, signResId));
        }
        Collections.shuffle(allPairs);
    }

    private void setupButtons() {
        prevButton.setOnClickListener(v -> moveToPreviousPage());
        nextButton.setOnClickListener(v -> moveToNextPage());
    }

    private void loadPage() {
        if (currentPage >= allPairs.size() / 3) {
            finish();
            return;
        }

        currentPagePairs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (currentPage * 3 + i < allPairs.size()) {
                currentPagePairs.add(allPairs.get(currentPage * 3 + i));
            }
        }

        List<Integer> imageResources = new ArrayList<>();
        for (ASLPair pair : currentPagePairs) {
            imageResources.add(pair.getGivenImageResId());
            imageResources.add(pair.getAnswerImageResId());
        }
        Collections.shuffle(imageResources);

        for (int i = 0; i < images.length; i++) {
            if (i < imageResources.size()) {
                images[i].setImageResource(imageResources.get(i));
                images[i].setTag(imageResources.get(i));
                images[i].setAlpha(1f);
                images[i].setVisibility(View.VISIBLE);
            } else {
                images[i].setVisibility(View.GONE);
            }
        }

        // Restore progress if page was partially completed
        if (pageProgress.containsKey(currentPage)) {
            List<Integer> matchedResources = pageProgress.get(currentPage);
            for (ImageView image : images) {
                if (image.getVisibility() == View.VISIBLE && matchedResources.contains((Integer) image.getTag())) {
                    image.setAlpha(0.4f);
                }
            }
        }

        updateNavigationButtons();
    }

    private void onImageClick(int index) {
        if (images[index].getAlpha() < 1f) {
            return; // Already matched
        }

        if (selectedImageIndex == -1) {
            selectedImageIndex = index;
            images[index].setAlpha(0.8f);
        } else {
            int firstImageResId = (Integer) images[selectedImageIndex].getTag();
            int secondImageResId = (Integer) images[index].getTag();

            if (isPairMatched(firstImageResId, secondImageResId)) {
                images[selectedImageIndex].setAlpha(0.4f);
                images[index].setAlpha(0.4f);
                saveProgress(firstImageResId, secondImageResId);
                if (isPageCompleted()) {
                    completedPages.add(currentPage);
                    pageProgress.remove(currentPage);
                }
                updateNavigationButtons();
            } else {
                images[selectedImageIndex].setAlpha(1f);
            }
            selectedImageIndex = -1;
        }
    }

    private boolean isPairMatched(int resId1, int resId2) {
        for (ASLPair pair : currentPagePairs) {
            if ((pair.getGivenImageResId() == resId1 && pair.getAnswerImageResId() == resId2) ||
                    (pair.getGivenImageResId() == resId2 && pair.getAnswerImageResId() == resId1)) {
                return true;
            }
        }
        return false;
    }

    private void saveProgress(int resId1, int resId2) {
        if (!pageProgress.containsKey(currentPage)) {
            pageProgress.put(currentPage, new ArrayList<>());
        }
        List<Integer> matchedResources = pageProgress.get(currentPage);
        matchedResources.add(resId1);
        matchedResources.add(resId2);
    }

    private boolean isPageCompleted() {
        for (ImageView image : images) {
            if (image.getVisibility() == View.VISIBLE && image.getAlpha() == 1f) {
                return false;
            }
        }
        return true;
    }

    private void moveToNextPage() {
        if (currentPage < allPairs.size() / 3 - 1) {
            currentPage++;
            loadPage();
        }
    }

    private void moveToPreviousPage() {
        if (currentPage > 0) {
            do {
                currentPage--;
            } while (currentPage > 0 && completedPages.contains(currentPage));

            if (!completedPages.contains(currentPage)) {
                loadPage();
            } else {
                updateNavigationButtons();
            }
        }
    }

    private void updateNavigationButtons() {
        prevButton.setEnabled(canMoveToPrevious());
        nextButton.setEnabled(currentPage < allPairs.size() / 3 - 1);
    }

    private boolean canMoveToPrevious() {
        for (int i = currentPage - 1; i >= 0; i--) {
            if (!completedPages.contains(i)) {
                return true;
            }
        }
        return false;
    }

    private static class ASLPair {
        private int givenImageResId;
        private int answerImageResId;

        public ASLPair(int givenImageResId, int answerImageResId) {
            this.givenImageResId = givenImageResId;
            this.answerImageResId = answerImageResId;
        }

        public int getGivenImageResId() {
            return givenImageResId;
        }

        public int getAnswerImageResId() {
            return answerImageResId;
        }
    }
}