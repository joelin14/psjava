package org.psjava.algo.search;

import org.junit.Assert;
import org.junit.Test;
import org.psjava.math.Function;
import org.psjava.math.ns.IntegerNumberSystem;


public class BinarySearchFirstFalseTest {

	@Test
	public void test() {
		int index = BinarySearchFirstFalse.search(IntegerNumberSystem.getInstance(), new Function<Integer, Boolean>() {
			@Override
			public Boolean get(Integer index) {
				return index < 400;
			}
		}, -10000, 10000, -1);
		Assert.assertEquals(400, index);
	}

}
