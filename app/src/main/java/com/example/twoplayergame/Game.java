package com.example.twoplayergame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public abstract class Game implements RequiresUpdate {
    private final SurfaceView surfaceView;

    private final List<Character> characters;

    private final ProjectilePool projectiles;

    private int width;
    private int height;
    private boolean isRunning;

    public Game(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        characters = new ArrayList<>();
        projectiles = new ProjectilePool();
        ProjectilePoolProvider.withInstance(projectiles);
    }

    public void setScreenSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void start() {
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public final void update(double deltaTime, double time) {
        if (!isRunning) {
            return;
        }

        updateGameEntities(deltaTime, time);
        renderGameEntities();

        onUpdate(deltaTime, time);

        handleCollisions();
        restrictPlayersToScreenBoundaries();
    }

    protected abstract void onUpdate(double deltaTime, double time);

    private void restrictPlayersToScreenBoundaries() {
        for (Character character : characters) {
            if (!(character instanceof Player)) {
                continue;
            }

            Vector2 position = character.getPosition();
            Vector2 halfSize = character.getHalfSize();

            double x = position.x;
            double y = position.y;

            if (position.x - halfSize.x < 0) {
                x = halfSize.x;
            }

            if (position.x + halfSize.x > width) {
                x = width - halfSize.x;
            }

            if (character.getRotation() == 0f) {
                if (position.y - halfSize.y < height / 2f) {
                    y = height / 2f + halfSize.y;
                }
                if (position.y + halfSize.y > height) {
                    y = height - halfSize.y;
                }
            } else if (character.getRotation() == 180f) {
                if (position.y - halfSize.y < 0) {
                    y = halfSize.y;
                }

                if (position.y + halfSize.y > height / 2f) {
                    y = height / 2f - halfSize.y;
                }
            }


            character.setPosition(new Vector2(x, y));
        }
    }

    private void handleCollisions() {
        List<Projectile> activeProjectiles = projectiles.getActive();
        for (Character character : characters) {
            int damage = 0;

            for (int i = 0; i < activeProjectiles.size(); i++) {
                Projectile projectile = activeProjectiles.get(i);
                if (hasCollision(character, projectile)) {
                    damage += projectile.getDamage();
                    projectiles.release(i);
                    i--;
                }
            }

            character.takeDamage(damage);
        }
    }

    protected abstract boolean hasCollision(Character character, Projectile projectile);

    protected boolean isProjectileOutOfBoundaries(Projectile projectile) {
        return projectile.getMax().x < 0
                || projectile.getMax().y < 0
                || projectile.getMin().x > width
                || projectile.getMin().y > height;
    }

    protected void renderGameEntities() {
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                surfaceView.draw(canvas);
                for (Character character : characters) {
                    character.render(canvas);
                }

                for (Projectile projectile : projectiles.getActive()) {
                    projectile.render(canvas);
                }
            }
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    protected void updateGameEntities(double deltaTime, double time) {
        for (Character character : characters) {
            character.update(deltaTime, time);
        }

        List<Projectile> active = projectiles.getActive();
        for (int i = 0; i < active.size(); i++) {
            Projectile projectile = active.get(i);
            projectile.update(deltaTime, time);

            if (isProjectileOutOfBoundaries(projectile)) {
                projectiles.release(i);
                i--;
            }
        }
    }

    public void addPlayer(Player player) {
        characters.add(player);
        player.addOnHealthChangedListener(this::onCharacterHealthChanged);
    }

    private void onCharacterHealthChanged(Character character, int current, int old) {
        if (current <= 0) {
            characters.remove(character);
            character.removeOnHealthChangedListener(this::onCharacterHealthChanged);
        }
    }
}