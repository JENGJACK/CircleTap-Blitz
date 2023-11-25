package com.example.individualassignment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.individualassignment.component.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {
    private TextView scoreTextView;
    private EditText nameEditText;
    private Button submitButton;
    private Button HomeButton;
    private Button Home;
    private Button LeaderboardButton;
    private LinearLayout lyXTop25;
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String SCORE_KEY = "score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        scoreTextView = findViewById(R.id.scoreTextView);
        nameEditText = findViewById(R.id.nameEditText);
        submitButton = findViewById(R.id.submitButton);
        HomeButton = findViewById(R.id.HomeButton);
        Home = findViewById(R.id.Home);
        LeaderboardButton = findViewById(R.id.leaderboardButton);
        lyXTop25 = findViewById(R.id.xtop25layout);


        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int score = prefs.getInt(SCORE_KEY, 0);
        scoreTextView.setText("Score: " + score);

        ArrayList<Player> players = getPlayersFromPrefs(prefs);
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return Integer.compare(p1.getScore(), p2.getScore());
            }
        });

        boolean isInTop25 = false;
        if (players.size() < 25) {
            isInTop25 = true; // If less than 25 players are in the leaderboard, the new score will be in the top 25
        } else {
            for (int i = 0; i < 25; i++) {
                if (score >= players.get(i).getScore()) {
                    isInTop25 = true;
                    break;
                }
            }
        }


        if (isInTop25) {
            showTop25Dialog();
            lyXTop25.setVisibility(View.GONE);
        } else {
            showNotTop25Dialog();
            nameEditText.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);
            HomeButton.setVisibility(View.GONE);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                if (name.isEmpty()) {
                    showEmptyNameErrorDialog();
                } else {
                    // Save name and score or perform other actions
                    savePlayerScore(name, score);
                    removeScore();
                    insertPlayerData();
                    Intent intent = new Intent(ResultActivity.this, LeaderboardActivity.class);
                    startActivity(intent);
                }
            }
        });

        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        LeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, LeaderboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private ArrayList<Player> getPlayersFromPrefs(SharedPreferences prefs) {
        Map<String, ?> allEntries = prefs.getAll();
        ArrayList<Player> players = new ArrayList<>();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("@Player:")) {
                String name = key.substring("@Player:".length());
                int score = (Integer) entry.getValue();
                players.add(new Player(name, score));
            }
        }
        return players;
    }

    private void insertPlayerData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        for (int i = 0; i < 25; i++) {
            String name = "Player " + i;
            int score = (int) (Math.random() * (13 - 5 + 1) + 5);
            String playerKey = "@Player:" + name;
            editor.putInt(playerKey, score);
        }

        editor.apply();

    }

    private void showTop25Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Congratulations!");
        builder.setMessage("You made it to the top 25!");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.setCancelable(false);
        builder.show();
        removeScore();
    }

    private void showNotTop25Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sorry!");
        builder.setMessage("You are not in the top 25.");
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.setCancelable(false);
        builder.show();
    }


    private void savePlayerScore(String name, int score) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String playerKey = "@Player:" + name;
        editor.putInt(playerKey, score);
        editor.apply();
    }

    private void showEmptyNameErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Please enter your name.");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.setCancelable(false);
        builder.show();
    }

    private void removeScore() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(SCORE_KEY);
        editor.apply();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}


