package lockfree;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import lockfree.selfOrganizing.SelfOrganizingList;
import lockfree.threads.AddingThread;

public class LockFreeCase2 {

	private final static int MAX_NUM = 1000;

	private final static int concurrentThreads = 10;

	private static long end = 0, start = 0;

	public static void main(String args[]) {

		ExecutorService executor = Executors.newFixedThreadPool(concurrentThreads);

		SelfOrganizingList sol = new SelfOrganizingList();
		start = System.nanoTime();
		for (int i = 1; i <= MAX_NUM; i++) {
			executor.submit(new AddingThread(sol, i));
		}

		try {
			end = System.nanoTime();
			executor.shutdown();
			executor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		double seconds = (end - start) / 1000000000.0;

		sol.list();

		System.out.println("Time: " + seconds + "s Size: " + sol.size());
	}

}
