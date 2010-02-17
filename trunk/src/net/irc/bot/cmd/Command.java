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
package net.irc.bot.cmd;

import net.irc.Entity;
import net.irc.IrcClient;

public interface Command {
	public String getName();
	
	public void execute(IrcClient client, Entity origin, String channel, String args);
	
	public String getHelp();
	
	public String getHelp(String alias);
	
	public boolean matchesAlias(String alias);
	
	public boolean isHidden();
}
