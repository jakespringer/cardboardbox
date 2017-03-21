package test;

import engine.*;
import java.util.Arrays;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import util.Resources;

public class SimpleRectTex {

    public SimpleRectTex(Vector3f pos) {
        this.pos = pos;

        shaderProgram = new ShaderProgram(Resources.getResource("src/glsl/simple_1.vert"), Resources.getResource("src/glsl/simple_1.frag"));

        // Create vertex data
        float[] positions = new float[]{
            // V0
            -0.5f, 0.5f, 0.5f,
            // V1
            -0.5f, -0.5f, 0.5f,
            // V2
            0.5f, -0.5f, 0.5f,
            // V3
            0.5f, 0.5f, 0.5f,
            // V4
            -0.5f, 0.5f, -0.5f,
            // V5
            0.5f, 0.5f, -0.5f,
            // V6
            -0.5f, -0.5f, -0.5f,
            // V7
            0.5f, -0.5f, -0.5f,
            // For text coords in top face
            // V8: V4 repeated
            -0.5f, 0.5f, -0.5f,
            // V9: V5 repeated
            0.5f, 0.5f, -0.5f,
            // V10: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V11: V3 repeated
            0.5f, 0.5f, 0.5f,
            // For text coords in right face
            // V12: V3 repeated
            0.5f, 0.5f, 0.5f,
            // V13: V2 repeated
            0.5f, -0.5f, 0.5f,
            // For text coords in left face
            // V14: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V15: V1 repeated
            -0.5f, -0.5f, 0.5f,
            // For text coords in bottom face
            // V16: V6 repeated
            -0.5f, -0.5f, -0.5f,
            // V17: V7 repeated
            0.5f, -0.5f, -0.5f,
            // V18: V1 repeated
            -0.5f, -0.5f, 0.5f,
            // V19: V2 repeated
            0.5f, -0.5f, 0.5f,};
        float[] texCoords = new float[]{
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,
            0.0f, 0.0f,
            0.5f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            // For text coords in top face
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.0f, 1.0f,
            0.5f, 1.0f,
            // For text coords in right face
            0.0f, 0.0f,
            0.0f, 0.5f,
            // For text coords in left face
            0.5f, 0.0f,
            0.5f, 0.5f,
            // For text coords in bottom face
            0.5f, 0.0f,
            1.0f, 0.0f,
            0.5f, 0.5f,
            1.0f, 0.5f,};
        int[] indices = new int[]{
            // Front face
            0, 1, 3, 3, 1, 2,
            // Top Face
            8, 10, 11, 9, 8, 11,
            // Right face
            12, 13, 7, 5, 12, 7,
            // Left face
            14, 15, 6, 4, 14, 6,
            // Bottom face
            16, 18, 19, 17, 16, 19,
            // Back face
            4, 6, 7, 5, 4, 7,};

        texture = new Texture("sprites/rock.png");

        // Put vertex data into VAO
        VAO = VertexArrayObject.createVAO(() -> {
            new BufferObject(GL_ARRAY_BUFFER, positions);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0 * 4 /* floats are 4 bytes */, 0);
            new BufferObject(GL_ARRAY_BUFFER, texCoords);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0 * 4 /* floats are 4 bytes */, 0);
            new BufferObject(GL_ELEMENT_ARRAY_BUFFER, indices);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
        });
    }

    public ShaderProgram shaderProgram;
    public Texture texture;
    public VertexArrayObject VAO;
    public Vector3f pos;

    public void draw() {
        shaderProgram.setUniform("modelViewMatrix", TestMain.camera.getViewMatrix(pos));
        shaderProgram.setUniform("projectionMatrix", Cam.getProjectionMatrix(70, 640, 480, .1f, 1000));

        shaderProgram.setUniform("texture_sampler", 0);

        Activatable.with(Arrays.asList(shaderProgram, texture, VAO), () -> {
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);
        });
    }
}
