package engine.scene;


import org.joml.Matrix4f;
import org.joml.Vector2d;

public interface ICamera {

    public Matrix4f getViewMatrix();
    public Matrix4f getProjectionMatrix();
    public Matrix4f getProjectionViewMatrix();
    public void move(Vector2d mousePosition, float elapsedTime);

}
