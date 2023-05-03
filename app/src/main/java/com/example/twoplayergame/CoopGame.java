package com.example.twoplayergame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.SurfaceView;

public class CoopGame extends Game {
    private final Bitmap enemyBitmap;
    private final Bitmap projectileBitmap;

    public CoopGame(SurfaceView surfaceView, Resources resources) {
        super(surfaceView);

        enemyBitmap = BitmapFactory.decodeResource(resources, R.drawable.player_two);
        projectileBitmap = BitmapFactory.decodeResource(resources, R.drawable.projectile);
    }

    private double timer;

    private Enemy enemy;
    private boolean started;

    @Override
    protected void onStart() {
        enemy = new CharacterBuilder()
                .withName("Stinky")
                .withHealth(100)
                .withBitmap(enemyBitmap)
                .withProjectileSpawner(new ProjectileSpawnerBuilder()
                        .withFireSpeed(0.25)
                        .withRadius(1)
                        .withArcDegrees(360)
                        .withProjectile(new ProjectileData(projectileBitmap, 80, 1), 15)
                        .build())
                .withProjectileSpawner(new ProjectileSpawnerBuilder()
                        .withFireSpeed(0.65)
                        .withRadius(1)
                        .withArcDegrees(60)
                        .withRotation(45)
                        .withProjectile(new ProjectileData(projectileBitmap, 60, 1), 3)
                        .build())
                .withProjectileSpawner(new ProjectileSpawnerBuilder()
                        .withFireSpeed(0.65)
                        .withRadius(1)
                        .withRotation(135)
                        .withArcDegrees(60)
                        .withProjectile(new ProjectileData(projectileBitmap, 60, 1), 3)
                        .build())
                .withProjectileSpawner(new ProjectileSpawnerBuilder()
                        .withFireSpeed(0.65)
                        .withRadius(1)
                        .withArcDegrees(60)
                        .withRotation(225)
                        .withProjectile(new ProjectileData(projectileBitmap, 60, 1), 3)
                        .build())
                .withProjectileSpawner(new ProjectileSpawnerBuilder()
                        .withFireSpeed(0.65)
                        .withRadius(1)
                        .withArcDegrees(60)
                        .withRotation(315)
                        .withProjectile(new ProjectileData(projectileBitmap, 60, 1), 3)
                        .build())
                .withPosition(new Vector2(getWidth() / 2f, getHeight() / 2f))
                .withScore(100)
                .buildEnemy();
        addCharacter(enemy);
    }

    @Override
    protected void onUpdate(double deltaTime, double time) {
        if (started) {
            return;
        }

        timer += deltaTime;

        if (timer >= 3) {
            enemy.setActive();
            timer = 0;
            started = true;
        }
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
