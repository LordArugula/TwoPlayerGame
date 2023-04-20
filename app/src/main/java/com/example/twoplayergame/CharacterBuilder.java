package com.example.twoplayergame;

import android.graphics.Bitmap;

public class CharacterBuilder {
    private Vector2 position;
    private float rotation;
    private Vector2 size;
    private Bitmap bitmap;

    private String name;
    private int health;
    private int score;
    private double movementSpeed;
    private ProjectileSpawner projectileSpawner;

    public CharacterBuilder withPosition(Vector2 position) {
        this.position = position;
        return this;
    }

    public CharacterBuilder withRotation(float degrees) {
        this.rotation = degrees;
        return this;
    }

    public CharacterBuilder withSize(Vector2 size) {
        this.size = size;
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
        this.projectileSpawner = projectileSpawner;
        return this;
    }

    public Player buildPlayer() {
        Player player = new Player(position, rotation, size, bitmap);
        player.setName(name);
        player.setHealth(health);
        player.setScore(score);
        player.setMovementSpeed(movementSpeed);
        player.setSpawnerData(projectileSpawner);
        return player;
    }
}
