package lockfree.threads;

import lockfree.selfOrganizing.SelfOrganizingList;

public class RemovingThread extends Thread {

	private SelfOrganizingList sol;
	private Integer removeable;
	public boolean fail = false;

	public RemovingThread(SelfOrganizingList sol, Integer removeable) {
		this.sol = sol;
		this.removeable = removeable;
	}

	@Override
	public void run() {
		fail = sol.remove(removeable);

	}

}
