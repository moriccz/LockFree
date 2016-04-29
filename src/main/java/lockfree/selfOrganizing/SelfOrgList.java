package lockfree.selfOrganizing;

import java.util.concurrent.atomic.AtomicStampedReference;

import common.interfaces.SelfOrganizingListInterface;
import lockfree.base.Node;

public class SelfOrgList implements SelfOrganizingListInterface<Node> {

	private final int NONE = 0;
	private final int ADD = 1;
	private final int SEARCH = 2;
	private final int STOPSEARCH = 5;
	private final int REMOVE = 3;
	private final int STOPREMOVE = 6;

	public AtomicStampedReference<Node> head;
	public AtomicStampedReference<Node> tail;

	public SelfOrgList() {
		head = new AtomicStampedReference<Node>(new Node(Node.INIT_INT, null), 0);
		tail = new AtomicStampedReference<Node>(new Node(Node.INIT_INT, head.getReference()), 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lockfree.selfOrganizing.SelfOrganizingListInterface#add(int) get to
	 * tail and insert element
	 */
	public boolean add(int value) {

		AtomicStampedReference<Node> current = head;
		AtomicStampedReference<Node> next = current.getReference().next;

		back: while (true) {
			// next is the tail
			if (next.getReference() == null) {
				// element before tail is not stamped
				if (current.getStamp() != NONE)
					continue back;

				// stamp tail
				if (next.attemptStamp(null, ADD)) {
					// place new node with value to tail
					if (next.compareAndSet(null, new Node(value, null), ADD, NONE))
						return true;
				}

				continue back;
			}

			current = next;
			next = next.getReference().next;

		}

	}
	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see lockfree.selfOrganizing.SelfOrganizingListInterface#remove(int)
	 * 
	 * we have in the list -> first -> second -> third-> -> first -> third ->
	 */

	public boolean remove(int value) {
		again: while (true) {
			AtomicStampedReference<Node> first = head;
			AtomicStampedReference<Node> second = first.getReference().next;
			AtomicStampedReference<Node> third = second.getReference().next;

			back: while (true) {

				Node oldFirst = first.getReference();
				Node oldSecond = second.getReference();

				// if the removable node is the second element
				if (second.getReference().getValue() == value) {
					// if the mask is not stamped by others
					if (first.getStamp() != NONE)
						continue again;

					if (second.getStamp() != NONE && third.getStamp() != NONE)
						continue back;

					// try stamp to begin removal
					if (first.attemptStamp(oldFirst, STOPREMOVE) && second.attemptStamp(oldSecond, REMOVE)
							&& third.attemptStamp(oldSecond.next(), STOPREMOVE)) {

						/*
						 * create new node with value of first and next
						 * reference of second to replace the first node
						 */

						Node afterRemove = new Node(oldFirst.getValue(), oldSecond.next());

						// replace the node
						if (first.compareAndSet(oldFirst, afterRemove, STOPREMOVE, NONE))
							return true;
					}
				} else {
					if (third.getReference() == null)
						return false;

					first = second;
					second = third;
					third = third.getReference().next;
				}
			}

		}
	}

	public Node search(int value) {
		again: while (true) {
			AtomicStampedReference<Node> before = head;
			AtomicStampedReference<Node> swapA = before.getReference().next;

			if (swapA.getReference().value == value)
				return swapA.getReference();

			AtomicStampedReference<Node> swapB = swapA.getReference().next;

			back: while (true) {

				Node oldPredecessor = before.getReference();
				Node oldReplacable = swapA.getReference();
				Node oldSearchable = swapB.getReference();
				// verify if it is the node with the searched value
				if (swapB.getReference().value == value) {

					if (before.getStamp() != NONE)
						continue again;

					// check if the mask is stamped anywhere
					if (swapA.getStamp() != NONE && swapB.getStamp() != NONE)
						continue back;

					// stamp the mask
					if (before.attemptStamp(oldPredecessor, SEARCH) && swapA.attemptStamp(oldReplacable, STOPSEARCH)
							&& swapB.attemptStamp(oldSearchable, STOPSEARCH)) {

						// create the swaped construction from pred-a-b ->
						// pred-b-a
						Node swappedNode = new Node(oldSearchable.value,
								new Node(oldReplacable.value, oldSearchable.next()));

						// try to set the node
						if (before.compareAndSet(oldPredecessor, new Node(oldPredecessor.value, swappedNode), SEARCH,
								NONE))
							return before.getReference();
					}
					continue back;
				}

				// if end of the list and it was not found
				if (swapB.getReference().next() == null) {
					return null;
				}
				before = swapA;
				swapA = swapB;
				swapB = swapB.getReference().next;

			}

		}
	}

	public boolean contains(int value) {
		while (true) {
			Node pred = head.getReference();

			if (pred == null)
				return false;

			if (pred.value == value)
				return true;

			pred = pred.next();

		}
	}

	public boolean list() {
		Node node = head.getReference().next();
		System.out.println("The List: ");
		System.out.print("[ ");
		int k = 1, row = 0;
		System.out.print(row + ": ");
		do {
			if (k == 10) {
				System.out.println(node.value);
				row++;
				k = 1;
				System.out.print(row + "0: ");
			} else {
				k++;
				System.out.print(node.value + " ");
			}

			node = node.next.getReference();
		} while (node != null);
		System.out.println(" ]");
		return false;
	}

	public int size() {
		int size = 0;
		Node node = head.getReference();
		while (node.next.getReference() != null) {

			size++;

			node = node.next.getReference();

		}
		return size;
	}

}
