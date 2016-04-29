package common.interfaces;

public interface SelfOrganizingListInterface<T> {

	public boolean add(int value);

	public boolean remove(int value);

	public T search(int value);

	public boolean contains(int value);

	public boolean list();

	public int size();

}
