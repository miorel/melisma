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

import net.irc.bot.CommandShell;

public class Google extends SearchCommand {
	public Google(CommandShell shell) {
		super(shell, "google", "(?:google|search)");
	}

	@Override
	protected String getEngineName() {
		return "Google";
	}

	@Override
	protected String getEngineUrlFormat() {
		return "http://www.google.com/search?q=%s";
	}
}
