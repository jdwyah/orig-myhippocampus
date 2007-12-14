package com.aavu.client.gui.ext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.ui.ListBox;

/**
 * Extension of ListBox to make it do what I always seem to want. ie let me store objects.
 * 
 * @author Jeff Dwyer
 * 
 */
public class ObjListBox<T> extends ListBox {

	private Map<String, T> objs = new HashMap<String, T>();

	public ObjListBox() {

	}

	public void addItem(String title, T element) {
		super.addItem(title);
		objs.put(title, element);
	}


	public T getSelectedObject() {
		return objs.get(getItemText(getSelectedIndex()));
	}

	public void setSelectedObject(Object o) {
		for (Iterator<Entry<String, T>> iter = objs.entrySet().iterator(); iter.hasNext();) {
			Entry<String, T> e = iter.next();
			if (e.getValue().equals(o)) {
				setSelectedText(e.getKey());
			}
		}
	}

	private void setSelectedText(Object key) {
		for (int i = 0; i < getItemCount(); i++) {
			if (getItemText(i).equals(key)) {
				setSelectedIndex(i);
				break;
			}
		}
	}
}
