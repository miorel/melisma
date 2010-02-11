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

package net.irc.bot.cmd;

import net.irc.Entity;
import net.irc.IrcClient;
import net.irc.bot.CommandShell;
import net.irc.cmd.PrivmsgCommand;

public class Logout extends AbstractCommand {
	public Logout(CommandShell shell) {
		super(shell, "logout", "(?:deauth(?:enticate)?|logout)");
	}
	
	@Override
	public void execute(IrcClient client, Entity origin, String channel, String args) {
		getTemporaryMemory().getNode("auth").removeChild(origin.toString());
		client.send(new PrivmsgCommand(origin.getNick(), "You are logged out."));
	}
	
	@Override
	public String getHelp() {
		String trigger = getShell().getTrigger();
		return String.format("Usage: \2%slogout\2. Logs you out of my user system.", trigger);
	}
}
