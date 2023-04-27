package com.example.twoplayergame;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ProjectilePool {
    private final Stack<Projectile> pooledProjectiles;
    private final List<Projectile> activeProjectiles;

    public ProjectilePool() {
        pooledProjectiles = new Stack<>();
        activeProjectiles = new ArrayList<>();
    }

    public Projectile obtain() {
        Projectile projectile;
        if (pooledProjectiles.size() == 0) {
            projectile = new Projectile();
        } else {
            projectile = pooledProjectiles.pop();
        }
        activeProjectiles.add(projectile);
        return projectile;
    }

    public void release(int position) {
        Projectile projectile = activeProjectiles.get(position);

        int end = activeProjectiles.size() - 1;
        Projectile last = activeProjectiles.get(end);
        activeProjectiles.set(position, last);
        activeProjectiles.remove(end);

        pooledProjectiles.push(projectile);
    }

    public List<Projectile> getActive() {
        return activeProjectiles;
    }
}
