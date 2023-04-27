package com.example.twoplayergame;

public class MainThread extends Thread {
    private final RequiresUpdate updatable;

    private boolean isRunning;

    public MainThread(RequiresUpdate updatable) {
        this.updatable = updatable;
    }

    @Override
    public synchronized void start() {
        super.start();
        isRunning = true;
    }

    @Override
    public void run() {
        final double ns = 1_000_000_000;

        long startTime = System.nanoTime();
        long lastTime = 0;

        while (isRunning) {
            long now = System.nanoTime();
            long elapsedTime = now - lastTime;

            updatable.update(elapsedTime / ns, (now - startTime) / ns);
            lastTime = now;
        }
    }
}
