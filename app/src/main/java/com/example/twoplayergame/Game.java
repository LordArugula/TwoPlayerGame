package com.example.twoplayergame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public abstract class Game implements RequiresUpdate {
    public interface OnGameWonListener {
        void onGameWon();
    }

    public interface OnGameLostListener {
        void onGameLost();
    }

    private final SurfaceView surfaceView;

    private final List<Character> players;
    private final List<Character> enemies;

    private final ProjectilePool projectiles;

    private int width;
    private int height;
    private boolean isRunning;

    private OnGameWonListener onGameWonListener;
    private OnGameLostListener onGameLostListener;

    public Game(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        players = new ArrayList<>();
        enemies = new ArrayList<>();
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
        if (!isRunning) {
            onStart();
        }
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    public void setOnGameWonListener(OnGameWonListener onGameWonListener) {
        this.onGameWonListener = onGameWonListener;
    }

    public void setOnGameLostListener(OnGameLostListener onGameLostListener) {
        this.onGameLostListener = onGameLostListener;
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

        if (players.size() == 0) {
            stop();
            onGameLostListener.onGameLost();
        }

        if (enemies.size() == 0) {
            stop();
            onGameWonListener.onGameWon();
        }
    }

    protected abstract void onStart();

    protected abstract void onUpdate(double deltaTime, double time);

    private void restrictPlayersToScreenBoundaries() {
        for (Character character : players) {
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
        for (int characterIndex = 0; characterIndex < players.size(); characterIndex++) {
            Character character = players.get(characterIndex);

            for (int projectileIndex = 0; projectileIndex < activeProjectiles.size(); projectileIndex++) {
                Projectile projectile = activeProjectiles.get(projectileIndex);
                if (hasCollision(character, projectile)) {
                    int damage = projectile.getDamage();
                    character.takeDamage(damage);

                    projectiles.release(projectileIndex);
                    projectileIndex--;

                    if (character.getHealth() <= 0) {
                        int end = players.size() - 1;
                        Character last = players.get(end);

                        players.set(characterIndex, last);
                        players.remove(end);
                        characterIndex--;

                        break;
                    }
                }
            }

        }

        for (int characterIndex = 0; characterIndex < enemies.size(); characterIndex++) {
            Character character = enemies.get(characterIndex);

            for (int projectileIndex = 0; projectileIndex < activeProjectiles.size(); projectileIndex++) {
                Projectile projectile = activeProjectiles.get(projectileIndex);

                if (hasCollision(character, projectile)) {
                    int damage = projectile.getDamage();
                    character.takeDamage(damage);

                    projectiles.release(projectileIndex);
                    projectileIndex--;

                    projectile.getOwner().addScore(1);

                    if (character.getHealth() <= 0) {
                        int end = enemies.size() - 1;
                        Character last = enemies.get(end);

                        enemies.set(characterIndex, last);
                        enemies.remove(end);
                        characterIndex--;

                        projectile.getOwner().addScore(character.getScore());
                        break;
                    }
                }
            }
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
                for (Character character : players) {
                    character.render(canvas);
                }

                for (Character character : enemies) {
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
        for (Character character : players) {
            character.update(deltaTime, time);
        }

        for (Character character : enemies) {
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

    public void addCharacter(Character character) {
        if (character instanceof Player) {
            players.add(character);
        } else if (character instanceof Enemy) {
            enemies.add(character);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}