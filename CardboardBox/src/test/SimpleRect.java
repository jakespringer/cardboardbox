package test;

import engine.Camera;
import engine.Activatable;
import engine.Window;
import opengl.BufferObject;
import opengl.ShaderProgram;
import opengl.VertexArrayObject;
import java.util.Arrays;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import util.Resources;

public class SimpleRect {

    public ShaderProgram shaderProgram;
    public VertexArrayObject VAO;
    public Vector3f pos;

    public SimpleRect(Vector3f pos) {
        this.pos = pos;

        shaderProgram = new ShaderProgram(Resources.getResource("src/glsl/simple.vert"), Resources.getResource("src/glsl/simple.frag"));

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
        VAO = VertexArrayObject.createVAO(() -> {
            new BufferObject(GL_ARRAY_BUFFER, vertices);
            new BufferObject(GL_ELEMENT_ARRAY_BUFFER, indices);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4 /* floats are 4 bytes */, 0);
            glEnableVertexAttribArray(0);
        });
    }

    public void draw() {
        shaderProgram.setUniform("worldMatrix", Window.camera.getWorldMatrix(pos));
        shaderProgram.setUniform("projectionMatrix", Camera.getProjectionMatrix(70, 640, 480, .1f, 1000));
        Activatable.with(Arrays.asList(shaderProgram, VAO), () -> {
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        });
    }
}
