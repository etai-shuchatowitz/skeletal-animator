import engine.GameEngine;
import engine.IGameLogic;
import engine.Window;
import game.InstanceAnimation;

public class Main {

    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new InstanceAnimation();
            Window.WindowOptions opts = new Window.WindowOptions();
            opts.showFps = true;
            opts.antialiasing = true;
            GameEngine gameEng = new GameEngine("GAME", vSync, opts, gameLogic);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}
