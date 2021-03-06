package org.psjava.ds.tree.segmenttree;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.psjava.ds.array.Array;
import org.psjava.ds.array.MutableArrayFromValues;
import org.psjava.ds.array.UniformArray;
import org.psjava.javautil.FromTo;

public class EnhancedRangeUpdatableSegmentTreeTest {

	private static final Random RANDOM = new Random();

	@Test
	public void testQuery() {
		EnhancedRangeUpdatableSegmentTree<Integer, Integer> tree = createAddTree(MutableArrayFromValues.create(1, 2, 3, 4, 5, 6, 7, 8));
		Assert.assertEquals(10, (int) tree.queryRange(0, 4));
	}

	@Test
	public void testUpdateSingle() {
		EnhancedRangeUpdatableSegmentTree<Integer, Integer> tree = createAddTree(MutableArrayFromValues.create(1, 2, 3, 4, 5, 6, 7, 8));
		tree.updateRange(0, 2, 1);
		Assert.assertEquals(38, (int) tree.queryRange(0, 8));
	}

	@Test
	public void testLazyUpdate() {
		EnhancedRangeUpdatableSegmentTree<Integer, Integer> tree = createAddTree(MutableArrayFromValues.create(1, 2, 3, 4, 5, 6, 7, 8));

		tree.updateRange(2, 4, 100);

		EnhancedRangeUpdatableSegmentTree<Integer, Integer>.NodeData data = tree.root.getLeft().getRight().getData();
		Assert.assertTrue(data.lazy);
		Assert.assertEquals(207, (int) data.merged); // 103+104

		int r = tree.queryRange(1, 3); // 2+103

		Assert.assertEquals(105, r);
		Assert.assertFalse(data.lazy);
	}

	@Test
	public void testFeatureUsingNaiveMethod() {
		int n = 100;
		EnhancedRangeUpdatableSegmentTree<Integer, Integer> tree = createAddTree(UniformArray.create(0, n));
		int[] a = new int[n];
		for (int i = 0; i < 100; i++) {
			int s = RANDOM.nextInt(n);
			int e = RANDOM.nextInt(n - s) + s + 1;
			int v = RANDOM.nextInt(20);
			if (i % 2 == 0) {
				tree.updateRange(s, e, v);
				for (int j : FromTo.get(s, e))
					a[j] += v;
			} else {
				int sum = 0;
				for (int j : FromTo.get(s, e))
					sum += a[j];
				Assert.assertEquals(sum, (int) tree.queryRange(s, e));
			}
		}
	}

	@Test(timeout = 10000)
	public void testPerformance() {
		int n = 100000;
		EnhancedRangeUpdatableSegmentTree<Integer, Integer> tree = createAddTree(UniformArray.create(0, n));
		for (int i = 0; i < 109000; i++) {
			int s = RANDOM.nextInt(n);
			int e = RANDOM.nextInt(n - s) + s + 1;
			if (i % 2 == 0)
				tree.updateRange(s, e, RANDOM.nextInt(20));
			else
				tree.queryRange(s, e);
		}
	}

	private EnhancedRangeUpdatableSegmentTree<Integer, Integer> createAddTree(Array<Integer> init) {
		return new EnhancedRangeUpdatableSegmentTree<Integer, Integer>(init, new EnhancedRangeUpdatableSegmentTreeOperator<Integer, Integer>() {

			@Override
			public Integer mergeSingleValue(Integer v1, Integer v2) {
				return v1 + v2;
			}

			@Override
			public Integer mergeRangeValue(Integer oldRangeValue, int rangeSize, Integer updateData) {
				return oldRangeValue + rangeSize * updateData;
			}

			@Override
			public Integer mergeUpdateData(Integer oldValue, Integer newValue) {
				return oldValue + newValue;
			}
		});
	}

}
