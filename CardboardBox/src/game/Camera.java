package game;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import chunk.Chunk;

public class Camera {
	private Vector3fc eye;
	private Vector3fc center;
	private Vector3fc up;
	
	public Camera(Vector3fc eye, Vector3fc center, Vector3fc up) {
		lookAt(eye, center, up);
	}
	
	public Camera(Vector3fc eye) {
		this(eye, 
			 eye.add(1, 0, 0, new Vector3f()),
			 new Vector3f(0, 1, 0));
	}
	
	public Matrix4f getViewProjectionMatrix(float fovy, float aspect, float zNear, float zFar) {
		return new Matrix4f()
				.perspective(fovy, aspect, zNear, zFar)
				.lookAt(eye, center, up);
	}
	
	public Vector3fc getEye() {
		return eye;
	}
	
	public Vector3fc getCenter() {
		return center;
	}
	
	public Vector3fc getUp() {
		return eye;
	}
	
	public void lookAt(Vector3fc eye, Vector3fc center, Vector3fc up) {
		this.eye = eye;
		this.center = center;
		this.up = up;
	}
	
	public void lookAt(float x, float y, float z, 
					   float cx, float cy, float cz, 
					   float ux, float uy, float uz) {
		lookAt(new Vector3f(x, y, z), new Vector3f(cx, cy, cz), new Vector3f(ux, uy, uz));
	}
	
	public Vector3ic getChunkPosition() {
		return new Vector3i((int) Math.floor(eye.x() / Chunk.SIDE_LENGTH),
							(int) Math.floor(eye.y() / Chunk.SIDE_LENGTH),
							(int) Math.floor(eye.z() / Chunk.SIDE_LENGTH));
	}
}
