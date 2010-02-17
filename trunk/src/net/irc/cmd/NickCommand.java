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

public class NickCommand extends IrcNicknameCommand {
	public NickCommand(Entity origin, String nick) {
		super(origin, nick);
	}
	
	public NickCommand(String nick) {
		this(null, nick);
	}

	public static NickCommand build(Entity origin, String[] param) {
		validateParam(param, 1);
		return new NickCommand(origin, param[0]);
	}
	
	@Override
	public String getCommand() {
		return "NICK";
	}

	@Override
	public IrcEvent getEvent(final IrcClient client) {
		return new AbstractIrcEvent(client) {
			@Override
			protected void doTrigger(EventListener listener) {
				if(listener instanceof IrcEventListener)
					((IrcEventListener) listener).nickEvent(client, NickCommand.this);
			}
		};
	}
}
