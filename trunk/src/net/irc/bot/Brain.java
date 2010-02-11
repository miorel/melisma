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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.irc.IrcClient;
import net.irc.IrcClientFactory;
import net.irc.IrcConfig;
import net.irc.IrcThread;

import org.xml.sax.SAXException;

import util.LogManager;
import util.ResourceLoader;
import util.ds.StringNode;
import util.thread.WorkerThread;

import com.googlecode.lawu.util.Iterators;
import com.googlecode.lawu.util.iterators.UniversalIterator;

public class Brain {
	private final ThreadGroup threadGroup;
	
	private final IrcThread ircThread;
	private final List<IrcClient> clients = new ArrayList<IrcClient>();
	
	private final String memoryFile;
	private final Memory memory;
	private final Memory tmpMemory;
	private final ObjectStore objectStore = new ObjectStore();
	
	private final WorkerThread worker;
	
	public Brain() throws SAXException, IOException {
		this("etc/config.xml");
	}
	
	public Brain(String configFile) throws SAXException, IOException {
		this(configFile, "etc/listeners.txt");
	}

	public Brain(String configFile, String listenersFile) throws SAXException, IOException {
		this(configFile, listenersFile, "var/memory.xml");
	}
	
	public Brain(String configFile, String listenersFile, String memoryFile) throws SAXException, IOException {
		this.threadGroup = new ThreadGroup(null);
		this.memoryFile = memoryFile;
		this.memory = new Memory(StringNode.parse(ResourceLoader.getInstance().getXmlDocument(memoryFile, true, true)));
		this.tmpMemory = new Memory(new StringNode());
		this.worker = new WorkerThread(threadGroup);
		this.ircThread = new IrcThread(threadGroup, new IrcClientFactory() {
			@Override
			public IrcClient build(IrcConfig config, Selector selector) {
				IrcClient ret = super.build(config, selector);
				clients.add(ret);
				return ret;
			}
		});
		loadClients(configFile);
		loadListeners(listenersFile);
	}
	
	protected void startLongTermMemory() {
		new Thread(threadGroup, new Runnable() {
			@Override
			public void run() {
				while(!Thread.interrupted()) {
					try {
						writeMemory();
					}
					catch(FileNotFoundException e) {
						LogManager.getInstance().report("MEMORY", e);
					}
					try {
						Thread.sleep(10 * 60 * 1000);
					}
					catch(InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
		}).start();
	}
	
	protected void loadClients(String configFile) throws SAXException, IOException {
		for(IrcConfig config: IrcConfig.parse(ResourceLoader.getInstance().getXmlDocument(configFile, true, true)))
			ircThread.addClient(config);
	}
	
	protected void loadListeners(String listenersFile) {
		Scanner sc = ResourceLoader.getInstance().getScanner(listenersFile, true, true);
		while(sc.hasNextLine()) {
			String line = sc.nextLine().replaceFirst("#.*$", "").trim();
			if(!line.isEmpty())
				try {
					BrainModule listener = (BrainModule) Class.forName(line).getConstructor(Brain.class).newInstance(this);
					LogManager.getInstance().info("LISTENERS", "Successfully loaded brain module " + listener.getClass());
					for(IrcClient client: clients)
						client.addListener(listener);
				}
				catch(InvocationTargetException e) {
					LogManager.getInstance().report("LISTENERS", e.getCause());
				}
				catch(Exception e) {
					LogManager.getInstance().report("LISTENERS", e);
				}
		}
	}
	
	public UniversalIterator<IrcClient> getClients() {
		return Iterators.adapt(clients);
	}
	
	public void start() {
		ircThread.start();
		worker.start();
		startLongTermMemory();
	}
	
	public void stop() {
		for(IrcClient client: clients)
			client.disconnect();
		threadGroup.interrupt();
	}
	
	public Memory getMemory() {
		return memory;
	}
	
	public Memory getTemporaryMemory() {
		return tmpMemory;
	}
	
	public ObjectStore getObjectStore() {
		return objectStore;
	}
	
	public void writeMemory() throws FileNotFoundException {
		PrintStream out = new PrintStream(memoryFile);
		memory.getRoot().write(out);
	}
	
	public ThreadGroup getThreadGroup() {
		return threadGroup;
	}
	
	public void invokeAsync(Runnable runnable) {
		worker.queueTask(runnable);
	}
}
