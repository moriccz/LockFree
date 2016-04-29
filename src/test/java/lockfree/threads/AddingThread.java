package lockfree.threads;

import lockfree.selfOrganizing.SelfOrganizingList;

public class AddingThread extends Thread {

	private SelfOrganizingList sol;
	private Integer addable;

	public AddingThread(SelfOrganizingList sol, Integer add) {
		this.sol = sol;
		this.addable = add;
	}

	@Override
	public void run() {
		sol.add(addable);

	}

}
