package lockFreeQueue;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;


public class Queue<T> {
	
	//Insert at tail and remove from head
	  private transient volatile Node<T> _head = new Node<T>(null, null);

	  private transient volatile Node<T> _tail;
    

	  private final AtomicReferenceFieldUpdater<Node, Node>
	    atomicUpdater =AtomicReferenceFieldUpdater.newUpdater(Node.class, Node.class, "_tail");
	  private final AtomicReferenceFieldUpdater<Node, Node>
	    atomicUpdater2 =AtomicReferenceFieldUpdater.newUpdater(Node.class, Node.class, "_head");
	  
    public Queue()
    {
        _head = null;
        _tail = _head;
    }

    public Queue(T value) 
    {
        _head = new Node<T>(value);
        _tail = _head; 
    }
    
    
    public T dequeue(){
    	Node<T> head;
        Node<T> oldHead;
        Node<T> oldTail;
        Node<T> next;

        while (true)
        {
            oldTail = _tail;
            head = _head.next;

            if (head == null)
                return null;

            oldHead = head;
            next = oldHead.next;

            if (head == oldHead)
            {
                if (oldHead == oldTail)
                {
                   atomicUpdater.compareAndSet( _tail, oldTail, next);

                    if (next == null && atomicUpdater2.compareAndSet(_head.next, oldHead, null))
                        return null; 
                }
                else
                {
                    if (atomicUpdater.compareAndSet(_head.next, oldHead, next))
                        return oldHead.value;
                }
            }
        }
    }
    
    
    public void enqueue(T item){
    	Node<T> node = new Node<T>(item);
        Node<T> oldTail;
        Node<T> next;

        while (true)
        {
            oldTail = _tail;

            if (_tail == null && atomicUpdater.compareAndSet(_tail, null, node))
                break;
            
            next = oldTail.next;

            if (_tail == oldTail)
            {
                if (next == null)
                {
                    if (atomicUpdater.compareAndSet(_tail.next, null, node))
                        break;
                }
                else
                {
                	atomicUpdater.compareAndSet(_tail, oldTail, next);
                }
            }
        }

        atomicUpdater.compareAndSet( _tail, oldTail, node);
        atomicUpdater2.compareAndSet( _head.next, null, node);
    }
    
    
    
	
}
