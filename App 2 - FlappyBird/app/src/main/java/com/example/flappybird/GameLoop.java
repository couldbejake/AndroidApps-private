package com.example.flappybird;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLoop extends Thread {

    // power ups.
    private static final double MAX_UPS = 70;
    private static final double UPS_PERIOD = 1E+3 / MAX_UPS;
    private final SurfaceHolder surfaceHolder;
    private final FlappyBird game;
    private boolean isRunning = false;
    private double averageUPS;
    private double averageFPS;

    /*

        Efficient game loop using TPS (ticks per second) and FPS (frames per second)

     */
    public GameLoop(FlappyBird game, SurfaceHolder surfaceHolder) {
        this.game = game;
        this.surfaceHolder = surfaceHolder;
    }

    public double getAverageUPS() {
        return averageUPS;
    }

    public double getAverageFPS() {
        return averageFPS;
    }

    public void startLoop() {
        isRunning = true;
        start();
    }

    @Override
    public void run() {
        super.run();

        // Declare timer and cycle count variables.

        int updateCount = 0;
        int frameCount = 0;

        long startTime;
        long elapsedTime;
        long sleepTime;

        Canvas canvas;

        startTime = System.currentTimeMillis();

        while (this.isRunning) {

            // try to update and render game

            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {

                    game.update();
                    updateCount++;

                    game.draw(canvas);

                }

                // could surround this with a try catch.

                surfaceHolder.unlockCanvasAndPost(canvas);
                frameCount++;

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

            elapsedTime = System.currentTimeMillis() - startTime;

            // pause game loop to not exceed target UPS

            sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);

            if (sleepTime > 0) {
                try {

                    sleep(sleepTime);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }

            // skip frames to keep up with target UPS

            while (sleepTime < 0) {

                game.update();

                updateCount++;
                elapsedTime = System.currentTimeMillis() - startTime;

                // pause game loop to not exceed target UPS

                sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);

            }

            // calculate average UPS and FPS

            if (elapsedTime >= 1000) {

                averageUPS = updateCount / (1E-3 * elapsedTime);
                averageFPS = frameCount / (1E-3 * elapsedTime);

                updateCount = 0;
                frameCount = 0;
                startTime = System.currentTimeMillis();

            }
        }
    }
}
