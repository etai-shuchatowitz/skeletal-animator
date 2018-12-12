package engine.render;

import anim.render.AnimatedModelRenderer;
import engine.display.Window;
import engine.scene.Scene;
import engine.skybox.SkyboxRenderer;

/**
 * This class represents the entire render engine.
 *
 * @author Karl
 *
 */
public class RenderEngine {

    private MasterRenderer renderer;

    private RenderEngine(MasterRenderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Updates the display.
     */
    public void update(Window window) {
        window.update();
    }

    /**
     * Renders the scene to the screen.
     *
     * @param scene
     *            - the game scene.
     */
    public void renderScene(Scene scene) {
        renderer.renderScene(scene);
    }

    /**
     * Cleans up the renderers and closes the display.
     */
    public void close(Window window) {
        renderer.cleanUp();
        window.windowShouldClose();
    }

    /**
     * Initializes a new render engine. Creates the display and inits the
     * renderers.
     *
     * @return
     */
    public static RenderEngine init() {
        SkyboxRenderer skyRenderer = new SkyboxRenderer();
        AnimatedModelRenderer entityRenderer = new AnimatedModelRenderer();
        MasterRenderer renderer = new MasterRenderer(entityRenderer, skyRenderer);
        return new RenderEngine(renderer);
    }

    /**
     * Clean up when the game is closed.
     */
    public void cleanup() {
        renderer.cleanUp();
    }

}
