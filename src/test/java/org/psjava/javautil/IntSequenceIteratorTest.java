package org.psjava.javautil;

import static org.junit.Assert.*;


import org.junit.Test;
import org.psjava.TestUtil;

public class IntSequenceIteratorTest {

	@Test
	public void testIntSequenceIterable() {
		assertEquals(TestUtil.toArrayList(4, 6, 8), TestUtil.toArrayListFromIterator(IntSequenceIterator.create(4, 2, 3)));
		assertEquals(TestUtil.toArrayList(), TestUtil.toArrayListFromIterator(IntSequenceIterator.create(4, 1, 0)));
	}

}
