package com.aavu.client.gui.dhtmlIslands;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;


public class IslandBanner extends AbsolutePanel{
	public IslandBanner(String text){
		
		super();
		
		Label shdw = new Label(text);
		shdw.addStyleName("Shadow");
		add(shdw,1,1);
		
		Label reg = new Label(text);
		reg.addStyleName("Text");
		add(reg,0,0);
		
		
				
		setStyleName("H-IslandBanner");
		
		DOM.setStyleAttribute(getElement(), "position", "absolute");
		DOM.setStyleAttribute(getElement(), "width", "200px");	
		DOM.setStyleAttribute(getElement(), "height", "60px");
	
	}
}

