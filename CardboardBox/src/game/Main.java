package game;

import static engine.Activatable.with;
import engine.BufferObject;
import engine.ShaderProgram;
import engine.VertexArrayObject;
import java.io.File;
import java.util.Arrays;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Main {

    public static Renderer renderer;

    public static void main(String[] args) {
        System.setProperty("org.lwjgl.librarypath", new File("native").getAbsolutePath());
        renderer = new Renderer();
        renderer.initialize();
        loop();
        renderer.cleanup();
    }

    private static void loop() {

        // Define shader sources
        String vertexShaderSource = "#version 330 core\n"
                + "layout (location = 0) in vec3 position;\n"
                + "void main()\n"
                + "{\n"
                + "    gl_Position = vec4(position.x, position.y, position.z, 1.0);\n"
                + "}";
        String fragmentShaderSource = "#version 330 core\n"
                + "out vec4 color;\n"
                + "void main()\n"
                + "{\n"
                + "    color = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n"
                + "}";

        ShaderProgram shaderProgram = new ShaderProgram(vertexShaderSource, fragmentShaderSource);

        // Create vertex data
        float[] vertices = {
            0.5f, 0.5f, 0.0f, // Top Right
            0.5f, -0.5f, 0.0f, // Bottom Right
            -0.5f, -0.5f, 0.0f, // Bottom Left
            -0.5f, 0.5f, 0.0f // Top Left
        };
        int[] indices = { // Note that we start from 0!
            0, 1, 3, // First Triangle
            1, 2, 3 // Second Triangle
        };

        // Put vertex data into VAO
        VertexArrayObject VAO = VertexArrayObject.createVAO(() -> {
            new BufferObject(GL_ARRAY_BUFFER, vertices);
            new BufferObject(GL_ELEMENT_ARRAY_BUFFER, indices);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4 /* floats are 4 bytes */, 0);
            glEnableVertexAttribArray(0);
        });

        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(renderer.window)) {
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
            
            if (Input.isPressed("exit")) {
            	glfwSetWindowShouldClose(renderer.window, true);
            }

            // Clear the color buffer
            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            // Render
            with(Arrays.asList(shaderProgram, VAO), () -> {
                glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
            });

            // Swap screen buffers
            glfwSwapBuffers(renderer.window); // swap the color buffers

            renderer.draw();            
        }

        VAO.destroy();
    }
}
