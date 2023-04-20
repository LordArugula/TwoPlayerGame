package com.example.twoplayergame;

public class MainThread extends Thread {
    private RequiresUpdate updatable;

    private int frameCount;

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
//        final double targetFrameRate = 50.0;
//        final double targetFrameTime = 1 / targetFrameRate;
        final double ns = 1_000_000_000;

        long startTime = System.nanoTime();
        long lastTime = 0;

        while (isRunning) {
            long now = System.nanoTime();
            long elapsedTime = now - lastTime;
            double dt = elapsedTime / ns;

            updatable.update(elapsedTime / ns, (now - startTime) / ns);
            frameCount++;
            lastTime = now;
        }
    }

    public void quit() {
        isRunning = false;
    }

    public void pause() {
    }
}
