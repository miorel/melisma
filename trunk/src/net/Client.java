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
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.LinkedList;
import java.util.Queue;

import net.event.ConnectedEvent;
import net.event.DisconnectedEvent;
import net.event.NetworkEvent;
import net.event.NetworkEventListener;
import net.event.ReadingEvent;
import net.event.WritingEvent;
import util.LogManager;
import event.EventManager;

public abstract class Client<C extends NetConfig> {
	public static final String CHARSET_NAME = "US-ASCII";
	public static final int BUFFER_SIZE = 1 << 12;
	public static final long CONNECTION_TIMEOUT = 5000;

	private final C config;
	
	private final EventManager<NetworkEventListener> eventManager;
	
	private SocketChannel channel;
	private InetSocketAddress address;
	private Selector selector;
	
	private final Queue<String> outputQueue;

	private final ByteBuffer byteBuffer;
	private final CharBuffer charBuffer;
	private final StringBuilder stringBuffer;

	private final CharsetDecoder decoder;
	private final CharsetEncoder encoder;

	public Client(C config, Selector selector) {
		if(config == null)
			throw new IllegalArgumentException("the configuration may not be null");
		this.config = config;
		if(selector == null)
			throw new IllegalArgumentException("the selector may not be null");
		this.selector = selector;
		this.outputQueue = new LinkedList<String>();
		this.byteBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
		this.charBuffer = CharBuffer.allocate(BUFFER_SIZE);
		this.stringBuffer = new StringBuilder();
		Charset charset = Charset.forName(CHARSET_NAME);
		this.decoder = charset.newDecoder();
		this.encoder = charset.newEncoder();
		this.eventManager = new EventManager<NetworkEventListener>();
	}
	
	public void addListener(NetworkEventListener listener) {
		eventManager.addListener(listener);
	}

	public void removeListener(NetworkEventListener listener) {
		eventManager.removeListener(listener);
	}
	
	public void distribute(NetworkEvent event) {
		eventManager.distribute(event);
	}
	
	protected C getConfig() {
		return config;
	}
	
	public InetSocketAddress getAddress() {
		return address;
	}

	private void setChannelInterest(int ops) throws ClosedChannelException {
		boolean wake = false;
		SelectionKey key = channel.keyFor(selector);
		if(key == null || key.interestOps() != ops)
			wake = true;
		channel.register(selector, ops, this);
		if(wake)
			selector.wakeup();
	}

	private void listenForConnect() throws ClosedChannelException {
		setChannelInterest(SelectionKey.OP_CONNECT);
	}

	private void listenForRead() throws ClosedChannelException {
		setChannelInterest(SelectionKey.OP_READ);
	}

	private void listenForReadWrite() throws ClosedChannelException {
		setChannelInterest(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
	}
	
	public void prepare() throws IOException {
		channel = SocketChannel.open();
		channel.configureBlocking(false);
		address = new InetSocketAddress(config.getHost(), config.getPort());
		channel.connect(address);
		listenForConnect();
	}
	
	public void connect() throws IOException {
		if(channel.isConnectionPending() && channel.finishConnect()) {
			listenForReadWrite();
			distribute(new ConnectedEvent(address));
		}
	}
	
	public void disconnect() {
		try {
			channel.socket().close();
		}
		catch(IOException e) {
			LogManager.getInstance().report(address, e);
		}
		distribute(new DisconnectedEvent(address));
	}
	
	public void read() throws IOException {
		if(channel.read(byteBuffer) < 0) {
			synchronized(outputQueue) {
				outputQueue.clear();
			}
			disconnect();
		}
		else {
			byteBuffer.flip();
			decoder.decode(byteBuffer, charBuffer, false);
			charBuffer.flip();
			stringBuffer.append(charBuffer);
			byteBuffer.clear();
			charBuffer.clear();
			scanBuffer(stringBuffer);
		}
	}
	
	public void write() throws IOException {
		String message;
		synchronized(outputQueue) {
			message = outputQueue.poll();
			if(outputQueue.isEmpty()) {
				try {
					listenForRead();
				}
				catch(ClosedChannelException e) {}
			}
		}
		if(message != null) {
			raiseWritingEvent(message);
			channel.write(encoder.encode(CharBuffer.wrap(message)));
		}
	}
	
	protected void raiseReadingEvent(String message) {
		distribute(new ReadingEvent(address, message));
	}

	protected void raiseWritingEvent(String message) {
		distribute(new WritingEvent(address, message));
	}
	
	protected abstract void scanBuffer(StringBuilder buffer);
	
	public void send(String message) {
		synchronized(outputQueue) {
			outputQueue.add(message);
			try {
				listenForReadWrite();
			}
			catch(ClosedChannelException e) {}
		}
	}
}
