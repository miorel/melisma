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

import com.googlecode.lawu.dp.Iterator;
import com.googlecode.lawu.util.Iterators;

import event.EventListener;

public class PingCommand extends IrcTargetsCommand {
	public PingCommand(Iterator<String> targets) {
		super(targets);
	}
	
	public PingCommand(Entity origin, Iterator<String> targets) {
		super(origin, targets);
	}
	
	public static PingCommand build(Entity origin, String[] param) {
		validateParam(param, 1);
		return new PingCommand(origin, Iterators.iterator(param[0].split(" ")));
	}
	
	@Override
	public IrcEvent getEvent(final IrcClient client) {
		return new AbstractIrcEvent(client) {
			@Override
			protected void doTrigger(EventListener listener) {
				if(listener instanceof IrcEventListener)
					((IrcEventListener) listener).pingEvent(client, PingCommand.this);
			}
		};
	}
	
	@Override
	public String getCommand() {
		return "PING";
	}
}
