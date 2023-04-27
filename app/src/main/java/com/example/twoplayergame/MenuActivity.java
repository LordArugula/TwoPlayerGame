package com.example.twoplayergame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(this::onClickPlayButton);
        Button highScoresButton = findViewById(R.id.high_scores_button);
        highScoresButton.setOnClickListener(this::onClickHighScoresButton);
    }

    private void onClickHighScoresButton(View view) {
        Intent intent = new Intent(this, ScoresActivity.class);
        startActivity(intent);
    }

    private void onClickPlayButton(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(String.valueOf(R.string.EXTRA_GAME_MODE), GameMode.COOP);
        startActivity(intent);
        finish();
    }
}
