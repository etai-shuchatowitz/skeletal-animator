package engine.render;

import engine.Timer;
import engine.display.MouseInput;
import engine.display.Window;

public interface IGameLogic {

    void init(Window window) throws Exception;

    void input(Window window, MouseInput mouseInput, Timer timer);

    void update(float interval, MouseInput mouseInput, Window window, Timer timer);

    void render(Window window);

    void cleanup();
}
