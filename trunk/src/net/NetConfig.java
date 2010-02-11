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

package net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetConfig implements Cloneable {
	public static final String LOCALHOST;
	static {
		String localhost = "127.0.0.1";
		try {
			localhost = InetAddress.getLocalHost().getHostAddress();
		}
		catch(UnknownHostException e) {}
		LOCALHOST = localhost;
	}
	
	private String hostname = LOCALHOST;
	private int port = 0;
	
	public String getHost() {
		return hostname;
	}
	
	public void setHostname(String hostname) {
		if(hostname == null || hostname.isEmpty())
			throw new IllegalArgumentException("the hostname may not be null or have zero length");
		this.hostname = hostname;
	}
	
	public NetConfig() {
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		if(port < 0)
			throw new IllegalArgumentException("the port may not be negative");
		this.port = port;
	}
	
	public NetConfig clone() {
		NetConfig ret = null;
		try {
			ret = (NetConfig) super.clone();
		}
		catch(CloneNotSupportedException e) {
		}
		return ret;
	}
}
