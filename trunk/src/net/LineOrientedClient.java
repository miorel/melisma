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
package net;

import java.nio.channels.Selector;

public abstract class LineOrientedClient<C extends NetConfig> extends Client<C> {
	public LineOrientedClient(C config, Selector selector) {
		super(config, selector);
	}

	@Override
	public void send(String message) {
		super.send(message + "\r\n");
	}
	
	@Override
	protected void raiseWritingEvent(String message) {
		super.raiseWritingEvent(message.substring(0, message.length() - 2));
	}
	
	@Override
	protected void scanBuffer(StringBuilder buffer) {
		for(int i = 0; i < buffer.length(); ++i) {
			char c = buffer.charAt(i);
			if(c == '\r' || c == '\n') {
				String line = buffer.substring(0, i);
				do ++i;
				while(i < buffer.length() && (buffer.charAt(i) == '\r' || buffer.charAt(i) == '\n'));
				buffer.delete(0, i);
				i = -1;
				raiseReadingEvent(line);
				process(line);
			}
		}
	}

	protected abstract void process(String line);
}