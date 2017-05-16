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
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.glfw.GLFW;

import chunk.SimplexNoiseChunkSupplier;
import chunk.World;
import entity.SimpleEntity;
import opengl.BufferObject;
import opengl.BufferObject.BufferObjectResource;
import opengl.BufferObject.Target;
import opengl.BufferObject.UsageHint;
import opengl.ShaderProgram;
import opengl.ShaderProgram.ShaderProgramResource;
import opengl.TextureObject;
import opengl.TextureObject.TextureObjectResource;
import opengl.VertexArrayObject;
import opengl.VertexArrayObject.VertexArrayObjectResource;
import util.Resources;

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
		world = new World(new SimplexNoiseChunkSupplier());
		
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
		
		SimpleEntity character = new SimpleEntity(new Vector3f(0, 0, 0));
		
		VertexArrayObject sunVao = new VertexArrayObject();
		BufferObject sunVbo = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, new float[] {
				0, 0, 0,
				1, 0, 0,
				0, 1, 0,
				1, 1, 0,
		});
		TextureObject sunTexture = new TextureObject(TextureObject.Target.TEXTURE_2D, new float[] {
				1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,
				1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,
				1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,
				1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,
				1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,
				1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,
				1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,
				0.0f, 0.90f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,     1.0f, 0.99f, 0.90f,
		}, 2, 2);
		ShaderProgram sunShader = new ShaderProgram(Resources.get("glsl/sun.vert"), Resources.get("glsl/sun.frag"));
		
		previousNanoTime = System.nanoTime();
		long handle = Window.getWindowHandle();
		while (!glfwWindowShouldClose(handle)) {
			currentNanoTime = System.nanoTime();
			deltaNanoTime = currentNanoTime - previousNanoTime;
			dt = (double) deltaNanoTime / 1e9;
			previousNanoTime = currentNanoTime;
			Window.updateFps(1 / dt);
			
            glfwPollEvents();
            updateCameraPosition();
            
            glClearColor(135.f / 256.f, 206.f / 256.f, 250.f / 256.f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); 
            
            // draw the sun
            try (VertexArrayObjectResource vaor = sunVao.use();
       			 ShaderProgramResource spr = sunShader.use();
       			 TextureObjectResource tor = sunTexture.use()) {
            	spr.setUniform("projectionMatrix", Scene.getProjectionMatrix());
            	spr.setUniform("modelMatrix", Scene.getCamera()
            			.getRotationalViewMatrix()
            			.rotate((float) Math.PI / 4.f, 1, 0, 0)
            			.translate(new Vector3f(-0.5f, -0.5f, -10)));
            	
       			glEnableVertexAttribArray(0);
       			try (BufferObjectResource bor = sunVbo.use()) {
       				glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
       			}
       			
       			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);       			
       			glDisableVertexAttribArray(0);
       		}
            
            // everything else goes in front of the sun
            glClear(GL_DEPTH_BUFFER_BIT);
            
            character.render();
            
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
        float cameraSpeed = glfwGetKey(Window.getWindowHandle(), GLFW.GLFW_KEY_LEFT_ALT) != 0 ? ((float) (10.0 * dt)) : (float) (200.0 * dt);
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
