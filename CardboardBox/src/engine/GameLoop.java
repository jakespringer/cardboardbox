package engine;

import chunk.Chunk;
import static engine.Window.window;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GameLoop {

    private static final Queue<Runnable> TO_RUN = new ConcurrentLinkedQueue();

    public static void onMainThread(Runnable r) {
        TO_RUN.add(r);
    }

    private static long prevTime;
    private static final double[] fpsHistory = new double[16];
    private static int fpsHistoryIndex = 0;

    public static void run() {
        while (!glfwWindowShouldClose(window)) {

            //FPS
            fpsHistory[fpsHistoryIndex] = 1.0e9 / (System.nanoTime() - prevTime);
            GLFW.glfwSetWindowTitle(Window.window, "" + (int) Arrays.stream(fpsHistory).average().getAsDouble());
            fpsHistoryIndex = (fpsHistoryIndex + 1) % fpsHistory.length;
            prevTime = System.nanoTime();

            glfwPollEvents();

            Entity.updateAll();

            while (!TO_RUN.isEmpty()) {
                TO_RUN.poll().run();
            }

            // Clear the color buffer
            glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            if (Chunk.shaderProgram != null) {
                Chunk.shaderProgram.setUniform("projectionMatrix", Camera.getProjectionMatrix(70, 640, 480, .1f, 1000));
            }

            // Render
            Entity.drawAll();

            glfwSwapBuffers(window);
        }

        Entity.destroyAll();
        Window.cleanup();
    }
}
