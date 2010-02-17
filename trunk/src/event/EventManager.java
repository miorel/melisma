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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Keeps a set of event listeners and distributes events to them.
 * 
 * @author Miorel-Lucian Palii
 */
public class EventManager<L extends EventListener> {
	private final Set<L> listenerSet;
	private final List<L> listenerList;
	private boolean listenersChanged = false;

	/**
	 * Prepares a new event manager, initialized with an empty list of listener set. 
	 */
	public EventManager() {
		this.listenerSet = new HashSet<L>();
		this.listenerList = new ArrayList<L>();
	}

	/**
	 * Adds the specified listener to this manager's listener set.
	 * 
	 * @param listener listener to add
	 */
	public void addListener(L listener) {
		synchronized(listenerSet) {
			if(listenerSet.add(listener))
				listenerList.add(listener);
		}
	}

	/**
	 * Removes the specified listener from this manager's listener set.
	 * 
	 * @param listener listener to remove
	 */
	public void removeListener(L listener) {
		synchronized(listenerSet) {
			if(listenerSet.remove(listener))
				listenersChanged = true;
		}
	}

	private Iterable<L> getListeners() {
		synchronized(listenerSet) {
			if(listenersChanged) {
				listenerList.clear();
				listenerList.addAll(listenerSet);
			}
		}
		return listenerList;
	}

	/**
	 * Distributes the given event by triggering it on all managed listeners in the order in which they were added to this manager.
	 * 
	 * @param event event to trigger
	 */
	public void distribute(Event event) {
		for(EventListener listener: getListeners())
			event.trigger(listener);
	}
}
