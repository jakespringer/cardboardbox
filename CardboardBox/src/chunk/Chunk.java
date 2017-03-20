package chunk;

import static engine.Activatable.with;
import engine.BufferObject;
import engine.ShaderProgram;
import engine.Texture;
import engine.VertexArrayObject;
import java.util.*;
import java.util.stream.IntStream;
import org.joml.Vector3i;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_3D;
import static org.lwjgl.opengl.GL12.glTexImage3D;
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
        if (x < 0 || y < 0 || z < 0 || x >= SIDE_LENGTH || y >= SIDE_LENGTH || z >= SIDE_LENGTH) {
            return false;
        }
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

    private static ShaderProgram shaderProgram;
    private VertexArrayObject VAO;
    private Texture t;

    public void load() {
        if (shaderProgram == null) {
            String vertexShaderSource = "#version 330 core\n"
                    + "layout (location = 0) in vec3 position;\n"
                    + "out vec3 outTexCoord;\n"
                    + "void main()\n"
                    + "{\n"
                    + "    gl_Position = vec4(position.x, position.y, position.z, 1.0);\n"
                    + "    outTexCoord = position;\n"
                    + "}";
            String fragmentShaderSource = "#version 330 core\n"
                    + "in vec3 outTexCoord;\n"
                    + "out vec4 color;\n"
                    + "uniform sampler3D colors;\n"
                    + "void main()\n"
                    + "{\n"
                    + "    //color = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n"
                    + "    color = texture(colors, outTexCoord);"
                    + "}";
            shaderProgram = new ShaderProgram(vertexShaderSource, fragmentShaderSource);
        }

        List<Vector3i> verts = new ArrayList();
        for (int x = 0; x < SIDE_LENGTH; x++) {
            for (int y = 0; y < SIDE_LENGTH; y++) {
                for (int z = 0; z < SIDE_LENGTH; z++) {
                    if (getExists(x, y, z)) {
                        if (!getExists(x - 1, y, z)) {
                            for (int c = 0; c < 4; c++) {
                                verts.add(new Vector3i(x, y + c % 2, z + c / 2));
                            }
                        }
                        if (!getExists(x + 1, y, z)) {
                            for (int c = 0; c < 4; c++) {
                                verts.add(new Vector3i(x + 1, y + c % 2, z + c / 2));
                            }
                        }
                        if (!getExists(x, y - 1, z)) {
                            for (int c = 0; c < 4; c++) {
                                verts.add(new Vector3i(x + c % 2, y, z + c / 2));
                            }
                        }
                        if (!getExists(x, y + 1, z)) {
                            for (int c = 0; c < 4; c++) {
                                verts.add(new Vector3i(x + c % 2, y + 1, z + c / 2));
                            }
                        }
                        if (!getExists(x, y, z - 1)) {
                            for (int c = 0; c < 4; c++) {
                                verts.add(new Vector3i(x + c / 2, y + c % 2, z));
                            }
                        }
                        if (!getExists(x, y, z + 1)) {
                            for (int c = 0; c < 4; c++) {
                                verts.add(new Vector3i(x + c / 2, y + c % 2, z + 1));
                            }
                        }
                    }
                }
            }
        }

        List<Vector3i> uniqueVerts = new ArrayList(new HashSet(verts));
        List<Integer> inds = new LinkedList();
        for (int i = 0; i < verts.size() / 4; i++) {
            for (int j : new int[]{0, 1, 3, 1, 2, 3}) {
                inds.add(uniqueVerts.indexOf(verts.get(i + j)));
            }
        }

        System.out.println(verts.size() + " " + uniqueVerts.size());

        int[] vertices = uniqueVerts.stream().flatMapToInt(v -> IntStream.of(v.x, v.y, v.z)).toArray();
        int[] indices = inds.stream().mapToInt(i -> i).toArray();
        VAO = VertexArrayObject.createVAO(() -> {
            new BufferObject(GL_ARRAY_BUFFER, vertices);
            new BufferObject(GL_ELEMENT_ARRAY_BUFFER, indices);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4 /* floats are 4 bytes */, 0);
            glEnableVertexAttribArray(0);
        });

        t = new Texture(GL_TEXTURE_3D);
        t.activate();
        int[] data = new int[32 * 32 * 32 * 3];
        for (int i = 0; i < data.length; i++) {
            data[i] = (int) (Math.random() * 256);
        }
        glTexImage3D(t.getID(), 0, GL_RGB, 32, 32, 32, 0, GL_RGB, GL_INT, data);
        t.deactivate();

        System.out.println("Created chunk");
    }

    public void unload() {
        VAO.destroy();
    }

    public void draw(int chunkX, int chunkY, int chunkZ) {
        shaderProgram.setUniform("colors", t.getID());
        with(Arrays.asList(shaderProgram, VAO), () -> {
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        });
    }
}
