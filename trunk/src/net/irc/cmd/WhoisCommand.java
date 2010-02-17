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
package net.irc.cmd;

import net.irc.Entity;

public class WhoisCommand extends IrcNicknameCommand {
	public WhoisCommand(Entity origin, String nick) {
		super(origin, nick);
	}
	
	public WhoisCommand(String nick) {
		this(null, nick);
	}
	
	@Override
	public String getCommand() {
		return "WHOIS";
	}
}
