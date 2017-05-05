package game;

import java.io.File;

public class Main {
	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", new File("native").getAbsolutePath());
		Window.initialize();
		GameLoop.run();
	}
}
