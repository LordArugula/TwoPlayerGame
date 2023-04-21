package com.example.twoplayergame;

import android.graphics.Bitmap;
import android.util.Log;

public class Player extends Character {
    // user input
    private Vector2 inputDirection;

    public Player(Vector2 position, float rotation, Bitmap drawable) {
        super(position, rotation, drawable);
        inputDirection = Vector2.zero();
    }

    @Override
    public void update(double deltaTime, double time) {
        setMovementDirection(inputDirection);
        super.update(deltaTime, time);
    }

    public void setFireInput(boolean shouldFire) {
        getSpawnerData().setActive(shouldFire);
    }

    public void setMoveInput(Vector2 input) {
        if (input.magnitudeSq() <= 0.00001) {
            inputDirection = Vector2.zero();
        } else {
            inputDirection = input;
        }
    }
}
