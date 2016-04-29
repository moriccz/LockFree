package locked.threads;

import common.interfaces.SelfOrganizingListInterface;

public class SearchLockedThread extends Thread {

	private SelfOrganizingListInterface sol;
	private Integer search;

	public SearchLockedThread(SelfOrganizingListInterface sol, Integer search) {
		this.sol = sol;
		this.search = search;
	}

	@Override
	public void run() {
		sol.search(search);

	}
}
