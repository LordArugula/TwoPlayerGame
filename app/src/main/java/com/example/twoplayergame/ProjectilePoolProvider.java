package com.example.twoplayergame;

public class ProjectilePoolProvider {
    private static ProjectilePool instance;

    public void setInstance(ProjectilePool instance) {
        ProjectilePoolProvider.instance = instance;
    }

    public ProjectilePool getInstance() {
        return instance;
    }
}
