package play;

import anim.model.AnimatedModel;
import engine.display.Window;
import engine.render.RenderEngine;
import engine.scene.Scene;

public class AnimationApp {

    /**
     * Initialises the engine and loads the scene. For every frame it updates the
     * camera, updates the animated entity (which updates the animation),
     * renders the scene to the screen, and then updates the display. When the
     * display is close the engine gets cleaned up.
     *
     * @param args
     */
    public static void main(String[] args) {

        RenderEngine engine = RenderEngine.init();

        Window.WindowOptions options = new Window.WindowOptions();
        Scene scene = SceneLoader.loadScene(options);

        while (!Window.isCloseRequested()) {
            scene.getCamera().move();
            for(AnimatedModel animatedModel : scene.getAnimatedModels()) {
                animatedModel.update();
            }
            engine.renderScene(scene);
            engine.update();
        }

        engine.close();

    }

}
