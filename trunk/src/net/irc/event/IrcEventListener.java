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

package net.irc.event;

import net.event.NetworkEventListener;
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

public interface IrcEventListener extends NetworkEventListener {
	public void pingEvent(IrcClient client, PingCommand command);

	public void privmsgEvent(IrcClient client, PrivmsgCommand command);

	public void noticeEvent(IrcClient client, NoticeCommand command);

	public void quitEvent(IrcClient client, QuitCommand command);

	public void nickEvent(IrcClient client, NickCommand command);

	public void joinEvent(IrcClient client, JoinCommand command);

	public void unknownCtcpCommandEvent(IrcClient client, CtcpCommand command);

	public void actionEvent(IrcClient client, ActionCommand command);

	public void unknownCommandEvent(IrcClient client, UnknownCommand command);

	public void inviteEvent(IrcClient client, InviteCommand command);

	public void kickEvent(IrcClient client, KickCommand command);
}
