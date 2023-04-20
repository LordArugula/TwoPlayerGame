package com.example.twoplayergame;

import android.security.identity.IdentityCredentialException;

public class Vector2 {
    public final double x;
    public final double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    private final static Vector2 forward = new Vector2(0, 1);
    private final static Vector2 zero = new Vector2(0, 0);

    public static Vector2 forward() {
        return forward;
    }

    public static Vector2 zero() {
        return zero;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public double magnitudeSq() {
        return x * x + y * y;
    }

    public Vector2 normalize() {
        double magnitude = magnitude();
        return new Vector2(x / magnitude, y / magnitude);
    }

    public static Vector2 rotate(Vector2 v, double radians) {
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        return new Vector2(v.x * cos - v.y * sin, v.x * sin + v.y * cos);
    }

    public static Vector2 add(Vector2 a, Vector2 b) {
        return new Vector2(a.x + b.x, a.y + b.y);
    }

    public static Vector2 sub(Vector2 a, Vector2 b) {
        return new Vector2(a.x - b.x, a.y - b.y);
    }

    public static Vector2 mul(Vector2 a, Vector2 b) {
        return new Vector2(a.x * b.x, a.y * b.y);
    }

    public static Vector2 mul(Vector2 a, double v) {
        return new Vector2(a.x * v, a.y * v);
    }

    public static Vector2 div(Vector2 a, Vector2 b) {
        return new Vector2(a.x / b.x, a.y / b.y);
    }

    public static double distance(Vector2 a, Vector2 b) {
        return sub(a, b).magnitude();
    }

    public static double distanceSq(Vector2 a, Vector2 b) {
        return sub(a, b).magnitudeSq();
    }
}
