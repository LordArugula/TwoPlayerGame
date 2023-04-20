package com.example.twoplayergame;

public interface Collider {
     Vector2 getSize();
     Vector2 getHalfSize();
     void setSize(Vector2 size);
     Vector2 getMin();
     Vector2 getMax();
     boolean checkCollision(Collider collider);

}
