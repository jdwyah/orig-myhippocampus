package com.aavu.client.gui.timeline;

import com.aavu.client.domain.TopicIdentifier;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

public class LineOfTime extends FocusPanel implements ClickListener {

	private TopicIdentifier ident;

	public LineOfTime(TopicIdentifier ident,int left,int width){
		
		this.ident = ident;
		
		DOM.createDiv();
		setStyleName("H-LineOfTime");
		
		DOM.setStyleAttribute(getElement(), "left", left+"");
		DOM.setStyleAttribute(getElement(), "width", width+"");
					
		addClickListener(this);
	}

	public void onClick(Widget sender) {
		Window.alert("ident "+ident);
	}
	
}
