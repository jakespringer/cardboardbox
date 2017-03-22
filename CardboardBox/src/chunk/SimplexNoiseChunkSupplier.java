package chunk;

import java.util.HashMap;
import util.Noise;
import util.VectorKey;

public class SimplexNoiseChunkSupplier implements ChunkSupplier {

    private Noise noise;
    private HashMap<VectorKey, Chunk> cache = new HashMap<>();
    private Chunk emptyChunk = new Chunk();

    public SimplexNoiseChunkSupplier(double seed) {
        noise = new Noise(seed);
    }

    public SimplexNoiseChunkSupplier() {
        this((double) System.currentTimeMillis());
    }

    @Override
    public Chunk get(int x, int y, int z) {
        VectorKey key = new VectorKey(x, y, z);
        if (cache.containsKey(key)) {
            return cache.get(key);
        } else {
            Chunk chunk = null;
            for (int i = 0; i < Chunk.SIDE_LENGTH; ++i) {
                for (int j = 0; j < Chunk.SIDE_LENGTH; ++j) {
                    int height = (int) Math.floor((noise.fbm(x * Chunk.SIDE_LENGTH + i, y * Chunk.SIDE_LENGTH + j, 4, 1 / 200.) + 2) * 15);
//                    int height = (int) Math.floor((noise.perlin((x * Chunk.SIDE_LENGTH - i) / 100.0f, (z * Chunk.SIDE_LENGTH - j) / 100.0f) + 1) * 10);
                    if ((height / Chunk.SIDE_LENGTH) == z) {
                        if (chunk == null) {
                            chunk = new Chunk();
                        }
                        for (int k = 0; k <= height % Chunk.SIDE_LENGTH; k++) {
                            chunk.setColor(i, j, k, 0x00AA00);
                        }
                    }
                }
            }

            if (chunk == null) {
                chunk = emptyChunk;
            }
            cache.put(key, chunk);
            return chunk;
        }
    }
}
