package lockfree.base;

public interface NodeInterface {

	public int getValue();

	public Node next();

	public boolean cas(Node newN, Node expectedN, int expectedStamp, int newStamp);

	public int isStamped();

	public boolean tryStamp(Node node, int newStamp);

}
