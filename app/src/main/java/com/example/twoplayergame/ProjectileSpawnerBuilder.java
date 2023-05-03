package com.example.twoplayergame;

public class ProjectileSpawnerBuilder {
    private ProjectileData projectile;
    private int count;
    private double fireSpeed;
    private float arc;
    private double radius;
    private float rotation;

    public ProjectileSpawnerBuilder withProjectile(ProjectileData projectileData, int count) {
        this.projectile = projectileData;
        this.count = count;
        return this;
    }

    public ProjectileSpawnerBuilder withRotation(float degrees) {
        this.rotation = degrees;
        return this;
    }

    public ProjectileSpawnerBuilder withFireSpeed(double speed) {
        this.fireSpeed = speed;
        return this;
    }

    public ProjectileSpawnerBuilder withArcDegrees(float degrees) {
        this.arc = (float) Math.toRadians(degrees);
        return this;
    }

    public ProjectileSpawnerBuilder withRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public ProjectileSpawner build() {
        ProjectileSpawner projectileSpawner = new ProjectileSpawner(projectile, count, fireSpeed, arc, radius);
        projectileSpawner.setRotation(rotation);
        return projectileSpawner;
    }
}
