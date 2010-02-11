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

import com.googlecode.lawu.util.Iterators;

import event.EventListener;

public class QuitCommand extends AbstractIrcCommand {
	private final String message;
	
	public QuitCommand(Entity origin, String message) {
		super(origin);
		if(message != null && message.isEmpty())
			message = null;
		this.message = message;
		validateMessage(message, false);
	}

	public QuitCommand(Entity origin) {
		this(origin, null);
	}

	public QuitCommand(String message) {
		this(null, message);
	}

	public QuitCommand() {
		this(null, null);
	}
	
	public static QuitCommand build(Entity origin, String[] param) {
		validateParam(param, 0, 1);
		return new QuitCommand(origin, param.length == 0 ? null : param[0]);
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean hasMessage() {
		return message == null;
	}

	@Override
	public Iterator<String> getArguments() {
		String[] args = hasMessage() ? new String[] {getMessage()} : new String[0];
		return Iterators.iterator(args);
	}

	@Override
	public String getCommand() {
		return "QUIT";
	}
	
	@Override
	public IrcEvent getEvent(final IrcClient client) {
		return new AbstractIrcEvent(client) {
			@Override
			protected void doTrigger(EventListener listener) {
				if(listener instanceof IrcEventListener)
					((IrcEventListener) listener).quitEvent(client, QuitCommand.this);
			}
		};
	}
}
