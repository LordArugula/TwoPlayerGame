package com.example.twoplayergame;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private final static Gson instance = new Gson();
    private static final Type scoresListType = new TypeToken<List<Score>>() {
    }.getType();

    public static List<Score> scoresFromJson(String jsonString) {
        if (jsonString == null) {
            return new ArrayList<>();
        }
        return instance.fromJson(jsonString, scoresListType);
    }

    public static String scoresToJson(List<Score> scores) {
        return instance.toJson(scores);
    }
}
