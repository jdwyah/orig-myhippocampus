package com.aavu.client.gui.dhtmlIslands;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;


public class IslandBanner extends AbsolutePanel{
	
	private static final double MIN_EM = .8;
	private static final double SCALE_DIVISOR = 6;
	private Label reg;
	private Label shdw;

	/**
	 * 
	 * NOTE 
	 * css value must be "fontSize" not "font-size" !!
	 * see http://groups.google.com/group/Google-Web-Toolkit/msg/5d2850a39637e56f
	 * @param text
	 * @param size
	 */
	public IslandBanner(String text,int size){
		
		super();
		
		double font_size = getFontFor(size);
		System.out.println("FONT "+getFontFor(size)+" "+size);
		shdw = new Label(text,true);
		DOM.setStyleAttribute(shdw.getElement(), "fontSize", font_size+"em");
		shdw.addStyleName("Shadow");
		add(shdw,1,1);
		
		reg = new Label(text,true);
		DOM.setStyleAttribute(reg.getElement(), "fontSize", font_size+"em");
		reg.addStyleName("Text");
		add(reg,0,0);
				
				
		setStyleName("H-IslandBanner");
		
		DOM.setStyleAttribute(getElement(), "position", "absolute");
		DOM.setStyleAttribute(getElement(), "width", "200px");	
		DOM.setStyleAttribute(getElement(), "height", "60px");
				
		
	}

	private double getFontFor(int size) {	
		if(size <= 0 ){
			size = 1;
		}
		double s = Math.log(size) / SCALE_DIVISOR + MIN_EM;
		return s;
	}

	/*
	 * TODO not working
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.UIObject#setWidth(java.lang.String)
	 */
	public void setWidth(String str) {
		DOM.setStyleAttribute(getElement(), "width", str);
		reg.setWidth(str);
		shdw.setWidth(str);
	}
	
}

