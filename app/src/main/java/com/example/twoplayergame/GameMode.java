package com.example.twoplayergame;

public enum GameMode {
    SOLO(0),
    PVP(1),
    COOP(2);

    private final int value;

    GameMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
