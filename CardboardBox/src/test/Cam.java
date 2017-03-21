package test;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class Cam {

    public Vector3f position = new Vector3f(0, 0, 0);
    public float pitch, yaw;
    public Vector3f up = new Vector3f(0, 0, 1);

    public Matrix4f getViewMatrix(Vector3f posOffset) {
        posOffset = posOffset.sub(position, new Vector3f());

        // If the pitch and yaw angles are in degrees,
        // they need to be converted to radians. Here
        // I assume the values are already converted to radians.
        float cosPitch = (float) Math.cos(pitch);
        float sinPitch = (float) Math.sin(pitch);
        float cosYaw = (float) Math.cos(yaw);
        float sinYaw = (float) Math.sin(yaw);

        Vector3f xaxis = new Vector3f(cosYaw, 0, -sinYaw);
        Vector3f yaxis = new Vector3f(sinYaw * sinPitch, cosPitch, cosYaw * sinPitch);
        Vector3f zaxis = new Vector3f(sinYaw * cosPitch, -sinPitch, cosPitch * cosYaw);

        // Create a 4x4 view matrix from the right, up, forward and eye position vectors
        return new Matrix4f(
                xaxis.x, yaxis.x, zaxis.x, 0,
                xaxis.y, yaxis.y, zaxis.y, 0,
                xaxis.z, yaxis.z, zaxis.z, 0,
                -posOffset.dot(xaxis), -posOffset.dot(yaxis), -posOffset.dot(zaxis), 1
        );
    }

    public static Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;
        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }
    
    public Vector3fc getLookAt() {
    	return new Vector3f((float) (Math.sin(yaw)*Math.cos(pitch)),
    						(float) (Math.sin(yaw)*Math.sin(pitch)),
    						(float) (Math.cos(yaw)));
    }
}
