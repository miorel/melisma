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

import java.util.regex.Pattern;

import net.irc.bot.Brain;
import net.irc.bot.CommandShell;
import net.irc.bot.Memory;
import net.irc.bot.ObjectStore;

public abstract class AbstractCommand implements Command {
	private final CommandShell shell;
	private final String name;
	private final String regex;
	
	public AbstractCommand(CommandShell shell, String name) {
		this(shell, name, Pattern.quote(name));
	}
	
	public AbstractCommand(CommandShell shell, String name, String regex) {
		if(shell == null)
			throw new IllegalArgumentException("the shell object can't be null");
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("the name can't be null or have zero length");
		if(regex == null || regex.isEmpty())
			throw new IllegalArgumentException("the regex can't be null or have zero length");
		if(!name.matches(regex))
			throw new IllegalArgumentException("the name must match the regex");
		this.shell = shell;
		this.name = name;
		this.regex = regex;	
	}
	
	protected CommandShell getShell() {
		return shell;
	}
	
	@Override
	public String getHelp(String alias) {
		if(alias == null)
			throw new IllegalArgumentException("the alias can't be null or have zero length");
		if(!matchesAlias(alias))
			throw new IllegalArgumentException("the alias must match this command");
		String name = getName();
		return name.equals(alias) ? getHelp() : String.format("\2%1$s%2$s\2 is an alias for \2%1$s%3$s\2.", getShell().getTrigger(), alias, name);
	}
	
	@Override
	public boolean matchesAlias(String alias) {
		if(alias == null || alias.isEmpty())
			throw new IllegalArgumentException("the alias can't be null or have zero length");
		return alias.matches(regex);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	protected Brain getBrain() {
		return getShell().getBrain();
	}

	protected Memory getMemory() {
		return getBrain().getMemory();
	}

	protected Memory getTemporaryMemory() {
		return getBrain().getTemporaryMemory();
	}

	protected ObjectStore getObjectStore() {
		return getBrain().getObjectStore();
	}
	
	@Override
	public boolean isHidden() {
		return false;
	}
}
