package game;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import java.io.File;

import org.joml.Vector3f;

import chunk.SimplexNoiseChunkSupplier;

public class Main {

    public static Renderer renderer;
    public static World world;
    public static Camera camera;

    public static void main(String[] args) {    	
        System.setProperty("org.lwjgl.librarypath", new File("native").getAbsolutePath());
        
        world = new World(new SimplexNoiseChunkSupplier());
        camera = new Camera(new Vector3f(0, 0, 0));
        renderer = new Renderer(camera, world);
        
        renderer.initialize();
        world.initialize();
        loop();
        world.cleanup();
        renderer.cleanup();
    }

    private static void loop() {
        while (!glfwWindowShouldClose(renderer.window)) {
            glfwPollEvents();
            
            if (Input.isPressed("exit")) {
            	glfwSetWindowShouldClose(renderer.window, true);
            }

            renderer.draw();
            glfwSwapBuffers(renderer.window);
        }
    }
}
