package play;

import anim.loader.AnimatedModelLoader;
import anim.model.AnimatedModel;
import engine.scene.ICamera;
import engine.scene.Scene;
import engine.utils.MyFile;

import java.util.ArrayList;
import java.util.List;

public class SceneLoader {

    /**
     * Sets up the scene. Loads the entity, load the animation, tells the entity
     * to do the animation, sets the light direction, creates the camera, etc...
     *
     * @return The entire scene.
     */
    public static Scene loadScene() {
        ICamera camera = new Camera();
        AnimatedModel entity = AnimatedModelLoader.loadEntity(new MyFile(GeneralSettings.MODEL_FILE),
                new MyFile(GeneralSettings.DIFFUSE_FILE));
        List<AnimatedModel> animatedModels = new ArrayList<>();
        animatedModels.add(entity);
//        Animation animation = AnimationLoader.loadAnimation(new MyFile(GeneralSettings.ANIM_FILE));
//        entity.doAnimation(animation);
        Scene scene = new Scene(animatedModels, camera);
        scene.setLightDirection(GeneralSettings.LIGHT_DIR);
        return scene;
    }

}
