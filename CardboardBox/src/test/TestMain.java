package test;

import chunk.SimplexNoiseChunkSupplier;
import game.Camera;
import game.Input;
import game.Renderer;
import static game.Renderer.HEIGHT;
import static game.Renderer.WIDTH;
import game.World;
import java.io.File;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import static org.lwjgl.opengl.GL11.*;

public class TestMain {

    public static Renderer renderer;
    public static World world;

    public static void main(String[] args) {
        System.setProperty("org.lwjgl.librarypath", new File("native").getAbsolutePath());

        world = new World(new SimplexNoiseChunkSupplier());
        renderer = new Renderer(new Camera(new Vector3f(0, 0, 0)), world);

        renderer.initialize();
        world.initialize();
        loop();
        world.cleanup();
        renderer.cleanup();
    }

    private static void loop() {
        camera = new Cam();

        initInput(renderer.window);

        SimpleRectTex r = new SimpleRectTex(new Vector3f(0, -5, 0));
        SimpleRect r2 = new SimpleRect(new Vector3f(0, -5, 0));

        while (!glfwWindowShouldClose(renderer.window)) {
            glfwPollEvents();

            if (Input.isPressed("exit")) {
                glfwSetWindowShouldClose(renderer.window, true);
            }

            // Clear the color buffer
            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            if (chunk.Chunk.shaderProgram != null) {
                chunk.Chunk.shaderProgram.setUniform("worldMatrix", camera.getViewMatrix(new Vector3f(0, 0, 0)));
                chunk.Chunk.shaderProgram.setUniform("projectionMatrix", Cam.getProjectionMatrix(70, 640, 480, .1f, 1000));
            }

            renderer.draw();

            // Render
//            camera.position = new Vector3f(0, 0, -5);
            if (glfwGetKey(renderer.window, GLFW_KEY_W) != 0) {
                camera.position.add(.1f, 0, 0);
            }
            if (glfwGetKey(renderer.window, GLFW_KEY_S) != 0) {
                camera.position.add(-.1f, 0, 0);
            }
            if (glfwGetKey(renderer.window, GLFW_KEY_A) != 0) {
                camera.position.add(0, 0, .1f);
            }
            if (glfwGetKey(renderer.window, GLFW_KEY_D) != 0) {
                camera.position.add(0, 0, -.1f);
            }
            if (glfwGetKey(renderer.window, GLFW_KEY_SPACE) != 0) {
                camera.position.add(0, .1f, 0);
            }
            if (glfwGetKey(renderer.window, GLFW_KEY_LEFT_SHIFT) != 0) {
                camera.position.add(0, -.1f, 0);
            }

            r.draw();
            r2.draw();

            glfwSwapBuffers(renderer.window);
        }
    }

    public static Cam camera;
    private static GLFWCursorPosCallback cursorPosCallback;

    public static void initInput(long window) {
        glfwSetCursorPosCallback(window, cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                glfwSetCursorPos(window, WIDTH / 2, HEIGHT / 2);

                float dx = (float) xpos - WIDTH / 2;
                float dy = (float) ypos - HEIGHT / 2;

                camera.yaw -= dx / 500;
                camera.pitch -= dy / 500;

                if (camera.pitch > 1.5) {
                    camera.pitch = 1.5f;
                }
                if (camera.pitch < -1.5) {
                    camera.pitch = -1.5f;
                }
            }
        });
    }
}
