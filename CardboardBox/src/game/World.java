package game;

import org.joml.Vector3ic;

import chunk.Chunk;
import chunk.ChunkSupplier;

public class World {
	private ChunkSupplier supplier;
	
	public World(ChunkSupplier supplier) {
		this.supplier = supplier;
	}
	
	public void initialize() {
	}
	
	public void draw(Camera camera, int renderDistance) {
		Vector3ic homeChunk = camera.getChunkPosition();
		int x = homeChunk.x();
		int y = homeChunk.y();
		int z = homeChunk.z();
		for (int i=0; i<renderDistance*2; ++i) {
			for (int j=0; j<renderDistance*2; ++j) {
				for (int k=0; k<renderDistance*2; ++k) {
					Chunk chunk = supplier.get(x+i-renderDistance, y+j-renderDistance, z+k-renderDistance);
					chunk.draw();
				}
			}
		}
	}
	
	public void cleanup() {
	}
}
