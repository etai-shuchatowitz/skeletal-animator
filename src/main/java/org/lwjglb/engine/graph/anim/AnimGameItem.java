package org.lwjglb.engine.graph.anim;

import org.lwjglb.engine.Timer;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.items.GameItem;

import java.util.Map;
import java.util.Optional;

import static org.lwjgl.glfw.GLFW.*;

public class AnimGameItem extends GameItem {

    private static final float RUN_SPEED = 7f;
    private static final float TURN_SPEED = 1f;

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

    public void move(Window window) {
        checkInputs(window);
        super.increaseRotation(0, (currentTurnSpeed * Timer.getFrameTimeSeconds()), 0);
        float distance = currentSpeed * Timer.getFrameTimeSeconds();

        System.out.println("Y: " + super.getRotation().y());
        float dx = (float) (distance * Math.sin(super.getRotation().y));
        float dz = (float) (distance * Math.cos(super.getRotation().y));

        super.increasePosition(dx, 0, dz);

    }

    private void checkInputs(Window window) {
        if (window.isKeyPressed(GLFW_KEY_I)) {
            this.currentSpeed = RUN_SPEED;
        } else if (window.isKeyPressed(GLFW_KEY_K)) {
            this.currentSpeed = -1 * RUN_SPEED;
        } else {
            this.currentSpeed = 0;
        }
        if (window.isKeyPressed(GLFW_KEY_J)) {
            this.currentTurnSpeed = -1 * TURN_SPEED;
        } else if (window.isKeyPressed(GLFW_KEY_L)) {
            this.currentTurnSpeed = TURN_SPEED;
        } else {
            currentTurnSpeed = 0;
        }
    }
}
