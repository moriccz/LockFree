package tests;


import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import lockFreeQueue.Queue;


public class enqDeqTest {

	Queue<Integer> sol=new Queue<Integer>(1);
	
	@BeforeTest
	public void build(){
		
	}
	
	

	@Test(threadPoolSize = 1, invocationCount = 4,  timeOut = 10000)
	public void test() {
		for(int i=0;i<4;i++)
		{
			sol.enqueue(i);
			System.out.println(i);
		}
		
		
		
		
	}
	
	@AfterTest
	public void showResult(){
		System.out.println("dequeue");

		for(int i=0;i<4;i++)
		System.out.println(sol.dequeue());
	}

}
