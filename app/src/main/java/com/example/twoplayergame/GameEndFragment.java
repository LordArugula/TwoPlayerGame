package com.example.twoplayergame;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class GameEndFragment extends Fragment {
    public GameEndFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_end, container, false);

        TextView title = view.findViewById(R.id.game_end_title);

        Button menuButton = view.findViewById(R.id.menu_button);
        menuButton.setOnClickListener(this::onClickMenuButton);

        Button scoresButton = view.findViewById(R.id.high_scores_button);
        scoresButton.setOnClickListener(this::onClickHighScoresButton);

        return view;
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