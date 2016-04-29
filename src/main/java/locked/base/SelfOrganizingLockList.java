package locked.base;

import java.util.concurrent.locks.ReentrantLock;

import common.interfaces.SelfOrganizingListInterface;

public class SelfOrganizingLockList implements SelfOrganizingListInterface<Node> {

	public Node head;
	public Node tail;
	private ReentrantLock lock;

	public SelfOrganizingLockList() {
		this.head = new Node(-1, null);
		this.tail = new Node(-1, head);
		lock = new ReentrantLock();

	}

	public boolean add(int value) {

		lock.lock();
		Node node = head;
		for (node = head; node != null; node = node.next) {
			if (node.next == null) {
				Node newNode = new Node(value, null);
				node.next = newNode;

				lock.unlock();
				return true;
			}
		}
		lock.unlock();
		return false;
	}

	public boolean remove(int value) {
		lock.lock();

		for (Node node = head; node.next != null; node = node.next) {
			if (node.next.value == value) {
				node.next = node.next.next;
				lock.unlock();
				return true;
			}

		}
		lock.unlock();
		return false;

	}

	public Node search(int value) {
		lock.lock();
		Node node = head;
		if (node.next.value == value)
			return node.next;
		for (node = head; node.next != null; node = node.next) {

			if (node.next.value == value) {
				int aux = node.next.value;
				node.next.value = node.value;
				node.value = aux;
				lock.unlock();
				return node;
			}

		}

		lock.unlock();
		return node;

	}

	public boolean contains(int value) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean list() {
		System.out.print("List: [ ");
		for (Node node = this.head.next; node != null; node = node.next) {
			System.out.print(node.value + " ");
		}
		System.out.println("]");
		return true;
	}

	public int size() {
		lock.lock();
		Node node = head.next;
		int size = 0;
		for (node = head.next; node != null; node = node.next) {
			size++;
		}
		lock.unlock();
		return size;
	}

}
