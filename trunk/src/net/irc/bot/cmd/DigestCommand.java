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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.irc.IrcClient;
import net.irc.bot.CommandShell;
import net.irc.cmd.PrivmsgCommand;

import com.googlecode.lawu.net.irc.Entity;

public abstract class DigestCommand extends AbstractCommand {
	public DigestCommand(CommandShell shell, String name) {
		super(shell, name);
	}

	public DigestCommand(CommandShell shell, String name, String regex) {
		super(shell, name, regex);
	}

	protected abstract String getAlgorithm();
	
	@Override
	public String getHelp() {
		String trigger = getShell().getTrigger();
		return String.format("Usage: \2%1$s%2$s string\2. Computes the %3$s digest of the specified string.", trigger, getName(), getAlgorithm());
	}

	@Override
	public void execute(IrcClient client, Entity origin, String channel, String args) {
		String target = channel == null ? origin.getNick() : channel;
		args = args.replaceFirst("^\\s+", "");
		try {
			MessageDigest md = MessageDigest.getInstance(getAlgorithm());
			md.update(args.getBytes());
			String digest = "";
			for(byte b: md.digest())
				digest += String.format("%02x", b);
			client.send(new PrivmsgCommand(target, getAlgorithm() + " digest of \"" + args + "\": " + digest));
		}
		catch(NoSuchAlgorithmException e) {
			client.send(new PrivmsgCommand(target, "There was a problem loading a " + getAlgorithm() + " algorithm implementation."));
		}
	}
}
