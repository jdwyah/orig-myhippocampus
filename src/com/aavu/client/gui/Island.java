package com.aavu.client.gui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Widget;

public class Island extends FocusWidget implements ClickListener{

	private String name;

	public Island(int size, int x_loc, int y_loc){
		super(DOM.createDiv());	
	    sinkEvents(Event.ONCLICK);
		
		name = "Island "+size+" at ("+x_loc+", "+y_loc+")";
		
		setStyleName("GuiTest-Island");
		
		DOM.setStyleAttribute(getElement(), "width", size+"px");
		DOM.setStyleAttribute(getElement(), "height", size+"px");
		
		Button b;
		
		addClickListener(this);
		
		//position: absolute; left: 610px; top: 155px;
		//DOM.setStyleAttribute(getElement(), "position", "absolute"); 		
		DOM.setStyleAttribute(getElement(), "top", y_loc+"px");
		DOM.setStyleAttribute(getElement(), "left", x_loc+"px");
		
		
//		DOM.setInnerText(getElement(), "");
		
		
//		SVGPanel sp = new SVGPanel(500, 300);
//
//		sp.add(sp.createCircle(x, y, size)
//				.setFill(Color.BLACK)
//				.setStrokeWidth(15)
//				.setStroke(Color.WHITE));
		
//		for(int x = 0;x < size; x++){
//			for(int y = 0; y < size; y++){
//			System.out.println("widget "+x+" "+y);
//				setWidget(x,y,new Image("img/earth.png"));
//				getCellFormatter().setHeight(x, y, "20px");
//				getCellFormatter().setWidth(x, y, "20px");
//			}
//		}
//		System.out.println("yo2");
//			
//		
	}

	public void onClick(Widget sender) {
		System.out.println("Clicked! "+name);
	}
	
}
