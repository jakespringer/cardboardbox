package chunk;

public class FlatChunkSupplier implements ChunkSupplier {
	private final Chunk flatChunk = new Chunk();
	private final Chunk emptyChunk = new Chunk();
	
	public FlatChunkSupplier(int height, int color) {
		for (int i=0; i<Chunk.SIDE_LENGTH; ++i) {
			for (int j=0; j<Chunk.SIDE_LENGTH; ++j) {
				flatChunk.setColor(i, height, j, color);
			}
		}
	}
	
	@Override
	public Chunk get(int x, int y, int z) {
		if (y == 0) {
			return flatChunk;
		} else {
			return emptyChunk;
		}
	}
}
