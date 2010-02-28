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
import java.util.Random;

import net.irc.IrcClient;
import net.irc.bot.CommandShell;
import net.irc.cmd.PrivmsgCommand;
import util.LogManager;
import util.ds.StringNode;

import com.googlecode.lawu.net.irc.Entity;

public class Login extends AbstractCommand {
	private final StringNode auth;
	private final MessageDigest md;
	
	public Login(CommandShell shell) throws NoSuchAlgorithmException {
		super(shell, "login", "(?:auth(?:enticate)?|login|identify)");
		this.auth = getMemory().getNode("auth");
		this.md = MessageDigest.getInstance("SHA-1");
		if(auth.isLeaf()) {
			String password = "";
			byte[] buf = new byte[16];
			new Random().nextBytes(buf);
			for(byte b: buf)
				password += String.format("%02x", b);
			auth.putValue("root", getEncryptedString(password));
			LogManager lm = LogManager.getInstance();
			lm.info("AUTH", "There don't appear to be any users!");
			lm.info("AUTH", "I've created a root user.");
			lm.info("AUTH", "You can identify with the command: login root " + password);
		}
	}
	
	protected String getEncryptedString(String string) {
		byte[] buf; 
		synchronized(md) {
			md.update(string.getBytes());
			buf = md.digest();
		}
		String ret = "";
		for(byte b: buf)
			ret += String.format("%02x", b);
		return ret;
	}
	
	@Override
	public void execute(IrcClient client, Entity origin, String channel, String args) {
		if(channel != null)
			client.send(new PrivmsgCommand(channel, "You probably don't want to do that publicly."));
		else {
			String[] arg = args.trim().split("\\s+");
			if(arg[0].isEmpty()) {
				String username = getTemporaryMemory().getValue("auth", origin.toString());
				client.send(new PrivmsgCommand(origin.getNick(), username == null ? "You are not logged in at this time." : String.format("You are currently logged in as %s.", username)));	
			}
			else {
				String username = arg.length > 1 ? arg[0] : origin.getNick();
				String password = arg[arg.length - 1];
				String encryptedPassword = getMemory().getValue("auth", username);
				if(getEncryptedString(password).equals(encryptedPassword)) {
					getTemporaryMemory().getNode("auth", origin.toString()).setValue(username);
					client.send(new PrivmsgCommand(origin.getNick(), String.format("You have successfully identified as %s.", username)));
				}
				else
					client.send(new PrivmsgCommand(origin.getNick(), "Wrong username or password."));
			}
		}
	}
	
	@Override
	public String getHelp() {
		String trigger = getShell().getTrigger();
		return String.format("Usage: \2%slogin [[username] password]\2. Handles authentication to my user system. If called with only a password, the username defaults to your nick. If called without arguments, tells you if you're logged in.", trigger);
	}
}
