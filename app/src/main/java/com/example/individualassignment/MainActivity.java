package com.example.individualassignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.individualassignment.component.Player;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        if (getPlayersFromPrefs(prefs).isEmpty()) {
            insertPlayerData();
        }

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
            int score = (int) (Math.random() * (13 - 4) + 5);
            String playerKey = "@Player:" + name;
            editor.putInt(playerKey, score);
        }
        editor.apply();

    }


    public void startNow(View view) {
        Intent intent = new Intent(this, Level1Activity.class);
        startActivity(intent);
        finish();
    }

    public void showLeaderboard(View view) {
        Intent intent = new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
        finish();
    }

    public void showRules(View view) {
        Intent intent = new Intent(this, RulesActivity.class);
        startActivity(intent);
        finish();
    }


}