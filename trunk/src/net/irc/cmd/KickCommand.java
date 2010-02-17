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

import net.irc.Entity;
import net.irc.IrcClient;
import net.irc.event.AbstractIrcEvent;
import net.irc.event.IrcEvent;
import net.irc.event.IrcEventListener;

import com.googlecode.lawu.util.Iterators;
import com.googlecode.lawu.util.iterators.UniversalIterator;

import event.EventListener;

public class KickCommand extends AbstractIrcCommand {
	private final String channel;
	private final String nick;
	private final String message;
	
	public KickCommand(Entity origin, String channel, String nick, String message) {
		super(origin);
		validateChannel(channel);
		validateNick(nick);
		validateMessage(message, false);
		this.channel = channel;
		this.nick = nick;
		this.message = message;
	}

	public KickCommand(String channel, String nick, String message) {
		this(null, channel, nick, message);
	}
	
	public KickCommand(Entity origin, String channel, String nick) {
		this(origin, channel, nick, null);
	}
	
	public KickCommand(String channel, String nick) {
		this(null, channel, nick, null);
	}
	
	public String getChannel() {
		return channel;
	}
	
	public String getNick() {
		return nick;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean hasMessage() {
		return message == null;
	}
	
	@Override
	public UniversalIterator<String> getArguments() {
		return Iterators.iterator(hasMessage() ? new String[] {getChannel(), getNick(), getMessage()} : new String[] {getChannel(), getNick()});
	}

	@Override
	public String getCommand() {
		return "KICK";
	}

	@Override
	public IrcEvent getEvent(final IrcClient client) {
		return new AbstractIrcEvent(client) {
			@Override
			protected void doTrigger(EventListener listener) {
				if(listener instanceof IrcEventListener)
					((IrcEventListener) listener).kickEvent(client, KickCommand.this);
			}
		};
	}
}
