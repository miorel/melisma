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
package event;

import com.googlecode.lawu.event.EventListener;

/**
 * Object representation of an event.
 * 
 * @author Miorel-Lucian Palii
 */
public interface Event {
	/**
	 * Triggers this event on the specified listener. If this event has been consumed, does nothing.
	 * 
	 * @param listener listener on which to trigger
	 */
	public void trigger(EventListener listener);

	/**
	 * Marks this event as having been handled, so that further attempts to trigger it on listeners will do nothing.
	 */
	public void consume();
	
	/**
	 * Checks whether or not this event has been consumed.
	 * 
	 * @return whether or not this event has been consumed
	 */
	public boolean isConsumed();
}
