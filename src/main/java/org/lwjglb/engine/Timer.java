package org.lwjglb.engine;

import java.math.BigDecimal;
import java.math.BigInteger;

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
        BigDecimal denom = new BigDecimal("1000000000");
        BigDecimal num = new BigDecimal(String.valueOf(currentFrameTime - lastFrameTime));
        delta = num.divide(denom).floatValue();
        lastFrameTime = currentFrameTime;
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }

    public static double getLastFrameTime() {
        return lastFrameTime;
    }
}