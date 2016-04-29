package locked.base;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import common.interfaces.NodeInterface;

public class LockedNode implements NodeInterface<LockedNode> {

	public int value;
	public LockedNode next;
	private Lock lock;

	public LockedNode(int value, LockedNode node) {
		this.value = value;
		this.next = node;
		lock = new ReentrantLock();
	}

	public void lock() {
		lock.lock();
	}

	public boolean trylock() {
		return lock.tryLock();
	}

	public void unlock() {
		lock.unlock();
	}

	public int getValue() {
		return value;
	}

	public LockedNode next() {
		return next;
	}
}
