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

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.irc.Entity;
import net.irc.IrcClient;
import net.irc.bot.CommandShell;
import net.irc.cmd.PrivmsgCommand;
import util.Dates;
import util.net.spoj.Language;

public class Spoj extends AsynchronousCommand {
	private static final DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
	
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
		String username = args.trim().toLowerCase();
		if(args.isEmpty())
			client.send(new PrivmsgCommand(target, "Please specify a username."));
		else if(!username.matches("[a-z][a-z\\d_]{2,13}"))
			client.send(new PrivmsgCommand(target, String.format("Invalid username: %s", args)));
		else {
			String url = String.format("http://www.spoj.pl/status/%s/signedlist/", username);
			try {
				List<String> tokens = new ArrayList<String>();
				Scanner sc = new Scanner(new URL(url).openStream());
				while(sc.hasNextLine()) {
					String line = sc.nextLine();
					if(line.matches("\\s*(?:\\|-+){7}\\|\\s*"))
						break;
				}
				if(sc.hasNextLine()) {
					Pattern p = Pattern.compile("\\s*([^\\|]+?)\\s*(?=\\|)");
					Matcher m = p.matcher(sc.nextLine().trim());
					while(m.find())
						tokens.add(m.group(1));
				}
				if(tokens.size() < 7)
					client.send(new PrivmsgCommand(target, String.format("Hmm, it doesn't look like %s made any submissions. Does this user even exist?", username)));
				else {
					String date = Dates.time(DATE_PARSER.parse(tokens.get(1) + " CET"));
					String language = tokens.get(6);
					try {
						language = Language.forSpojName(language).toString();
					}
					catch(Exception e) {}
					client.send(new PrivmsgCommand(target, String.format("%s - %s submitted %s in %s and received %s.", date, username, tokens.get(2), language, tokens.get(3))));
				}
			}
			catch(Exception e) {
				client.send(new PrivmsgCommand(target, "There was a problem getting the data. Try again later."));
			}
		}
	}
}
