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

public class Uptime extends AbstractCommand {
	private final long birth;
	
	public Uptime(CommandShell shell) {
		super(shell, "uptime");
		birth = System.currentTimeMillis();
	}

	@Override
	public String getHelp() {
		String trigger = getShell().getTrigger();
		return String.format("Usage: \2%suptime\2. Prints how long I've been alive.", trigger);
	}

	@Override
	public void execute(IrcClient client, Entity origin, String channel, String args) {
		String target = channel == null ? origin.getNick() : channel;
		client.send(new PrivmsgCommand(target, String.format("I was born %d seconds ago.", (System.currentTimeMillis() - birth) / 1000)));
	}
}
