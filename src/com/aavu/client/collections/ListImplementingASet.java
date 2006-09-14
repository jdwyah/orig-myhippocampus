package com.aavu.client.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ListImplementingASet implements Set {

	private List implementingList = new ArrayList();
	
	public List getList(){
		return implementingList;
	}
	
	public boolean add(Object o) {
		if(!implementingList.contains(o)){
			return implementingList.add(o);
		}
		return false;		
	}

	public boolean addAll(Collection c) {
		boolean rtn = false;
		for (Iterator iter = c.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if(add(element)){
				rtn = true;			
			}
		}
		return implementingList.addAll(c);
	}

	public void clear() {
		implementingList.clear();
	}

	public boolean contains(Object o) {
		return implementingList.contains(o);
	}

	public boolean containsAll(Collection c) {
		return implementingList.containsAll(c);
	}

	public boolean isEmpty() {
		return implementingList.isEmpty();
	}

	public Iterator iterator() {
		return implementingList.iterator();
	}

	public boolean remove(Object o) {
		return implementingList.remove(o);
	}

	public boolean removeAll(Collection c) {
		return implementingList.removeAll(c);
	}

	public boolean retainAll(Collection c) {
		return implementingList.retainAll(c);
	}

	public int size() {
		return implementingList.size();
	}

	public Object[] toArray() {
		return implementingList.toArray();
	}

	/**
	 * Not applicable? List interface doesn't seem to have this in GWT land.
	 */
	public Object[] toArray(Object[] a) {
		throw new UnsupportedOperationException();
		//return implementingList.toArray(a);
	}

}
