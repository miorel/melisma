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
package net.irc.cmd;

import java.net.SocketAddress;

import net.irc.Entity;
import net.irc.IrcClient;
import net.irc.event.AbstractIrcEvent;
import net.irc.event.IrcEvent;

import com.googlecode.lawu.dp.Iterator;
import com.googlecode.lawu.util.Strings;

import event.EventListener;

public abstract class AbstractIrcCommand implements IrcCommand {
	private final Entity origin;
	
	public AbstractIrcCommand(Entity origin) {
		this.origin = origin;
	}
	
	public Entity getOrigin() {
		return origin;
	}
	
	public SocketAddress getAddress() {
		return origin == null ? null : origin.getAddress();
	}
	
	protected void validateNick(String nick) {
		validateString("the nickname", nick, false, false);
	}

	protected void validateChannel(String channel) {
		validateString("the channel name", channel, false, false);
	}
	
	protected void validateMessage(String message, boolean required) {
		validateString("the message", message, !required, required);
	}

	protected static void validateParam(String[] param, int size) {
		validateParam(param, size, size);
	}
	
	protected static void validateParam(String[] param, int minSize, int maxSize) {
		if(param == null)
			throw new IllegalArgumentException("parameter array may not be null");
		if(param.length < minSize || param.length > maxSize)
			throw new IllegalArgumentException("parameter array had unexpected size");
		for(String p: param)
			if(p == null)
				throw new IllegalArgumentException("parameter may not be null");	
	}
	
	protected void validateString(String identifier, String string, boolean nullAllowed, boolean emptyAllowed) {
		String problem = null;
		if(string == null) {
			if(!nullAllowed) {
				problem = identifier + " may not be null";
				if(emptyAllowed)
					problem += ", use the empty string instead";
			}
		}
		else if(string.isEmpty()) {
			if(!emptyAllowed) {
				problem = identifier + " may not be zero length";
				if(nullAllowed)
					problem += ", use null instead";
			}
		}
		else if(!Strings.isSingleLine(string))
			problem = identifier + " may not be multi-line";
		if(problem != null)
			throw new IllegalArgumentException(problem);
	}

	@Override
	public void execute(IrcClient client) {
		StringBuilder sb = new StringBuilder();
		sb.append(getCommand().toUpperCase());
		for(Iterator<String> args = getArguments(); !args.isDone();) {
			String arg = args.current();
			args.advance();
			sb.append(' ');
			if(args.isDone() && !arg.matches("\\S*"))
				sb.append(':');
			sb.append(arg);
		}
		client.send(sb.toString());
	}
	
	public IrcEvent getEvent(IrcClient client) {
		return new AbstractIrcEvent(client) {
			@Override
			protected void doTrigger(EventListener listener) {
				throw new IllegalStateException(String.format("client did not expect %s command", getCommand()));
			}
		};
	}
}
