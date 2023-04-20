package com.example.twoplayergame;

public class ProjectileSpawnerBuilder {
    private ProjectileData projectile;
    private int count;
    private double fireSpeed;
    private double arc;
    private double radius;

    public ProjectileSpawnerBuilder withProjectile(ProjectileData projectileData, int count) {
        this.projectile = projectileData;
        this.count = count;
        return this;
    }

    public ProjectileSpawnerBuilder withFireSpeed(double speed) {
        this.fireSpeed = speed;
        return this;
    }

    public ProjectileSpawnerBuilder withArcDegrees(double degrees) {
        this.arc = Math.toRadians(degrees);
        return this;
    }

    public ProjectileSpawnerBuilder withRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public ProjectileSpawner build() {
        return new ProjectileSpawner(projectile, count, fireSpeed, arc, radius);
    }
}
