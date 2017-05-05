package util;

public class DebugTimer implements AutoCloseable {
	private String message;
	private long previousTime;
	
	private DebugTimer(String message) {
		this.message = message;
		this.previousTime = System.nanoTime();
	}

	public static DebugTimer measure(String message) {
		return new DebugTimer(message + ", ");
	}
	
	public static DebugTimer measure() {
		return new DebugTimer("");
	}
	
	@Override
	public void close() {
		double secondsPassed = (System.nanoTime() - previousTime) / 1e9;
		System.out.println(message + "seconds_elapsed=" + secondsPassed);
	}
}
