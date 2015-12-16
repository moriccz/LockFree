package lockFreeQueue;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;


public class Queue<T> {
	
	//Insert at tail and remove from head
	  private transient volatile Node<T> _head = new Node<T>(null, null);

	  private transient volatile Node<T> _tail;
    

	  AtomicReference<Node> nextRef=new AtomicReference<Node>();
      AtomicReference<Node> tailRef=new AtomicReference<Node>();
	 
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
    	
        Node<T> oldHead;
        Node<T> next;
        Node<T> oldTail;
        Node<T> head;
        
        tailRef.set(_tail);
        nextRef.set(_head.getNext());
        
        while (true)
        {
        	 oldTail = tailRef.get();
             head = nextRef.get();

            if (head == null)
                return null;

            oldHead = nextRef.get();
            next = oldHead.getNext();

            if (next == oldHead)
            {
                if (oldHead == oldTail)
                {
                   tailRef.compareAndSet(oldTail, next);

                    if (next == null && nextRef.compareAndSet(oldHead, null))
                        return null; 
                }
                else
                {
                    if (nextRef.compareAndSet(oldHead, next))
                        return oldHead.getValue();
                }
            }
        }
    }
    
    
    public void enqueue(T item){
    	Node<T> node = new Node<T>(item);
        
    	tailRef.set(_tail);
    	nextRef.set(_tail.getNext());
    	 
        Node<T> oldTail;
        Node<T> next;
        while (true)
        {
        	
        	
            oldTail=tailRef.get();
            if (oldTail==null && tailRef.compareAndSet(null, node))
                break;
            
            next=oldTail.getNext();

            if (oldTail == tailRef.get())
            {
                if (nextRef.get() == null)
                {
                    if (nextRef.compareAndSet(null, node))
                        break;
                }
                else
                {
                	tailRef.compareAndSet(oldTail, next);
                }
            }
        }

        tailRef.compareAndSet( oldTail, node);
        nextRef.compareAndSet( null, node);
    }
    
    
    
	
}
