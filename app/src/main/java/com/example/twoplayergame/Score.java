package com.example.twoplayergame;

public class Score {
    private String name;
    private int value;

    public Score(String name, int score) {
        this.name = name;
        this.value = score;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
