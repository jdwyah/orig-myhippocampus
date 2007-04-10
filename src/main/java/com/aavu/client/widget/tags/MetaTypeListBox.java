package com.aavu.client.widget.tags;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.domain.Meta;
import com.google.gwt.user.client.ui.ListBox;

/**
 * ListBox of Date, Text, Topic meta types. 
 * 
 * @author Jeff Dwyer
 *
 */
public class MetaTypeListBox extends ListBox {

	private List allMetaTypes;

	public void addItems(List pallMetaTypes) {
		this.allMetaTypes = pallMetaTypes;

		for (Iterator iter = allMetaTypes.iterator(); iter.hasNext();) {
			Meta element = (Meta) iter.next();
			addItem(element.getType());
		}
	}

	public Meta getSelectedMeta() {
		
		String text = getItemText(getSelectedIndex());
		
		for (Iterator iter = allMetaTypes.iterator(); iter.hasNext();) {
			Meta element = (Meta) iter.next();
			
			if(element.getType().equals(text)){
				return element;
			}
		}
		return null;
	}

	public void setMetaType(String type) {
		for(int i=0; i < getItemCount(); i++){
			if(type.equals(getItemText(i))){
				setSelectedIndex(i);
				return;
			}		
		}		
	}


	

}
