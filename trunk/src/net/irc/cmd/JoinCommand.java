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

package net.irc.cmd;

import java.util.Iterator;

import event.EventListener;

import net.irc.Entity;
import net.irc.IrcClient;
import net.irc.event.AbstractIrcEvent;
import net.irc.event.IrcEvent;
import net.irc.event.IrcEventListener;
import util.iterator.ArrayIterator;

public class JoinCommand extends AbstractIrcCommand {
	private final String channel;
	private final String key;
	
	public JoinCommand(Entity origin, String channel, String key) {
		super(origin);
		validateChannel(channel);
		validateString("the channel key", key, true, false);
		this.channel = channel;
		this.key = key;
	}
	
	public JoinCommand(String channel, String key) {
		this(null, channel, key);
	}
	
	public JoinCommand(Entity origin, String channel) {
		this(origin, channel, null);
	}
	
	public JoinCommand(String channel) {
		this(null, channel, null);
	}

	public static JoinCommand build(Entity origin, String[] param) {
		validateParam(param, 1, 2);
		String channel = param[0];
		String key = param.length > 1 ? param[1] : null;
		return new JoinCommand(origin, channel, key);
	}
	
	public String getChannel() {
		return channel;
	}
	
	public String getKey() {
		return key;
	}
	
	public boolean hasKey() {
		return key != null;
	}

	@Override
	public Iterator<String> getArguments() {
		String[] args = hasKey() ? new String[] {getChannel(), getKey()} : new String[] {getChannel()};
		return new ArrayIterator<String>(args);
	}

	@Override
	public String getCommand() {
		return "JOIN";
	}

	@Override
	public IrcEvent getEvent(final IrcClient client) {
		return new AbstractIrcEvent(client) {
			@Override
			protected void doTrigger(EventListener listener) {
				if(listener instanceof IrcEventListener)
					((IrcEventListener) listener).joinEvent(client, JoinCommand.this);
			}
		};
	}
}
