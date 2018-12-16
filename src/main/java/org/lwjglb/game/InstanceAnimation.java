package org.lwjglb.game;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjglb.engine.*;
import org.lwjglb.engine.graph.Camera;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.Renderer;
import org.lwjglb.engine.graph.anim.AnimGameItem;
import org.lwjglb.engine.graph.anim.Animation;
import org.lwjglb.engine.graph.lights.DirectionalLight;
import org.lwjglb.engine.items.GameItem;
import org.lwjglb.engine.items.SkyBox;
import org.lwjglb.engine.loaders.assimp.AnimMeshesLoader;
import org.lwjglb.engine.loaders.assimp.StaticMeshesLoader;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class InstanceAnimation implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private Scene scene;

    private static final float CAMERA_POS_STEP = 0.40f;

    private float angleInc;

    private float lightAngle;

    private boolean firstTime;

    private boolean sceneChanged;

    private MouseBoxSelectionDetector selectDetector;

    private List<Animation> animations;

    public InstanceAnimation() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        angleInc = 0;
        lightAngle = 90;
        firstTime = true;
        animations = new ArrayList<>();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        scene = new Scene();

        Mesh[] terrainMesh = StaticMeshesLoader.load("src/main/resources/models/terrain/terrain.obj", "src/main/resources/models/terrain");
        GameItem terrain = new GameItem(terrainMesh);
        terrain.setScale(100.0f);

        List<GameItem> gameItemList = new ArrayList<>();
        gameItemList.add(terrain);
        
        scene.setGameItems(gameItemList);

        // Shadows
        scene.setRenderShadows(true);

        selectDetector = new MouseBoxSelectionDetector();

        // Setup  SkyBox
        float skyBoxScale = 100.0f;
        SkyBox skyBox = new SkyBox("src/main/resources/models/skybox.obj", new Vector4f(0.65f, 0.65f, 0.65f, 1.0f));
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);

        // Setup Lights
        setupLights();

        camera.getPosition().x = -1.5f;
        camera.getPosition().y = 3.0f;
        camera.getPosition().z = 4.5f;
        camera.getRotation().x = 15.0f;
        camera.getRotation().y = 390.0f;
    }

    private void setupLights() {
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));
        sceneLight.setSkyBoxLight(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional Light
        float lightIntensity = 1.0f;
        Vector3f lightDirection = new Vector3f(0, 1, 1);
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightDirection, lightIntensity);
        sceneLight.setDirectionalLight(directionalLight);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) throws Exception {
        sceneChanged = false;
        cameraInc.set(0, 0, 0);
        if(window.isKeyPressed(GLFW_KEY_0)) {
            AnimGameItem animItem = AnimMeshesLoader.loadAnimGameItem("src/main/resources/models/model.dae", "");
            animations.add(animItem.getCurrentAnimation());
            animItem.setPosition(10, 0, 10);
            List<GameItem> gameItems = scene.getGameItems();
            gameItems.add(animItem);
            scene.setGameItems(gameItems);
        }
        if(window.isKeyPressed(GLFW_KEY_1)) {
            AnimGameItem animGameItem = AnimMeshesLoader.loadAnimGameItem("src/main/resources/models/bob/boblamp.md5mesh", "");
            animations.add(animGameItem.getCurrentAnimation());
            animGameItem.setScale(0.2f);
            List<GameItem> gameItems = scene.getGameItems();
            gameItems.add(animGameItem);
            scene.setGameItems(gameItems);
        }
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
            for (int i = 0; i < scene.getGameItems().size(); i++) {
                GameItem gameItem = scene.getGameItems().get(i);
                if(gameItem.isSelected()) {
                    animations.get(i).nextFrame();
                }
            }

        }
        if (window.isKeyPressed(GLFW_KEY_I)) {
            sceneChanged = true;
            for (int i = 0; i < scene.getGameItems().size(); i++) {
                GameItem gameItem = scene.getGameItems().get(i);
                if(gameItem instanceof AnimGameItem) {
                    AnimGameItem animGameItem = (AnimGameItem) gameItem;
                    animGameItem.moveForward();
                }
            }
        } else if (window.isKeyPressed(GLFW_KEY_K)) {
            sceneChanged = true;
            for (int i = 0; i < scene.getGameItems().size(); i++) {
                GameItem gameItem = scene.getGameItems().get(i);
                if(gameItem instanceof AnimGameItem) {
                    AnimGameItem animGameItem = (AnimGameItem) gameItem;
                    animGameItem.moveBackward();
                }
            }
        }
        if (window.isKeyPressed(GLFW_KEY_J)) {
            sceneChanged = true;
            for (int i = 0; i < scene.getGameItems().size(); i++) {
                GameItem gameItem = scene.getGameItems().get(i);
                if(gameItem instanceof AnimGameItem) {
                    AnimGameItem animGameItem = (AnimGameItem) gameItem;
                    animGameItem.turnClockwise();
                }
            }
        } else if (window.isKeyPressed(GLFW_KEY_L)) {
            sceneChanged = true;
            for (int i = 0; i < scene.getGameItems().size(); i++) {
                GameItem gameItem = scene.getGameItems().get(i);
                if(gameItem instanceof AnimGameItem) {
                    AnimGameItem animGameItem = (AnimGameItem) gameItem;
                    animGameItem.turnCounterClockwise();
                }
            }
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput, Window window) {
        if (mouseInput.isRightButtonPressed()) {
            // Update camera based on mouse            
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
            sceneChanged = true;
        }

        if(mouseInput.isLeftButtonPressed()) {
            GameItem[] gameItems = new GameItem[scene.getGameItems().size()];
            gameItems = scene.getGameItems().toArray(gameItems);
            selectDetector.selectGameItem(gameItems, window, mouseInput.getCurrentPos(), camera);
        }

        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        lightAngle += angleInc;
        if (lightAngle < 0) {
            lightAngle = 0;
        } else if (lightAngle > 180) {
            lightAngle = 180;
        }
        float zValue = (float) Math.cos(Math.toRadians(lightAngle));
        float yValue = (float) Math.sin(Math.toRadians(lightAngle));
        Vector3f lightDirection = this.scene.getSceneLight().getDirectionalLight().getDirection();
        lightDirection.x = 0;
        lightDirection.y = yValue;
        lightDirection.z = zValue;
        lightDirection.normalize();

//        if (animations.size() != 0) {
//            for (Animation animation : animations) {
//                animation.nextFrame();
//            }
//        }

//        for (int i = 0; i < scene.getGameItems().size(); i++) {
//            GameItem gameItem = scene.getGameItems().get(i);
//            if(gameItem instanceof AnimGameItem) {
//                AnimGameItem animGameItem = (AnimGameItem) gameItem;
//                animGameItem.move();
//            }
//        }

        // Update view matrix
        camera.updateViewMatrix();
    }

    @Override
    public void render(Window window) {
        if (firstTime) {
            sceneChanged = true;
            firstTime = false;
        }
        renderer.render(window, camera, scene, sceneChanged);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();

        scene.cleanup();
    }
}