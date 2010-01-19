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

import java.util.Random;

import net.irc.Entity;
import net.irc.IrcClient;
import net.irc.bot.CommandShell;
import net.irc.cmd.PrivmsgCommand;

public class Roll extends AbstractCommand {
	private final Random rnd;
	
	public Roll(CommandShell shell) {
		super(shell, "roll");
		this.rnd = new Random();
	}

	@Override
	public String getHelp() {
		String trigger = getShell().getTrigger();
		return String.format("Usage: \2%1$sroll [n [s]]\2. Rolls \2n\2 \2s\2-sided dice. If not specified, \2s\2 defaults to 6, and \2n\2 to 1.", trigger);
	}

	@Override
	public void execute(IrcClient client, Entity origin, String channel, String args) {
		String target = channel == null ? origin.getNick() : channel;
		String[] arg = args.trim().toLowerCase().split("\\s+");
		int n = 1;
		int s = 6;
		String response = null;
		if(!arg[0].isEmpty()) {
			String nStr = arg[0];
			if(nStr.matches("-(?:\\d*\\.?\\d+|\\d+\\.?\\d*)"))
				response = "Are your dice made of antimatter?";
			else if(nStr.matches("\\d*\\.\\d+"))
				response = "Looks like one of your dice was abused.";
			else if(nStr.matches("[ivxlcdm]+"))
				response = "Alea iacta est.";
			else if(nStr.matches("\\d+\\.?")) {
				try {
					n = Integer.parseInt(nStr.replaceAll("\\.$", ""));
					if(n > 128)
						throw new NumberFormatException();
					if(n == 0)
						response = "That won't be a very interesting roll.";
				}
				catch(NumberFormatException e) {
					response = "Sorry, but that's a few dice too many.";
				}
			}
			else
				response = "Stick to the decimal number system, 'kay?";
		}
		if(response == null && arg.length > 1) {
			String sStr = arg[1];
			if(sStr.matches("(?:-(?:\\d*\\.?\\d+|\\d+\\.?\\d*))"))
				response = "Your dice have a fascinating topology!";
			else if(sStr.matches("\\d*\\.\\d+"))
				response = "Fractional requests get fractional responses.";
			else if(sStr.matches("[ivxlcdm]+"))
				response = "Alea iacta est.";
			else if(sStr.matches("\\d+\\.?")) {
				try {
					s = Integer.parseInt(sStr.replaceAll("\\.$", ""));
				}
				catch(Exception e) {
					response = "I don't like that many sides. Try again.";
				}
				if(response == null && s <= 1)
					response = "Those dice won't roll very well.";
			}
			else
				response = "Stick to the decimal number system, 'kay?";
		}
		if(response == null) {
			StringBuilder sb = new StringBuilder();
			sb.append("Roll results:");
			while(n-- != 0)
				sb.append(' ').append(rnd.nextInt(s) + 1);
			response = sb.toString();
		}
		client.send(new PrivmsgCommand(target, response));
	}
}
