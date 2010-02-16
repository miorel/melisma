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
import util.LogManager;

public class Bitly extends AbstractCommand {
	private final com.googlecode.lawu.net.Bitly bitly;
	
	public Bitly(CommandShell shell) {
		super(shell, "bitly", "(?:bitly|short(?:en)?)");
		this.bitly = (com.googlecode.lawu.net.Bitly) getObjectStore().getObject("bitly");
	}
	
	@Override
	public void execute(IrcClient client, Entity origin, String channel, String args) {
		String url = args.trim();
		String target = channel == null ? origin.getNick() : channel;
		if(url.isEmpty()) {
			client.send(new PrivmsgCommand(target, "I haven't yet implemented clairvoyance. Please specify a URL to shorten."));
		}
		else
			try {
				client.send(new PrivmsgCommand(target, "Shortened URL: " + bitly.shorten(url)));
			}
			catch(Exception e) {
				LogManager.getInstance().report("BITLY", e);
				client.send(new PrivmsgCommand(target, "There was a problem shortening the URL. Please try again later."));
			}
	}

	@Override
	public String getHelp() {
		String trigger = getShell().getTrigger();
		return String.format("Usage: \2%1$sbitly url\2. Shortens the given URL through the http://bit.ly/ service.", trigger);
	}
}
