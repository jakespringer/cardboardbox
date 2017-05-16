package physics;

import org.joml.Vector2f;

import util.Misc;

public class AABoundingBox2f {
	private Vector2f bottomLeft;
	private Vector2f topRight;
	
	public AABoundingBox2f(Vector2f bottomLeft, Vector2f topRight) {
		this.bottomLeft = new Vector2f(bottomLeft);
		this.topRight = new Vector2f(topRight);
	}
	
	public boolean intersects(Vector2f p) {
		return (p.x > bottomLeft.x
				&& p.y > bottomLeft.y
				&& topRight.x > p.x
				&& topRight.y > p.y);
	}
	
	public boolean intersects(AABoundingBox2f b) {
		// Separating Axis Theorem (SAT):
		// two possible planes of separation: x, y
		return Misc.rangeIntersects(bottomLeft.x, topRight.x, b.bottomLeft.x, b.topRight.x)
				|| Misc.rangeIntersects(bottomLeft.y, topRight.y, b.bottomLeft.y, b.topRight.y);
	}
}
