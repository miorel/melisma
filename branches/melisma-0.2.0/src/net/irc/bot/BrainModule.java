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

package net.irc.bot;

import net.irc.event.AbstractIrcEventListener;

public class BrainModule extends AbstractIrcEventListener {
	private final Brain brain;
	
	public BrainModule(Brain brain) {
		if(brain == null)
			throw new IllegalArgumentException("the brain object may not be null");
		this.brain = brain;
	}
	
	public Brain getBrain() {
		return brain;
	}
	
	public Memory getMemory() {
		return getBrain().getMemory();
	}

	public Memory getTemporaryMemory() {
		return getBrain().getTemporaryMemory();
	}
	
	public ObjectStore getObjectStore() {
		return getBrain().getObjectStore();
	}
	
	public Object getObject(String key) {
		return getObjectStore().getObject(key);
	}
	
	public void putObject(String key, Object object) {
		getObjectStore().putObject(key, object);
	}
}
