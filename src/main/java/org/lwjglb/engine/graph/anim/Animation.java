package org.lwjglb.engine.graph.anim;

import org.joml.Matrix4f;
import org.lwjglb.engine.Timer;

import java.util.List;

public class Animation {

    private int currentFrame;

    private List<AnimatedFrame> frames;

    private String name;
    
    private double duration;

    private double animationTime = 0;

    public Animation(String name, List<AnimatedFrame> frames, double duration) {
        this.name = name;
        this.frames = frames;
        currentFrame = 0;
        this.duration = duration;
    }

    public AnimatedFrame getCurrentFrame() {
        return this.frames.get(currentFrame);
    }
    
    public List<AnimatedFrame> getFrames() {
        return frames;
    }

    public String getName() {
        return name;
    }

    public void update() {
        increaseAnimationTime();
        calculateCurrentAnimationPose();
    }

    /**
     * Increases the current animation time which allows the animation to
     * progress. If the current animation has reached the end then the timer is
     * reset, causing the animation to loop.
     */
    private void increaseAnimationTime() {
        animationTime += Timer.getFrameTimeSeconds();
        if (animationTime > duration) {
            this.animationTime %= duration;
        }
    }

    private Matrix4f[] calculateCurrentAnimationPose() {
        AnimatedFrame[] currentFrames = getPreviousAndNextFrames();
        System.out.println("Interpolating between " + currentFrames[0].getTime() + " and " + currentFrames[1].getTime());
        double progression = calculateProgression(currentFrames[0], currentFrames[1]);
        return interpolatePoses(currentFrames[0], currentFrames[1], progression);
    }

    private AnimatedFrame[] getPreviousAndNextFrames() {
        AnimatedFrame previousFrame = frames.get(0);
        AnimatedFrame nextFrame = frames.get(0);
        for (int i = 1; i < frames.size(); i++) {
            nextFrame = frames.get(i);
            if (nextFrame.getTime() > animationTime) {
                break;
            }
            previousFrame = frames.get(i);
        }
        return new AnimatedFrame[] { previousFrame, nextFrame };
    }

    private double calculateProgression(AnimatedFrame previousFrame, AnimatedFrame nextFrame) {
        double totalTime = nextFrame.getTime() - previousFrame.getTime();
        double currentTime = animationTime - previousFrame.getTime();
        return currentTime / totalTime;
    }

    private Matrix4f[] interpolatePoses(AnimatedFrame prevFrame, AnimatedFrame nextFrame, double progression) {
        Matrix4f[] currentPose = new Matrix4f[prevFrame.getJointMatrices().length];
        for (int i = 0; i < prevFrame.getJointMatrices().length; i++) {
            Matrix4f prevJointMatrix = prevFrame.getJointMatrices()[i];
            Matrix4f nextTransform = nextFrame.getJointMatrices()[i];
            Matrix4f currentTransform = new Matrix4f();
            prevJointMatrix.lerp(nextTransform, (float) progression, currentTransform);
            currentPose[i] = currentTransform;

            if(prevJointMatrix.m00() != 1) {
                System.out.println("Prev Joint Matrix: " + prevJointMatrix);
                System.out.println("Next Joint Matrix : " + nextTransform);

                System.out.println("Current transform: " + currentTransform);
                System.out.println("Progression is: " + progression);
            }

            frames.get(currentFrame).updateInterpolatedJointMatrix(i, currentTransform);

//            System.out.println("Prev frame joint matrix is: " + frames.get(currentFrame).getJointMatrices()[i]);
        }
        return currentPose;
    }

}
