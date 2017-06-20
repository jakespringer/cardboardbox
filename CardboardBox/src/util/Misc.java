package util;

import java.util.function.Function;

public class Misc {
	public static boolean rangeIntersects(float lowA, float highA, float lowB, float highB) {
		return ((lowA < lowB && highA > lowB)
				|| (lowA < highB && highA >  highB)
				|| (lowB < lowA && highB > lowA)
				|| (lowB < highA && highB >  highA));
	}
	
	@SuppressWarnings("unchecked")
	public static <U, V> V[] map(U[] array, Function<U, V> mappingFunction) {
		V[] mapping = (V[]) new Object[array.length];
		for (int i=0; i<array.length; ++i) {
			mapping[i] = mappingFunction.apply(array[i]);
		}
		return mapping;
	}
}
