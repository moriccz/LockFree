package tests;


import org.testng.annotations.Test;

import lockFreeQueue.Queue;


public class enqDeqTest {

	Queue<Integer> sol=new Queue<Integer>(1);

	@Test(threadPoolSize = 3, invocationCount = 9,  timeOut = 10000)
	public void test() {
		for(int i=0;i<10;i++)
		sol.enqueue(i);
		
		
	}

}
