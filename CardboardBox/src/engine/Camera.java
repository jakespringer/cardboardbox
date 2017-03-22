package engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    public Vector3f position = new Vector3f(0, 0, 0);
    public float horAngle, vertAngle;
    public Vector3f up = new Vector3f(0, 0, 1);

    private Matrix4f getViewMatrix() {
        return new Matrix4f()
                .rotate(vertAngle - (float) Math.PI / 2, new Vector3f(1, 0, 0))
                .rotate((float) Math.PI / 2 - horAngle, new Vector3f(0, 0, 1))
                .translate(position.mul(-1, new Vector3f()));

        // Why am I adding/subtracting floats from the angles? Idk, but it works.
    }

    public Matrix4f getWorldMatrix(Vector3f translate) {
        return getViewMatrix().translate(translate);
    }

    public static Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;
        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }

    public Vector3f facing() {
        return new Vector3f((float) (Math.cos(vertAngle) * Math.cos(horAngle)),
                (float) (Math.cos(vertAngle) * Math.sin(horAngle)),
                (float) (Math.sin(vertAngle)));
    }
}
