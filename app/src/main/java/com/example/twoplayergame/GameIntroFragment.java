package com.example.twoplayergame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GameIntroFragment extends Fragment {
    public GameIntroFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_intro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Animation inOut = AnimationUtils.loadAnimation(getContext(), R.anim.in_out);

        TextView startText = view.findViewById(R.id.start_text);
        startText.setText("Ready");
        startText.setAnimation(inOut);
        inOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                switch (startText.getText().toString()) {
                    case "Ready":
                        startText.setText("Go!");
                        animation.start();
                        break;
                    case "Go!":
                        startText.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        inOut.start();
    }
}
