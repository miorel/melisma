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

package net.irc.cmd;

import java.util.Iterator;

import net.irc.Entity;
import util.iterator.ArrayIterator;

public class PassCommand extends AbstractIrcCommand {
	private final String password;
	
	public PassCommand(Entity origin, String password) {
		super(origin);
		validateString("the password", password, false, false);
		this.password = password;
	}
	
	public PassCommand(String password) {
		this(null, password);
	}
	
	public String getPassword() {
		return password;
	}

	@Override
	public Iterator<String> getArguments() {
		return new ArrayIterator<String>(new String[] {password});
	}

	@Override
	public String getCommand() {
		return "PASS";
	}
}
