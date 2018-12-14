package org.lwjglb.engine;

import org.lwjglb.engine.graph.InstancedMesh;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.items.GameItem;
import org.lwjglb.engine.items.SkyBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {

    private final Map<Mesh, List<GameItem>> meshMap;

    private final Map<InstancedMesh, List<GameItem>> instancedMeshMap;

    private SkyBox skyBox;

    private SceneLight sceneLight;

    private boolean renderShadows;

    private List<GameItem> gameItems;

    public Scene() {
        meshMap = new HashMap();
        instancedMeshMap = new HashMap();
        renderShadows = true;
        gameItems = new ArrayList<>();
    }

    public Map<Mesh, List<GameItem>> getGameMeshes() {
        return meshMap;
    }

    public Map<InstancedMesh, List<GameItem>> getGameInstancedMeshes() {
        return instancedMeshMap;
    }

    public boolean isRenderShadows() {
        return renderShadows;
    }

    public void handleGameItems() {
        // Create a map of meshes to speed up rendering
        int numGameItems = gameItems != null ? gameItems.size() : 0;
        for (int i = 0; i < numGameItems; i++) {
            GameItem gameItem = gameItems.get(i);
            Mesh[] meshes = gameItem.getMeshes();
            for (Mesh mesh : meshes) {
                boolean instancedMesh = mesh instanceof InstancedMesh;
                List<GameItem> list = instancedMesh ? instancedMeshMap.get(mesh) : meshMap.get(mesh);
                if (list == null) {
                    list = new ArrayList<>();
                    if (instancedMesh) {
                        instancedMeshMap.put((InstancedMesh) mesh, list);
                    } else {
                        meshMap.put(mesh, list);
                    }
                }
                list.add(gameItem);
            }
        }
    }

    public List<GameItem> getGameItems() {
        return gameItems;
    }

    public void setGameItems(List<GameItem> gameItems) {
        this.gameItems = gameItems;
        handleGameItems();
    }

    public void cleanup() {
        for (Mesh mesh : meshMap.keySet()) {
            mesh.cleanUp();
        }
        for (Mesh mesh : instancedMeshMap.keySet()) {
            mesh.cleanUp();
        }
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }

    public void setRenderShadows(boolean renderShadows) {
        this.renderShadows = renderShadows;
    }

    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }

    public SceneLight getSceneLight() {
        return sceneLight;
    }

    public void setSceneLight(SceneLight sceneLight) {
        this.sceneLight = sceneLight;
    }

}
