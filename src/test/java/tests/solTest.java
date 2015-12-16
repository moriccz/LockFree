package tests;

import org.testng.annotations.Test;

import lockFreeQueue.Queue;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

public class solTest {
	
	Queue<Integer> sol=new Queue<Integer>(1);
	
@Test(threadPoolSize = 1, invocationCount = 1,  timeOut = 10000)
   public void f() {
	//for(int i=0;i<4;i++){
	//.out.println("merge");
	//}
		sol.enqueue(5);
		sol.enqueue(5);
		sol.enqueue(5);
		
		System.out.println(sol.dequeue());
  }
  @BeforeTest
  public void beforeTest() {
  }

  @AfterTest
  public void afterTest() {
	//  for(int i=0;i<4;i++){
	//	  System.out.println("gata");
	 // }
		//	System.out.println(sol.dequeue());
  }

}
