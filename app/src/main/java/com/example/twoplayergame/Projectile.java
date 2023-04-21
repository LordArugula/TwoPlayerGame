package com.example.twoplayergame;

public class Projectile extends GameEntity implements RequiresUpdate {
    private Character owner;
    private int damage;
    // movement data
    private Vector2 movementDirection;
    private double movementSpeed;

    public Projectile() {
        super(new Vector2(0, 0), 0, null);
    }

    @Override
    public void update(double deltaTime, double time) {
        Vector2 delta = Vector2.mul(movementDirection, movementSpeed * deltaTime);
        setPosition(Vector2.add(getPosition(), delta));
    }

    public void setMovementDirection(Vector2 movementDirection) {
        this.movementDirection = movementDirection;
    }

    public void setMovementSpeed(double movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Character getOwner() {
        return owner;
    }

    public void setOwner(Character owner) {
        this.owner = owner;
    }
}
