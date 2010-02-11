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

import net.irc.Entity;
import net.irc.IrcClient;
import net.irc.event.AbstractIrcEvent;
import net.irc.event.IrcEvent;
import net.irc.event.IrcEventListener;
import util.iterator.ArrayIterator;
import event.EventListener;

public class InviteCommand extends AbstractIrcCommand {
	private final String nick;
	private final String channel;
	
	public InviteCommand(Entity origin, String nick, String channel) {
		super(origin);
		validateNick(nick);
		validateChannel(channel);
		this.nick = nick;
		this.channel = channel;
	}

	public InviteCommand(String nick, String channel) {
		this(null, nick, channel);
	}
	
	public String getChannel() {
		return channel;
	}
	
	public String getNick() {
		return nick;
	}

	@Override
	public Iterator<String> getArguments() {
		return new ArrayIterator<String>(new String[] {getNick(), getChannel()});
	}

	@Override
	public String getCommand() {
		return "INVITE";
	}

	@Override
	public IrcEvent getEvent(final IrcClient client) {
		return new AbstractIrcEvent(client) {
			@Override
			protected void doTrigger(EventListener listener) {
				if(listener instanceof IrcEventListener)
					((IrcEventListener) listener).inviteEvent(client, InviteCommand.this);
			}
		};
	}
}
