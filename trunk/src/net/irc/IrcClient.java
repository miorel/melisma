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

package net.irc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.Selector;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.LineOrientedClient;
import net.event.ConnectedEvent;
import net.event.DisconnectedEvent;
import net.event.ReadingEvent;
import net.event.WritingEvent;
import net.irc.cmd.ActionCommand;
import net.irc.cmd.CtcpCommand;
import net.irc.cmd.InviteCommand;
import net.irc.cmd.IrcCommand;
import net.irc.cmd.JoinCommand;
import net.irc.cmd.KickCommand;
import net.irc.cmd.NickCommand;
import net.irc.cmd.NoticeCommand;
import net.irc.cmd.PingCommand;
import net.irc.cmd.PongCommand;
import net.irc.cmd.PrivmsgCommand;
import net.irc.cmd.QuitCommand;
import net.irc.cmd.UnknownCommand;
import net.irc.cmd.UserCommand;
import net.irc.event.IrcEventListener;
import util.LogManager;
import util.ResourceLoader;

public class IrcClient extends LineOrientedClient<IrcConfig> {
	private final static Pattern COMMAND = Pattern.compile("\\s*(?::(\\S+)\\s+)?(\\S+)\\s*(.*)");
	private final static Pattern COMMAND_PARAM = Pattern.compile(":.*|\\S+", Pattern.DOTALL);
	
	private final static Map<String, Method> IRC_STRING_COMMANDS = new HashMap<String, Method>();
	static {
		Properties ircStringCmds = ResourceLoader.getInstance().getProperties("lib/irc_cmds.properties");
		if(ircStringCmds != null)
			for(String cmd: ircStringCmds.stringPropertyNames()) {
				Method m = null;
				try {
					m = Class.forName(ircStringCmds.getProperty(cmd)).getMethod("build", Entity.class, String[].class);
				}
				catch(Exception e) {
				}
				if(m != null)
					IRC_STRING_COMMANDS.put(cmd.toUpperCase(), m);
			}
	}
	
	private String actualNick;
	private Set<String> channels = new HashSet<String>();
	
