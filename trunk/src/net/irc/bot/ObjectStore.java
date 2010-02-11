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

package net.irc.bot;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import util.LogManager;
import util.ResourceLoader;
import util.net.Bitly;

public class ObjectStore {
	private final Map<String,Object> objectStore = new HashMap<String,Object>();
	
	public ObjectStore() {
		prepareUrlShortener();
	}
	
	public Object getObject(String key) {
		return objectStore.get(key);
	}
	
	public void putObject(String key, Object object) {
		objectStore.put(key, object);
	}
	
	protected void prepareUrlShortener() {
		Properties properties = ResourceLoader.getInstance().getProperties("etc/bitly.properties", true, false);
		try {
			putObject("bitly", new Bitly(properties.getProperty("login"), properties.getProperty("key")));
		}
		catch(Exception e) {
			LogManager.getInstance().report("OBJ_STORE", e);
		}
	}
}
