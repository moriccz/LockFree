package locked.base;

import common.interfaces.SelfOrganizingListInterface;

public class SelfOrganizingListFineGrained implements SelfOrganizingListInterface<LockedNode> {

	public LockedNode head;
	public LockedNode tail;

	public SelfOrganizingListFineGrained() {
		this.head = new LockedNode(-1, null);
		this.tail = new LockedNode(-1, head);

	}

	public boolean add(int value) {
		LockedNode prev = head;

		while (true) {
			prev.lock();
			if (prev.next == null) {

				LockedNode newNode = new LockedNode(value, null);
				prev.next = newNode;
				prev.unlock();
				return true;
			}
			prev.unlock();
			prev = prev.next;

		}

	}

	public boolean remove(int value) {
		LockedNode prev = head;
		LockedNode curr = prev.next;

		while (true) {
			prev.lock();
			curr.lock();
			if (curr.value == value) {

				prev.next = curr.next;

				curr.unlock();
				prev.unlock();
				return true;
			}

			curr.unlock();
			prev.unlock();

			if (curr.next == null)
				return false;

			prev = curr;
			curr = curr.next;

		}

	}

	public LockedNode search(int value) {

		LockedNode node = head;
		LockedNode nextNode = node.next;
		if (nextNode.value == value)
			return nextNode;

		while (true) {
			node.lock();
			nextNode.lock();

			if (nextNode.value == value) {
				int aux = nextNode.value;
				nextNode.value = node.value;
				node.value = aux;

				nextNode.unlock();
				node.unlock();
				return node;
			}

			nextNode.unlock();
			node.unlock();

			if (nextNode.next == null)
				return null;

			node = nextNode;
			nextNode = nextNode.next;
		}

	}

	public boolean contains(int value) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean list() {
		System.out.print("List: [ ");
		for (LockedNode node = this.head.next; node != null; node = node.next) {
			System.out.print(node.value + " ");
		}
		System.out.println("]");
		return true;
	}

	public int size() {
		LockedNode node = head.next;
		int size = 0;
		for (node = head.next; node != null; node = node.next) {
			size++;
		}
		return size;
	}

}
