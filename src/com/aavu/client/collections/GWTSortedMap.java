package com.aavu.client.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GWTSortedMap implements Map {

	private ListImplementingASet keys = new ListImplementingASet();
	private Map map = new HashMap();

	private boolean dirty = false;

	private Comparator compare = null;
	
	/**
	 * use default comparator
	 *
	 */
	public GWTSortedMap(){
		
	}
	/**
	 * specify key comparator 
	 * 
	 * @param comp
	 */
	public GWTSortedMap(Comparator comp){
		this.compare = comp;
	}
	
	public Set keySet() {
		if(dirty){
			System.out.println("SORT ");
			if(compare == null){
				Collections.sort(keys.getList());
			}else{
				Collections.sort(keys.getList(),compare);
			}
		}
		return keys;
	}

	/**
	 * Carefull. This class manages this itself, but if you want to prevent
	 * a sort you can... 
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	/**
	 * Unimplemented. Call keySet() instead
	 */
	public Set entrySet() {
		throw new UnsupportedOperationException();
	}
	/**
	 * Unimplemented. Call keySet() instead
	 */
	public Collection values() {
		throw new UnsupportedOperationException();
	}

	public Object put(Object key, Object value) {
		dirty  = true;
		keys.add(key);
		return map.put(key, value);		
	}

	public void putAll(Map t) {
		dirty = true;
		keys.addAll(t.keySet());
		map.putAll(t);
	}

	public Object remove(Object key) {
		keys.remove(key);
		return map.remove(key);
	}

	public void clear() {
		keys.clear();
		map.clear();
	}

	public boolean containsKey(Object key) {
		return keys.contains(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Object get(Object key) {
		return map.get(key);
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public int size() {
		return map.size();
	}

}
