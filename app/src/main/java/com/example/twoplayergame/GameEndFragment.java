package com.example.twoplayergame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        TextView title = view.findViewById(R.id.game_end_title);
        title.setText(endTitle);

        Button menuButton = view.findViewById(R.id.menu_button);
        menuButton.setOnClickListener(this::onClickMenuButton);

        Button scoresButton = view.findViewById(R.id.high_scores_button);
        scoresButton.setOnClickListener(this::onClickHighScoresButton);
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