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
import net.irc.cmd.PrivmsgCommand;
import util.thread.ScheduledThread;

public class Timer extends AbstractCommand {	
	private final ScheduledThread timerThread;
	
	public Timer(CommandShell shell) {
		super(shell, "timer");
		this.timerThread = new ScheduledThread(getBrain().getThreadGroup());
		this.timerThread.start();
	}
	
	@Override
	public String getHelp() {
		String trigger = getShell().getTrigger();
		return String.format("Usage: \2%stimer duration\2. Sets a timer for the specified duration in seconds. You will be notified when the timer expires.", trigger);
	}

	@Override
	public void execute(final IrcClient client, final Entity origin, final String channel, String args) {
		final String target = channel == null ? origin.getNick() : channel;
		String response = null;
		long time = 0;
		String durationStr = args.trim().split("\\s+")[0];
		if(durationStr.isEmpty())
			response = "You ought to specify a duration.";
		else if(durationStr.matches("-(?:\\d*\\.?\\d+|\\d+\\.?\\d*)"))
			response = "I'm an only child.";
		else if(durationStr.matches("\\d*\\.\\d+"))
			response = "That decimal point is bothering me, please remove it. You shouldn't expect that level of resolution through IRC anyway.";
		else if(durationStr.matches("[ivxlcdm]+"))
			response = "Fugit irreparabile tempus.";
		else if(durationStr.matches("\\d+\\.?")) {
			try {
				time = Long.parseLong(durationStr.replaceAll("\\.?$", "000"));
				if(time == 0)
					response = "Too short! (That's what she said.)";
				else {
					long now = System.currentTimeMillis();
					time += now;	
					if(time < now)
						response = "Epoch fail.";
				}
			}
			catch(NumberFormatException e) {
				response = "I'm betting you won't be around that long.";	
			}
		}
		else
			response = "I prefer decimal numbers.";
		if(response == null) {
			response = String.format("Okay, %s, I'm setting a timer just for you.", origin.getNick());
			timerThread.queueTask(time, new Runnable() {
				@Override
				public void run() {
					String msg = "Your timer has expired!";
					if(channel != null)
						msg = origin.getNick() + ": " + msg;
					client.send(new PrivmsgCommand(target, msg));		
				}
			});
		}
		client.send(new PrivmsgCommand(target, response));
	}
}
