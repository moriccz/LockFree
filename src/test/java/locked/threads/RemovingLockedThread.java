package locked.threads;

import common.interfaces.SelfOrganizingListInterface;
import lockfree.selfOrganizing.SelfOrganizingList;

public class RemovingLockedThread extends Thread {

	private  SelfOrganizingListInterface sol;
	private Integer removeable;
	public boolean fail = false;

	public RemovingLockedThread( SelfOrganizingListInterface sol, Integer removeable) {
		this.sol = sol;
		this.removeable = removeable;
	}

	@Override
	public void run() {
		fail = sol.remove(removeable);

	}

}
