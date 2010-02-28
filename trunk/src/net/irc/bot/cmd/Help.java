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

import net.irc.IrcClient;
import net.irc.bot.CommandShell;
import net.irc.cmd.PrivmsgCommand;

import com.googlecode.lawu.net.irc.Entity;

public class Help extends AbstractCommand {
	public Help(CommandShell shell) {
		super(shell, "help");
	}
	
	@Override
	public void execute(IrcClient client, Entity origin, String channel, String args) {
		args = args.trim().toLowerCase();
		String[] cmd = args.isEmpty() ? new String[0] : args.split("\\s+");
		String target = origin.getNick();
		if(cmd.length == 0) {
			String trigger = getShell().getTrigger();
			client.send(new PrivmsgCommand(target, "Hey there! I'm a bot. Right now I mostly observe human behavior but I dream of one day passing the Turing test. Until then, you'll need to use special language to interact with me."));
			client.send(new PrivmsgCommand(target, String.format("If you write a message that starts with my trigger (currently \2%1$s\2), I'll interpret it as a command. You've already discovered \2%1$shelp\2. Try \2%1$scmds\2 for a list of things I can do and \2%1$shelp command\2 for more detailed usage information on a specific command.", trigger)));			
		}
		else {
			Command command = getShell().getCommandForAlias(cmd[0]);
			if(command  != null)
				client.send(new PrivmsgCommand(target, command.getHelp(cmd[0])));
			else {
				String trigger = getShell().getTrigger();
				String format = getTemporaryMemory().getValue(CommandShell.UNKNOWN_CMD);
				client.send(new PrivmsgCommand(target, String.format(format, cmd[0], trigger)));
			}
		}
	}

	@Override
	public String getHelp() {
		String trigger = getShell().getTrigger();
		return String.format("Usage: \2%shelp [command]\2. Prints usage information for the command or a general message if one isn't specified.", trigger);
	}
}
