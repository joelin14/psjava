package org.psjava.algo.search;

import org.psjava.ds.array.Array;

public class LinearSearch {

	public static <T> int search(Array<T> array, T value, int def) {
		for (int i = 0; i < array.size(); i++)
			if (array.get(i).equals(value))
				return i;
		return def;
	}

}
