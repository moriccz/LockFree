package lockfree;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import lockfree.selfOrganizing.SelfOrganizingList;

public class LockFreeCase1 {

	SelfOrganizingList sol;

	Long start = (long) 0, end = (long) 0;
	int i = 0;
	static Long len = (long) 0;

	Iterator<Integer> it;
	Iterator<Integer> itD;

	int count = 0;

	@BeforeTest
	protected void setUp() throws Exception {

		sol = new SelfOrganizingList();

		System.gc();

		Set<Integer> setE = new HashSet<Integer>();
		Set<Integer> setD = new HashSet<Integer>();
		for (int i = 1; i < 1000; i++)
			// setE.add(i);
			sol.add(5);
		for (int i = 0; i < 20000; i += 2)
			setD.add(i);
		sol.add(4);
		sol.add(6);
		it = setE.iterator();
		itD = setD.iterator();

		start = System.nanoTime();
	}

	@AfterTest
	protected void tearDown() throws Exception {
		end = System.nanoTime();

		double seconds = (end - start) / 1000000000.0;
		System.out.println("Time: " + seconds + "s Size: " + sol.size() + " Not found: " + count);

		sol.list();

	}

	@Test(threadPoolSize = 8, invocationCount = 1000, timeOut = 10000)
	public void test() {
		sol.add(itD.next());
		sol.add(5);
		sol.search(6);
		if (!sol.remove(5))
			count++;
		// sol.add(4);

	}

	// @Test(threadPoolSize = 5, invocationCount = 1005, timeOut = 10000)
	// public void test2() {
	// sol.search(999);
	// }

}
