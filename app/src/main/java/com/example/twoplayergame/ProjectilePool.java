package com.example.twoplayergame;

import java.util.Stack;

public class ProjectilePool {
    public interface OnProjectileObtained {
        void onProjectileObtained(Projectile projectile);
    }

    private final Stack<Projectile> pooledProjectiles;
    private OnProjectileObtained onProjectileObtainedListener;

    public ProjectilePool(OnProjectileObtained onProjectileObtainedListener) {
        this.onProjectileObtainedListener = onProjectileObtainedListener;
        pooledProjectiles = new Stack<>();
    }

    public Projectile obtain() {
        if (pooledProjectiles.size() == 0) {
            Projectile projectile = new Projectile();
            onProjectileObtainedListener.onProjectileObtained(projectile);
            return projectile;
        }

        Projectile projectile = pooledProjectiles.pop();
        onProjectileObtainedListener.onProjectileObtained(projectile);
        return projectile;
    }

    public void release(Projectile projectile) {
        pooledProjectiles.push(projectile);
    }
}
