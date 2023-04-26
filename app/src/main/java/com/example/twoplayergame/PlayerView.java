package com.example.twoplayergame;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PlayerView {
    private final Handler handler;
    private TextView healthText;
    private TextView scoreText;

    public PlayerView(Character character) {
        handler = new Handler(Looper.getMainLooper());
        character.addOnHealthChangedListener(this::onHealthChanged);
        character.addOnScoreChangedListener(this::onScoreChanged);
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    public void bind(ViewGroup viewGroup, Player player) {
        healthText = viewGroup.findViewById(R.id.health_text);
        healthText.setText(Integer.toString(player.getHealth()));

        scoreText = viewGroup.findViewById(R.id.score_text);
        scoreText.setText(Integer.toString(player.getScore()));

        Button fireButton = viewGroup.findViewById(R.id.fire_button);
        fireButton.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    player.setFireInput(true);
                    break;
                case MotionEvent.ACTION_UP:
                    player.setFireInput(false);
                    break;
            }
            return false;
        });

        View joystickView = viewGroup.findViewById(R.id.joystick_view);
        Joystick joystick = new Joystick(joystickView, player::setMoveInput);
        joystickView.setRotation(player.getRotation());
    }

    @SuppressLint("SetTextI18n")
    public void onHealthChanged(Character character, int health) {
        handler.post(() -> healthText.setText(Integer.toString(health)));
    }

    @SuppressLint("SetTextI18n")
    public void onScoreChanged(Character character, int score) {
        handler.post(() -> scoreText.setText(Integer.toString(score)));
    }
}
