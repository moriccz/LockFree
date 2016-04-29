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
import locked.threads.AddingLockedThread;
import locked.threads.RemovingLockedThread;

public class AddRemoveExecutorImpl extends LockedExecutor {

	private int failCount = 0;

	Iterator<Integer> it;
	Iterator<Integer> itD;

	Set<Integer> setAdd = new HashSet<Integer>();
	Set<Integer> setRemove = new HashSet<Integer>();

	private int adds = 1000;
	private int removes = 500;

	public int getAdds() {
		return adds;
	}

	public void setAdds(int adds) {
		this.adds = adds;
	}

	public int getRemoves() {
		return removes;
	}

	public void setRemoves(int removes) {
		this.removes = removes;
	}

	public AddRemoveExecutorImpl(int adds, int removes, SelfOrganizingListInterface sol) {
		this.sol = sol;
		this.adds = adds;
		this.removes = removes;

		for (int i = 0; i < adds; i++)
			setAdd.add(i);

		for (int i = 0; i < removes; i++)
			setRemove.add(i);

		it = setAdd.iterator();
		itD = setRemove.iterator();

	}

	public HashMap<String, String> addRemove() {
		List<RemovingLockedThread> removeList = new ArrayList<RemovingLockedThread>();

		this.start = System.nanoTime();

		for (int i = 0; i < adds; i++) {
			executor.submit(new AddingLockedThread(sol, it.next()));
		}

		for (int i = 1; i <= removes; i++) {
			Thread t = new RemovingLockedThread(sol, itD.next());

			executor.submit(t);
			removeList.add((RemovingLockedThread) t);
		}

		try {

			executor.shutdown();
			executor.awaitTermination(10, TimeUnit.SECONDS);
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

		System.out.println("AddRemove Time: " + seconds + "s Size: " + sol.size() + " RemoveFailed: " + failCount);

		return map;
	}

}
