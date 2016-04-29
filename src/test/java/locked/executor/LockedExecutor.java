package locked.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.interfaces.SelfOrganizingListInterface;

public abstract class LockedExecutor {

	private final int concurrentThreads = 5;

	public long end = 0;

	public long start = 0;

	public ExecutorService executor = Executors.newFixedThreadPool(concurrentThreads);

	protected SelfOrganizingListInterface sol;
}
