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
import util.Dates;

import com.googlecode.lawu.dp.Iterator;
import com.googlecode.lawu.net.irc.Entity;
import com.googlecode.lawu.net.www.spoj.Submission;
import com.googlecode.lawu.net.www.spoj.User;

public class Spoj extends AsynchronousCommand {	
	public Spoj(CommandShell shell) {
		super(shell, "spoj");
	}

	@Override
	public String getHelp() {
		String trigger = getShell().getTrigger();
		return String.format("Usage: \2%sspoj user\2. Reports the result of user's last submission on the Sphere Online Judge (SPOJ - http://www.spoj.pl/).", trigger);
	}
	
	@Override
	protected void doExecute(IrcClient client, Entity origin, String channel, String args) {
		String target = channel == null ? origin.getNick() : channel;
		String username = args.trim();
		if(args.isEmpty())
			client.send(new PrivmsgCommand(target, "Please specify a username."));
		else {
			try {
				User spojUser = new User(username);
				Iterator<Submission> submits = spojUser.getSubmissions().reverse();
				if(submits.isDone()) 
					client.send(new PrivmsgCommand(target, String.format("Hmm, it doesn't look like %s made any submissions. Does this user even exist?", username)));
				else {
					Submission s = submits.current();
					String msg = String.format("%s - %s submitted %s in %s", Dates.time(s.getDate()), username, s.getProblem(), s.getLanguage().getRealName());
					switch(s.getResult()) {
					
					}
					client.send(new PrivmsgCommand(target, msg));
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				client.send(new PrivmsgCommand(target, "Error! " + e.getMessage()));
			}
		}
	}
}
