package com.example.twoplayergame;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

public class Joystick {
    public interface OnJoystickMoveListener {
        void onJoystickMove(Vector2 movement);
    }

    private float joystickWidth;
    private float joyStickHeight;

    private boolean isActive;
    private OnJoystickMoveListener listener;
    private final View joystick;

    @SuppressLint("ClickableViewAccessibility")
    public Joystick(View joystickView, OnJoystickMoveListener listener) {
        this.listener = listener;
        joystick = joystickView.findViewById(R.id.joystick_handle);
        joystick.addOnLayoutChangeListener(this::onHandleLayoutChange);

        joystickView.setOnTouchListener((this::onTouch));
    }

    private void onHandleLayoutChange(View view, int left, int top, int right, int bottom, int i4, int i5, int i6, int i7) {
        joystickWidth = view.getWidth();
        joyStickHeight = view.getHeight();
    }

    private boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                isActive = true;
                handleJoystickMotion(view, motionEvent);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (isActive) {
                    handleJoystickMotion(view, motionEvent);
                }
                break;
            }
            case MotionEvent.ACTION_UP:
                if (isActive) {
                    float centerX = (view.getWidth() - joystickWidth) / 2f;
                    float centerY = (view.getHeight() - joyStickHeight) / 2f;
                    joystick.setX(centerX);
                    joystick.setY(centerY);
                    isActive = false;

                    listener.onJoystickMove(Vector2.zero());
                }
                break;
            default:
                return false;
        }
        return true;
    }

    private void handleJoystickMotion(View view, MotionEvent motionEvent) {
        float posX = motionEvent.getX();
        float posY = motionEvent.getY();

        float centerX = view.getWidth() / 2f;
        float centerY = view.getHeight() / 2f;

        float dx = posX - centerX;
        float dy = posY - centerY;

        float distSq = dx * dx + dy * dy;
        float radius = centerX - (joystickWidth / 2);
        float radiusSq = radius * radius;

        if (distSq > radiusSq) {
            float scalar = radius / (float) Math.sqrt(distSq);
            dx *= scalar;
            dy *= scalar;
            posX = centerX + dx;
            posY = centerY + dy;
        }

        joystick.setX(posX - (joystickWidth / 2));
        joystick.setY(posY - (joyStickHeight / 2));

        float deadZoneRadius = 0.25f;
        if (distSq < deadZoneRadius * deadZoneRadius) {
            listener.onJoystickMove(Vector2.zero());
        } else {
            listener.onJoystickMove(new Vector2(dx / radius, dy / radius));
        }
    }
}
