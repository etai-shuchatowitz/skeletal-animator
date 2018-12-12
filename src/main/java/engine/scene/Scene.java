package engine.scene;

import anim.model.AnimatedModel;
import org.joml.Vector3f;

import java.util.List;

/**
 * Represents all the stuff in the scene (just the camera, light, and model
 * really).
 *
 * @author Karl
 *
 */
public class Scene {

    private final ICamera camera;

    private List<AnimatedModel> animatedModels;

    private Vector3f lightDirection = new Vector3f(0, -1, 0);

    public Scene(List<AnimatedModel> models, ICamera cam) {
        this.animatedModels = models;
        this.camera = cam;
    }

    /**
     * @return The scene's camera.
     */
    public ICamera getCamera() {
        return camera;
    }

    public List<AnimatedModel> getAnimatedModels() {
        return animatedModels;
    }

    public void addAnimatedModel(AnimatedModel animatedModel) {
        animatedModels.add(animatedModel);
    }

    /**
     * @return The direction of the light as a vector.
     */
    public Vector3f getLightDirection() {
        return lightDirection;
    }

    public void setLightDirection(Vector3f lightDir) {
        this.lightDirection.set(lightDir);
    }

}
