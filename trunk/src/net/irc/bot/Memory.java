/*
 * Copyright (C) 2010 Miorel-Lucian Palii
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
package net.irc.bot;

import static com.googlecode.lawu.util.Iterators.adapt;
import static com.googlecode.lawu.util.Iterators.iterator;
import util.ds.StringNode;

import com.googlecode.lawu.dp.Iterator;

public class Memory {
	private final StringNode root;
	
	public Memory(StringNode root) {
		this.root = root;
	}
	
	public StringNode getRoot() {
		return root;
	}
	
	public StringNode getNode(Iterator<String> keys) {
		StringNode ret = root;
		for(String key: adapt(keys)) {
			StringNode nextRet = ret.getChild(key);
			if(nextRet == null)
				nextRet = ret.putValue(key, null);
			ret = nextRet;
		}
		return ret;
	}
	
	public StringNode getNode(String... keys) {
		return getNode(iterator(keys));
	}
	
	public String getValue(Iterator<String> keys) {
		return getNode(keys).getValue();
	}

	public String getValue(String... keys) {
		return getNode(keys).getValue();
	}
}
