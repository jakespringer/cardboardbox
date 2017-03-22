package chunk;

import engine.Window;
import java.util.*;
import java.util.stream.IntStream;
import opengl.BufferObject;
import opengl.ShaderProgram;
import opengl.Texture;
import opengl.VertexArrayObject;
import org.joml.Vector3f;
import org.joml.Vector3i;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import util.Resources;

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

    public static final List<Vector3i> allDirs = Arrays.asList(
            new Vector3i(-1, 0, 0), new Vector3i(1, 0, 0),
            new Vector3i(0, -1, 0), new Vector3i(0, 1, 0),
            new Vector3i(0, 0, -1), new Vector3i(0, 0, 1));

    public static ShaderProgram shaderProgram;
    private List<VertexArrayObject> VAOList = new LinkedList();
    private List<Integer> numIndicesList = new LinkedList();
    private Texture t;

    public void load() {
        if (shaderProgram == null) {
            shaderProgram = new ShaderProgram(Resources.getResource("src/glsl/chunk.vert"),
                    Resources.getResource("src/glsl/chunk.frag"));
        }

        for (Vector3i dir : allDirs) {
            createVAO(dir);
        }

        t = new Texture(colors);
    }

    private void createVAO(Vector3i dir) {
        List<Vector3i> verts = new ArrayList();
        for (int x = 0; x < SIDE_LENGTH; x++) {
            for (int y = 0; y < SIDE_LENGTH; y++) {
                for (int z = 0; z < SIDE_LENGTH; z++) {
                    if (getExists(x, y, z)) {
                        if (!getExists(x + dir.x, y + dir.y, z + dir.z)) {
                            for (int c = 0; c < 8; c++) {
                                Vector3i toAdd = new Vector3i(c % 2, c / 2 % 2, c / 4);
                                if ((toAdd.x - .5) * dir.x + (toAdd.y - .5) * dir.y + (toAdd.z - .5) * dir.z > 0) {
                                    verts.add(new Vector3i(x, y, z).add(toAdd));
                                }
                            }
                        }
                    }
                }
            }
        }

        List<Vector3i> uniqueVerts = new ArrayList(new HashSet(verts));
        List<Integer> inds = new LinkedList();
        for (int i = 0; i < verts.size() / 4; i++) {
            for (int j : new int[]{0, 1, 2, 1, 2, 3}) {
                inds.add(uniqueVerts.indexOf(verts.get(4 * i + j)));
            }
        }

        int[] vertices = uniqueVerts.stream().flatMapToInt(v -> IntStream.of(v.x, v.y, v.z)).toArray();
        int[] indices = inds.stream().mapToInt(i -> i).toArray();

        VAOList.add(VertexArrayObject.createVAO(() -> {
            new BufferObject(GL_ARRAY_BUFFER, vertices);
            glVertexAttribPointer(0, 3, GL_INT, false, 3 * 4 /* floats are 4 bytes */, 0);
            new BufferObject(GL_ELEMENT_ARRAY_BUFFER, indices);
            glEnableVertexAttribArray(0);
        }));
        numIndicesList.add(indices.length);
    }

    public void unload() {
//        for (VertexArrayObject VAO : VAOList) {
//            VAO.destroy();
//        }
    }

    public void draw(int chunkX, int chunkY, int chunkZ) {
        shaderProgram.setUniform("worldMatrix", Window.camera.getWorldMatrix(new Vector3f(chunkX, chunkY, chunkZ).mul(SIDE_LENGTH)));
//        shaderProgram.setUniform("texture_sampler", 0);

        for (int i = 0; i < 6; i++) {

            shaderProgram.setUniform("normal", allDirs.get(i));

            int numIndices = numIndicesList.get(i);
            shaderProgram.activate();
            t.activate();
            VAOList.get(i).activate();
//            with(Arrays.asList(shaderProgram, t, VAOList.get(i)), () -> {
//                glEnableVertexAttribArray(0);
//                glEnableVertexAttribArray(1);
            glDrawElements(GL_TRIANGLES, numIndices, GL_UNSIGNED_INT, 0);
//            });
        }
    }
}
