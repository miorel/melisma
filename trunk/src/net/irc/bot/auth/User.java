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
package net.irc.bot.auth;

import net.irc.Entity;
import net.irc.bot.CommandShell;

public class User {
	private final CommandShell shell;
	private final String name;
	
	private User(CommandShell shell, String name) {
		if(shell == null)
			throw new IllegalArgumentException("the shell object can't be null");
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("the name can't be null or have zero length");
		this.shell = shell;
		this.name = name;
	}
	
	public String getName() { 
		return name;
	}
	
	protected CommandShell getShell() {
		return shell;
	}
	
	public void login(Entity entity, String password) {
		
	}

	public void logout(Entity entity) {
		
	}
}
