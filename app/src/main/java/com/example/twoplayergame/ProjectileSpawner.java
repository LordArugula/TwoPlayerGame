package com.example.twoplayergame;

public class ProjectileSpawner {
    private ProjectileData projectileData;
    private int projectileCount;

    private float rotation;
    private float arc;
    private double radius;
    private double fireRate;

    private double timer;

    private boolean isActive;

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getArc() {
        return arc;
    }

    public void setArc(float radians) {
        this.arc = radians;
    }

    public double getFireRate() {
        return fireRate;
    }

    public void setFireRate(double fireRate) {
        this.fireRate = fireRate;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getProjectileCount() {
        return projectileCount;
    }

    public void setProjectileCount(int projectileCount) {
        this.projectileCount = projectileCount;
    }

    public ProjectileSpawner(ProjectileData projectileData, int projectileCount, double fireSpeed, float arc, double radius) {
        this.projectileData = projectileData;
        this.projectileCount = projectileCount;

        this.fireRate = 1.0 / fireSpeed;
        this.arc = arc;
        this.radius = radius;
    }

    public void updateTimer(double deltaTime) {
        timer += deltaTime;
    }

    public void resetTimer() {
        timer = 0;
    }

    public boolean canFireProjectiles() {
        return timer >= fireRate && isActive;
    }

    public ProjectileData getProjectileData() {
        return projectileData;
    }

    public void setProjectileData(ProjectileData projectileData) {
        this.projectileData = projectileData;
    }

    public void spawnProjectiles(Character character, ProjectilePool pool) {
        Vector2 position = character.getPosition();
        Vector2 forward = character.getForward();
        float rotation = (float) Math.toRadians(character.getRotation() + 180);
        float rotationOffset = (float) Math.toRadians(this.rotation);

        float angleSpread = arc / projectileCount;
        float PI = (float) Math.PI;
        float startAngle = ((angleSpread / 2) - (arc / 2)) + PI;

        for (int i = 0; i < projectileCount; i++) {
            Projectile projectile = pool.obtain();
            projectile.setOwner(character);
            projectile.setDamage(projectileData.getDamage());

            projectile.setSize(projectileData.getSize());
            projectile.setBitmap(projectileData.getBitmap());

            float projectileRotation = startAngle + angleSpread * i + rotationOffset;
            projectile.setRotation((float) Math.toDegrees(rotation + projectileRotation));

            Vector2 projectileDirection = Vector2.rotate(forward, projectileRotation);
            projectile.setMovementDirection(projectileDirection);
            projectile.setMovementSpeed(projectileData.getSpeed());

            Vector2 projectilePosition = Vector2.add(position, Vector2.mul(projectileDirection, radius));
            projectile.setPosition(projectilePosition);
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
