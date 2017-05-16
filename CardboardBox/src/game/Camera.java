package game;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    public Vector3f position = new Vector3f(0, 0, 0);
    public float horAngle, vertAngle;
    public Vector3f up = new Vector3f(0, 1, 0);

    public Matrix4f getViewMatrix() {
        return new Matrix4f()
                .rotate(vertAngle, new Vector3f(1, 0, 0))
                .rotate(horAngle, new Vector3f(0, 1, 0))
                .translate(position);
    }
    
    public Matrix4f getRotationalViewMatrix() {
    	return new Matrix4f()
    			.rotate(vertAngle, new Vector3f(1, 0, 0))
                .rotate(horAngle, new Vector3f(0, 1, 0));
    }

    public Matrix4f getModelViewMatrix(Vector3f translate) {
        return getViewMatrix().translate(translate);
    }

    public static Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;
        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }

    public Vector3f facing() {
        return new Vector3f((float) (Math.cos(vertAngle) * Math.sin(-horAngle)),
        		(float) (Math.sin(vertAngle)),
        		(float) (Math.cos(vertAngle) * Math.cos(-horAngle)));
    }
}