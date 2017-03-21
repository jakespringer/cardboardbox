package game;

import java.util.HashMap;

import org.joml.Vector3i;
import org.joml.Vector3ic;

import chunk.Chunk;
import chunk.ChunkSupplier;
import util.VectorKey;

public class World {
	private static final int RENDER_DISTANCE = 4;
	private ChunkSupplier supplier;
	private Vector3ic lowerBoundLoaded;
	private Vector3ic upperBoundLoaded;
	private HashMap<VectorKey, Chunk> chunks = new HashMap<>();
	
	public World(ChunkSupplier supplier) {
		this.supplier = supplier;
		this.lowerBoundLoaded = new Vector3i(0, 0, 0);
		this.upperBoundLoaded = new Vector3i(0, 0, 0);
	}
	
	public void initialize() {
	}
	
	public void draw(Camera camera) {
		Vector3ic homeChunk = camera.getChunkPosition();
		int x = homeChunk.x();
		int y = homeChunk.y();
		int z = homeChunk.z();
		
		int upperX = x + RENDER_DISTANCE;
		int upperY = y + RENDER_DISTANCE;
		int upperZ = z + RENDER_DISTANCE;
		int lowerX = x - RENDER_DISTANCE;
		int lowerY = y - RENDER_DISTANCE;
		int lowerZ = z - RENDER_DISTANCE;
		int upperLoadedX = upperBoundLoaded.x();
		int upperLoadedY = upperBoundLoaded.y();
		int upperLoadedZ = upperBoundLoaded.z();
		int lowerLoadedX = lowerBoundLoaded.x();
		int lowerLoadedY = lowerBoundLoaded.y();
		int lowerLoadedZ = lowerBoundLoaded.z();
		
		for (int i=lowerLoadedX; i<upperLoadedX; ++i) {
			for (int j=lowerLoadedY; j<upperLoadedY; ++j) {
				for (int k=lowerLoadedZ; k<upperLoadedZ; ++k) {
					if (!((lowerX <= i && i < upperX)
					   && (lowerY <= j && j < upperY)
					   && (lowerZ <= k && k < upperZ))) {
						VectorKey key = new VectorKey(i, j, k);
						chunks.get(key).unload();
						chunks.remove(key);
					}
				}
			}
		}
		
		for (int i=lowerX; i<upperX; ++i) {
			for (int j=lowerY; j<upperY; ++j) {
				for (int k=lowerZ; k<upperZ; ++k) {
					if (!((lowerLoadedX <= i && i < upperLoadedX)
					   && (lowerLoadedY <= j && j < upperLoadedY)
					   && (lowerLoadedZ <= k && k < upperLoadedZ))) {
						VectorKey key = new VectorKey(i, j, k);
						chunks.put(key, supplier.get(i, j, k));
						chunks.get(key).load();
					}
				}
			}
		}
		
		upperBoundLoaded = new Vector3i(upperX, upperY, upperZ);
		lowerBoundLoaded = new Vector3i(lowerX, lowerY, lowerZ);
		
		for (int i=0; i<RENDER_DISTANCE*2; ++i) {
			for (int j=0; j<RENDER_DISTANCE*2; ++j) {
				for (int k=0; k<RENDER_DISTANCE*2; ++k) {
					int cx = x+i-RENDER_DISTANCE;
					int cy = y+j-RENDER_DISTANCE;
					int cz = z+k-RENDER_DISTANCE;
					chunks.get(new VectorKey(cx, cy, cz)).draw(cx, cy, cz);
				}
			}
		}
	}
	
	public void cleanup() {
	}
}
