package com.example.twoplayergame;

public class ProjectilePoolProvider {
    private static ProjectilePool instance;

    public static void withInstance(ProjectilePool projectilePool) {
        instance = projectilePool;
    }

    public ProjectilePool getInstance() {
        return instance;
    }
}
