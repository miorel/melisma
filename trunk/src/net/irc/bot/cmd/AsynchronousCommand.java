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

import net.irc.Entity;
import net.irc.IrcClient;
import net.irc.bot.CommandShell;

public abstract class AsynchronousCommand extends AbstractCommand {
	public AsynchronousCommand(CommandShell shell, String name) {
		super(shell, name);
	}

	public AsynchronousCommand(CommandShell shell, String name, String regex) {
		super(shell, name, regex);
	}

	@Override
	public final void execute(final IrcClient client, final Entity origin, final String channel, final String args) {
		getBrain().invokeAsync(new Runnable() {
			@Override
			public void run() {
				doExecute(client, origin, channel, args);
			}
		});
	}
	
	protected abstract void doExecute(IrcClient client, Entity origin, String channel, String args);
}
