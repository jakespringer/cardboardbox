package util;

import java.util.Collection;
import java.util.Iterator;

public class InsertOnlyArray<T> {
	private T[] backingArray;
	
	@SuppressWarnings("unchecked")
	public InsertOnlyArray(int capacity) {
		backingArray = (T[]) new Object[capacity];
	}
	
	@SafeVarargs
	public InsertOnlyArray(T... values) {
		this(values.length);
		for (int i=0; i<values.length; ++i) {
			set(i, values[i]);
		}
	}
	
	public InsertOnlyArray(Collection<T> values) {
		this(values.size());
		int size = values.size();
		Iterator<T> iter = values.iterator();
		for (int i=0; i<size; ++i) {
			set(i, iter.next());
		}
	}
	
	public InsertOnlyArray() {
		this(32);
	}
	
	public <J extends T> J set(int index, J value) {
		if (index >= backingArray.length) {
			grow(((index * 3) / 2) + 1);
		}
		
		backingArray[index] = value;
		
		return value;
	}
	
	public T get(int index) {
		if (index >= backingArray.length) {
			return null;
		} else {
			return backingArray[index];
		}
	}
	
	protected void grow() {
		grow(((backingArray.length * 3) / 2) + 1);
	}
	
	@SuppressWarnings("unchecked")
	protected void grow(int newSize) {
		assert newSize > backingArray.length;
		T[] oldArray = backingArray;
		backingArray = (T[]) new Object[newSize];
		System.arraycopy(oldArray, 0, backingArray, 0, oldArray.length);
	}
}
