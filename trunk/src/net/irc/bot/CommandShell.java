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
package net.irc.bot;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.irc.Entity;
import net.irc.IrcClient;
import net.irc.bot.cmd.Command;
import net.irc.cmd.PrivmsgCommand;
import util.LogManager;
import util.ResourceLoader;
import util.ds.StringNode;

import com.googlecode.lawu.util.Iterators;
import com.googlecode.lawu.util.iterators.UniversalIterator;

public class CommandShell extends BrainModule {	
	private Map<String,Command> commands = new HashMap<String,Command>();
	private Set<Command> commandSet = new HashSet<Command>();
	
	public static final String[] TRIGGER = {"vars", "shell", "trigger"}; 
	public static final String[] UNKNOWN_CMD = {"modules", "shell", "phrases", "unknown_cmd"};
	
	public CommandShell(Brain brain) {
		super(brain);
		prepareStrings();
		for(String line: Iterators.lines(ResourceLoader.getInstance().getScanner("etc/commands.txt", true, true))) {
			line = line.replaceFirst("#.*$", "").trim();
			if(!line.isEmpty())
				try {
					Command command = (Command) Class.forName(line).getConstructor(CommandShell.class).newInstance(this);
					LogManager.getInstance().info("SHELL", "Successfully loaded command " + command.getClass());
					commands.put(command.getName().toLowerCase(), command);
					commandSet.add(command);
				}
				catch(InvocationTargetException e) {
					LogManager.getInstance().report("SHELL", e.getCause());
				}
				catch(Exception e) {
					LogManager.getInstance().report("SHELL", e);
				} 
		}
	}

	protected void prepareStrings() {
		Memory mem = getTemporaryMemory();
		mem.getNode(UNKNOWN_CMD).setValue("Unrecognized command: \2%1$s\2.");
	}
	
	@Override
	public void privmsgEvent(IrcClient client, PrivmsgCommand command) {
		Entity origin = command.getOrigin();
		String channel = command.getTarget();
		if(channel.equals(client.getActualNick()))
			channel = null;
		Matcher m = Pattern.compile("(?:\\s*" + Pattern.quote(getTrigger()) + "\\s*)+(\\S+)\\s*(.*)").matcher(command.getMessage());
		if(m.matches())
			handleCommand(client, origin, channel, m.group(1).toLowerCase(), m.group(2));
	}

	public String getTrigger() {
		StringNode node = getMemory().getNode(TRIGGER);
		String ret = node.getValue();
		if(ret == null) {
			ret = ">";
			node.setValue(ret);
		}
		return ret;
	}

	public void setTrigger(String trigger) {
		if(trigger == null || !trigger.matches("[^\\w\\s]+"))
			throw new IllegalArgumentException("trigger may not be null or contain whitespace or word characters");
		StringNode node = getMemory().getNode(TRIGGER);
		node.setValue(trigger);
	}
	
	public UniversalIterator<Command> getCommands() {
		return Iterators.adapt(commandSet);
	}
	
	public Command getCommandForAlias(String alias) {
		Command ret = commands.get(alias);
		if(ret == null)
			for(Command cmd: commands.values()) 
				if(cmd.matchesAlias(alias)) {
					commands.put(alias, cmd);
					ret = cmd;
					break;
				}
		return ret;
	}
	
	protected void handleCommand(IrcClient client, Entity origin, String channel, String command, String args) {
		Command commandObj = getCommandForAlias(command);
		if(commandObj != null)
			try {
				commandObj.execute(client, origin, channel, args);
			}
			catch(Exception e) {
				LogManager.getInstance().report("SHELL", e);
			}
		else {
			String format = getTemporaryMemory().getValue(UNKNOWN_CMD);
			client.send(new PrivmsgCommand(origin.getNick(), String.format(format, command, getTrigger())));
		}
	}
}
