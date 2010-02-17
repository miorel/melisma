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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import net.irc.Entity;
import net.irc.IrcClient;
import net.irc.bot.CommandShell;
import net.irc.cmd.PrivmsgCommand;
import util.ResourceLoader;

import com.googlecode.lawu.util.Strings;

public class Acronym extends AbstractCommand {
	private final Map<Character,List<String>> noun;
	private final Map<Character,List<String>> adjective;
	private final Random rnd = new Random();
	
	public Acronym(CommandShell shell) {
		super(shell, "acronym", "(?:acro(nym)?|acky)");
		Scanner sc;
		this.noun = new HashMap<Character,List<String>>();
		this.adjective = new HashMap<Character,List<String>>();
		for(char c = 'a'; c <= 'z'; ++c) {
			this.noun.put(c, new ArrayList<String>());
			this.adjective.put(c, new ArrayList<String>());
		}
		sc = ResourceLoader.getInstance().getScanner("lib/nouns.txt");
		while(sc.hasNextLine()) {
			String s = sc.nextLine();
			this.noun.get(s.charAt(0)).add(s);
		}
		sc = ResourceLoader.getInstance().getScanner("lib/adjectives.txt");
		while(sc.hasNextLine()) {
			String s = sc.nextLine();
			this.adjective.get(s.charAt(0)).add(s);
		}
	}

	@Override
	public void execute(IrcClient client, Entity origin, String channel, String args) {
		String target = channel == null ? origin.getNick() : channel;
		args = args.trim().split("\\s+")[0].toLowerCase();
		if(args.isEmpty())
			client.send(new PrivmsgCommand(target, "Please specify the acronym you want me to decipher."));
		else if(!args.matches("[a-z]+"))
			client.send(new PrivmsgCommand(target, "Letters only, if you please."));
		else {
			String response = String.format("%s = %s", args.toUpperCase(), getAcronym(args));
			if(response.length() > 512)
				response = "Wow, that's long! (That's what she said.)";
			client.send(new PrivmsgCommand(target, response));
		}
	}

	@Override
	public String getHelp() {
		String trigger = getShell().getTrigger();
		return String.format("Usage: \2%sacronym abbreviation\2. Makes up a (probably nonsensical) acronym that has the specified abbreviation.", trigger);
	}

	protected String getAcronym(String word) {
		word = word.toLowerCase();
		String ret = "";
		for(int i = 0; i < word.length() - 1; ++i) {
			List<String> list = adjective.get(word.charAt(i));
			ret += list.get(rnd.nextInt(list.size()));
			ret += " ";
		}
		List<String> list = noun.get(word.charAt(word.length() - 1));
		ret += list.get(rnd.nextInt(list.size()));
		return Strings.toTitleCase(ret);
	}
}
