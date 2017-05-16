package chunk;

import static chunk.Chunk.SIDE_LENGTH;
import static chunk.Chunk.SIDE_LENGTH_2;
import util.Noise;

public class SimplexNoiseChunkSupplier implements ChunkSupplier {

    private static final int OCTAVES = 4;
    private static final double FREQUENCY = 1 / 500.;
    private static final double HEIGHT = 100;

    public static final double MAX_Z = 2. * HEIGHT / SIDE_LENGTH;

    private final Noise noise;

    public SimplexNoiseChunkSupplier(double seed) {
        noise = new Noise(seed);
    }

    public SimplexNoiseChunkSupplier() {
        this((double) System.currentTimeMillis());
    }

    @Override
    public BlockArray get(int x, int y, int z) {
        if (z + 1 > MAX_Z || z < -MAX_Z) {
            return null;
        }

        int blockDownsampling = 4;
        int colorDownsampling = 8;

        double[][][] blocks = fbmDownsample(noise, x, y, z, OCTAVES, FREQUENCY, blockDownsampling);
        double[][][] red = fbmDownsample(noise, 1000 + x, y, z, 2, 1 / 1000., colorDownsampling);
        double[][][] green = fbmDownsample(noise, 2000 + x, y, z, 2, 1 / 1000., colorDownsampling);
        double[][][] blue = fbmDownsample(noise, 3000 + x, y, z, 2, 1 / 1000., colorDownsampling);

        BlockArray ba = null;
        int count = 0;
        for (int i = -1; i <= SIDE_LENGTH; i++) {
            for (int j = -1; j <= SIDE_LENGTH; j++) {
                for (int k = -1; k <= SIDE_LENGTH; k++) {
                    if (sample(blocks, i, j, k, blockDownsampling) * HEIGHT > z * SIDE_LENGTH + k) {
                        if (ba == null) {
                            ba = new BlockArray();
                        }

                        int r = validateColor(100 * sample(red, i, j, k, colorDownsampling));
                        int g = validateColor(150 + 100 * sample(green, i, j, k, colorDownsampling));
                        int b = validateColor(100 * sample(blue, i, j, k, colorDownsampling));

                        ba.setColor(i, j, k, 0x10000 * r + 0x100 * g + b);
                        count++;
                    }
                }
            }
        }
        if (count == 0 || count == SIDE_LENGTH_2 * SIDE_LENGTH_2 * SIDE_LENGTH_2) {
            return null;
        }
        return ba;
    }

    private static double[][][] fbmDownsample(Noise noise, int x, int y, int z, int octaves, double frequency, int downSampling) {
        double[][][] samples = new double[SIDE_LENGTH / downSampling + 2][SIDE_LENGTH / downSampling + 2][SIDE_LENGTH / downSampling + 2];
        for (int i = -1; i <= SIDE_LENGTH / downSampling; i++) {
            for (int j = -1; j <= SIDE_LENGTH / downSampling; j++) {
                for (int k = -1; k <= SIDE_LENGTH / downSampling; k++) {
                    samples[i + 1][j + 1][k + 1] = noise.fbm(x * SIDE_LENGTH + i * downSampling, y * SIDE_LENGTH + j * downSampling, z * SIDE_LENGTH + k * downSampling, octaves, frequency);
                }
            }
        }
        return samples;
    }

    private static double sample(double[][][] samples, double i, double j, double k, int downSampling) {
        double x = i / downSampling;
        double y = j / downSampling;
        double z = k / downSampling;

        int x0 = (int) Math.floor(x);
        int x1 = (int) Math.ceil(x);
        int y0 = (int) Math.floor(y);
        int y1 = (int) Math.ceil(y);
        int z0 = (int) Math.floor(z);
        int z1 = (int) Math.ceil(z);

        double xd = x - x0;
        double yd = y - y0;
        double zd = z - z0;

        double c00 = samples[x0 + 1][y0 + 1][z0 + 1] * (1 - xd) + samples[x1 + 1][y0 + 1][z0 + 1] * xd;
        double c01 = samples[x0 + 1][y0 + 1][z1 + 1] * (1 - xd) + samples[x1 + 1][y0 + 1][z1 + 1] * xd;
        double c10 = samples[x0 + 1][y1 + 1][z0 + 1] * (1 - xd) + samples[x1 + 1][y1 + 1][z0 + 1] * xd;
        double c11 = samples[x0 + 1][y1 + 1][z1 + 1] * (1 - xd) + samples[x1 + 1][y1 + 1][z1 + 1] * xd;

        double c0 = c00 * (1 - yd) + c10 * yd;
        double c1 = c01 * (1 - yd) + c11 * yd;

        return c0 * (1 - zd) + c1 * zd;
    }

    private static int validateColor(double x) {
        return (int) Math.min(Math.max(0, x), 255);
    }
}
