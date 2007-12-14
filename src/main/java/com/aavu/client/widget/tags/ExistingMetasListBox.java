package com.aavu.client.widget.tags;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.domain.Meta;
import com.aavu.client.gui.ext.ObjListBox;
import com.google.gwt.core.client.GWT;

/**
 * ListBox of Date, Text, Topic meta types.
 * 
 * @author Jeff Dwyer
 * 
 */
public class ExistingMetasListBox extends ObjListBox<Meta> {

	private List<Meta> allMetaTypes;
	private Meta type;

	public ExistingMetasListBox(Meta type) {
		this.type = type;
	}

	public void addItems(List<Meta> pallMetaTypes) {
		this.allMetaTypes = pallMetaTypes;

		clear();

		for (Meta element : allMetaTypes) {

			// GWT safe reflection
			if (GWT.getTypeName(element).equals(GWT.getTypeName(type))) {
				addItem(element.getTitle(), element);
			}
		}
	}


	public Meta getSelectedMeta() {
		return (Meta) getSelectedObject();
	}

	public void select(Meta meta) {

		// TODO Auto-generated method stub

	}


	// public void setMetaType(String type) {
	// for(int i=0; i < getItemCount(); i++){
	// if(type.equals(getItemText(i))){
	// setSelectedIndex(i);
	// return;
	// }
	// }
	// }




}
