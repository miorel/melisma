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
package net.irc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import net.NetConfig;

import org.w3c.dom.Node;

import util.LogManager;

import com.googlecode.lawu.dp.Iterator;
import com.googlecode.lawu.util.Iterators;
import com.googlecode.lawu.util.iterators.UniversalIterator;

public class IrcConfig extends NetConfig {
	public static final int DEFAULT_PORT = 6667;
	
	private String serverPassword = null;
	
	private String desiredNick = null;
	private String accountPassword = null;
	private String userName = null;
	private String realName = null;
	
	private List<String> initialChannels = new ArrayList<String>();
	
	public IrcConfig() {
		setPort(DEFAULT_PORT);
	}
	
	public String getServerPassword() {
		return serverPassword;
	}
	
	public void setServerPassword(String serverPassword) {
		if(serverPassword != null && serverPassword.isEmpty())
			throw new IllegalArgumentException("the server password may not have zero length, use null instead");
		this.serverPassword = serverPassword;
	}

	public String getDesiredNick() {
		if(desiredNick == null)
			setDesiredNick(getUserName());
		return desiredNick;
	}
	
	public void setDesiredNick(String desiredNick) {
		if(desiredNick != null && desiredNick.isEmpty())
			throw new IllegalArgumentException("the desired nickname may not have zero length, use null instead to choose value automatically");
		this.desiredNick = desiredNick;
	}

	public String getAccountPassword() {
		return accountPassword;
	}
	
	public void setAccountPassword(String accountPassword) {
		if(accountPassword != null && accountPassword.isEmpty())
			throw new IllegalArgumentException("the account password may not have zero length, use null instead");
		this.accountPassword = accountPassword;
	}
	
	public String getUserName() {
		if(userName == null) {
			String user = System.getProperty("user.name");
			if(user == null) {
				StringBuilder sb = new StringBuilder();
				Random rnd = new Random();
				for(int n = 8; n >= 0; --n)
					sb.append((char) ('a' + rnd.nextInt('z' - 'a' + 1)));
				user = sb.toString();
			}
			setUserName(user);
		}
		return userName;
	}
	
	public void setUserName(String userName) {
		if(userName != null && userName.isEmpty())
			throw new IllegalArgumentException("the user name may not have zero length, use null instead to choose value automatically");
		this.userName = userName;
	}
	
	public String getRealName() {
		if(realName == null)
			realName = " ";
		return realName;
	}
	
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	public UniversalIterator<String> getInitialChannels() {
		return Iterators.iterator(initialChannels);
	}
	
	public void setInitialChannels(Iterator<String> initialChannels) {
		this.initialChannels.clear();
		for(String channel: Iterators.adapt(initialChannels))
			this.initialChannels.add(channel);
	}
	
	public static UniversalIterator<IrcConfig> parse(Node node) {
		Stack<IrcConfig> configs = new Stack<IrcConfig>();
		IrcConfig prototype = new IrcConfig();
		for(Node n: Iterators.tree(node)) {
			String name = n.getNodeName();
			switch(n.getNodeType()) {
			case Node.TEXT_NODE:
				String text = n.getTextContent();
				String parentName = n.getParentNode().getNodeName();
				if(parentName.equals("hostname")) 
					configs.peek().setHostname(text.trim());
				else if(parentName.equals("port"))
					configs.peek().setPort(Integer.parseInt(text.trim()));
				else if(parentName.equals("serverpass"))
					configs.peek().setServerPassword(text.trim());
				else if(parentName.equals("nick"))
					(n.getParentNode().getParentNode().getNodeName().equals("network") ? configs.peek() : prototype).setDesiredNick(text.trim());
				else if(parentName.equals("pass"))
					configs.peek().setAccountPassword(text.trim());
				else if(parentName.equals("username"))
					(n.getParentNode().getParentNode().getNodeName().equals("network") ? configs.peek() : prototype).setUserName(text.trim());
				else if(parentName.equals("realname"))
					(n.getParentNode().getParentNode().getNodeName().equals("network") ? configs.peek() : prototype).setRealName(text);
				else if(parentName.equals("name"))
					configs.peek().initialChannels.add(text.trim());
				break;
			case Node.ELEMENT_NODE:
				if(name.equals("network"))
					configs.push(prototype.clone());
				break;
			}		
		}
		LogManager.getInstance().info("CONFIG", "Read configuration for " + configs.size() + " networks.");
		return Iterators.iterator(configs);
	}

	@SuppressWarnings("unchecked")
	public IrcConfig clone() {
		IrcConfig ret = (IrcConfig) super.clone();
		ret.initialChannels = (ArrayList<String>) ((ArrayList) initialChannels).clone();
		return ret;
	}
}
