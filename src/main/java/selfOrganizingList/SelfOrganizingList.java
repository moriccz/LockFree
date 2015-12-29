package selfOrganizingList;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import lockFreeQueue.Node;

/* head      tail
 *  N1 - N2 - N3 - null
 *  v1   v2   v3
 * 
 * Self organizing List with Transpose Method
 * Remove implemented as in regular list
 * Insert element is added to the tail
 * Search return searched item and swaps the Node with the one in the left 
 */
public class SelfOrganizingList<T> {

	
	AtomicReference<Node> tailRef=new AtomicReference<Node>();
    AtomicReference<Node> headRef=new AtomicReference<Node>();
   

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public SelfOrganizingList(int i, int j){
    	Node<T> tail=new Node(0,null);
    	tailRef.set(tail);
    	headRef.set(new Node(null,new Node(2,new Node(3,new Node(4,new Node(5,tailRef.get()))))));
    }
	
	  
	  
   public SelfOrganizingList()
   {
		
   	headRef.set(new Node<T>(null,null));
   	tailRef.set(headRef.get());
   }

   @SuppressWarnings("unchecked")
public SelfOrganizingList(T value) 
   {

   	tailRef.set(new Node<T>(value,null));
   	headRef.set(new Node<T>(null,tailRef.get())); 
   }
   
   /*
    * insert the elemenent to the end of the list
    */
   @SuppressWarnings("unchecked")
public void insert(T item)
   {
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
   }
   
   @SuppressWarnings({ "unchecked", "rawtypes" })
public boolean remove(AtomicReference<Node> remNext)
   {
	   
	 Node<T> current=remNext.get();
	 Node<T> replace=current;
	 replace.setNext(current.getNext().getNext());
	 
	 if(remNext.compareAndSet(current, replace))  return true;
	 										else return false;
         
   }
   
@SuppressWarnings({ "rawtypes", "unchecked" })
public Node<T> remove(T item)
   {
       Node<T> node = headRef.get();

       while (node != null)
       {
    	   if(node.getNext()!=null)
           if (node.getNext().getValue().equals(item))
           {
        	   AtomicReference<Node> remNext=new AtomicReference<Node>(node);
               remove(remNext);
               return node;
           }
            
           node = node.getNext();
       }
       
       return null;
   }
   
   
   @SuppressWarnings({ "unchecked", "rawtypes" })
public Node<T> search(T item)
   {
	   Node<T> node = headRef.get();
	  
	   
       while (node != null)
       {
           if (node == null || node.getNext()==null)
               break;
           
           if (node.getNext().getValue().equals(item))
           {
        	   AtomicReference<Node> newNode = new AtomicReference<Node>(node);
               if(newNode.compareAndSet(node,swap(newNode).get())){
            		//node=swap(newNode).get();
            	   	return newNode.get();
            	   	}
           }
            
           node = node.getNext();
       }
	   
	   return new Node(-1,null);
   }
   
   /*
    * 
    * set prev value as matched value
    * 	  matched value prev value
    *     
    */
   @SuppressWarnings({ "unchecked", "rawtypes" })
public AtomicReference<Node> swap(AtomicReference<Node> currRef)
   {
	   if(currRef==null)
		   return null;
	   
	   Node<T> currNode=currRef.get(); 
	   
	   Node<T> newNext=new Node(currRef.get().getValue(),currRef.get().getNext().getNext());
	   Node<T> newCurr=new Node(currRef.get().getNext().getValue(),newNext);
	   
	   while(currRef.get().equals(currNode)){
		   
		    if(currRef.compareAndSet(currNode, newCurr))
		       return currRef;
		   
	   }
	   
	   return null;
   }
   
}
