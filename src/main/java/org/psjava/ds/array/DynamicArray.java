package org.psjava.ds.array;

import java.util.Iterator;

import org.psjava.javautil.EqualityTester;
import org.psjava.javautil.IterableEqualityTester;
import org.psjava.javautil.IterableHash;
import org.psjava.javautil.IterableToString;
import org.psjava.javautil.StrictEqualityTester;


public class DynamicArray<T> implements MutableArray<T>, EqualityTester<Array<T>> {
	
	public static <T> DynamicArray<T> create() {
		return new DynamicArray<T>();
	}
	
	private T[] a;
	private int asize;
	
	@SuppressWarnings("unchecked")
	public DynamicArray() {
		asize = 0;
		a = (T[])new Object[1];
	}

	@Override
	public T get(int index) {
		return a[index];
	}	
	
	@Override
	public void set(int index, T value) {
		a[index] = value;		
	}
	
	@Override
	public int size() {
		return asize;
	}
	
	public void clear() {
		asize = 0;
	}
	
	@SuppressWarnings("unchecked")
	public void reserve(int size) {
		if(a.length < size) {
			T[] na = (T[])new Object[size];
			for(int i=0;i<a.length;i++)
				na[i] = a[i];
			a = na;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addToLast(T value) {
		if(a.length == asize) {
			T[] ta = (T[])new Object[asize*2];
			for(int i=0;i<asize;i++)
				ta[i] = a[i];
			a =ta;
		}
		a[asize++] = value;						
	}
	
	public T removeLast() {
		T r = a[asize - 1];
		a[--asize] = null;
		return r;
	}
	
	@Override
	public boolean equals(Object obj) {
		return StrictEqualityTester.areEqual(this, obj, this);
	}

	@Override
	public boolean areEqual(Array<T> o1, Array<T> o2) {
		return IterableEqualityTester.areEqual(o1, o2);
	}

	@Override
	public int hashCode() {
		return IterableHash.hash(this);
	}

	@Override
	public final boolean isEmpty() {
		return asize == 0;
	}

	@Override
	public final Iterator<T> iterator() {
		return ArrayIterator.create(this);
	}

	@Override
	public final String toString() {
		return IterableToString.toString(this);
	}
	
}
