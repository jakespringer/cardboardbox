package engine;

import chunk.Chunk;
import static engine.Window.window;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GameLoop {

    public static void run() {
        while (!glfwWindowShouldClose(window)) {

            glfwPollEvents();

            Entity.updateAll();

            // Clear the color buffer
            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            if (Chunk.shaderProgram != null) {
                Chunk.shaderProgram.setUniform("projectionMatrix", Camera.getProjectionMatrix(70, 640, 480, .1f, 1000));
            }

            // Render
            Entity.drawAll();

            glfwSwapBuffers(window);
        }
    }
}
