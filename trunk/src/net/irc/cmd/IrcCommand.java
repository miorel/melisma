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
import com.googlecode.lawu.util.iterators.UniversalIterator;

public interface IrcCommand {
	public void execute(IrcClient client);
	
	public Entity getOrigin();
	
	public String getCommand();
	
	public UniversalIterator<String> getArguments();
	
	public IrcEvent getEvent(IrcClient client);
}
