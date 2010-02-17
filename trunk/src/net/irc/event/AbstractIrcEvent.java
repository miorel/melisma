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

import java.net.SocketAddress;

import net.irc.IrcClient;
import event.AbstractEvent;

public abstract class AbstractIrcEvent extends AbstractEvent implements IrcEvent {
	private final IrcClient client;
	
	public AbstractIrcEvent(IrcClient client) {
		this.client = client;
	}

	public IrcClient getClient() {
		return client;
	}

	@Override
	public SocketAddress getAddress() {
		return client == null ? null : client.getAddress();
	}
}
