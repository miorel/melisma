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

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import util.LogManager;
import util.ds.Pair;

public class ScheduledThread extends Thread {
	private final static Runnable nullRunnable = null;
	
	private final Queue<Pair<Long,Runnable>> taskQueue = new PriorityQueue<Pair<Long,Runnable>>(1, new Comparator<Pair<Long,Runnable>>() {
		@Override
		public int compare(Pair<Long,Runnable> a, Pair<Long,Runnable> b) {
			return a.getFirst().compareTo(b.getFirst());
		}
	});
	
	public ScheduledThread() {
		super();
	}

	public ScheduledThread(String name) {
		super(name);
	}

	public ScheduledThread(ThreadGroup group, String name, long stackSize) {
		super(group, null, name, stackSize);
	}

	public ScheduledThread(ThreadGroup group) {
		super(group, nullRunnable);
	}

	public ScheduledThread(ThreadGroup group, String name) {
		super(group, name);
	}

	@Override
	public void run() {
		while(!interrupted()) {
			Runnable task = null;
			boolean keepGoing = true;
			synchronized(taskQueue) {
				for(long nap = 0; taskQueue.isEmpty() || (nap = taskQueue.peek().getFirst() - System.currentTimeMillis()) > 0;)
					try {
						taskQueue.wait(taskQueue.isEmpty() ? 0 : nap);
					}
					catch(InterruptedException e) {
						keepGoing = false;
						interrupt();
						break;
					}
				if(keepGoing && !taskQueue.isEmpty())
					task = taskQueue.poll().getSecond();
			}
			if(task != null)
				try {
					task.run();
				}
				catch(Throwable t) {
					LogManager.getInstance().report("SCHEDULER", t);
				}
		}
	}
	
	public void queueTask(long time, Runnable runnable) {
		synchronized(taskQueue) {
			taskQueue.add(new Pair<Long,Runnable>(time, runnable));
			taskQueue.notifyAll();
		}
	}
}
