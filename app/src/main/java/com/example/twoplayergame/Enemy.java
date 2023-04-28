package com.example.twoplayergame;

import android.graphics.Bitmap;

public class Enemy extends Character {
    public Enemy(Vector2 position, float rotation, Bitmap drawable) {
        super(position, rotation, drawable);

        addOnHealthChangedListener(this::onHealthChanged);
    }

    @Override
    public void update(double deltaTime, double time) {
        setRotation(getRotation() + 5f * (float) deltaTime);
        super.update(deltaTime, time);
    }

    private void onHealthChanged(Character character, int current, int old) {
        if (current <= 25) {
            // todo phase 4

        } else if (current <= 50) {
            // todo phase 3

        } else if (current <= 75) {
            // todo phase 2

        }

    }

    public void setActive() {
        for (ProjectileSpawner spawner : getProjectileSpawners()) {
            spawner.setActive(true);
        }
    }
}
