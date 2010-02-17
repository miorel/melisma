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
package util;

import java.io.PrintStream;

public class LogManager {
	private static final LogManager instance = new LogManager();
	
	private PrintStream printStream = System.out;
	
	private LogManager() {
	}
	
	public void log(Object domain, long time, char marker, String message) {
		printStream.printf("[%1$s] %2$d %3$s%3$s%3$s %4$s%n", domain, time, marker, message);
	}
	
	public void log(Object domain, char marker, String message) {
		log(domain, System.currentTimeMillis(), marker, message);
	}

	public void log(Object domain, String message) {
		log(domain, '*', message);
	}

	public void log(String message) {
		log("[MAIN]", message);
	}
	
	public void warn(Object domain, String message) {
		log(domain, '!', message);
	}

	public void info(Object domain, String message) {
		log(domain, message);
	}
	
	public void incoming(Object domain, String message) {
		log(domain, '<', message);
	}

	public void outgoing(Object domain, String message) {
		log(domain, '>', message);
	}
	
	public void report(Object domain, Throwable throwable) {
		warn(domain, "Caught: " + throwable);
		throwable.printStackTrace(printStream);
	}
	
	public static LogManager getInstance() {
		return instance;
	}
}
