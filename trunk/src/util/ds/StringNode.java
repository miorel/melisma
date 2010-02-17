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
package util.ds;

import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.Nodes;

import com.googlecode.lawu.util.Strings;

public class StringNode {
	private Map<String,StringNode> children = new HashMap<String,StringNode>();
	private String value;
	
	public StringNode(String value) {
		this.value = value;
	}
	
	public StringNode() {
		this(null);
	}
	
	public String getValue() {
		return value;
	}
	
	public Map<String,StringNode> getChildren() {
		return Collections.unmodifiableMap(children);
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public StringNode getChild(String key) {
		return children.get(key);
	}
	
	public void putChild(String key, StringNode child) {
		if(key == null || child == null)
			throw new IllegalArgumentException("can't have null key or child");
		children.put(key, child);
	}

	public StringNode putValue(String key, String value) {
		StringNode ret = wrap(value);
		putChild(key, ret);
		return ret;
	}
	
	protected StringNode wrap(String value) {
		return new StringNode(value);
	}
	
	public void removeChild(String key) {
		children.remove(key);
	}
	
	public boolean isLeaf() {
		return getChildren().isEmpty();
	}
	
	public boolean hasValue() {
		return getValue() != null;
	}
	
	public void write(PrintStream out) {
		out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		doWrite(out);
		out.flush();
	}
	
	protected void doWrite(PrintStream out) {
		out.print("<node>");
		if(hasValue()) {
			out.print("<value>");
			out.print(Strings.escapeXml(getValue()));
			out.print("</value>");
		}
		for(Entry<String,StringNode> entry: getChildren().entrySet()) {
			out.print("<child><key>");
			out.print(Strings.escapeXml(entry.getKey().toString()));
			out.print("</key>");
			entry.getValue().doWrite(out);
			out.print("</child>");
		}
		out.print("</node>");
	}
	
	public static StringNode parse(Node xml) {
		StringNode ret = new StringNode();
		if(xml != null && !xml.getNodeName().equals("node"))
			xml = Nodes.getChildNode(xml, "node");
		if(xml != null) {
			NodeList xmlChildren = xml.getChildNodes();
			for(int i = 0; i != xmlChildren.getLength(); ++i) {
				Node xmlChild = xmlChildren.item(i);
				String xmlChildName = xmlChild.getNodeName();
				if(xmlChildName.equals("value")) {
					String xmlValue = Nodes.getText(xmlChild);
					if(xmlValue == null)
						throw new NoSuchElementException("empty node value");
					ret.setValue(xmlValue);
				}
				else if(xmlChildName.equals("child")) {
					String key = Nodes.getText(xmlChild, "key");
					Node childAsNode = Nodes.getChildNode(xmlChild, "node");
					if(key == null)
						throw new NoSuchElementException("missing child key");
					if(childAsNode == null)
						throw new NoSuchElementException("missing child node");
					ret.putChild(key, parse(childAsNode));
				}
			}
		}
		return ret;
	}
}
