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

package util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.iterator.ArrayIterator;

public class Strings {
	private final static Pattern WORDS = Pattern.compile("(\\S)(\\S*)");
	
	private final static Map<String,String> XML_ESCAPE = new HashMap<String,String>();
	static {
		XML_ESCAPE.put("<", "&lt;");
		XML_ESCAPE.put(">", "&gt;");
		XML_ESCAPE.put("'", "&apos;");
		XML_ESCAPE.put("\"", "&quot;");
		XML_ESCAPE.put("&", "&amp;");
	}
	
	private Strings() {
	}
	
	public static String toTitleCase(String string) {
		return toTitleCase(string, Integer.MAX_VALUE);
	}

	public static String toTitleCase(String string, int limit) {
		StringBuffer sb = new StringBuffer();
		Matcher m = WORDS.matcher(string.toLowerCase());
		while(m.find() && --limit >= 0)
			m.appendReplacement(sb, m.group(1).toUpperCase() + m.group(2));
		m.appendTail(sb);
		return sb.toString();
	}
	
	public static String multiply(char character, int count) {
		return multiply(Character.toString(character), count);
	}
	
	public static String multiply(String string, int count) {
		if(string == null)
			throw new RuntimeException("can't multiply null string");
		if(count < 0)
			throw new RuntimeException("can't repeat a negative number of times");
		StringBuilder sb = new StringBuilder(string.length() * count);
		while(count-- > 0)
			sb.append(string);
		return sb.toString();
	}
	
	public static String join(String separator, Iterator<String> strings) {
		if(separator == null)
			throw new IllegalArgumentException("can't have null separator, use the empty string instead");
		if(separator == null)
			throw new IllegalArgumentException("can't join a null iteration");
		StringBuilder sb = new StringBuilder();
		while(strings.hasNext())
			sb.append(separator).append(strings.next());
		return sb.substring(separator.length());
	}
	
	public static boolean isSingleLine(String string) {
		boolean ret = true;
		for(int i = string.length(); --i >= 0;)
			if(Character.getType(string.charAt(i)) == Character.LINE_SEPARATOR) {
				ret = false;
				break;
			}
		return ret;
	}
	
	public static String escapeXml(String text) {
		StringBuffer sb = new StringBuffer();
		Matcher m = Pattern.compile("[<>&\'\"]").matcher(text);
		while(m.find())
			m.appendReplacement(sb, XML_ESCAPE.get(m.group()));
		m.appendTail(sb);
		return sb.toString();
	}
	
	public static String join(String separator, String... strings) {
		return join(separator, new ArrayIterator<String>(strings));
	}
}
