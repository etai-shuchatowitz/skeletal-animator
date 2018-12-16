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

    public double getDuration() {
        return this.duration;        
    }
    
    public List<AnimatedFrame> getFrames() {
        return frames;
    }

    public String getName() {
        return name;
    }

    public AnimatedFrame getNextFrame() {
        nextFrame();
        return this.frames.get(currentFrame);
    }

    public void update() {
        increaseAnimationTime();
        Matrix4f[] currentPoses = calculateCurrentAnimationPose();
        frames.get(currentFrame).setInterpolatedJointMatrices(currentPoses);
        int nextFrame = currentFrame + 1;
        if (nextFrame > frames.size() - 1) {
            currentFrame = 0;
        } else {
            currentFrame = nextFrame;
        }
    }

    public void nextFrame() {
        int nextFrame = currentFrame + 1;
        if (nextFrame > frames.size() - 1) {
            currentFrame = 0;
        } else {
            currentFrame = nextFrame;
        }
    }

    /**
     * Increases the current animation time which allows the animation to
     * progress. If the current animation has reached the end then the timer is
     * reset, causing the animation to loop.
     */
    private void increaseAnimationTime() {
        animationTime += Timer.getCurrentTime();
        if (animationTime > duration) {
            this.animationTime %= duration;
        }
    }

    private Matrix4f[] calculateCurrentAnimationPose() {
        AnimatedFrame previousFrame = frames.get(currentFrame);
        AnimatedFrame nextFrame = frames.get(currentFrame+1);
        double progression = calculateProgression(previousFrame, nextFrame);
        return interpolatePoses(previousFrame, nextFrame, progression);
    }

    private double calculateProgression(AnimatedFrame previousFrame, AnimatedFrame nextFrame) {
        double totalTime = nextFrame.getTime() - previousFrame.getTime();
        double currentTime = animationTime - previousFrame.getTime();
        return currentTime / totalTime;
    }

    private Matrix4f[] interpolatePoses(AnimatedFrame prevFrame, AnimatedFrame nextFrame, double progression) {
        Matrix4f[] currentPoses = new Matrix4f[prevFrame.getJointMatrices().length];
        for (int i = 0; i < prevFrame.getJointMatrices().length; i++) {
            Matrix4f prevJointMatrix = prevFrame.getJointMatrices()[i];
            Matrix4f nextTransform = nextFrame.getJointMatrices()[i];
            Matrix4f currentTransform = prevJointMatrix.lerp(nextTransform, (float) progression);
            currentPoses[i] = currentTransform;
        }
        return currentPoses;
    }

}
