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

public class AddRemoveExecutorImpl extends LockFreeExecutor {

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

	public AddRemoveExecutorImpl(int adds, int removes) {
		this.adds = adds;
		this.removes = removes;

		for (int i = 0; i < adds; i++)
			sol.add(i);

		for (int i = adds; i < 2 * adds; i++)
			setAdd.add(i);

		for (int i = 0; i < removes; i++)
			setRemove.add(i);

		it = setAdd.iterator();
		itD = setRemove.iterator();

	}

	public HashMap<String, String> addRemove() {
		List<RemovingThread> removeList = new ArrayList<RemovingThread>();

		this.start = System.nanoTime();

		for (int i = 0; i < adds; i++) {
			executor.submit(new AddingThread(sol, it.next()));
		}

		for (int i = 0; i < removes; i++) {
			Thread t = new RemovingThread(sol, itD.next());

			executor.submit(t);
			removeList.add((RemovingThread) t);
		}

		try {

			executor.shutdown();
			executor.awaitTermination(2, TimeUnit.SECONDS);
			this.end = System.nanoTime();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		double seconds = (end - start) / 1000000000.0;

		sol.list();

		for (RemovingThread thread : removeList) {
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
