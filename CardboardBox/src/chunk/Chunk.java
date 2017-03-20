package chunk;

import static engine.Activatable.with;
import engine.BufferObject;
import engine.ShaderProgram;
import engine.VertexArrayObject;
import java.util.Arrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Chunk {

    public static final int SIDE_LENGTH = 32;
    private int[] colors = new int[SIDE_LENGTH * SIDE_LENGTH * SIDE_LENGTH];

    private int getColor(int x, int y, int z) {
        return colors[x * SIDE_LENGTH * SIDE_LENGTH + y * SIDE_LENGTH + z];
    }

    private boolean getExists(int x, int y, int z) {
        return colors[x * SIDE_LENGTH * SIDE_LENGTH + y * SIDE_LENGTH + z] != 0;
    }

    void setColor(int x, int y, int z, int color) {
        if (color == 0) {
            color = 0x00010101;
        }

        colors[x * SIDE_LENGTH * SIDE_LENGTH + y * SIDE_LENGTH + z] = color;
    }

    void remove(int x, int y, int z) {
        colors[x * SIDE_LENGTH * SIDE_LENGTH + y * SIDE_LENGTH + z] = 0;
    }

    private VertexArrayObject VAO;

    public void load() {
        float[] vertices = {};
        int[] indices = {};
        VAO = VertexArrayObject.createVAO(() -> {
            new BufferObject(GL_ARRAY_BUFFER, vertices);
            new BufferObject(GL_ELEMENT_ARRAY_BUFFER, indices);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4 /* floats are 4 bytes */, 0);
            glEnableVertexAttribArray(0);
        });
    }

    public void unload() {
        VAO.destroy();
        VAO.deactivate();
    }

    public void draw(int chunkX, int chunkY, int chunkZ) {
        with(Arrays.asList(shaderProgram, VAO), () -> {
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        });
    }

    static ShaderProgram shaderProgram;

    static {
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

        shaderProgram = new ShaderProgram(vertexShaderSource, fragmentShaderSource);
    }
}
