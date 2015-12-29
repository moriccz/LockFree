package lockFreeQueue;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;


public class Queue<T> {
	
	//Insert at tail and remove from head
	 private transient volatile Node<T> _head = new Node<T>(null, null);
	 
	 public AtomicReference<Node> headRef=new AtomicReference<Node>();
     public AtomicReference<Node> tailRef=new AtomicReference<Node>();
     
    public Queue(int i, int j){
    	Node<T> tail=new Node(0,null);
    	tailRef.set(tail);
    	headRef.set(new Node(null,new Node(2,new Node(3,new Node(4,new Node(5,tailRef.get()))))));
    }
    public Queue()
    {
    	headRef.set(_head.getNext());
    	tailRef.set(headRef.get());
    }
      
    public Queue(T value) 
    {
    	tailRef.set(new Node<T>(value,null));
    	_head.setNext(tailRef.get());
    	headRef.set(_head.getNext());
    }
    
    
    public T dequeue(){
    	
        Node<T> oldHead;
        Node<T> next;
        Node<T> oldTail;
        
        
        while (true)
        {
        	oldTail = tailRef.get();
            oldHead = headRef.get();
            
            if (oldHead == null)
                return null;

            
            next = oldHead.getNext();
           
                if (oldHead == oldTail)
                {
                   tailRef.compareAndSet(oldTail, next);
                   
                   if (next == null && headRef.compareAndSet(oldHead, next))
                        return oldTail.getValue(); 
                
                }
                else
                { 
                	T ret=(T)next.getValue();
                    if (headRef.compareAndSet(oldHead, next))
                        return ret;
                }
            
        }
    }
    
    
    public void enqueue(T item){
    	Node<T> node = new Node<T>(item,null);
        
    	
        Node<T> oldTail;
        Node<T> next;
        
        while (true)
        {
        	oldTail=tailRef.get();
            
            if (oldTail == null){	
            System.out.println("break 1");
        	break;
        	}
            
            
            
            next=oldTail.getNext();
            
            if (oldTail == tailRef.get())
            {
                if (tailRef.compareAndSet(oldTail, node)){
                	System.out.println("break 2");
                	break;
                	}
            }
            else{
            	tailRef.compareAndSet(oldTail, next);
            	
            }
        }
        
        System.out.println("break 3");
        
    //    if(headRef.get().getNext()==null && tailRef!=null)
    //    	headRef.get().setNext(tailRef.get());
        
       
       
    }
    
    
    
	
}
