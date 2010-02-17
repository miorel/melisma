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
package util;

import org.w3c.dom.Node;

import com.googlecode.lawu.util.Iterators;

public class Nodes {
	private Nodes() {
	}
	
	public static Node getChildNode(Node node, String... names) {
		for(String name: names) {
			if(node == null)
				break;
			Node next = null;
			for(Node cur: Iterators.iterator(node.getChildNodes()))
				if(cur.getNodeName().equals(name)) {
					next = cur;
					break;
				}
			node = next;
		}
		return node;
	}
	
	public static String getText(Node node, String... names) {
		String ret = null;
		node = getChildNode(node, names);
		if(node != null)
			for(Node cur: Iterators.iterator(node.getChildNodes()))
				if(cur.getNodeType() == Node.TEXT_NODE) {
					ret = cur.getTextContent();
					break;
				}
		return ret;
	}
}
