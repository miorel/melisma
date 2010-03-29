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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.googlecode.lawu.util.Streams;

public class ResourceLoader {
	private static final ResourceLoader resourceLoader = new ResourceLoader();
	private static final ClassLoader classLoader = ResourceLoader.class.getClassLoader();
	private static final DocumentBuilder documentBuilder;
	static {
		DocumentBuilder tmpDocBuilder = null;
		try {
			tmpDocBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch(ParserConfigurationException e) {
			LogManager.getInstance().report("[RESOURCE]", e);
		}
		documentBuilder = tmpDocBuilder;
	}
	
	private ResourceLoader() {
	}

	public static ResourceLoader getInstance() {
		return ResourceLoader.resourceLoader;
	}

	public InputStream getStream(String path) {
		return getStream(path, false, false);
	}

	public InputStream getStream(String path, boolean allowOverride, boolean create) {
		InputStream ret = null;
		File file = new File(path);
		if(allowOverride && file.exists() && file.canRead() && file.isFile()) {
			try {
				ret = new FileInputStream(file);
			}
			catch(FileNotFoundException e) {}
		}
		if(ret == null)
			ret = classLoader.getResourceAsStream(path);
		if(create && !file.exists()) {
			try {
				Streams.copy(ret, file);
			}
			catch(Exception e) {}
			ret = getStream(path, allowOverride, false);
		}
		return ret;
	}

	public Properties getProperties(String path) {
		return getProperties(path, false, false);
	}

	public Properties getProperties(String path, boolean allowOverride, boolean create) {
		InputStream stream = getStream(path, allowOverride, create);
		Properties ret = null;
		if(stream != null) {
			ret = new Properties();
			try {
				ret.load(stream);
			}
			catch(IOException e) {
				ret = null;
			}
		}
		return ret;
	}
	
	public Scanner getScanner(String path) {
		return getScanner(path, false, false);
	}

	public Scanner getScanner(String path, boolean allowOverride, boolean create) {
		InputStream stream = getStream(path, allowOverride, create);
		return stream == null ? null : new Scanner(stream);
	}
	
	public Document getXmlDocument(String path) throws SAXException, IOException {
		return getXmlDocument(path, false, false);
	}

	public Document getXmlDocument(String path, boolean allowOverride, boolean create) throws SAXException, IOException {
		InputStream stream = getStream(path, allowOverride, create);
		return stream == null ? null : documentBuilder.parse(stream);
	}
}
