package locked.base;

import common.interfaces.NodeInterface;

public class Node implements NodeInterface<Node> {

	public int value;
	public Node next;

	public Node(int value, Node node) {
		this.value = value;
		this.next = node;
	}

	public int getValue() {
		return value;
	}

	public Node next() {
		return next;
	}

}
