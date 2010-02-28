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
import net.irc.event.IrcEvent;

import com.googlecode.lawu.net.irc.Entity;

public class SqueryCommand extends IrcTargetMessageCommand {	
	public SqueryCommand(Entity origin, String target, String message) {
		super(origin, target, message);
	}
	
	public SqueryCommand(String target, String message) {
		this(null, target, message);
	}
	
	@Override
	public String getCommand() {
		return "SQUERY";
	}

	@Override
	public IrcEvent getEvent(IrcClient client) {
		// TODO Auto-generated method stub
		return null;
	}
}
