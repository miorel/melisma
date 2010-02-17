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
package net.irc.cmd;

import java.util.ArrayList;
import java.util.List;

import net.irc.Entity;

import com.googlecode.lawu.dp.Iterator;
import com.googlecode.lawu.util.Iterators;
import com.googlecode.lawu.util.Strings;
import com.googlecode.lawu.util.iterators.UniversalIterator;

public abstract class IrcTargetsCommand extends AbstractIrcCommand {
	private final String[] targets;
	
	public IrcTargetsCommand(Iterator<String> targets) {
		this(null, targets);
	}
	
	public IrcTargetsCommand(Entity origin, Iterator<String> targets) {
		super(origin);
		List<String> targetsList = new ArrayList<String>();
		for(String target: Iterators.adapt(targets)) {
			validateString("targets", target, false, false);
			targetsList.add(target);
		}
		if(targetsList.isEmpty())
			throw new IllegalArgumentException("can't " + getVerb() + " without targets");
		this.targets = targetsList.toArray(new String[targetsList.size()]);
	}
	
	protected String getVerb() {
		return getCommand().toLowerCase();
	}
	
	public UniversalIterator<String> getTargets() {
		return Iterators.iterator(targets);
	}
	
	@Override
	public UniversalIterator<String> getArguments() {
		return Iterators.iterator(new String[] {Strings.join(" ", getTargets())});
	}
}
