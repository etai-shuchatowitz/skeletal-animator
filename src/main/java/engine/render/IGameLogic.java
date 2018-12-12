package engine.render;

import engine.display.MouseInput;
import engine.display.Window;
import engine.scene.ICamera;

public interface IGameLogic {

    void init(Window window, ICamera camera) throws Exception;

    void input(Window window, MouseInput mouseInput, ICamera camera);

    void update(float interval, MouseInput mouseInput, Window window, ICamera camera);

    void render(Window window, ICamera camera);

    void cleanup();
}
