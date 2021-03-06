package org.psjava.math;

import java.util.Comparator;

public class Min {
	public static <T> T min(T v1, T v2, Comparator<T> comp) {
		if(comp.compare(v1, v2) < 0)
			return v1;
		else
			return v2;
	}
}
