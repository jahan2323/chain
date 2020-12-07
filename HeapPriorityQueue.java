package cs313;

import java.util.Comparator;

public class HeapPriorityQueue<K,V> extends AbstractPriorityQueue<K,V> {

	public LinkedBinaryTree<Entry<K,V>> heap = new LinkedBinaryTree<>();
	
	public HeapPriorityQueue() {
		super(); //superconstructor uses DefaultComparator
	}
	
	public HeapPriorityQueue(Comparator<K> comp) {
		super(comp);
	}
	
	//helper method
	private void swap(Position<Entry<K,V>> a, Position<Entry<K,V>> b) {
		Entry<K,V> temp = a.getElement();
		this.heap.set(a, b.getElement());
		this.heap.set(b, temp);
	}
	
	private void upheap(Position<Entry<K,V>> p) {
		//may need to continue bubbling until root is reached
		while(p != this.heap.root()) {
			
			//compare with parent
			Position<Entry<K,V>> parent = this.heap.parent(p);
			
			//if current key >= parent key, stop bubbling
			if (this.compare(p.getElement(), parent.getElement()) >= 0) break;
			
			//otherwise swap and keep bubbling
			this.swap(p, parent);
			p = parent;
		}
	}

	private void downheap(Position<Entry<K,V>> p) {
		//may need to keep bubbling until a leaf node is reached
		while(this.heap.left(p) != null) {
			
			//find smallest child key
			Position<Entry<K,V>> left = this.heap.left(p);
			Position<Entry<K,V>> smallest = left;
			
			if (this.heap.right(p) != null) {
				Position<Entry<K,V>> right = this.heap.right(p);
				if (this.compare(right.getElement(), left.getElement()) < 0) 
					smallest = right;
			}
			
			//compare with smallest child			
			//if smallest key >= current key, stop bubbling
			if (this.compare(smallest.getElement(), p.getElement()) >= 0) 
				break;
			
			//otherwise swap and keep bubbling
			this.swap(p, smallest);
			p = smallest;
		}
	}
	
	private Position<Entry<K,V>> lastPosition() {
		//naive implementation (there is a more efficient way to find last position)
		Position<Entry<K,V>> cursor = null;
		for (Position<Entry<K,V>> p : this.heap.breadthfirst() ) {
			cursor = p;
		}
		return cursor;
	}
	
	//creates an empty position at the next available place to maintain complete tree property
	private Position<Entry<K,V>> addNext(Entry<K,V> e) {
		if (this.isEmpty()) return this.heap.addRoot(e);
		
		//naive implementation (there is a more efficient way to find next position)
		//find parent for the next position
		Position<Entry<K,V>> parent = null;
		int count = 0, newSize = this.size() + 1;
		for (Position<Entry<K,V>> p : this.heap.breadthfirst() ) {
			/*
			 * iterate through positions using breadthfirst traversal
			 * until we find the parent of the node we are about to create
			 * From the array-based implementation, we know that the parent of a node
			 * can be found at index (i-1) / 2.  Since we aren't dealing with an array, 
			 * we start counting at 1 instead of 0 so we compute i / 2 to find the parent
			 */
			if ( ++count == newSize / 2 ) {
				parent = p;
				break;
			}
		}
		
		//if newSize is even, next position will be a left child
		//(all left even numbered nodes in breadthfirst traversal are left children)
		Position<Entry<K,V>> next;
		if (newSize % 2 == 0)
			next = this.heap.addLeft(parent, e);
		else
			next = this.heap.addRight(parent, e);
		return next;
	}
	
	//O(log n)
	@Override
	public Entry<K, V> insert(K key, V value) {
		this.checkKey(key);
		
		//step one: create a new node with the given data
		Entry<K,V> newest = new PQEntry<>(key, value);
		
		//addNext inserts the node at the next available position to maintain tree completeness
		Position<Entry<K,V>> p = this.addNext(newest);
		
		//step two: perform upheap bubbling to maintain heap order property
		this.upheap(p);
		
		return newest;
	}
	
	//O(log n)
	@Override
	public Entry<K, V> removeMin() {
		if (this.isEmpty()) return null;
		
		//step one: return the element with minimum key
		Entry<K,V> min = this.min();
		
		//step two: swap root and last node
		Position<Entry<K,V>> last = this.lastPosition();
		this.swap(this.heap.root(), last);
		
		//remove last node so that tree completeness is maintained
		this.heap.remove(last);
		
		//step three: perform downheap bubbling to maintain heap order property
		if (!this.isEmpty())
			this.downheap(this.heap.root());
		
		return min;
	}

	@Override
	public Entry<K, V> min() {
		if (this.isEmpty()) return null;
		return this.heap.root().getElement();
	}

	@Override
	public int size() {
		return this.heap.size();
	}

	public static void main(String[] args) {
		PriorityQueue<Integer, Integer> pq = new HeapPriorityQueue<>();
		
		for (int i = 0; i < 10; i++) {
			pq.insert(i, i);
			pq.insert(i, i);
		}
		
		while(!pq.isEmpty()) {
			System.out.println(pq.removeMin().getKey());
		}
		
		System.out.println();
		
		for (int i = 0; i > -10; i--) {
			pq.insert(i, i);
			pq.insert(i, i);
		}
		
		while(!pq.isEmpty()) {
			System.out.println(pq.removeMin().getKey());
		}
	}

}