	public IrcClient(IrcConfig config, Selector selector) {
		super(config, selector);
		final LogManager lm = LogManager.getInstance();
		addListener(new IrcEventListener() {
			@Override
			public void connected(ConnectedEvent event) {
				lm.info("SOCKET " + event.getAddress(), "Connected!");
				IrcConfig config = getConfig();
				send(new NickCommand(getDesiredNick()));
				send(new UserCommand(config.getUserName(), config.getRealName()));
			}
			
			@Override
			public void pingEvent(IrcClient client, PingCommand command) {
				send(new PongCommand(command.getTargets()));
			}

			@Override
			public void nickEvent(IrcClient client, NickCommand command) {
				if(getActualNick().equals(command.getOrigin().getNick()))
					setActualNick(command.getNick());
			}
			
			@Override
			public void joinEvent(IrcClient client, JoinCommand command) {
				if(getActualNick().equals(command.getOrigin().getNick()))
					channels.add(command.getChannel());
			}
			
			@Override
			public void writing(WritingEvent event) {
				lm.outgoing("SOCKET " + event.getAddress(), event.getMessage());
				Matcher m = Pattern.compile("nick :?(\\S+).*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(event.getMessage());
				if(m.matches())
					setActualNick(m.group(1));
			}

			@Override
			public void actionEvent(IrcClient client, ActionCommand command) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void inviteEvent(IrcClient client, InviteCommand command) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void kickEvent(IrcClient client, KickCommand command) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void noticeEvent(IrcClient client, NoticeCommand command) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void privmsgEvent(IrcClient client, PrivmsgCommand command) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void quitEvent(IrcClient client, QuitCommand command) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void unknownCommandEvent(IrcClient client, UnknownCommand command) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void unknownCtcpCommandEvent(IrcClient client, CtcpCommand command) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void disconnected(DisconnectedEvent event) {
				lm.info("SOCKET " + event.getAddress(), "Disconnected.");
			}

			@Override
			public void reading(ReadingEvent event) {
				lm.incoming("SOCKET " + event.getAddress(), event.getMessage());
			}
		});
	}
	
	public String getDesiredNick() {
		return getConfig().getDesiredNick();
	}

	public String getAccountPassword() {
		return getConfig().getAccountPassword();
	}

	public Iterator<String> getChannels() {
		return Collections.unmodifiableSet(channels).iterator();
	}
	
	public Iterator<String> getInitialChannels() {
		return getConfig().getInitialChannels();
	}
	
	public String getServerPassword() {
		return getConfig().getServerPassword();
	}	
	
	public String getUserName() {
		return getConfig().getUserName();
	}
	
	public String getRealName() {
		return getConfig().getRealName();
	}
	
	public String getActualNick() {
		return actualNick;
	}

	protected void setActualNick(String actualNick) {
		this.actualNick = actualNick;
	}
	
	protected void process(String message) {
		Matcher m = COMMAND.matcher(message);
		if(m.matches()) {
			String origin = m.group(1);
			String command = m.group(2);
			String params = m.group(3);
			Stack<String> paramStack = new Stack<String>();
			m = COMMAND_PARAM.matcher(params);
			while(m.find())
				paramStack.push(m.group());
			if(!paramStack.isEmpty() && paramStack.peek().startsWith(":"))
				paramStack.push(paramStack.pop().substring(1));
			Entity originObj = origin == null ? null : new Entity(getAddress(), origin);
			String[] param = paramStack.toArray(new String[paramStack.size()]);
			if(command.matches("\\d{3}"))
				processCommand(originObj, Integer.parseInt(command), param);
			else
				processCommand(originObj, command.toUpperCase(), param);
		}
		else {

		}
	}
	
	protected void processCommand(Entity origin, String command, String[] param) {
		IrcCommand commandObj = null;
		Method method = IRC_STRING_COMMANDS.get(command.toUpperCase());
		if(method != null) {
			try {
				commandObj = (IrcCommand) method.invoke(null, origin, param);
			}
			catch(InvocationTargetException e) {
				LogManager.getInstance().report("[IRC]", e.getCause());
			}
			catch(Exception e) {
				LogManager.getInstance().report("[IRC]", e);
			}
		}
		if(commandObj == null) 
			commandObj = new UnknownCommand(origin, command, param);
		distribute(commandObj.getEvent(this));
	}

	protected void processCommand(Entity origin, int command, String[] param) {
		Matcher m;
//			Pattern p = Pattern.compile(this.wildcardStringToRegex(param[0]), Pattern.DOTALL);
//			if(!p.matcher(getActualNick()).matches())
//				LogManager.getInstance().warn("First parameter of numeric command didn't match my nickname.");
//			else
		switch(command) {
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 250:
			break;
		case 251:
			break;
		case 252:
			break;
		case 254:
			break;
		case 255:
			break;
		case 265:
			break;
		case 266:
			break;
		case 301:
			break;
		case 311:
			break;
		case 312:
			break;
		case 318:
			break;
		case 319:
			break;
		case 320:
			break;
		case 332:
			break;
		case 333:
			break;
		case 353:
			break;
		case 366:
			break;
		case 372:
			break;
		case 375:
			break;
		case 376:
			for(Iterator<String> channels = getConfig().getInitialChannels(); channels.hasNext();)
			send(new JoinCommand(channels.next()));
			if(getAccountPassword() != null)
				send(new PrivmsgCommand("NickServ", String.format("IDENTIFY %s %s", getDesiredNick(), getAccountPassword())));
			break;
		case 401:
			break;
		case 433:
			m = Pattern.compile("(.*?)(\\d*)").matcher(getActualNick());
			m.matches();
			String num = m.group(2);
			int n = 1 + (num.isEmpty() ? 0 : Integer.parseInt(num));
			send(new NickCommand(m.group(1) + n));
			break;
		case 477:
			break;
		case 901:
			break;
		case 902:
			break;
		}
	}
	
	public void send(IrcCommand command) {
		command.execute(this);
	}
}
