package com.example.twoplayergame;

public interface Transform {
    Vector2 getPosition();
    void setPosition(Vector2 position);
    float getRotation();
    void setRotation(float degrees);
    Vector2 getForward();
    void move(Vector2 direction);
}
