package game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import chunk.World;

public class GameLoop {
	private static long currentNanoTime;
	private static long previousNanoTime;
	private static long deltaNanoTime;
	private static double dt;
	
	private static double[] xBuffer = new double[1], yBuffer = new double[1];
	private static double xCursorPrevious;
	private static double yCursorPrevious;
	
	private static World world;
	
	public static void run() {
		world = new World();
		
		Thread computeThread = new Thread(GameLoop::computeThreadRun);
		computeThread.setDaemon(true);
		computeThread.start();
		mainThreadRun();
	}
	
	public static void mainThreadRun() {
		glfwGetCursorPos(Window.getWindowHandle(), xBuffer, yBuffer);
		xCursorPrevious = xBuffer[0];
		yCursorPrevious = yBuffer[0];
		
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LESS);
		
		previousNanoTime = System.nanoTime();
		long handle = Window.getWindowHandle();
		while (!glfwWindowShouldClose(handle)) {
			currentNanoTime = System.nanoTime();
			deltaNanoTime = currentNanoTime - previousNanoTime;
			dt = (double) deltaNanoTime / 1e9;
			previousNanoTime = currentNanoTime;
			Window.updateFps(1 / dt);
			
            glfwPollEvents();
            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); 
            
            updateCameraPosition();
            
            world.update();
            world.render();
            
            glfwSwapBuffers(handle);
        }
				
		Window.cleanup();
	}
	
	public static void computeThreadRun() {
		long handle = Window.getWindowHandle();
		while (!glfwWindowShouldClose(handle)) {
			world.compute();
		}
	}
	
	public static void updateCameraPosition() {
		Camera camera = Scene.getCamera();
		if (glfwGetKey(Window.getWindowHandle(), GLFW_KEY_ESCAPE) != 0) {
            glfwSetWindowShouldClose(Window.getWindowHandle(), true);
        }
        float cameraSpeed = (float) (200.0 * dt);
        if (glfwGetKey(Window.getWindowHandle(), GLFW_KEY_W) != 0) {
            Vector3fc forward = camera.facing();
            Vector3f horizontalForward = forward.mul(cameraSpeed, new Vector3f());
//            horizontalForward.y = 0;
            camera.position.add(horizontalForward);
        }
        if (glfwGetKey(Window.getWindowHandle(), GLFW_KEY_S) != 0) {
            Vector3fc forward = camera.facing();
            Vector3f horizontalForward = forward.mul(-cameraSpeed, new Vector3f());
//            horizontalForward.y = 0;
            camera.position.add(horizontalForward);
        }
        if (glfwGetKey(Window.getWindowHandle(), GLFW_KEY_A) != 0) {
            Vector3fc forward = camera.facing();
            Vector3f horizontalForward = forward.mul(cameraSpeed, new Vector3f());
            horizontalForward.y = 0;
            horizontalForward.rotateAbout((float) Math.PI / 2, 0, 1, 0);
            camera.position.add(horizontalForward);
        }
        if (glfwGetKey(Window.getWindowHandle(), GLFW_KEY_D) != 0) {
            Vector3fc forward = camera.facing();
            Vector3f horizontalForward = forward.mul(cameraSpeed, new Vector3f());
            horizontalForward.y = 0;
            horizontalForward.rotateAbout((float) -Math.PI / 2, 0, 1, 0);
            camera.position.add(horizontalForward);
        }
        
        if (glfwGetKey(Window.getWindowHandle(), GLFW_KEY_SPACE) != 0) {
            camera.position.add(0, -cameraSpeed, 0);
        }
        if (glfwGetKey(Window.getWindowHandle(), GLFW_KEY_LEFT_SHIFT) != 0) {
            camera.position.add(0, cameraSpeed, 0);
        }
        
        glfwGetCursorPos(Window.getWindowHandle(), xBuffer, yBuffer);
        
        double dx = xBuffer[0] - xCursorPrevious;
        double dy = yBuffer[0] - yCursorPrevious;
		xCursorPrevious = xBuffer[0];
		yCursorPrevious = yBuffer[0];

		camera.horAngle += dx / 500;
        camera.vertAngle += dy / 500;
        
        if (camera.vertAngle > 1.5) {
            camera.vertAngle = 1.5f;
        }
        
        if (camera.vertAngle < -1.5) {
            camera.vertAngle = -1.5f;
        }
	}
}
