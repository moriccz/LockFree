package tests;


import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import lockFreeQueue.Queue;


public class enqDeqTest {

	Queue<Integer> sol;
	
	@BeforeTest
	public void build(){
		sol=new Queue<Integer>(1,2);
	}
	
	

	@Test(threadPoolSize = 3, invocationCount = 3,  timeOut = 10000)
	public void test() {
		
		Integer k=0;
		
		
		k=sol.dequeue();
		if(k!=null)
			System.out.println(k);
		else System.out.println("null");
		
		
	}
	
	@AfterTest
	public void showResult(){
		
		
	}

}
