package com.example.twoplayergame;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Character extends GameEntity implements RequiresUpdate {
    public interface OnHealthChangedListener {
        void onHealthChanged(Character player, int health);
    }

    public interface OnScoreChangedListener {
        void onScoreChanged(Character player, int score);
    }

    private final List<OnHealthChangedListener> onHealthChangedListeners;
    private final List<OnScoreChangedListener> onScoreChangedListeners;

    // character information
    private String name;
    private int health;
    private int score;

    // movement data
    private Vector2 movementDirection;
    private double movementSpeed;

    // projectile spawner data
    private ProjectileSpawner projectileSpawner;

    public Character(Vector2 position, float rotation, Bitmap drawable) {
        super(position, rotation, drawable);

        onHealthChangedListeners = new ArrayList<>();
        onScoreChangedListeners = new ArrayList<>();

        movementDirection = Vector2.zero();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        for (OnHealthChangedListener listener : onHealthChangedListeners) {
            listener.onHealthChanged(this, health);
        }
    }

    public void takeDamage(int amount) {
        health -= amount;
        for (OnHealthChangedListener listener : onHealthChangedListeners) {
            listener.onHealthChanged(this, health);
        }
    }

    public void takeHealing(int amount) {
        health += amount;
        for (OnHealthChangedListener listener : onHealthChangedListeners) {
            listener.onHealthChanged(this, health);
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;

        for (OnScoreChangedListener listener : onScoreChangedListeners) {
            listener.onScoreChanged(this, score);
        }
    }

    public void addScore(int amount) {
        score += amount;

        for (OnScoreChangedListener listener : onScoreChangedListeners) {
            listener.onScoreChanged(this, score);
        }
    }

    public void setMovementDirection(Vector2 movementDirection) {
        this.movementDirection = movementDirection;
    }

    public void setMovementSpeed(double movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public void setSpawnerData(ProjectileSpawner projectileSpawner) {
        this.projectileSpawner = projectileSpawner;
    }

    public ProjectileSpawner getSpawnerData() {
        return projectileSpawner;
    }

    public void addOnHealthChangedListener(OnHealthChangedListener onHealthChanged) {
        onHealthChangedListeners.add(onHealthChanged);
    }

    public void addOnScoreChangedListener(OnScoreChangedListener onScoreChanged) {
        onScoreChangedListeners.add(onScoreChanged);
    }

    @Override
    public void update(double deltaTime, double time) {
        Vector2 delta = Vector2.mul(movementDirection, movementSpeed * deltaTime);
        move(delta);

        projectileSpawner.updateTimer(deltaTime);
        if (projectileSpawner.canFireProjectiles()) {
            projectileSpawner.spawnProjectiles(this, new ProjectilePoolProvider().getInstance());
            projectileSpawner.resetTimer();
        }
    }
}


