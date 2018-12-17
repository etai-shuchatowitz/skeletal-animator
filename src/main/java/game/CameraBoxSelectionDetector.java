package game;

import engine.items.GameItem;
import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CameraBoxSelectionDetector {

    private final Vector3f max;

    private final Vector3f min;

    private final Vector2f nearFar;

    private Vector3f dir;

    public CameraBoxSelectionDetector() {
        dir = new Vector3f();
        min = new Vector3f();
        max = new Vector3f();
        nearFar = new Vector2f();
    }

    protected boolean selectGameItem(GameItem[] gameItems, Vector3f origin, Vector3f dir) {

        boolean selected = false;
        GameItem selectedGameItem = null;
        float closestDistance = Float.POSITIVE_INFINITY;
        for (GameItem gameItem : gameItems) {
            gameItem.setSelected(false);
            if (Intersectionf.intersectRaySphere(origin, dir, gameItem.getPosition(),
                    gameItem.getMesh().getBoundingRadius(), nearFar) && nearFar.x < closestDistance) {
                closestDistance = nearFar.x;
                selectedGameItem = gameItem;
            }
        }
        if (selectedGameItem != null) {
            selectedGameItem.setSelected(true);
            selected = true;
        }
        return selected;
    }
}
