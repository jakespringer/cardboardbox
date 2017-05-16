package game;

import org.joml.Matrix4f;

public class Scene {
	private static Camera camera = new Camera();
	
	public static Camera getCamera() {
		return camera;
	}
	
	public static Matrix4f getProjectionMatrix() {
		return Camera.getProjectionMatrix(1.f, (float)Window.getWidth(), (float)Window.getHeight(), 0.1f, 1000.f);
	}
}
