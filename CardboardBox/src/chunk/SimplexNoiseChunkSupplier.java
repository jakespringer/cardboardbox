package chunk;

import static chunk.Chunk.SIDE_LENGTH;
import static chunk.Chunk.SIDE_LENGTH_2;
import util.Noise;

public class SimplexNoiseChunkSupplier implements ChunkSupplier {

    private static final int OCTAVES = 4;
    private static final double FREQUENCY = 1 / 200.;
    private static final double HEIGHT = 50;

    private final Noise noise;

    public SimplexNoiseChunkSupplier(double seed) {
        noise = new Noise(seed);
    }

    public SimplexNoiseChunkSupplier() {
        this((double) System.currentTimeMillis());
    }

    @Override
    public Chunk get(int x, int y, int z) {
        if ((z + 1) * SIDE_LENGTH > 2 * HEIGHT || z * SIDE_LENGTH < -2 * HEIGHT) {
            return null;
        }
        Chunk chunk = null;
        int count = 0;
        for (int i = -1; i <= SIDE_LENGTH; i++) {
            for (int j = -1; j <= SIDE_LENGTH; j++) {
                for (int k = -1; k <= SIDE_LENGTH; k++) {
                    if (noise.fbm(x * SIDE_LENGTH + i, y * SIDE_LENGTH + j, z * SIDE_LENGTH + k,
                            OCTAVES, FREQUENCY) * HEIGHT > z * SIDE_LENGTH + k) {
                        if (chunk == null) {
                            chunk = new Chunk();
                        }

                        int r = validateColor(50 * noise.fbm(1000 + x * SIDE_LENGTH + i, y * SIDE_LENGTH + j, z * SIDE_LENGTH + k, 2, 1 / 200.));
                        int g = validateColor(200 + 50 * noise.fbm(2000 + x * SIDE_LENGTH + i, y * SIDE_LENGTH + j, z * SIDE_LENGTH + k, 2, 1 / 200.));
                        int b = validateColor(50 * noise.fbm(3000 + x * SIDE_LENGTH + i, y * SIDE_LENGTH + j, z * SIDE_LENGTH + k, 2, 1 / 200.));

                        chunk.setColor(i, j, k, 0x10000 * r + 0x100 * g + b);
                        count++;
                    }
                }
            }
        }
        if (count == 0 || count == SIDE_LENGTH_2 * SIDE_LENGTH_2 * SIDE_LENGTH_2) {
            return null;
        }
        return chunk;
    }

    private static int validateColor(double x) {
        return (int) Math.min(Math.max(0, x), 255);
    }
}
