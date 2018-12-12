package engine.skybox;

import engine.shaders.ShaderProgram;
import engine.shaders.uniforms.UniformMatrix;
import engine.utils.MyFile;

public class SkyboxShader extends ShaderProgram {

    private static final MyFile VERTEX_SHADER = new MyFile("src/main/java/engine/skybox/skyboxVertex.glsl");
    private static final MyFile FRAGMENT_SHADER = new MyFile("src/main/java/engine/skybox/skyboxFragment.glsl");

    protected UniformMatrix projectionViewMatrix = new UniformMatrix("projectionViewMatrix");

    public SkyboxShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, "in_position");
        super.storeAllUniformLocations(projectionViewMatrix);
    }
}
