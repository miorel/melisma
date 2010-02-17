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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.irc.Entity;
import net.irc.IrcClient;
import net.irc.bot.CommandShell;
import net.irc.cmd.PrivmsgCommand;

public class Pick extends AbstractCommand {
	private final Random rnd;
	
	public Pick(CommandShell shell) {
		super(shell, "pick", "(?:pick|choose|select)");
		this.rnd = new Random();
	}

	@Override
	public String getHelp() {
		String trigger = getShell().getTrigger();
		return String.format("Usage: \2%1$spick A [or B [or C ...]]\2. Randomly picks one of the choices. Example: \2%1$spick apple pie or strawberry cheesecake\2", trigger);
	}

	@Override
	public void execute(IrcClient client, Entity origin, String channel, String args) {
		String target = channel == null ? origin.getNick() : channel;
		StringBuilder sb = new StringBuilder();
		List<String> choices = new ArrayList<String>();
		for(String choice: args.trim().split("(?:\\s+|^)[Oo][Rr](?:\\s+[Oo][Rr])*(?:\\s+|$)"))
			if(!choice.isEmpty())
				choices.add(choice);
		if(choices.isEmpty())
			sb.append("You haven't given me any choices.");
		else if(choices.size() == 1)
			sb.append("Surprisingly enough, I decided to pick: ").append(choices.get(0));
		else
			sb.append("I pick: ").append(choices.get(rnd.nextInt(choices.size())));
		client.send(new PrivmsgCommand(target, sb.toString()));
	}
}
