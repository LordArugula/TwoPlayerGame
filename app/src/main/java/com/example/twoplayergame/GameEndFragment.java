package com.example.twoplayergame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GameEndFragment extends Fragment {
    private final String endTitle;

    public GameEndFragment(String endTitle) {
        super(R.layout.fragment_game_end);
        this.endTitle = endTitle;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Animation slideInLeft = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);

        TextView title = view.findViewById(R.id.game_end_title);
        title.setText(endTitle);
        title.setAnimation(slideInLeft);

        Button menuButton = view.findViewById(R.id.menu_button);
        menuButton.setOnClickListener(this::onClickMenuButton);

        Button scoresButton = view.findViewById(R.id.high_scores_button);
        scoresButton.setOnClickListener(this::onClickHighScoresButton);

        slideInLeft.start();
    }

    private void onClickHighScoresButton(View view) {
        Intent intent = new Intent(getContext(), ScoresActivity.class);
        startActivity(intent);
    }

    private void onClickMenuButton(View view) {
        Intent intent = new Intent(getContext(), MenuActivity.class);
        startActivity(intent);
    }
}