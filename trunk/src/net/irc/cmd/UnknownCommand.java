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

import java.util.Arrays;
import java.util.Iterator;

import net.irc.Entity;
import net.irc.IrcClient;
import net.irc.event.AbstractIrcEvent;
import net.irc.event.IrcEvent;
import net.irc.event.IrcEventListener;

import com.googlecode.lawu.util.Iterators;

import event.EventListener;

public class UnknownCommand extends AbstractIrcCommand {
	private final String command;
	private final String[] param;
	
	public UnknownCommand(Entity origin, String command, String[] param) {
		super(origin);
		validateString("the command ", command, false, false);
		validateParam(param, 0, Integer.MAX_VALUE);
		this.command = command;
		this.param = Arrays.copyOf(param, param.length);
	}

	@Override
	public Iterator<String> getArguments() {
		return Iterators.iterator(param);
	}

	@Override
	public String getCommand() {
		return command;
	}

	@Override
	public IrcEvent getEvent(final IrcClient client) {
		return new AbstractIrcEvent(client) {
			@Override
			protected void doTrigger(EventListener listener) {
				if(listener instanceof IrcEventListener)
					((IrcEventListener) listener).unknownCommandEvent(client, UnknownCommand.this);
			}
		};
	}
}
