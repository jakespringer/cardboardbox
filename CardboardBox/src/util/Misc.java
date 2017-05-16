package util;

public class Misc {
	public static boolean rangeIntersects(float lowA, float highA, float lowB, float highB) {
		return ((lowA < lowB && highA > lowB)
				|| (lowA < highB && highA >  highB)
				|| (lowB < lowA && highB > lowA)
				|| (lowB < highA && highB >  highA));
	}
}
