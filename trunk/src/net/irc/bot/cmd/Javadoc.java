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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.irc.IrcClient;
import net.irc.bot.CommandShell;
import net.irc.cmd.PrivmsgCommand;

import com.googlecode.lawu.net.irc.Entity;
import com.googlecode.lawu.net.www.UrlShortener;
import com.googlecode.lawu.util.Iterators;
import com.googlecode.lawu.util.Strings;

public class Javadoc extends AbstractCommand {
	private final List<String> packages;
	private final UrlShortener shortener = new com.googlecode.lawu.net.www.Isgd();
	
	public Javadoc(CommandShell shell) throws IOException {
		super(shell, "javadoc");
		this.packages = Iterators.list(Iterators.lines(Strings.getUrl("http://java.sun.com/javase/6/docs/api/package-list")));
	}
	
	@Override
	public void execute(IrcClient client, Entity origin, String channel, String args) {
		String className = args.trim();
		String target = channel == null ? origin.getNick() : channel;
		if(className.isEmpty())
			client.send(new PrivmsgCommand(target, "Class name, please!"));
		else {
			List<String> results = new ArrayList<String>();
			for(String pkg: packages) {
				String fullClassName = String.format("%s.%s", pkg, className);
				try {
					results.add(Class.forName(fullClassName).getName());
				}
				catch(Exception e) {
				}
			}
			if(results.isEmpty())
				client.send(new PrivmsgCommand(target, "Sorry, I couldn't find any classes with that name. Please note that my lookup is case sensitive."));
			else {
				Collections.sort(results);
				try {
					String msg = "Results: ";
					for(String result: results)
						msg += result + " - " + shortener.shorten("http://java.sun.com/javase/6/docs/api/" + result.replace('.', '/') + ".html") + ", ";
					msg = msg.substring(0, msg.length() - 2);
					if(msg.length() > 500)
						msg = msg.substring(0, 500) + "...";
					client.send(new PrivmsgCommand(target, msg));
				}
				catch(Exception e) {
					e.printStackTrace();
					client.send(new PrivmsgCommand(target, "Oh noes, something bad happened."));	
				}
			}
		}
	}

	@Override
	public String getHelp() {
		String trigger = getShell().getTrigger();
		return String.format("Usage: \2%1$sjavadoc class\2. Links to documentation for the class with given name.", trigger);
	}
}
