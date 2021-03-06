package org.psjava.ds.trie;

import org.psjava.javautil.ConvertedDataIterable;
import org.psjava.javautil.DataConverter;
import org.psjava.javautil.StringMerger;

public class Trie<T> {

	private final TrieNodeFactory<T> factory;
	private final TrieNode<T> root;

	public Trie(TrieNodeFactory<T> nodeFactory) {
		this.factory = nodeFactory;
		root = factory.create();
	}

	public void add(Iterable<T> sequence) {
		TrieNode<T> cur = root;
		for (T v : sequence) {
			TrieNode<T> subTrie = cur.getChild(v, null);
			if (subTrie == null) {
				subTrie = factory.create();
				cur.putChild(v, subTrie);
			}
			cur = subTrie;
		}
	}

	public TrieNode<T> getRoot() {
		return root;
	}

	@Override
	public String toString() {
		return getNodeString(root);
	}

	// TODO separate to some helper class
	private String getNodeString(final TrieNode<T> node) {
		String r = "";
		if (node == root)
			r += "Trie";
		if (node.getChildCount() > 0) {
			r += "(";
			r += StringMerger.merge(ConvertedDataIterable.create(node.getEdges(), new DataConverter<T, String>() {
				@Override
				public String convert(T c) {
					return c + getNodeString(node.getChild(c));
				}
			}), ",");
			r += ")";
		}
		return r;
	}

}
