package locked.threads;

import common.interfaces.SelfOrganizingListInterface;

public class AddingLockedThread extends Thread {

	private SelfOrganizingListInterface sol;
	private Integer addable;

	public AddingLockedThread(SelfOrganizingListInterface sol, Integer add) {
		this.sol = sol;
		this.addable = add;
	}

	@Override
	public void run() {
		sol.add(addable);

	}

}
