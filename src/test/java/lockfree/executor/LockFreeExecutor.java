package lockfree.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lockfree.selfOrganizing.SelfOrganizingList;

public abstract class LockFreeExecutor {

	private final int concurrentThreads = 8;

	public long end = 0;

	public long start = 0;

	public ExecutorService executor = Executors.newFixedThreadPool(concurrentThreads);

	public SelfOrganizingList sol = new SelfOrganizingList();

}
