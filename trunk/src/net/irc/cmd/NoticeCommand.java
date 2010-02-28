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

import net.irc.IrcClient;
import net.irc.event.AbstractIrcEvent;
import net.irc.event.IrcEvent;
import net.irc.event.IrcEventListener;

import com.googlecode.lawu.event.EventListener;
import com.googlecode.lawu.net.irc.Entity;

public class NoticeCommand extends IrcTargetMessageCommand {	
	public NoticeCommand(Entity origin, String target, String message) {
		super(origin, target, message);
	}
	
	public NoticeCommand(String target, String message) {
		this(null, target, message);
	}

	public static NoticeCommand build(Entity origin, String[] param) {
		validateParam(param, 2);
		return new NoticeCommand(origin, param[0], param[1]);
	}
	
	@Override
	public String getCommand() {
		return "NOTICE";
	}

	@Override
	public IrcEvent getEvent(final IrcClient client) {
		return new AbstractIrcEvent(client) {
			@Override
			protected void doTrigger(EventListener listener) {
				if(listener instanceof IrcEventListener)
					((IrcEventListener) listener).noticeEvent(client, NoticeCommand.this);
			}
		};
	}
}
