package org.psjava.dp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.psjava.goods.GoodMutableMapFactory;
import org.psjava.goods.GoodMutableSetFactory;

public class MemoizationFactoryTest {

	MemoizationFactory instance = new MemoizationFactory(GoodMutableMapFactory.getInstance(), GoodMutableSetFactory.getInstance());

	@Test
	public void testFibonacci() {
		Memoization<Integer, Integer> memo = instance.create(new MemoizationFunction<Integer, Integer>() {
			@Override
			public Integer get(Integer input, Memoization<Integer, Integer> memo) {
				if (input == 0)
					return 0;
				else if (input == 1)
					return 1;
				else
					return memo.get(input - 2) + memo.get(input - 1);
			}
		});
		assertEquals(55, (int) memo.get(10));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInfiniteRecursion() {
		Memoization<Integer, Integer> memo = instance.create(new MemoizationFunction<Integer, Integer>() {
			@Override
			public Integer get(Integer input, Memoization<Integer, Integer> memo) {
				return memo.get(3);
			}
		});
		memo.get(3);
	}

}
