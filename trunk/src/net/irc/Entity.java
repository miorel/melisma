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

package net.irc;

import java.net.SocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Entity {
	private final SocketAddress address;
	private final String nick;
	private final String identd;
	private final String host;

	private final static Pattern ENTITY_INFO = Pattern.compile("(?:([^!@]+)!)?(?:([^@]+)@)?(.+)", Pattern.DOTALL);

	public Entity(SocketAddress address, String nick, String identd, String host) {
		if(nick != null && nick.isEmpty())
			throw new IllegalArgumentException("the nickname may not be zero-length, use null instead");
		if(identd != null && identd.isEmpty())
			throw new IllegalArgumentException("the identifier may not be zero-length, use null instead");
		if(host == null || host.isEmpty())
			throw new IllegalArgumentException("the host may not be null or zero-length");
		this.address = address;
		this.nick = nick;
		this.identd = identd;
		this.host = host;
	}

	public Entity(SocketAddress address, String entityInfo) {
		if(entityInfo == null)
			throw new IllegalArgumentException("can't parse null string");
		Matcher m = Entity.ENTITY_INFO.matcher(entityInfo);
		if(!m.matches())
			throw new IllegalArgumentException("improperly formatted info string");
		this.address = address;
		this.nick = m.group(1);
		this.identd = m.group(2);
		this.host = m.group(3);
	}

	public SocketAddress getAddress() {
		return address;
	}
	
	public String getNick() {
		return nick;
	}

	public String getIdentd() {
		return identd;
	}

	public String getHost() {
		return host;
	}

	public boolean isUser() {
		return nick != null && identd != null;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	public static Entity forInfo(SocketAddress address, String entityInfo) {
		Entity ret = null;
		try {
			ret = new Entity(address, entityInfo);
		}
		catch(IllegalArgumentException e) {}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if(o instanceof Entity) {
			Entity u = (Entity) o;
			ret = (this.address == null ? u.address == null : this.address.equals(u.address))
				&& (this.nick == null ? u.nick == null : this.nick.equals(u.nick))
				&& (this.identd == null ? u.identd == null : this.identd.equals(u.identd))
				&& this.host.equals(u.host);
		}
		return ret;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(nick != null)
			sb.append(nick).append('!');
		if(identd != null)
			sb.append(identd).append('@');
		sb.append(host).append(" on ").append(address);
		return sb.toString();
	}
}
