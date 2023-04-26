package com.example.twoplayergame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {
    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView scoreText;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.score_name_text);
            scoreText = itemView.findViewById(R.id.score_value_text);
        }
    }

    private final List<Score> scores;

    public ScoreAdapter(List<Score> scores) {
        this.scores = scores;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.score_item, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Score score = scores.get(position);
        holder.nameText.setText(score.getName());
        holder.scoreText.setText(String.format(Locale.getDefault(), "%d", score.getValue()));
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }
}
