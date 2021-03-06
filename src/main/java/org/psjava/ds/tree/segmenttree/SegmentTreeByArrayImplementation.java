package org.psjava.ds.tree.segmenttree;

import org.psjava.ds.array.Array;
import org.psjava.ds.tree.BinaryTreeByArray;
import org.psjava.javautil.AssertStatus;
import org.psjava.math.BinaryOperator;

public class SegmentTreeByArrayImplementation<T> implements SegmentTree<T> {

	/**
	 * This class is for only simple replacement updating. This class has an
	 * advantage of BinaryTreeByArray's speed.
	 */

	private final BinaryOperator<T> merger;
	private final BinaryTreeByArray<T> tree;
	private final int size;

	public SegmentTreeByArrayImplementation(final Array<T> initialData, final BinaryOperator<T> merger) {
		this.merger = merger;
		size = initialData.size();
		tree = new BinaryTreeByArray<T>();
		int root = tree.createRoot(initialData.get(0));
		construct(root, initialData, 0, initialData.size());
	}

	private void construct(int node, Array<T> initialData, int start, int end) {
		if (end - start == 1) {
			tree.setValue(node, initialData.get(start));
		} else {
			T any = initialData.get(0);
			int mid = (start + end) / 2;
			int left = tree.putChild(node, false, any);
			int right = tree.putChild(node, true, any);
			construct(left, initialData, start, mid);
			construct(right, initialData, mid, end);
			tree.setValue(node, merger.calc(tree.getValue(left), tree.getValue(right)));
		}
	}

	@Override
	public T query(int start, int end) {
		AssertStatus.assertTrue(start < end);
		return queryRecursively(tree.getRootPointer(), 0, size, start, end);
	}

	private T queryRecursively(int node, int nodeStart, int nodeEnd, int start, int end) {
		if (nodeStart == start && nodeEnd == end) {
			return tree.getValue(node);
		} else {
			int mid = (nodeStart + nodeEnd) / 2;
			if (end <= mid)
				return queryRecursively(tree.getLeft(node), nodeStart, mid, start, end);
			else if (mid <= start)
				return queryRecursively(tree.getRight(node), mid, nodeEnd, start, end);
			else
				return merger.calc(queryRecursively(tree.getLeft(node), nodeStart, mid, start, mid), queryRecursively(tree.getRight(node), mid, nodeEnd, mid, end));
		}
	}

	@Override
	public void update(int position, T value) {
		updateRecursively(tree.getRootPointer(), 0, size, position, value);
	}

	private void updateRecursively(int node, int nodeStart, int nodeEnd, int position, T value) {
		if (nodeEnd - nodeStart == 1) {
			tree.setValue(node, value);
		} else {
			int left = tree.getLeft(node);
			int right = tree.getRight(node);
			int mid = (nodeStart + nodeEnd) / 2;
			if (position < mid)
				updateRecursively(left, nodeStart, mid, position, value);
			else
				updateRecursively(right, mid, nodeEnd, position, value);
			tree.setValue(node, merger.calc(tree.getValue(left), tree.getValue(right)));
		}
	}
}