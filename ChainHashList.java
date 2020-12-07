package cs313;

public class ChainHashList<E> implements List<E> {

	//data is stored in a map
	private ChainHashMap<Integer, E> map;
	
	public ChainHashList(int capacity) {
		this.map = new ChainHashMap<>(capacity);
	}
	
	public ChainHashList() {
		this(10);
	}
	
	@Override
	public void set(int i, E e) {
		//need to restrict indices since the map will allow any integers as keys
		if (i < 0 || i >= this.size()) throw new IndexOutOfBoundsException();
		this.map.put(i, e);
	}

	@Override
	public void add(int i, E e) {
		if (i < 0 || i > this.size()) throw new IndexOutOfBoundsException();
		
		/*
		 * Before adding, need to shift over all elements with keys from from size to i
		 * so no entry has key i
		 */
		for (int j = this.size(); j > i; j--) {
			E val = this.map.remove( j-1 );
			this.map.put(j, val);
		}
		
		/*
		 * now that any entry that previously had key i has been moved, 
		 * map.put will create a new entry (increasing the map size)
		 */
		this.map.put(i, e);
	}

	@Override
	public E get(int i) {
		if (i < 0 || i >= this.size()) throw new IndexOutOfBoundsException();
		return this.map.get(i);
	}

	@Override
	public E remove(int i) {
		if (i < 0 || i >= this.size()) throw new IndexOutOfBoundsException();
		
		E oldVal = this.map.remove(i);
		
		/*
		 * after removing, need to shift over all elements from size to i
		 * so there is no gap between integer keys
		 */
		for (int j = i; j < this.size(); j++) {
			E val = this.map.remove( j+1 );
			this.map.put(j, val);
		}
		
		return oldVal;
	}

	@Override
	public int size() {
		return this.map.size();
	}

	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}

	public static void main(String[] args) {
		List<Integer> l = new ChainHashList<>();
		
		for (int i = 0; i < 100; i++) {
			l.add(i, i * 2);
		}
		
		for (int i = 0; i < 100; i++) {
			System.out.println(l.get(i));
		}
		
		int idx = 0;
		while(idx < l.size()) {
			l.remove(idx);
			idx++;
		}
		
		System.out.println();
		
		System.out.println(l.size() + " entries");
		
		for (int i = 0; i < l.size(); i++) {
			System.out.println(l.get(i));
			l.set(i, 0);
		}
		
		System.out.println();
		
		System.out.println(l.size() + " entries");
		
		for (int i = 0; i < l.size(); i++) {
			System.out.println(l.get(i));
		}
		
		
	}
	
}
