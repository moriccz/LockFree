package lockfree.selfOrganizing;

import lockfree.base.Node;

public interface SelfOrganizingListInterface {

	public boolean add(int value);

	public boolean remove(int value);

	public Node search(int value);

	public boolean contains(int value);

	public boolean list();

	public int size();

}
