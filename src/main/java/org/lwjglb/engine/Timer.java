package org.lwjglb.engine;

public class Timer {

    private static double lastFrameTime;
    
    public static void init() {
        lastFrameTime = getTime();
    }

    /**
     * @return Current time in seconds
     */
    public static double getTime() {
        return System.nanoTime() / 1000_000_000.0;
    }

    public static float getElapsedTime() {
        double time = getTime();
        float elapsedTime = (float) (time - lastFrameTime);
        lastFrameTime = time;
        return elapsedTime;
    }

    public static double getLastFrameTime() {
        return lastFrameTime;
    }
}