package play;

import engine.display.Window;
import engine.render.GameEngine;
import engine.render.IGameLogic;

public class Main {

    /**
     * Initialises the engine and loads the scene. For every frame it updates the
     * camera, updates the animated entity (which updates the animation),
     * renders the scene to the screen, and then updates the display. When the
     * display is close the engine gets cleaned up.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new AnimationApp();
//            Window.WindowOptions opts = new Window.WindowOptions();
//            opts.cullFace = false;
//            opts.showFps = true;
//            opts.compatibleProfile = true;
//            opts.antialiasing = true;
//            opts.frustumCulling = false;
//            GameEngine gameEng = new GameEngine("GAME", vSync, opts, gameLogic);
//            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }

}
