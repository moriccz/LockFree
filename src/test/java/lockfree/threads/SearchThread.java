package lockfree.threads;

import lockfree.selfOrganizing.SelfOrganizingList;

public class SearchThread extends Thread {

	private SelfOrganizingList sol;
	private Integer search;

	public SearchThread(SelfOrganizingList sol, Integer search) {
		this.sol = sol;
		this.search = search;
	}

	@Override
	public void run() {
		sol.search(search);

	}
}
