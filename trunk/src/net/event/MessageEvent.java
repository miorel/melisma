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
package net.event;

import java.net.SocketAddress;

public abstract class MessageEvent extends AbstractNetworkEvent {
	private final String message;
	
	public MessageEvent(SocketAddress address, String message) {
		super(address);
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
