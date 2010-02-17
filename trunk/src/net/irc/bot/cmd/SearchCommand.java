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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.irc.Entity;
import net.irc.IrcClient;
import net.irc.bot.CommandShell;
import net.irc.cmd.PrivmsgCommand;
import util.LogManager;

import com.googlecode.lawu.net.UrlShortener;

public abstract class SearchCommand extends AsynchronousCommand {
	public SearchCommand(CommandShell shell, String name) {
		super(shell, name);
	}

	public SearchCommand(CommandShell shell, String name, String regex) {
		super(shell, name, regex);
	}

	protected String transformUrl(String url) {
		UrlShortener shortener = (UrlShortener) getObjectStore().getObject("bitly");
		if(shortener != null)
			try {
				url = shortener.shorten(url);
			}
			catch(Exception e) {}
		return url;
	}
	
	protected String prepareUrl(String format, String arg) throws UnsupportedEncodingException {
		return transformUrl(String.format(format, URLEncoder.encode(arg, "UTF-8")));
	}
	
	protected abstract String getEngineName();
	
	protected abstract String getEngineUrlFormat();
	
	@Override
	protected void doExecute(IrcClient client, Entity origin, String channel, String args) {
		String target = channel == null ? origin.getNick() : channel;
		if(args.isEmpty())
			client.send(new PrivmsgCommand(target, "Search terms, please!"));
		else
			try {
				client.send(new PrivmsgCommand(target, getEngineName() + " search for \"" + args + "\": " + prepareUrl(getEngineUrlFormat(), args)));
			}
			catch(Exception e) {
				LogManager.getInstance().report("SHELL SEARCH", e);
				client.send(new PrivmsgCommand(target, "Something really bad happened, so I can't help you right now. Try again later."));
			}
	}
	
	@Override
	public String getHelp() {
		String trigger = getShell().getTrigger();
		return String.format("Usage: \2%1$s%2$s query\2. Links to a %3$s search of the specified terms.", trigger, getName(), getEngineName());
	}
}
