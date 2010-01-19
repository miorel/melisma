/*
 * Copyright (C) 2009-2010 Miorel-Lucian Palii
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 */

package util.iterator;

import java.util.NoSuchElementException;

public class ArrayIterator<T> extends IteratorImpl<T> {
	private final T[] array;
	private int pointer;
	
	public ArrayIterator(T... array) {
		if(array == null)
			throw new IllegalArgumentException("can't iterate over null array");
		this.array = array;
		this.pointer = 0;
	}
	
	@Override
	public boolean hasNext() {
		return pointer < array.length;
	}

	@Override
	public T next() {
		if(!hasNext())
			throw new NoSuchElementException("iteration finished");
		return array[pointer++];
	}
}
