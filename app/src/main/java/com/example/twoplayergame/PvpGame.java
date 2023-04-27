package com.example.twoplayergame;

import android.view.SurfaceView;

public class PvpGame extends Game {

    public PvpGame(SurfaceView surfaceView) {
        super(surfaceView);
    }

    @Override
    protected void onUpdate(double deltaTime, double time) {

    }

    @Override
    protected boolean hasCollision(Character character, Projectile projectile) {
        return projectile.getOwner() != character
                && character.checkCollision(projectile);
    }
}