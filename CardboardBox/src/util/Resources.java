package util;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Resources {
	private static ClassLoader classLoader = Resources.class.getClassLoader();
	
	public static String getResource(String path) {
		File file = new File(classLoader.getResource(path).getFile());
		StringBuilder result = new StringBuilder("");
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}
			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	
}
