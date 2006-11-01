package com.aavu.client.gui.ext;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Because who ever wanted a ListBox with strings in it? Store the associated
 * object so we can retrieve it.
 * 
 * @author Jeff Dwyer
 *
 */
public class ObjectListBox extends Composite {
	
	private ListBox listBox = new ListBox();
	private Vector vec = new Vector();
		
	public ObjectListBox(){		
		initWidget(listBox);
	}

	public void addItem(String string, Object object) {
		listBox.addItem(string);
		vec.add(object);		
	}
	public Object getSelectedObject(){
		return vec.get(listBox.getSelectedIndex());
	}
	public void setSelectedObject(Object toSelect){
		int i = 0;
		for (Iterator iter = vec.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if(toSelect == obj){
				listBox.setSelectedIndex(i);
			}		
			i++;
		}
	}
	/**
	 * Just compare the type of the objects in the list
	 * 
	 * 	no .getClass() in GWT makes this a little uglier to avoid nulls 
	 * @param toSelect
	 */
	public void setSelectedObjectToType(Object toSelect){
		int i = 0;
		String toSelString = GWT.getTypeName(toSelect);
		for (Iterator iter = vec.iterator(); iter.hasNext();) {
			Object obj = iter.next();
					
			String thisString = GWT.getTypeName(obj);
			if(toSelString == null){
				if(thisString == null){
					listBox.setSelectedIndex(i);
					return;
				}else{
					continue;
				}
			}
			if(toSelString.equals(thisString)){
				listBox.setSelectedIndex(i);
			}			
			i++;
		}
	}

	public void addChangeListener(ChangeListener listener) {
		listBox.addChangeListener(listener);	
	}
}
