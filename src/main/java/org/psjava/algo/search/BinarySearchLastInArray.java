package org.psjava.algo.search;

import java.util.Comparator;

import org.psjava.ds.array.Array;
import org.psjava.ds.array.FunctionByArray;
import org.psjava.math.ns.IntegerNumberSystem;

public class BinarySearchLastInArray {

	public static <T> int search(Array<T> array, Comparator<T> sortedOrder, T target, int def) {
		return BinarySearchLast.search(IntegerNumberSystem.getInstance(), FunctionByArray.wrap(array), sortedOrder, 0, array.size(), target, def);
	}

}
