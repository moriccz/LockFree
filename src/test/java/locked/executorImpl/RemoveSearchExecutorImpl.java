package locked.executorImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import common.interfaces.SelfOrganizingListInterface;
import locked.executor.LockedExecutor;
import locked.threads.RemovingLockedThread;
import locked.threads.SearchLockedThread;

public class RemoveSearchExecutorImpl extends LockedExecutor {

	private int failCount = 0;

	Iterator<Integer> it;
	Iterator<Integer> itD;

	Set<Integer> setAdd = new HashSet<Integer>();
	Set<Integer> setRemove = new HashSet<Integer>();

	private int removes = 500;
	private int searches = 550;
	private int searchable = 1000000;

	public RemoveSearchExecutorImpl(int solSize, int removes, int searches, Integer searchable,
			SelfOrganizingListInterface sol) {
		this.sol = sol;
		this.removes = removes;
		this.searchable = searchable;
		this.searches = searches;

		for (int i = 0; i < solSize; i++)
			sol.add(i);

		for (int i = 0; i < removes; i++)
			setRemove.add(i);

		sol.add(searchable);

		it = setAdd.iterator();
		itD = setRemove.iterator();
	}

	public HashMap<String, String> removeSearch() {
		List<RemovingLockedThread> removeList = new ArrayList<RemovingLockedThread>();

		this.start = System.nanoTime();

		for (int i = 0; i < removes; i++) {
			Thread t = new RemovingLockedThread(sol, itD.next());

			executor.submit(t);
			removeList.add((RemovingLockedThread) t);
		}

		for (int i = 0; i < searches; i++) {
			executor.submit(new SearchLockedThread(sol, searchable));
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

		for (RemovingLockedThread thread : removeList) {
			if (!thread.fail)
				failCount++;
		}

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seconds", String.valueOf(seconds));
		map.put("size", String.valueOf(sol.size()));
		map.put("failCount", String.valueOf(failCount));

		System.out.println("RemoveSearch Time: " + seconds + "s Size: " + sol.size() + " RemoveFailed: " + failCount);
		return map;
	}
}
