package org.lwjglb.engine;

public class Timer {

    private static double lastFrameTime;
    private static float delta;
    
    public static void init() {
        lastFrameTime = getCurrentTime();
    }

    /**
     * @return Current time in seconds
     */
    public static long getCurrentTime() {
        return System.nanoTime();
    }

    public static void update() {
        double currentFrameTime = getCurrentTime();

        System.out.println("currentFrameTime: " + currentFrameTime);
        System.out.println("lastFrameTime: " + lastFrameTime);
        delta = (float) (currentFrameTime - lastFrameTime) / 100000000;
        System.out.println("delta is: " + delta);
        lastFrameTime = currentFrameTime;
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }

    public static double getLastFrameTime() {
        return lastFrameTime;
    }
}