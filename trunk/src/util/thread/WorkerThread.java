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

package util.thread;

import java.util.LinkedList;
import java.util.Queue;

import util.LogManager;

public class WorkerThread extends Thread {
	private final static Runnable nullRunnable = null;
	
	private final Queue<Runnable> taskQueue = new LinkedList<Runnable>();
	
	public WorkerThread() {
		super();
	}

	public WorkerThread(String name) {
		super(name);
	}

	public WorkerThread(ThreadGroup group, String name, long stackSize) {
		super(group, null, name, stackSize);
	}

	public WorkerThread(ThreadGroup group) {
		super(group, nullRunnable);
	}

	public WorkerThread(ThreadGroup group, String name) {
		super(group, name);
	}

	@Override
	public void run() {
		while(!interrupted()) {
			Runnable task = null;
			boolean keepGoing = true;
			synchronized(taskQueue) {
				while(taskQueue.isEmpty())
					try {
						taskQueue.wait();
					}
					catch(InterruptedException e) {
						keepGoing = false;
						interrupt();
						break;
					}
				task = taskQueue.poll();
			}
			if(keepGoing && task != null)
				try {
					task.run();
				}
				catch(Throwable t) {
					LogManager.getInstance().report("WORKER", t);
				}
		}
	}
	
	public void queueTask(Runnable runnable) {
		synchronized(taskQueue) {
			taskQueue.add(runnable);
			taskQueue.notifyAll();
		}
	}
}
