package game;

import chunk.SimplexNoiseChunkSupplier;
import engine.Entity;
import engine.GameLoop;
import engine.Window;
import static engine.Window.*;
import java.io.File;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class Main {

    private static GLFWCursorPosCallback cursorPosCallback;

    public static void main(String[] args) {
        System.setProperty("org.lwjgl.librarypath", new File("native").getAbsolutePath());

        Window.init();

        new World(new SimplexNoiseChunkSupplier(Math.random())).create();

        glfwSetCursorPosCallback(window, cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                glfwSetCursorPos(window, WIDTH / 2, HEIGHT / 2);

                float dx = (float) xpos - WIDTH / 2;
                float dy = (float) ypos - HEIGHT / 2;

                camera.horAngle -= dx / 500;
                camera.vertAngle += dy / 500;

                if (camera.vertAngle > 1.5) {
                    camera.vertAngle = 1.5f;
                }
                if (camera.vertAngle < -1.5) {
                    camera.vertAngle = -1.5f;
                }
            }
        });

        new Entity() {
            @Override
            public void update() {
                if (glfwGetKey(window, GLFW_KEY_ESCAPE) != 0) {
                    glfwSetWindowShouldClose(window, true);
                }
                float cameraSpeed = 0.8f;
                if (glfwGetKey(window, GLFW_KEY_W) != 0) {
                    Vector3fc forward = camera.facing();
                    Vector3f horizontalForward = forward.mul(cameraSpeed, new Vector3f());
                    horizontalForward.z = 0;
                    camera.position.add(horizontalForward);
                }
                if (glfwGetKey(window, GLFW_KEY_S) != 0) {
                    Vector3fc forward = camera.facing();
                    Vector3f horizontalForward = forward.mul(-cameraSpeed, new Vector3f());
                    horizontalForward.z = 0;
                    camera.position.add(horizontalForward);
                }
                if (glfwGetKey(window, GLFW_KEY_A) != 0) {
                    Vector3fc forward = camera.facing();
                    Vector3f horizontalForward = forward.mul(cameraSpeed, new Vector3f());
                    horizontalForward.z = 0;
                    horizontalForward.rotateAbout((float) Math.PI / 2, 0, 0, 1);
                    camera.position.add(horizontalForward);
                }
                if (glfwGetKey(window, GLFW_KEY_D) != 0) {
                    Vector3fc forward = camera.facing();
                    Vector3f horizontalForward = forward.mul(cameraSpeed, new Vector3f());
                    horizontalForward.z = 0;
                    horizontalForward.rotateAbout((float) -Math.PI / 2, 0, 0, 1);
                    camera.position.add(horizontalForward);
                }
                if (glfwGetKey(window, GLFW_KEY_SPACE) != 0) {
                    camera.position.add(0, 0, cameraSpeed);
                }
                if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) != 0) {
                    camera.position.add(0, 0, -cameraSpeed);
                }
            }
        }.create();

        GameLoop.run();
    }
}
