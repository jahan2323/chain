package cs313;

import java.util.ArrayList;
import java.util.Comparator;

public class SortedTableMap<K,V> extends AbstractMap<K,V> implements SortedMap<K,V> {

	private ArrayList<Entry<K,V>> table = new ArrayList<>();
	private Comparator<K> comp;
	
	public SortedTableMap(Comparator<K> comp) {
		this.comp = comp;
	}
	
	public SortedTableMap() {
		this.comp = new DefaultComparator<>();
	}
	
	private int search(K key) {
		/*
		 * binary search can be used since sorted order is maintained
		 * this binary search is slightly modified so that if an exact match
		 * isn't found it will return the index where it should belong
		 */
		int i = 0, j = this.size();
		while (i < j) {
			int m = (i + j) / 2;
			int comparison = this.comp.compare(key, this.table.get(m).getKey());
			if (comparison > 0) i = m + 1;
			else j = m;
		}
		return i;
	}
	
	@Override
	public V put(K key, V val) {
		Entry<K,V> newEntry = new MapEntry<>(key, val);
		int idx = this.search(key);
		//determine whether to create new entry or update existing
		if (idx < this.size()) {
			Entry<K,V> existingEntry = this.table.get(idx);
			if (existingEntry.getKey().equals(key))
				return this.table.set(idx, newEntry).getValue();
		}
		this.table.add(idx, newEntry);
		return null;
	}

	@Override
	public V get(K key) {
		int idx = this.search(key);
		if (idx == this.size()) return null;
		Entry<K,V> existingEntry = this.table.get(idx);
		//determine if exact match was found
		if (existingEntry.getKey().equals(key)) 
			return existingEntry.getValue();
		else
			return null;
	}

	@Override
	public V remove(K key) {
		int idx = this.search(key);
		if (idx == this.size()) return null;
		Entry<K,V> existingEntry = this.table.get(idx);
		//determine if exact match was found
		if (existingEntry.getKey().equals(key)) 
			return this.table.remove(idx).getValue();
		else 
			return null;
	}

	@Override
	public Iterable<Entry<K, V>> entrySet() {
		//ArrayList is already Iterable so it can be returned directly
		return this.table;
	}

	@Override
	public int size() {
		return this.table.size();
	}

	@Override
	public Entry<K, V> firstEntry() {
		//index out of bounds will occur if table is empty
		if (this.isEmpty()) return null;
		return this.table.get(0);
	}

	@Override
	public Entry<K, V> lastEntry() {
		//index out of bounds will occur if table is empty
		if (this.isEmpty()) return null;
		return this.table.get(this.size() - 1);
	}

	@Override
	public Entry<K, V> floorEntry(K key) {
		int idx = this.search(key);
		if (this.isEmpty()) return null;
		//check if exact match was found
		if (idx < this.size()) {
			Entry<K,V> existingEntry = this.table.get(idx);
			if (existingEntry.getKey().equals(key))
				return existingEntry;
		}
		//if search ended at 0 and there isn't an exact match, then there is nothing lower than the key
		if (idx == 0) return null;
		else return this.table.get(idx - 1);
	}

	@Override
	public Entry<K, V> ceilingEntry(K key) {
		int idx = this.search(key);
		//if search returned table size, there is no larger element
		if (this.isEmpty() || idx == this.size()) return null;
		//will either return an exact match or the next higher entry
		return this.table.get(idx);
	}

	@Override
	public Entry<K, V> lowerEntry(K key) {
		int idx = this.search(key);
		if (idx == 0) return null;
		//idx is either an exact match or the next higher position, so return the previous entry
		else return this.table.get(idx - 1);
	}

	@Override
	public Entry<K, V> higherEntry(K key) {
		int idx = this.search(key);
		if (this.isEmpty() || idx == this.size()) return null;
		Entry<K,V> existingEntry = this.table.get(idx);
		//check if exact match was found
		if (existingEntry.getKey().equals(key)) {
			//if nothing is higher, return null
			if (idx == this.size() - 1) return null;
			//otherwise return then next higher entry
			else return this.table.get(idx + 1);
		} else {
			//if no existing entry was found, idx is the next higher
			return this.table.get(idx);
		}
	}

	@Override
	public Iterable<Entry<K, V>> subMap(K fromKey, K toKey) {
		if (this.isEmpty()) return new ArrayList<>();
		int startIdx = this.search(fromKey);
		int endIdx = this.search(toKey);
		ArrayList<Entry<K,V>> iterable = new ArrayList<>(endIdx - startIdx);
		//start is inclusive, end is exclusive
		for (int i = startIdx; i < endIdx; i++)
			iterable.add(iterable.size(), this.table.get(i));
		return iterable;
	}

	public static void main(String[] args) {
		SortedMap<Double, Integer> map = new SortedTableMap<>();
		
		for (int i = 0; i < 10; i+=2)
			map.put((double) i,i);
		
		map.put(1.5, 0);
		
		for (int i = 1; i < 10; i+=2)
			map.put((double) i,i);
		
		
		for (Entry<Double,Integer> e : map.entrySet()) {
			System.out.println(e.getKey());
		}
		
		System.out.println();
		
		for (int i = 10; i >= 0; i-=2)
			map.remove((double) i);
		
		
		for (Entry<Double,Integer> e : map.entrySet()) {
			System.out.println(e.getKey());
		}
		
		System.out.println("After removal");
		
		for (Entry<Double,Integer> e : map.subMap(1.5, 7.0)) {
			System.out.println(e.getKey());
		}
		
		System.out.println(map.get(9.0));
		System.out.println(map.size());
		System.out.println();
		System.out.println(map.higherEntry(8.7).getKey());
	}
	
}
