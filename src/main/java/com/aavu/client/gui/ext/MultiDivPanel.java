package com.aavu.client.gui.ext;

import com.aavu.client.gui.LocationSettingWidget;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class MultiDivPanel extends ComplexPanel implements LocationSettingWidget {

	Element main;
	
	public MultiDivPanel(){
		main = DOM.createDiv();
		setElement(main);
		//addStyleName("H-MainMap");
	}
	public void add(Widget w) {
		add(w, main);	   
	}
	
	public void add(Widget widget, int left, int top) {
		add(widget);
		setWidgetPosition(widget, left, top);		
	}
	public Widget getWidget() {		
		return this;
	}
	public void setWidgetPosition(Widget widget, int left, int top) {
		DOM.setStyleAttribute(widget.getElement(), "left", left+"px");
		DOM.setStyleAttribute(widget.getElement(), "top", top+"px");		
	}
}
