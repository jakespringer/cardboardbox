package game;

import chunk.Chunk;
import chunk.ChunkSupplier;
import engine.Entity;
import engine.GameLoop;
import engine.Window;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import opengl.ShaderProgram;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector3i;
import org.joml.Vector3ic;
import util.Resources;
import util.VectorKey;

public class World extends Entity {

    public static final int LOAD_DISTANCE = 6;
    public static final int UNLOAD_DISTANCE = 30;

    public static final int NUM_THREADS = 6;

    private ChunkSupplier supplier;
    private HashMap<VectorKey, Chunk> chunks = new HashMap<>();
    private ThreadPoolExecutor threadPool;

    public World(ChunkSupplier supplier) {
        this.supplier = supplier;
        threadPool = new ThreadPoolExecutor(NUM_THREADS, NUM_THREADS, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
        threadPool.allowCoreThreadTimeOut(true);
    }

    @Override
    protected void createInner() {
        if (Chunk.shaderProgram == null) {
            Chunk.shaderProgram = new ShaderProgram(Resources.getResource("src/glsl/chunk.vert"), Resources.getResource("src/glsl/chunk.frag"));
        }
    }

    @Override
    protected void destroyInner() {
        threadPool.shutdownNow();
    }

    @Override
    public void update() {
        Vector3ic homeChunk = posToChunk(Window.camera.position);
        int x = homeChunk.x();
        int y = homeChunk.y();
        int z = homeChunk.z();

        int upperX = x + LOAD_DISTANCE;
        int upperY = y + LOAD_DISTANCE;
        int upperZ = z + LOAD_DISTANCE;
        int lowerX = x - LOAD_DISTANCE;
        int lowerY = y - LOAD_DISTANCE;
        int lowerZ = z - LOAD_DISTANCE;

        for (int i = lowerX; i < upperX; ++i) {
            for (int j = lowerY; j < upperY; ++j) {
                for (int k = lowerZ; k < upperZ; ++k) {
                    VectorKey key = new VectorKey(i, j, k);
                    if (!chunks.containsKey(key)) {
                        chunks.put(key, null);
                        int i2 = i;
                        int j2 = j;
                        int k2 = k;
                        threadPool.execute(() -> {
                            Chunk c = supplier.get(i2, j2, k2);
                            if (c != null) {
                                c.pos = new Vector3i(i2, j2, k2);
                                c.generate();
                                chunks.put(key, c);
                                GameLoop.onMainThread(() -> c.create());
                            }
                        });
                    }
                }
            }
        }
    }

    public static Vector3f chunkToPos(Vector3ic pos) {
        return new Vector3f(pos.x(), pos.y(), pos.z()).mul(Chunk.SIDE_LENGTH);
    }

    public static Vector3i posToChunk(Vector3fc pos) {
        return new Vector3i((int) Math.floor(pos.x() / Chunk.SIDE_LENGTH), (int) Math.floor(pos.y() / Chunk.SIDE_LENGTH), (int) Math.floor(pos.z() / Chunk.SIDE_LENGTH));
    }
}
