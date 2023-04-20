package com.example.twoplayergame;

import android.graphics.Bitmap;

public class ProjectileData {
    private Bitmap bitmap;

    private double speed;
    private int damage;

    private Vector2 size;

    public ProjectileData(Bitmap bitmap, double speed, int damage, Vector2 size) {
        this.bitmap = bitmap;
        this.speed = speed;
        this.damage = damage;
        this.size = size;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
