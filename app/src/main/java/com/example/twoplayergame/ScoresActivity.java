package com.example.twoplayergame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScoresActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    private ScoreAdapter scoreAdapter;
    private List<Score> scores;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_scores);

        sharedPreferences = getSharedPreferences(ScoreUtils.SHARED_PREF_FILE, MODE_PRIVATE);

        scores = ScoreUtils.getScores(sharedPreferences);
        scoreAdapter = new ScoreAdapter(scores);

        RecyclerView scoresRecyclerView = findViewById(R.id.scores_recycler_view);
        scoresRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        scoresRecyclerView.setAdapter(scoreAdapter);

        Button menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(this::onClickMenuButton);

        Button clearScoresButton = findViewById(R.id.clear_scores_button);
        clearScoresButton.setOnClickListener(this::onClickClearScoresButton);
    }

    private void onClickMenuButton(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void onClickClearScoresButton(View view) {
        int count = scores.size();
        scores.clear();
        ScoreUtils.clearScores(sharedPreferences);
        scoreAdapter.notifyItemRangeRemoved(0, count);
    }
}

