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

import java.security.NoSuchAlgorithmException;

import net.irc.Entity;
import net.irc.IrcClient;
import net.irc.bot.CommandShell;

public class Set extends AbstractCommand {
	public Set(CommandShell shell) throws NoSuchAlgorithmException {
		super(shell, "var", "var(?:iable)?");
	}
	
	@Override
	public void execute(IrcClient client, Entity origin, String channel, String args) {
		
	}
	
	@Override
	public String getHelp() {
		String trigger = getShell().getTrigger();
		return String.format("Usage: \2%1$svar [[entity] variable [= value]]\2. Access a variable for an entity. If a value is specified, sets the variable to that value. If not, the current value is reported. The entity is derived from context if unspecified. Valid entities depend on the variable. If the command is called with no arguments, lists available variables.", trigger);
	}
}
