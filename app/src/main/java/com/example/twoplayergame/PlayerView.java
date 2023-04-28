package com.example.twoplayergame;

import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

public class PlayerView {
    private final Handler handler;
    private TextView healthText;
    private TextView scoreText;

    private Player player;
    private View joystickView;
    private View fireButton;

    public PlayerView() {
        handler = new Handler(Looper.getMainLooper());
    }

    public void bind(ViewGroup viewGroup, Player player) {
        this.player = player;
        player.addOnHealthChangedListener(this::onHealthChanged);
        player.addOnScoreChangedListener(this::onScoreChanged);

        healthText = viewGroup.findViewById(R.id.health_text);
        onHealthChanged(player, player.getHealth(), 0);
        scoreText = viewGroup.findViewById(R.id.score_text);
        onScoreChanged(player, player.getScore());

        fireButton = viewGroup.findViewById(R.id.fire_button);
        fireButton.setOnTouchListener(this::onTouchFireButton);

        joystickView = viewGroup.findViewById(R.id.joystick_view);
        Joystick joystick = new Joystick(joystickView, player::setMoveInput);
        joystickView.setRotation(player.getRotation());
    }

    private boolean onTouchFireButton(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                player.setFireInput(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                player.setFireInput(false);
                break;
            default:
                return false;
        }
        return true;
    }

    public void onHealthChanged(Character character, int current, int old) {
        handler.post(() -> {
            if (current <= 0) {
                joystickView.setVisibility(View.INVISIBLE);
                fireButton.setVisibility(View.INVISIBLE);
            }
            healthText.setText(String.format(Locale.getDefault(), "%d", current));
        });
    }

    public void onScoreChanged(Character character, int score) {
        handler.post(() -> scoreText.setText(String.format(Locale.getDefault(), "%d", score)));
    }
}
