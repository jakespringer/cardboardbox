package chunk;

import engine.Entity;

public class Chunk1 extends Entity {

//    // -------------------------------------------------------------------------------------------------------
//    // Storing block colors
////    public static final int SIDE_LENGTH = 64;
////    static final int SIDE_LENGTH_2 = (SIDE_LENGTH + 2);
//    public Vector3i pos;
//    private int[] colors = new int[SIDE_LENGTH_2 * SIDE_LENGTH_2 * SIDE_LENGTH_2];
//
//    public int getColor(int x, int y, int z) {
//        return colors[(x + 1) * SIDE_LENGTH_2 * SIDE_LENGTH_2 + (y + 1) * SIDE_LENGTH_2 + (z + 1)];
//    }
//
//    boolean isSolid(int x, int y, int z) {
//        return getColor(x, y, z) != 0;
//    }
//
//    void setColor(int x, int y, int z, int color) {
//        colors[(x + 1) * SIDE_LENGTH_2 * SIDE_LENGTH_2 + (y + 1) * SIDE_LENGTH_2 + (z + 1)] = color;
//    }
//
//    // -------------------------------------------------------------------------------------------------------
//    // Loading the chunk
//    private final List<int[]> vertices = new LinkedList(), indices = new LinkedList();
//
//    public void generate() {
//        for (Vector3i dir : allDirs) {
//            List<Vector3i> verts = new ArrayList();
//            for (int x = 0; x < SIDE_LENGTH; x++) {
//                for (int y = 0; y < SIDE_LENGTH; y++) {
//                    for (int z = 0; z < SIDE_LENGTH; z++) {
//                        if (isSolid(x, y, z)) {
//                            if (!isSolid(x + dir.x, y + dir.y, z + dir.z)) {
//                                for (int c = 0; c < 8; c++) {
//                                    Vector3i toAdd = new Vector3i(c % 2, c / 2 % 2, c / 4);
//                                    if ((toAdd.x - .5) * dir.x + (toAdd.y - .5) * dir.y + (toAdd.z - .5) * dir.z > 0) {
//                                        verts.add(new Vector3i(x, y, z).add(toAdd));
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            List<Vector3i> uniqueVerts = new ArrayList(new HashSet(verts));
//            List<Integer> inds = new LinkedList();
//            for (int i = 0; i < verts.size() / 4; i++) {
//                for (int j : new int[]{0, 1, 2, 1, 2, 3}) {
//                    inds.add(uniqueVerts.indexOf(verts.get(4 * i + j)));
//                }
//            }
//
//            vertices.add(uniqueVerts.stream().flatMapToInt(v -> IntStream.of(v.x, v.y, v.z)).toArray());
//            indices.add(inds.stream().mapToInt(i -> i).toArray());
//
//            numIndicesList.add(inds.size());
//        }
//    }
//
//    @Override
//    public void update() {
//        if (posToChunk(Window.camera.position).distance(pos) > World.UNLOAD_DISTANCE) {
//            for (VertexArrayObject VAO : VAOList) {
//                VAO.destroy();
//            }
//            VAOList.clear();
//        }
//    }
//
//    // -------------------------------------------------------------------------------------------------------
//    // Rendering the blocks
//    private static final List<Vector3i> allDirs = Arrays.asList(
//            new Vector3i(-1, 0, 0), new Vector3i(1, 0, 0),
//            new Vector3i(0, -1, 0), new Vector3i(0, 1, 0),
//            new Vector3i(0, 0, -1), new Vector3i(0, 0, 1));
//
//    public static ShaderProgram shaderProgram;
//    private List<VertexArrayObject> VAOList = new LinkedList();
//    private List<Integer> numIndicesList = new LinkedList();
//    private Texture t;
//
//    @Override
//    public void draw() {
//        if (numIndicesList.stream().anyMatch(i -> i != 0)) {
//            if (VAOList.isEmpty() && posToChunk(Window.camera.position).distance(pos) < World.LOAD_DISTANCE) {
//                synchronized (this) {
//                    for (int i = 0; i < 6; i++) {
//                        int i2 = i;
//                        VAOList.add(VertexArrayObject.createVAO(() -> {
//                            new BufferObject(GL_ARRAY_BUFFER, vertices.get(i2));
//                            glVertexAttribPointer(0, 3, GL_INT, false, 3 * 4 /* floats are 4 bytes */, 0);
//                            new BufferObject(GL_ELEMENT_ARRAY_BUFFER, indices.get(i2));
//                            glEnableVertexAttribArray(0);
//                        }));
//                    }
//                }
//                t = new Texture(this);
//            }
//
//            if (!VAOList.isEmpty()) {
//                shaderProgram.setUniform("worldMatrix", Window.camera.getWorldMatrix(new Vector3f(pos.x, pos.y, pos.z).mul(SIDE_LENGTH)));
//
//                for (int i = 0; i < 6; i++) {
//                    shaderProgram.setUniform("normal", allDirs.get(i));
//                    //shaderProgram.activate();
//                    t.activate();
//                    VAOList.get(i).activate();
//                    glDrawElements(GL_TRIANGLES, numIndicesList.get(i), GL_UNSIGNED_INT, 0);
//                }
//            }
//        }
//    }
}
