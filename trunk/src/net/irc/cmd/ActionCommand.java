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
import event.EventListener;

public class ActionCommand extends CtcpCommand {
	public ActionCommand(Entity origin, String target, String action) {
		super(origin, target, "ACTION", action);
		validateString("the action", action, false, false);
	}

	public ActionCommand(String target, String action) {
		this(null, target, action);
	}
	
	public static ActionCommand build(Entity origin, String target, String param) {
		return new ActionCommand(origin, target, param);
	}

	public String getAction() {
		return getCtcpArguments();
	}
	
	@Override
	public IrcEvent getEvent(final IrcClient client) {
		return new AbstractIrcEvent(client) {
			@Override
			protected void doTrigger(EventListener listener) {
				if(listener instanceof IrcEventListener)
					((IrcEventListener) listener).actionEvent(client, ActionCommand.this);
			}
		};
	}
}
