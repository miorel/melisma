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

package net.event;

import event.EventListener;

public abstract class AbstractNetworkEventListener implements EventListener {
	public void connected(ConnectedEvent event) {	
	}

	public void disconnected(DisconnectedEvent event) {	
	}
	
	public void writing(WritingEvent event) {	
	}

	public void reading(ReadingEvent event) {
	}
}
