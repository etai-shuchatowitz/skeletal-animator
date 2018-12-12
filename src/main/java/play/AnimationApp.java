package play;

import anim.animation.Animation;
import anim.loader.AnimatedModelLoader;
import anim.loader.AnimationLoader;
import anim.model.AnimatedModel;
import engine.Timer;
import engine.display.MouseInput;
import engine.display.Window;
import engine.render.IGameLogic;
import engine.render.RenderEngine;
import engine.scene.Scene;
import engine.utils.MyFile;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class AnimationApp implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.40f;

    private final RenderEngine renderer;
    private final Vector3f cameraInc;
    private final Camera camera;
    private Scene scene;
    private float angleInc;

    private float lightAngle;

    private boolean firstTime;

    private boolean sceneChanged;

    public AnimationApp() {
        renderer = RenderEngine.init();
        camera = new Camera(0, 0);
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
    }

    @Override
    public void init(Window window) throws Exception {
        AnimatedModel entity = AnimatedModelLoader.loadEntity(new MyFile(GeneralSettings.MODEL_FILE),
                new MyFile(GeneralSettings.DIFFUSE_FILE));
        List<AnimatedModel> animatedModels = new ArrayList<>();
        animatedModels.add(entity);
        Animation animation = AnimationLoader.loadAnimation(new MyFile(GeneralSettings.ANIM_FILE));
        entity.doAnimation(animation);
        scene = new Scene(animatedModels, camera);
        scene.setLightDirection(GeneralSettings.LIGHT_DIR);
    }

    @Override
    public void input(Window window, MouseInput mouseInput, Timer timer) {
        sceneChanged = false;
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            sceneChanged = true;
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            sceneChanged = true;
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            sceneChanged = true;
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            sceneChanged = true;
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            sceneChanged = true;
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            sceneChanged = true;
            cameraInc.y = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            sceneChanged = true;
            angleInc -= 0.05f;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            sceneChanged = true;
            angleInc += 0.05f;
        } else {
            angleInc = 0;
        }
        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            sceneChanged = true;
            for(AnimatedModel animatedModel : scene.getAnimatedModels()) {
                animatedModel.update(timer);
            }
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput, Window window, Timer timer) {
        if (mouseInput.isRightButtonPressed()) {
            camera.move(mouseInput.getCurrentPos(), timer.getElapsedTime());
        }
    }

    @Override
    public void render(Window window) {
        if (firstTime) {
            sceneChanged = true;
            firstTime = false;
        }
        renderer.renderScene(scene);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
    }
}
