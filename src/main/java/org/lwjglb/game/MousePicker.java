package org.lwjglb.game;

import org.joml.*;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.graph.Camera;

public class MousePicker {

    private Vector3f currentRay;

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Camera camera;

    public MousePicker(Camera cam, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        this.camera = cam;
        this.projectionMatrix = projectionMatrix;
        this.viewMatrix = viewMatrix;
    }

    public void update(Matrix4f view, Vector2d mousePosition, Window window) {
        viewMatrix = view;
        currentRay = calculateMouseRay(mousePosition, window);
    }

    private Vector3f calculateMouseRay(Vector2d mousePosition, Window window) {
        float mouseX = (float) mousePosition.x;
        float mouseY = (float) mousePosition.y;

        Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY, window);
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
        Vector4f eyeCoords = toEyeCoords(clipCoords);
        Vector3f worldRay = toWorldCoords(eyeCoords);
        return worldRay;
    }

    private Vector4f toEyeCoords(Vector4f clipCoords) {
        Matrix4f invertedProjection = projectionMatrix.invert();
        Vector4f eyeCoords = invertedProjection.transform(clipCoords);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords) {
        Matrix4f invertedView = viewMatrix.invert();
        Vector4f rayWorld = invertedView.transform(eyeCoords);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z).normalize();
        return mouseRay;
    }

    private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY, Window window) {
        int wdwWitdh = window.getWidth();
        int wdwHeight = window.getHeight();

        float x = (2 * mouseX) / (float)wdwWitdh - 1.0f;
        float y = 1.0f - (2 * mouseY) / (float)wdwHeight;

        return new Vector2f(x, y);
    }

    public Vector3f getCurrentRay() {
        return currentRay;
    }


}
