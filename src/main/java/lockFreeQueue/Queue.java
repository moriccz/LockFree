package lockFreeQueue;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;


public class Queue<T> {
	
	//Insert at tail and remove from head
	  private transient volatile Node<T> _head = new Node<T>(null, null);

	  private transient volatile Node<T> _tail;
    

	  @SuppressWarnings("rawtypes")
	private final AtomicReferenceFieldUpdater<Queue, Node>
	    tailUpdater =AtomicReferenceFieldUpdater.newUpdater(Queue.class, Node.class, "_tail");
	  @SuppressWarnings("rawtypes")
	private final AtomicReferenceFieldUpdater<Node, Node>
	    nextUpdater =AtomicReferenceFieldUpdater.newUpdater(Node.class, Node.class, "next");
	 
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
                   tailUpdater.compareAndSet( this, oldTail, next);

                    if (next == null && nextUpdater.compareAndSet(_head, oldHead, null))
                        return null; 
                }
                else
                {
                    if (nextUpdater.compareAndSet(_head, oldHead, next))
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

            if (_tail == null && tailUpdater.compareAndSet(this, null, node))
                break;
            
            next = oldTail.next;

            if (_tail == oldTail)
            {
                if (next == null)
                {
                    if (nextUpdater.compareAndSet(_tail.next, null, node))
                        break;
                }
                else
                {
                	tailUpdater.compareAndSet(this, oldTail, next);
                }
            }
        }

        tailUpdater.compareAndSet(this, oldTail, node);
        nextUpdater.compareAndSet( _head.next, null, node);
    }
    
    
    
	
}
