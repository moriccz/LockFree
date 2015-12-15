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

	
	 private transient volatile Node<T> _head = new Node<T>(null, null);

	  private transient volatile Node<T> _tail;
   

	  @SuppressWarnings("rawtypes")
	private final AtomicReferenceFieldUpdater<SelfOrganizingList, Node>
	    tailUpdater =AtomicReferenceFieldUpdater.newUpdater(SelfOrganizingList.class, Node.class, "_tail");
	  @SuppressWarnings("rawtypes")
	private final AtomicReferenceFieldUpdater<Node, Node>
	    nextUpdater =AtomicReferenceFieldUpdater.newUpdater(Node.class, Node.class, "next");
	
	  
	  
   public SelfOrganizingList()
   {
       _head = null;
       _tail = _head;
   }

   public SelfOrganizingList(T value) 
   {
       _head = new Node<T>(value);
       _tail = _head; 
   }
   
   /*
    * insert the elemenent to the end of the list
    */
   public void insert(T item)
   {
	   Node<T> node = new Node<T>(item);
       Node<T> oldTail;
       Node<T> next;

       while (true)
       {
           oldTail = _tail;

           if (_tail == null && tailUpdater.compareAndSet(this, null, node))
               break;
           
           next = oldTail.getNext();

           if (_tail == oldTail)
           {
               if (next == null)
               {
                   if (nextUpdater.compareAndSet(_tail, null, node))
                       break;
               }
               else
               {
               	tailUpdater.compareAndSet(this, oldTail, next);
               }
           }
       }

       tailUpdater.compareAndSet(this, oldTail, node);
       nextUpdater.compareAndSet( _tail, null, node);
   }
   
   public boolean remove(Node<T> item)
   {

       Node<T> next;
       Node<T> oldReference;
       Node<T> newReference;

       do
       {
           oldReference = item;
           next = item.getNext();

           if (next==null)
           {
               newReference = null;
           }
           else
           {
               newReference = new Node<T>( next.getValue(),next.getNext());
           }
           
       } while (!nextUpdater.compareAndSet(item, oldReference, newReference));
	return false;   
   }
   
@SuppressWarnings("unused")
public void remove(T item)
   {
       Node<T> node = _head;

       while (node != null)
       {
           if (node == null)
               return;

           if (node.getValue().equals(item))
           {
               remove(node);
               break;
           }
            
           node = node.getNext();
       }
   }
   
   
   public Node<T> search(T item)
   {
	   Node<T> node = _head;

       while (node != null)
       {
           if (node == null || node.getNext()==null)
               return null;
           
           if (node.getNext().getValue().equals(item))
           {
               if(swap(node))
            	   	return node;
           }
            
           node = node.getNext();
       }
	   
	   return null;
   }
   
   /*
    * 
    * set prev value as matched value
    * 	  matched value prev value
    *     
    */
   public boolean swap(Node<T> prev)
   {
	   AtomicReference<Node> nodePrev=new AtomicReference<Node>(prev); 
	   AtomicReference<Node> nodeNext=new AtomicReference<Node>(prev.getNext()); 
	  
	   
	   Node<T> newNext=new Node(nodePrev.get().getValue(),nodeNext.get());
	   Node<T> newPrev=new Node(nodeNext.get().getValue(),nodePrev.get());
	   
	   while(nodePrev.get().getNext().equals(nodeNext.get())){
		   
		   if(nodeNext.compareAndSet(nodeNext.get(), newNext)&&
		      nodePrev.compareAndSet(nodePrev.get(), newPrev))
			   return true;
		   
	   }
	   return false;
   }
   
}
