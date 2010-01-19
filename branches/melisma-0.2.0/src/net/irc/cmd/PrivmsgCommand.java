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

import net.irc.Entity;
import net.irc.IrcClient;
import net.irc.event.AbstractIrcEvent;
import net.irc.event.IrcEvent;
import net.irc.event.IrcEventListener;
import event.EventListener;

public class PrivmsgCommand extends IrcTargetMessageCommand {	
	public PrivmsgCommand(Entity origin, String target, String message) {
		super(origin, target, message);
	}
	
	public PrivmsgCommand(String target, String message) {
		this(null, target, message);
	}

	public static PrivmsgCommand build(Entity origin, String[] param) {
		validateParam(param, 2);
		return CtcpCommand.CTCP.matcher(param[1]).matches() ? CtcpCommand.build(origin, param) : new PrivmsgCommand(origin, param[0], param[1]);
	}
	
	@Override
	public String getCommand() {
		return "PRIVMSG";
	}

	@Override
	public IrcEvent getEvent(final IrcClient client) {
		return new AbstractIrcEvent(client) {
			@Override
			protected void doTrigger(EventListener listener) {
				if(listener instanceof IrcEventListener)
					((IrcEventListener) listener).privmsgEvent(client, PrivmsgCommand.this);
			}
		};
	}
}
