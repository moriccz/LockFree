package tests;

import org.testng.annotations.Test;

import selfOrganizingList.SelfOrganizingList;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

public class solTest {
	
	SelfOrganizingList<Integer> sol=new SelfOrganizingList<Integer>(1,2);
	
@Test(threadPoolSize = 1, invocationCount = 1,  timeOut = 1000000)
   public void f() {
	sol.remove(4);
	
	//sol.insert(6);
	
	Integer x=sol.search(new Integer(5)).getValue();
	System.out.println(x);
  }
  @BeforeTest
  public void beforeTest() {
  }

  @AfterTest
  public void afterTest() {
	
	  
	  int x=0;
	  x++;
  }

}
