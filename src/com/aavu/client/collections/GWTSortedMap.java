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

	public Set keySet() {
		if(dirty){
			Collections.sort(keys.getList(),new Comparator(){

				public int compare(Object o1, Object o2) {
					String o = (String)o1;
					String oo = (String) o2;
					return o.toLowerCase().compareTo(o.toLowerCase());
				}});			
		}
		return keys;
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
