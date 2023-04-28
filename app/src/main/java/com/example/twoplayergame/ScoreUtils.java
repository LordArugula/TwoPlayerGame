package com.example.twoplayergame;

import android.content.SharedPreferences;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScoreUtils {
    public static final String SHARED_PREF_FILE = "com.two_player_game.scores";
    public static final String SCORES = "SCORES";

    public static List<Score> getScores(SharedPreferences sharedPreferences) {
        String scoresJson = sharedPreferences.getString(SCORES, null);
        List<Score> scores = JsonUtils.scoresFromJson(scoresJson);
        return scores;
    }

    public static void clearScores(SharedPreferences preferences) {
        preferences.edit()
                .clear()
                .apply();
    }

    public static void addScores(SharedPreferences preferences, List<Score> scores) {
        List<Score> allScores = getScores(preferences);
        allScores.addAll(scores);

        allScores.sort(Comparator.comparingInt(Score::getValue).reversed());
        List<Score> topScores = allScores.stream().limit(10).collect(Collectors.toList());

        String scoresJson = JsonUtils.scoresToJson(topScores);
        preferences.edit()
                .putString(SCORES, scoresJson)
                .apply();
    }
}
