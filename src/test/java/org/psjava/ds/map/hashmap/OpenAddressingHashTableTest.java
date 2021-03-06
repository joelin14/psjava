package org.psjava.ds.map.hashmap;

import static org.junit.Assert.*;

import org.junit.Test;
import org.psjava.ds.map.hashmap.QuadraticProbing;
import org.psjava.javautil.IterableToString;
import org.psjava.javautil.VarargsIterable;

public class OpenAddressingHashTableTest {

	@Test
	public void testCalcBucketSize() {
		assertEquals(1, OpenAddressingHashTable.calcBucketSize(0));
		assertEquals(1 << 28, OpenAddressingHashTable.calcBucketSize(100000000));
	}

	@Test
	public void testPutToCurrentArray() {
		OpenAddressingHashTable<Integer, String> table = create(4);
		Object[][] data = { { 1, "A" }, { 1, "B" }, { 9, "C" }, { 17, "D" } };
		putToCurrentArray(table, data);
		assertEquals("(null,1=B,9=C,null,null,17=D,null,null)", toBucketString(table));
		assertEquals(3, table.load);
		table.remove(9);
		assertEquals("(null,1=B,<removed>,null,null,17=D,null,null)", toBucketString(table));
		assertEquals(3, table.load);
		table.put(9, "E");
		assertEquals("(null,1=B,<removed>,9=E,null,17=D,null,null)", toBucketString(table));
		assertEquals(4, table.load);
	}

	@Test
	public void testCapacityExtension() {
		OpenAddressingHashTable<Integer, String> table = create(1);
		putToCurrentArray(table, new Object[][] { { 2, "A" }, { 1, "B" } });
		assertEquals("(2=A,1=B)", toBucketString(table));
		table.ensureArraysCapacity(2);
		assertEquals("(null,1=B,2=A,null)", toBucketString(table));
	}

	@Test
	public void testLazyDeletion() {
		OpenAddressingHashTable<Integer, String> table = create(1);
		table.putToCurrentArray(1, "A");
		table.remove(1);
		assertEquals("(null,<removed>)", toBucketString(table));
		table.ensureArraysCapacity(2);
		assertEquals("(null,null,null,null)", toBucketString(table));
	}

	@Test
	public void testAutoExpansionByPut() {
		OpenAddressingHashTable<Integer, String> table = create(1);
		table.put(1, "A");
		table.put(2, "B");
		assertEquals("(null,1=A,2=B,null)", toBucketString(table));
	}

	@Test
		public void testFindEntry() {
			OpenAddressingHashTable<Integer, String> table = create(2);
			table.put(1, "A");
			assertEquals("A", table.get(1, null));
			table.put(5, "B");
			assertEquals("A", table.get(1, null));
			assertEquals("B", table.get(5, null));
			assertNull(table.get(2, null));
		}

	@Test
	public void testSize() {
		OpenAddressingHashTable<Integer, Integer> table = create(2);
		table.put(1, 0);
		assertEquals(1, table.size());
		table.put(2, 0);
		assertEquals(2, table.size());
	}

	@Test
	public void testIteration() {
		OpenAddressingHashTable<Integer, String> table = create(4);
		putToCurrentArray(table, new Object[][] { { 17, "A" }, { 1, "B" }, { 9, "C" } });
		assertEquals("(17=A,1=B,9=C)", IterableToString.toString(table));
	}

	@Test
	public void testRemove() {
		OpenAddressingHashTable<Integer, Integer> table = create(4);
		for (int v : new int[] { 1, 9, 17, 25 })
			table.putToCurrentArray(v, 0);
		table.remove(9);
		assertEquals(3, table.size());
		assertEquals(1, table.lazyDeletedCount);
	}

	private <K, V> OpenAddressingHashTable<K, V> create(int reserve) {
		return new OpenAddressingHashTable<K, V>(QuadraticProbing.create(), reserve);
	}

	private static void putToCurrentArray(OpenAddressingHashTable<Integer, String> table, Object[][] data) {
		for (Object[] d : data)
			table.putToCurrentArray((Integer) d[0], (String) d[1]);
	}

	private static String toBucketString(OpenAddressingHashTable<Integer, String> table) {
		return IterableToString.toString(VarargsIterable.create(table.bucket));
	}

}
