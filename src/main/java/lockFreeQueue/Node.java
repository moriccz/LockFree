package lockFreeQueue;


public class Node<T> {


	public volatile T value;
    public volatile Node<T> next;

    public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public Node<T> getNext() {
		return next;
	}

	public void setNext(Node<T> next) {
		this.next = next;
	}

	public Node(){
    	
    }
    
    public Node(T x) {
     	this.value=x; 
     	}

     public Node(T x, Node<T> n) { 
     	this.value=x;
     	this.next=n;
     }
}
