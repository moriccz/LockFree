package lockfree.selfOrganizing;

import java.util.concurrent.atomic.AtomicStampedReference;

import common.interfaces.SelfOrganizingListInterface;
import lockfree.base.Node;

public class SelfOrganizingList implements SelfOrganizingListInterface<Node> {

	private final int NONE = 0;
	private final int ADD = 1;
	private final int SEARCH = 2;
	private final int REMOVE = 3;
	private final int BEFOREREMOVE = 6;
	private final int AFTERREMOVE = 7;
	public AtomicStampedReference<Node> head;
	public AtomicStampedReference<Node> tail;

	public SelfOrganizingList() {
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
			Node oldCurrent = current.getReference();

			if (next.getReference() == null) {
				// element before tail is not stamped
				if (current.getStamp() != NONE && next.getStamp() != NONE)
					continue back;

				// stamp tail
				Node newCurrent = new Node(oldCurrent.value, new Node(value, null));

				if (current.attemptStamp(oldCurrent, ADD)) {
					// place new node with value to tail
					if (current.compareAndSet(oldCurrent, newCurrent, ADD, NONE))
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

		back: while (true) {
			AtomicStampedReference<Node> first = head;
			AtomicStampedReference<Node> second = first.getReference().next;
			AtomicStampedReference<Node> third = second.getReference().next;

			again: while (true) {

				Node oldFirst = first.getReference();
				Node oldSecond = second.getReference();
				// if the removable node is the second element
				if (second.getReference().getValue() == value) {

					// if the mask is not stamped by others
					if (first.getStamp() != NONE && second.getStamp() != NONE && third.getStamp() != NONE)
						continue back;

					// try stamp to begin removal
					if (first.attemptStamp(oldFirst, BEFOREREMOVE) && second.attemptStamp(oldSecond, REMOVE)
							&& third.attemptStamp(oldSecond.next(), AFTERREMOVE)) {

						/*
						 * create new node with value of first and next
						 * reference of second to replace the first node
						 */

						Node afterRemove = new Node(oldFirst.getValue(), oldSecond.next());

						// replace the node
						if (first.compareAndSet(oldFirst, afterRemove, BEFOREREMOVE, NONE))
							return true;

					}
				}
				if (third.getReference() == null)
					return false;

				first = second;
				second = third;
				third = third.getReference().next;

			}
		}

	}

	public Node search(int value) {
		again: while (true) {

			// AtomicStampedReference<Node> before = head;

			AtomicStampedReference<Node> swapA = head;
			AtomicStampedReference<Node> swapB = swapA.getReference().next;

			if (swapB.getReference().value == value)
				return swapB.getReference();

			back: while (true) {

				// Node oldPredecessor = before.getReference();
				Node oldReplacable = swapA.getReference();
				Node oldSearchable = swapB.getReference();
				// verify if it is the node with the searched value
				if (swapB.getReference().value == value) {

					// check if the mask is stamped anywhere
					// if (before.getStamp() != NONE)
					// continue again;

					if (swapA.getStamp() != NONE)
						continue again;

					if (swapB.getStamp() != NONE)
						continue back;

					// stamp the mask
					/* before.attemptStamp(oldPredecessor, STOPSEARCH) && */

					if (swapA.attemptStamp(oldReplacable, SEARCH) && swapB.attemptStamp(oldSearchable, SEARCH)) {

						// create the swaped construction from pred-a-b ->
						// pred-b-a
						Node swappedNode = new Node(oldSearchable.value,
								new Node(oldReplacable.value, oldSearchable.next()));

						// try to set the node
						if (swapA.compareAndSet(oldReplacable, swappedNode, SEARCH, NONE)) {
							return swapA.getReference();

						}
					}
					continue back;
				}

				// if end of the list and it was not found
				if (swapB.getReference().next() == null) {
					return null;
				}
				// before = swapA;

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
		while (node != null) {
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
		}
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
