package com.example.twoplayergame;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class CharacterBuilder {
    private Vector2 position = Vector2.zero();
    private float rotation;
    private Bitmap bitmap;

    private String name;
    private int health;
    private int score;
    private double movementSpeed;
    private final List<ProjectileSpawner> projectileSpawners;

    public CharacterBuilder() {
        projectileSpawners = new ArrayList<>();
    }

    public CharacterBuilder withPosition(Vector2 position) {
        this.position = position;
        return this;
    }

    public CharacterBuilder withRotation(float degrees) {
        this.rotation = degrees;
        return this;
    }

    public CharacterBuilder withBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }

    public CharacterBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CharacterBuilder withHealth(int health) {
        this.health = health;
        return this;
    }

    public CharacterBuilder withScore(int score) {
        this.score = score;
        return this;
    }

    public CharacterBuilder withMovementSpeed(double movementSpeed) {
        this.movementSpeed = movementSpeed;
        return this;
    }

    public CharacterBuilder withProjectileSpawner(ProjectileSpawner projectileSpawner) {
        this.projectileSpawners.add(projectileSpawner);
        return this;
    }

    public Player buildPlayer() {
        Player player = new Player(position, rotation, bitmap);
        player.setName(name);
        player.setHealth(health);
        player.setScore(score);
        player.setMovementSpeed(movementSpeed);
        for (ProjectileSpawner projectileSpawner : projectileSpawners) {
            player.addSpawnerData(projectileSpawner);
        }
        return player;
    }

    public Enemy buildEnemy() {
        Enemy enemy = new Enemy(position, rotation, bitmap);
        enemy.setName(name);
        enemy.setHealth(health);
        enemy.setScore(score);
        enemy.setMovementSpeed(movementSpeed);
        for (ProjectileSpawner projectileSpawner : projectileSpawners) {
            enemy.addSpawnerData(projectileSpawner);
        }
        return enemy;
    }
}
