package org.psjava.javautil;

public class Java2DArrayFiller {

	public static <T> void fill(T[][] array, T value) {
		for (T[] sub : array)
			Java1DArrayFiller.fill(sub, value);
	}
}
