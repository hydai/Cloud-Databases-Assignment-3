package org.vanilladb.core.server.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.vanilladb.core.util.PropertiesFetcher;

/**
 * The task manager of VanillaCore. This manager is responsible for maintaining
 * the thread pool of worker thread.
 * 
 */
public class TaskMgr {
	private ExecutorService executor;
	private final static int THREAD_POOL_SIZE;

	static {
		THREAD_POOL_SIZE = PropertiesFetcher.getPropertyAsInteger(
				TaskMgr.class.getName() + ".THREAD_POOL_SIZE", 150);
	}

	public TaskMgr() {
		executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
	}

	public void runTask(Task task) {
		executor.execute(task);
	}
}
