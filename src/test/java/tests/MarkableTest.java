package tests;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicMarkableReference;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class MarkableTest {

	AtomicMarkableReference<Queue<Integer>> ref;
	Queue<Integer> queue;

	private int increment = 0;

	@Test(threadPoolSize = 10, invocationCount = 10, timeOut = 10000)
	public void f() {

		Queue<Integer> oldQueue = queue;

		queue.offer(increment++);
		ref.compareAndSet(oldQueue, queue, false, true);

	}

	@Test(threadPoolSize = 6, invocationCount = 6, timeOut = 10000)
	public void f2() {

		Queue<Integer> oldQueue = queue;

		queue.poll();
		ref.compareAndSet(oldQueue, queue, true, false);

	}

	@BeforeTest
	public void beforeTest() {

		queue = new LinkedBlockingQueue<Integer>();
		ref = new AtomicMarkableReference<Queue<Integer>>(queue, false);
		Assert.assertEquals(ref.isMarked(), false);
	}

	@AfterTest
	public void afterTest() {

		Iterator<Integer> it = queue.iterator();

		System.out.println("After test");

		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}

}
