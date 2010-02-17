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

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

import util.LogManager;

public class SelectingThread<C extends NetConfig> extends Thread {
	private final Selector selector;
	private final ClientFactory<C> factory;
	
	private final static Runnable nullRunnable = null;
	
	public SelectingThread(ThreadGroup group, ClientFactory<C> factory) throws IOException {
		super(group, nullRunnable);
		if(factory == null)
			throw new IllegalArgumentException("the client factory may not be null");
		this.factory = factory;
		this.selector = Selector.open();
	}
	
	public void addClient(C config) throws IOException {
		factory.build(config, selector).prepare();
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		while(!Thread.interrupted()) {
			int s = 0;
			try {
				s = selector.select();
			}
			catch(IOException e) {
				interrupt();
			}
			if(s > 0) {
				Set<SelectionKey> keys = selector.selectedKeys();
				for(SelectionKey key: keys) {
					Client<C> client = (Client<C>) key.attachment();
					try {
						if(key.isValid() && key.isConnectable())
							client.connect();
						if(key.isValid() && key.isReadable())
							client.read();
						if(key.isValid() && key.isWritable())
							client.write();
					}
					catch(IOException e) {
						LogManager.getInstance().report("[SELECTOR]", e);
					}
				}
				keys.clear();
			}
		}
	}
}
