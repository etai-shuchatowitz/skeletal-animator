package org.lwjglb.engine.graph.anim;

import org.lwjglb.engine.Timer;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.items.GameItem;

import java.util.Map;
import java.util.Optional;

public class AnimGameItem extends GameItem {

    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;

    private Map<String, Animation> animations;

    private Animation currentAnimation;

    public AnimGameItem(Mesh[] meshes, Map<String, Animation> animations) {
        super(meshes);
        this.animations = animations;
        Optional<Map.Entry<String, Animation>> entry = animations.entrySet().stream().findFirst();
        currentAnimation = entry.isPresent() ? entry.get().getValue() : null;
    }

    public Animation getAnimation(String name) {
        return animations.get(name);
    }

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(Animation currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

//    public void move() {
//        System.out.println("I am moving");
//        super.increaseRoation(0, (float) (currentTurnSpeed * Timer.getFrameTimeSeconds()), 0);
//        float distance = currentSpeed * (float) Timer.getFrameTimeSeconds();
//
//        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotation().y)));
//        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotation().y)));
//
//        super.setPosition(dx, 0, dz);
//
//    }

    public void moveForward() {
        this.currentSpeed = RUN_SPEED;
    }

    public void moveBackward() {
        this.currentSpeed = -1 * RUN_SPEED;
    }

    public void turnClockwise() {
        this.currentTurnSpeed = -1 * TURN_SPEED;
    }

    public void turnCounterClockwise() {
        this.currentTurnSpeed = TURN_SPEED;
    }

}
