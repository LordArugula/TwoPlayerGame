package com.example.twoplayergame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameEntity implements Transform, Renderer, Collider {
    // transform
    private Vector2 position;
    private float rotation;
    // collider size (assuming box collider)
    private Vector2 size;
    private Vector2 halfSize;
    // renderer
    private Bitmap bitmap;

    public GameEntity(Vector2 position, float rotation, Bitmap bitmap) {
        this.position = position;
        this.rotation = rotation;
        this.size = new Vector2(bitmap.getWidth(), bitmap.getHeight());
        this.halfSize = Vector2.mul(size, 0.5);
        this.bitmap = bitmap;
    }

    @Override
    public void render(Canvas canvas) {
        if (rotation != 0f) {
            canvas.save();
            canvas.rotate(rotation, (float) position.x, (float) position.y);
            canvas.drawBitmap(bitmap, (float) (position.x - halfSize.x), (float) (position.y - halfSize.y), null);
            canvas.restore();
        } else {
            canvas.drawBitmap(bitmap, (float) (position.x - halfSize.x), (float) (position.y - halfSize.y), null);
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public Vector2 getSize() {
        return size;
    }

    @Override
    public Vector2 getHalfSize() {
        return halfSize;
    }

    @Override
    public Vector2 getMin() {
        return Vector2.sub(position, halfSize);
    }

    @Override
    public Vector2 getMax() {
        return Vector2.add(position, halfSize);
    }

    @Override
    public void setSize(Vector2 size) {
        this.size = size;
        this.halfSize = Vector2.mul(size, 0.5);
    }

    @Override
    public boolean checkCollision(Collider collider) {
        Vector2 minA = getMin();
        Vector2 maxA = getMax();
        Vector2 minB = collider.getMin();
        Vector2 maxB = collider.getMax();

        return maxA.x >= minB.x
                && maxB.x >= minA.x
                && maxA.y >= minB.y
                && maxB.y >= minA.y;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(float degrees) {
        this.rotation = degrees;
    }

    @Override
    public Vector2 getForward() {
        return Vector2.rotate(Vector2.forward(), Math.toRadians(rotation));
    }

    @Override
    public void move(Vector2 direction) {
        position = Vector2.add(position, direction);
    }
}

