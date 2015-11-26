package lockFreeQueue;


public class Node<T> {


	public volatile T value;
    public volatile Node<T> next;

    public Node(T x) {
     	this.value=x; 
     	}

     public Node(T x, Node<T> n) { 
     	this.value=x;
     	this.next=n;
     }
}
