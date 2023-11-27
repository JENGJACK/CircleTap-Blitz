package com.example.individualassignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.individualassignment.component.DefaultCircleView;

import java.util.ArrayList;
import java.util.Random;

public class Level4Activity extends AppCompatActivity {

    private TextView scoreText;
    private TextView timerText;
    private GridLayout circleGrid;
    private Button startButton;

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String SCORE_KEY = "score";
    private SharedPreferences prefs;

    private LottieAnimationView animationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level4);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        animationView = findViewById(R.id.timerid);

        scoreText = findViewById(R.id.scoreText);
        timerText = findViewById(R.id.timerText);
        circleGrid = findViewById(R.id.circleGrid);
        startButton = findViewById(R.id.startButton);

        int score = prefs.getInt(SCORE_KEY, 0);
        TextView scorePlate = findViewById(R.id.scorePlate);
        scorePlate.setText("Score: " + score);


        setupGame();
        startButton.setOnClickListener(v -> startGame());
    }

    private void setupGame() {
        ArrayList<DefaultCircleView> circleViews = new ArrayList<>();
        if (circleGrid.getChildCount() == 0) {
            for (int i = 0; i < 25; i++) {
                DefaultCircleView circleView = new DefaultCircleView(this, null, 4); // Level 2
                circleGrid.addView(circleView);
                circleViews.add(circleView);
            }
        } else {
            for (int i = 0; i < circleGrid.getChildCount(); i++) {
                DefaultCircleView circleView = (DefaultCircleView) circleGrid.getChildAt(i);
                circleView.setColor(Color.GRAY);
                circleViews.add(circleView);
            }
        }
    }


    private void startGame() {
        Random random = new Random();
        int[] score = {0};
        scoreText.setText("Score: 0");
        animationView.setRepeatCount(LottieDrawable.INFINITE); // This will make the animation repeat indefinitely
        animationView.playAnimation();
        ArrayList<DefaultCircleView> circleViews = new ArrayList<>();
        for (int i = 0; i < circleGrid.getChildCount(); i++) {
            DefaultCircleView circleView = (DefaultCircleView) circleGrid.getChildAt(i);
            circleView.setColor(Color.GRAY);
            circleViews.add(circleView);
        }

        CountDownTimer timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                startButton.setEnabled(false); // Disable the start button once the game starts
                Button startButton = findViewById(R.id.startButton);
                startButton.setBackgroundColor(Color.parseColor("#CCCCCC"));

                timerText.setText(String.format("00:%02d", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                timerText.setText("00:00");
                showEndDialog(scoreText);
            }
        };

        timer.start();

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaultCircleView circleView = (DefaultCircleView) v;
                if (circleView.getColor() == Color.YELLOW) { // Check if the clicked circle is highlighted
                    circleView.setColor(Color.GREEN);
                    score[0]++;
                    scoreText.setText("Score: " + score[0]);
                    lightRandomCircle(circleViews, random, this);

                    // Change back to grey immediately
                    circleView.post(() -> {
                        circleView.setColor(Color.GRAY);
                        circleView.setOnClickListener(this);
                    });
                }
            }
        };

        lightRandomCircle(circleViews, random, clickListener);
    }

    private void lightRandomCircle(ArrayList<DefaultCircleView> circleViews, Random random, View.OnClickListener clickListener) {
        ArrayList<DefaultCircleView> unlitCircles = new ArrayList<>();

        for (DefaultCircleView circleView : circleViews) {
            if (circleView.getColor() == Color.GRAY) {
                unlitCircles.add(circleView);
            }
        }

        if (unlitCircles.isEmpty()) {
            return;
        }

        int index = random.nextInt(unlitCircles.size());
        DefaultCircleView circleView = unlitCircles.get(index);
        circleView.setColor(Color.YELLOW);
        circleView.setOnClickListener(clickListener);
    }

    private void showEndDialog(TextView scoreText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int score = Integer.parseInt(scoreText.getText().toString().split(" ")[1]);
        builder.setTitle("Congratulation");
        builder.setMessage("Score: " + (prefs.getInt(SCORE_KEY, 0) + score));
        builder.setPositiveButton("Leaderboard", (dialog, which) -> {
            dialog.dismiss();
            saveScore(score);
            Intent intent = new Intent(this, ResultActivity.class);
            startActivity(intent);
        });
        builder.setCancelable(false);
        builder.show();

    }

    private void saveScore(int score) {
        int previousScore = prefs.getInt(SCORE_KEY, 0);
        int newScore = previousScore + score;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SCORE_KEY, newScore);
        editor.apply();
    }

    private void removeScore() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(SCORE_KEY);
        editor.apply();
    }

    public void onHomeButtonClicked(View view) {
        removeScore();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}