package org.psjava.ds.trie;
public interface TrieNode<T> {
	int getChildCount();
	boolean hasChild(T ch);
	TrieNode<T> getChild(T ch, TrieNode<T> def);
	TrieNode<T> getChild(T ch);
	Iterable<T> getEdges();
	void putChild(T ch, TrieNode<T> node);
}