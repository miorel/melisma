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

import static com.googlecode.lawu.util.Iterators.iterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.irc.Entity;
import net.irc.IrcClient;
import net.irc.bot.CommandShell;
import net.irc.cmd.PrivmsgCommand;
import util.ds.StringNode;

import com.googlecode.lawu.util.Strings;

public class Cmds extends AbstractCommand {
	public Cmds(CommandShell shell) {
		super(shell, "cmds", "cmd(?:s|list)");
		StringNode node = getTemporaryMemory().getNode(CommandShell.UNKNOWN_CMD);
		node.setValue(node.getValue() + " Try \2%2$scmds\2 for a command list.");
	}
	
	@Override
	public void execute(IrcClient client, Entity origin, String channel, String args) {
		String target = origin.getNick();
		List<String> commands = new ArrayList<String>();
		for(Command command: getShell().getCommands())
			if(!command.isHidden())
				commands.add("\2" + command.getName() + "\2");
		Collections.sort(commands);
		client.send(new PrivmsgCommand(target, "Command list: " + Strings.join(", ", iterator(commands))));
	}
	
	@Override
	public String getHelp() {
		String trigger = getShell().getTrigger();
		return String.format("Usage: \2%scmds\2. Lists some of the commands I understand. There may be others but you'll have to discover them on your own :)", trigger);
	}
}
