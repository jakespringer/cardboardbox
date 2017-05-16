package physics;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class AABoundingBox3f {
	private Vector3f bottomLeft;
	private Vector3f topRight;
	
	public AABoundingBox3f(Vector3f bottomLeft, Vector3f topRight) {
		this.bottomLeft = new Vector3f(bottomLeft);
		this.topRight = new Vector3f(topRight);
	}
	
	public boolean intersects(Vector3f p) {
		return (p.x > bottomLeft.x
				&& p.y > bottomLeft.y
				&& p.z > bottomLeft.z
				&& topRight.x > p.x
				&& topRight.y > p.y
				&& topRight.z > p.z);
	}
	
	public boolean intersects(AABoundingBox3f b) {
		// Separating Axis Theorem (SAT):
		// three possible planes of separation: xy, xz, yz
		AABoundingBox2f aXy = new AABoundingBox2f(new Vector2f(bottomLeft.x, bottomLeft.y), new Vector2f(topRight.x, topRight.y));
		AABoundingBox2f aXz = new AABoundingBox2f(new Vector2f(bottomLeft.x, bottomLeft.z), new Vector2f(topRight.x, topRight.z));
		AABoundingBox2f aYz = new AABoundingBox2f(new Vector2f(bottomLeft.y, bottomLeft.z), new Vector2f(topRight.y, topRight.z));
		AABoundingBox2f bXy = new AABoundingBox2f(new Vector2f(b.bottomLeft.x, b.bottomLeft.y), new Vector2f(b.topRight.x, b.topRight.y));
		AABoundingBox2f bXz = new AABoundingBox2f(new Vector2f(b.bottomLeft.x, b.bottomLeft.z), new Vector2f(b.topRight.x, b.topRight.z));
		AABoundingBox2f bYz = new AABoundingBox2f(new Vector2f(b.bottomLeft.y, b.bottomLeft.z), new Vector2f(b.topRight.y, b.topRight.z));
		return aXy.intersects(bXy) || aXz.intersects(bXz) || aYz.intersects(bYz);
	}
}
