package lockfree.base;

import java.util.concurrent.atomic.AtomicStampedReference;

import common.interfaces.NodeInterface;

public class Node implements NodeInterface<Node> {

	public static final int INIT_INT = -1;

	public int value;
	public volatile AtomicStampedReference<Node> next;

	public Node(final int value, final Node next) {
		this.value = value;
		this.next = new AtomicStampedReference<Node>(next, 0);
	}

	public int getValue() {
		return value;
	}

	public Node next() {
		return next.getReference();
	}

	public boolean cas(Node newN, Node expectedN, int expectedStamp, int newStamp) {
		return next.compareAndSet(expectedN, newN, expectedStamp, newStamp);

	}

	public int isStamped() {
		return next.getStamp();
	}

	public boolean tryStamp(Node node, int newStamp) {
		return next.attemptStamp(node, newStamp);
	}

}
