package com.example.twoplayergame;

import android.view.SurfaceView;

public class CoopGame extends Game {
    public CoopGame(SurfaceView surfaceView) {
        super(surfaceView);
    }

    @Override
    protected void onUpdate(double deltaTime, double time) {

    }

    @Override
    protected boolean hasCollision(Character character, Projectile projectile) {
        if (projectile.getOwner() instanceof Player) {
            return !(character instanceof Player) && character.checkCollision(projectile);
        } else if (projectile.getOwner() instanceof Enemy) {
            return (character instanceof Player) && character.checkCollision(projectile);
        } else {
            return false;
        }
    }
}
