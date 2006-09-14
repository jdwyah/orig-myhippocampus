package com.aavu.client.gui.ext;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class MultiDivPanel extends ComplexPanel {

	Element main;
	
	public MultiDivPanel(){
		main = DOM.createDiv();
		setElement(main);
		
	}
	public void add(Widget w) {
		add(w, main);	   
	}
}
