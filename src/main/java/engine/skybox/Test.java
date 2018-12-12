package engine.skybox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Test {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream isr = new FileInputStream(new File("src/main/java/engine/skybox/skyboxVertex.glsl"));
        System.out.println(isr);
    }
}
