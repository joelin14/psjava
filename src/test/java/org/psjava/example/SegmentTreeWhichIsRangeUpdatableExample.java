package org.psjava.example;

import junit.framework.Assert;

import org.junit.Test;
import org.psjava.ds.array.Array;
import org.psjava.ds.array.MutableArrayFromValues;
import org.psjava.ds.tree.segmenttree.RangeUpdatableSegmentTree;
import org.psjava.math.BinaryOperator;

public class SegmentTreeWhichIsRangeUpdatableExample {
	@Test
	public void example() {

		// This is an advanced version of segment tree.
		// This updates in range in O(logn) time.

		Array<Integer> init = MutableArrayFromValues.create(1, 1, 1, 1, 1);

		RangeUpdatableSegmentTree<Integer> sumTree = RangeUpdatableSegmentTree.create(init, new BinaryOperator<Integer>() {
			@Override
			public Integer calc(Integer a, Integer b) {
				return a + b;
			}
		});

		// Query by range.

		int sum1 = sumTree.query(0, 5);
		Assert.assertEquals(5, sum1);

		// Update range, this is done fast!

		sumTree.updateRange(1, 4, 100);
		int sum2 = sumTree.query(0, 5);
		Assert.assertEquals(302, sum2);

	}
}
