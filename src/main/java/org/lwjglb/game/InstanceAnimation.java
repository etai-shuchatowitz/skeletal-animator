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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    private List<Animation> animations;

    private MouseBoxSelectionDetector mouseBoxSelectionDetector;

    public InstanceAnimation() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        angleInc = 0;
        lightAngle = 90;
        firstTime = true;
        animations = new ArrayList<>();
        mouseBoxSelectionDetector = new MouseBoxSelectionDetector();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        scene = new Scene();

        Mesh[] terrainMesh = StaticMeshesLoader.load("src/main/resources/models/terrain/terrain.obj", "src/main/resources/models/terrain");
        GameItem terrain = new GameItem(terrainMesh);
        terrain.setName("Terrain");
        terrain.setScale(100.0f);

        Mesh[] houseMesh = StaticMeshesLoader.load("src/main/resources/models/house/house.obj", "src/main/resources/models/house");
        GameItem house = new GameItem(houseMesh);
        house.setName("house");
        house.setPosition(-10, 0, -10);


        List<GameItem> gameItemList = new ArrayList<>();
        gameItemList.add(terrain);
        gameItemList.add(house);

        scene.setGameItems(gameItemList);

        // Shadows
        scene.setRenderShadows(true);

        // Setup  SkyBox
        float skyBoxScale = 100.0f;
        SkyBox skyBox = new SkyBox("src/main/resources/models/skybox.obj", new Vector4f(0.65f, 0.65f, 0.65f, 1.0f));
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);

        // Setup Lights
        setupLights();

        camera.getPosition().x = -1.5f;
        camera.getPosition().y = 7.0f;
        camera.getPosition().z = 15f;
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
        if (window.isKeyPressed(GLFW_KEY_0)) {
            AnimGameItem animItem = AnimMeshesLoader.loadAnimGameItem("src/main/resources/models/model.dae", "");
            animations.add(animItem.getCurrentAnimation());
            animItem.setPosition(10, 0, 0);
            animItem.setName("cowboy");
            System.out.println("There are " + animItem.getMeshes().length + " meshes in cowboy");
            List<GameItem> gameItems = scene.getGameItems();
            gameItems.add(animItem);
            scene.setGameItems(gameItems);

            scene.addToSelectableGameItems(animItem);
        }
        if (window.isKeyPressed(GLFW_KEY_1)) {
            AnimGameItem animGameItem = AnimMeshesLoader.loadAnimGameItem("src/main/resources/models/bob/boblamp.md5mesh", "");
            animations.add(animGameItem.getCurrentAnimation());
            animGameItem.setScale(0.2f);
            animGameItem.setPosition(30, 0, 30);
            animGameItem.setName("bob");
            List<GameItem> gameItems = scene.getGameItems();
            gameItems.add(animGameItem);
            scene.setGameItems(gameItems);

            scene.addToSelectableGameItems(animGameItem);
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
            for (int i = 0; i < animations.size(); i++) {
                if (animations.get(i).getFrames().size() < 50) {
                    animations.get(i).update();
                }
            }
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput, Window window) {

        for(int i = 0; i < scene.getSelectableGameItems().size(); i++) {
            if(scene.getSelectableGameItems().get(i).getName().equalsIgnoreCase("bob")) {
                animations.get(i).nextFrame();
            } else if (scene.getSelectableGameItems().get(i).getName().equalsIgnoreCase("cowboy")) {
                if(scene.getSelectableGameItems().get(i) instanceof AnimGameItem) {
                    AnimGameItem animGameItem = (AnimGameItem) scene.getSelectableGameItems().get(i);
                    List<GameItem> otherItems = scene.getGameItems();
                    otherItems.remove(animGameItem);
                    animGameItem.move(window, otherItems);
                }
            }
        }

        GameItem[] gameItems = new GameItem[scene.getSelectableGameItems().size()];
        gameItems = scene.getSelectableGameItems().toArray(gameItems);

        mouseBoxSelectionDetector.selectGameItem(gameItems, window, mouseInput.getCurrentPos(), camera);

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

        if (mouseInput.isRightButtonPressed()) {
            // Update camera based on mouse
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
            sceneChanged = true;
        } else if (mouseInput.isLeftButtonPressed()) {
//            for (Map.Entry<Mesh, List<GameItem>> entry : scene.getGameMeshes().entrySet()) {
//                List<GameItem> tempGameItemList = new ArrayList<>();
//                List<GameItem> tempSelectable = new ArrayList<>();
//                for (GameItem gameItem : entry.getValue()) {
//                    if(!gameItem.isSelected()) {
//                        tempGameItemList.add(gameItem);
//                        if(!gameItem.getName().equalsIgnoreCase("terrain")) {
//                            tempSelectable.add(gameItem);
//                        }
//                    }
//                }
//                scene.getGameMeshes().put(entry.getKey(), tempGameItemList);
//                scene.setGameItems(tempGameItemList);
//                scene.setSelectableGameItems(tempSelectable);
//            }
//            sceneChanged = true;
        }

        for (GameItem gameItem : scene.getGameItems()) {
            System.out.println(gameItem.getName());
        }

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
