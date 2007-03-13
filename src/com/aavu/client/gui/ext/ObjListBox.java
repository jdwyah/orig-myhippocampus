package com.aavu.client.gui.ext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.ui.ListBox;

/**
 * Extension of ListBox to make it do what I always seem to want. 
 * ie let me store objects. 
 * 
 * @author Jeff Dwyer
 *
 */
public class ObjListBox extends ListBox {

	private Map objs = new HashMap();
	
	public ObjListBox(){
		
	}
	
	public void addItem(String title, Object element) {
		super.addItem(title);
		objs.put(title, element);
	}
	

	public Object getSelectedObject() {		
		return objs.get(getItemText(getSelectedIndex()));
	}
	
	public void setSelectedObject(Object o){
		for (Iterator iter = objs.entrySet().iterator(); iter.hasNext();) {
			Entry e = (Entry) iter.next();
			if(e.getValue().equals(o)){
				setSelectedText(e.getKey());
			}
		}
	}

	private void setSelectedText(Object key) {
		for(int i =0; i < getItemCount(); i++){
			if(getItemText(i).equals(key)){
				setSelectedIndex(i);
				break;
			}
		}
	}
}
