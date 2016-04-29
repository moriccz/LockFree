package lockfree.executorImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import lockfree.executor.LockFreeExecutor;
import lockfree.threads.AddingThread;
import lockfree.threads.RemovingThread;
import lockfree.threads.SearchThread;

public class AddRemoveSearchExecutorImpl extends LockFreeExecutor {

	private int failCount = 0;

	Iterator<Integer> it;
	Iterator<Integer> itD;

	Set<Integer> setAdd = new HashSet<Integer>();
	Set<Integer> setRemove = new HashSet<Integer>();

	private int adds = 1000;
	private int removes = 500;
	private int searches = 550;
	private int searchable = 1000000;

	public AddRemoveSearchExecutorImpl(int adds, int removes, int searches, int searchable) {
		this.adds = adds;
		this.removes = removes;
		this.searchable = searchable;
		this.searches = searches;

		for (int i = 1; i < adds; i++)
			setAdd.add(i);

		for (int i = 0; i < removes; i++)
			setRemove.add(i);

		setAdd.add(searchable);

		it = setAdd.iterator();
		itD = setRemove.iterator();

	}

	public HashMap<String, String> addRemoveSearch() {

		List<RemovingThread> removeList = new ArrayList<RemovingThread>();

		this.start = System.nanoTime();

		for (int i = 1; i <= adds; i++) {
			executor.submit(new AddingThread(sol, it.next()));
		}

		for (int i = 1; i <= removes; i++) {
			Thread t = new RemovingThread(sol, itD.next());

			executor.submit(t);
			removeList.add((RemovingThread) t);
		}

		for (int i = 0; i < searches; i++) {
			executor.submit(new SearchThread(sol, searchable));
		}

		try {

			executor.shutdown();
			executor.awaitTermination(2, TimeUnit.SECONDS);
			this.end = System.nanoTime();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		double seconds = (end - start) / 1000000000.0;

		// sol.list();

		for (RemovingThread thread : removeList) {
			if (!thread.fail)
				failCount++;
		}

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seconds", String.valueOf(seconds));
		map.put("size", String.valueOf(sol.size()));
		map.put("failCount", String.valueOf(failCount));

		System.out
				.println("AddRemoveSearch Time: " + seconds + "s Size: " + sol.size() + " RemoveFailed: " + failCount);

		return map;
	}

}
