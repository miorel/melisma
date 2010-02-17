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
package net.irc.event;

import net.event.AbstractNetworkEventListener;
import net.irc.IrcClient;
import net.irc.cmd.ActionCommand;
import net.irc.cmd.CtcpCommand;
import net.irc.cmd.InviteCommand;
import net.irc.cmd.JoinCommand;
import net.irc.cmd.KickCommand;
import net.irc.cmd.NickCommand;
import net.irc.cmd.NoticeCommand;
import net.irc.cmd.PingCommand;
import net.irc.cmd.PrivmsgCommand;
import net.irc.cmd.QuitCommand;
import net.irc.cmd.UnknownCommand;

public abstract class AbstractIrcEventListener extends AbstractNetworkEventListener implements IrcEventListener {
	@Override
	public void pingEvent(IrcClient client, PingCommand command) {
	}

	@Override
	public void privmsgEvent(IrcClient client, PrivmsgCommand command) {
	}

	@Override
	public void noticeEvent(IrcClient client, NoticeCommand command) {		
	}

	@Override
	public void quitEvent(IrcClient client, QuitCommand command) {	
	}

	@Override
	public void nickEvent(IrcClient client, NickCommand command) {
	}

	@Override
	public void joinEvent(IrcClient client, JoinCommand command) {
	}

	@Override
	public void unknownCtcpCommandEvent(IrcClient client, CtcpCommand command) {
	}

	@Override
	public void actionEvent(IrcClient client, ActionCommand command) {
	}

	@Override
	public void unknownCommandEvent(IrcClient client, UnknownCommand command) {
	}

	@Override
	public void inviteEvent(IrcClient client, InviteCommand command) {
	}

	@Override
	public void kickEvent(IrcClient client, KickCommand command) {
	}
}
