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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.irc.Entity;
import net.irc.IrcClient;
import net.irc.event.AbstractIrcEvent;
import net.irc.event.IrcEvent;
import net.irc.event.IrcEventListener;
import util.LogManager;
import util.ResourceLoader;
import event.EventListener;

public class CtcpCommand extends PrivmsgCommand {
	public static final Pattern CTCP = Pattern.compile("\1([a-z]+)\\s*(.*)\1", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	
	private final static Map<String, Method> CTCP_COMMANDS = new HashMap<String, Method>();
	static {
		Properties ircStringCmds = ResourceLoader.getInstance().getProperties("lib/irc_ctcp_cmds.properties");
		if(ircStringCmds != null)
			for(String cmd: ircStringCmds.stringPropertyNames()) {
				Method m = null;
				try {
					m = Class.forName(ircStringCmds.getProperty(cmd)).getMethod("build", Entity.class, String.class, String.class);
				}
				catch(Exception e) {
				}
				if(m != null)
					CTCP_COMMANDS.put(cmd.toUpperCase(), m);
			}
	}
	
	private final String ctcpCommand;
	private final String ctcpArgs;
	
	private CtcpCommand(Entity origin, String target, String message, boolean dummy) {
		super(origin, target, message);
		Matcher m = CTCP.matcher(message);
		if(!m.matches())
			throw new IllegalArgumentException("message is not proper CTCP");
		this.ctcpCommand = m.group(1).toUpperCase();
		this.ctcpArgs = m.group(2).isEmpty() ? null : m.group(2);
	}
	
	public CtcpCommand(Entity origin, String target, String message) {
		this(origin, target, message, false);
	}
	
	public CtcpCommand(String target, String message) {
		this(null, target, message, false);
	}

	public CtcpCommand(Entity origin, String target, String ctcpCommand, String ctcpArgs) {
		this(origin, target, String.format(ctcpArgs == null || ctcpArgs.isEmpty() ? "\1%s\1" : "\1%s %s\1", ctcpCommand == null ? "" : ctcpCommand.toUpperCase(), ctcpArgs));
		validateString("the CTCP command", ctcpCommand, false, false);
	}

	public CtcpCommand(String target, String ctcpCommand, String ctcpArgs) {
		this(null, target, ctcpCommand, ctcpArgs);
	}

	public String getCtcpCommand() {
		return ctcpCommand;
	}

	public String getCtcpArguments() {
		return ctcpArgs;
	}
	
	public static CtcpCommand build(Entity origin, String[] param) {
		validateParam(param, 2);
		CtcpCommand ret = new CtcpCommand(origin, param[0], param[1]);
		Method method = CTCP_COMMANDS.get(ret.getCtcpCommand());
		if(method != null)
			try {
				ret = (CtcpCommand) method.invoke(null, origin, ret.getTarget(), ret.getCtcpArguments());
			}
			catch(InvocationTargetException e) {
				LogManager.getInstance().report("CTCP", e.getCause());
			}
			catch(Exception e) {
				LogManager.getInstance().report("CTCP", e);
			}
		return ret;
	}

	@Override
	public IrcEvent getEvent(final IrcClient client) {
		return new AbstractIrcEvent(client) {
			@Override
			protected void doTrigger(EventListener listener) {
				if(listener instanceof IrcEventListener)
					((IrcEventListener) listener).unknownCtcpCommandEvent(client, CtcpCommand.this);
			}
		};
	}
}
