package lockfree.selfOrganizing;

import lockfree.base.Node;

public class SelfOrganizingList implements SelfOrganizingListInterface {

	private final int NONE = 0;

	private final int ADD = 0;
	private final int SEARCH = 0;
	private final int REMOVE = 0;
	private final int NOCHANGE = 0;

	public Node head;
	public Node tail;

	public SelfOrganizingList() {
		head = new Node(Node.INIT_INT, null);
		tail = new Node(Node.INIT_INT, head);
	}

	public Node iterate(int value) {
		again: while (true) {
			Node pred = head;
			Node curr = pred.next.getReference();

			// TODO better only if curr is the searched to get back
			if (pred.getValue() == value) {
				if (pred.isStamped() != 0)
					continue again;
				return pred;
			}

			if (curr.next != null) {
				pred = curr;
				curr = curr.next.getReference();
			} else
				return pred;
		}

	}

	public boolean add(int value) {

		again: while (true) {
			Node pred = head;
			Node curr = pred.next();

			while (true) {

				if (curr == null) {
					if (pred.isStamped() != NONE)
						continue again;
					if (pred.next.attemptStamp(curr, ADD))
						continue again;

					Node node = new Node(value, curr);
					if (pred.next.compareAndSet(curr, node, ADD, NONE))
						return true;

				}

				// if(curr!=null){
				pred = curr;
				curr = curr.next.getReference();
				// }
			}

		}
	}

	public boolean remove(int value) {
		again: while (true) {
			Node pred = head;
			Node curr = pred.next();
			Node next = curr.next();
			while (true) {

				if (curr.value == value) {
					if (pred.isStamped() != NONE && curr.isStamped() != NONE)
						continue again;
					pred.tryStamp(curr, REMOVE);
					curr.tryStamp(next, NOCHANGE);

					if (pred.cas(next, curr, REMOVE, NONE))
						return true;
				}
				if (next.next() == null)
					return false;
				pred = curr;
				curr = next;
				next = next.next();

			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.licence.lockfree.selfOrganizing.SelfOrganizingListInterface#search(
	 * int) <-----------> head - - - - pred - replacable - seached - - - tail
	 */
	public Node search(int value) {

		again: while (true) {
			Node pred = head;
			Node replacable = pred.next();
			Node searched = replacable.next();

			if (replacable.value == value)
				return replacable;

			while (true) {
				if (searched.value == value) {
					if (pred.isStamped() != NONE && replacable.isStamped() != NONE && searched.isStamped() != NONE)
						continue again;
					pred.tryStamp(replacable, SEARCH);
					replacable.tryStamp(searched, NOCHANGE);

					Node newNode = new Node(searched.value, new Node(replacable.value, searched.next()));

					if (pred.cas(newNode, replacable, SEARCH, NONE))
						return pred.next();

				}

				if (searched.next() == null)
					return null;

				pred = replacable;
				replacable = searched;
				searched = searched.next();

			}

		}
	}

	public boolean contains(int value) {
		while (true) {
			Node pred = head;

			if (pred == null)
				return false;

			if (pred.value == value)
				return true;

			pred = pred.next();

		}

	}

	public boolean list() {
		Node node = head.next.getReference();
		System.out.println("The List: ");
		System.out.print("[ ");

		do {
			System.out.print(node.value + " ");

			node = node.next.getReference();
		} while (node != null);
		System.out.println(" ]");
		return false;
	}

	public int size() {

		int size = 0;
		Node node = head;
		while (node.next.getReference() != null) {

			size++;

			node = node.next.getReference();

		}
		return size;
	}

}
