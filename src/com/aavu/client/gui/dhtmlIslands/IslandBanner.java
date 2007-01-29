package com.aavu.client.gui.dhtmlIslands;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;


public class IslandBanner extends AbsolutePanel{
	
	private static final double HALF_MIN_EM = .4;
	private static final double SCALE_DIVISOR = 6;
	private Label reg;
	private Label shdw;
	private int size;

	/**
	 * 
	 * NOTE 
	 * css value must be "fontSize" not "font-size" !!
	 * see http://groups.google.com/group/Google-Web-Toolkit/msg/5d2850a39637e56f
	 * 
	 * @param text
	 * @param size
	 */
	public IslandBanner(String text,int size){
		
		super();
		this.size = size;
		
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

	public double getFontFor(int size) {
		return getFontFor(size,1);
	}
	public double getFontFor(int size,double zoom) {	
		if(size <= 0 ){
			size = 1;
		}
		double s = (Math.log(size) / SCALE_DIVISOR + HALF_MIN_EM + (zoom *HALF_MIN_EM));
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

	public void addClickListener(ClickListener listener) {
		reg.addClickListener(listener);
	}
	public void setText(String text){
		reg.setText(text);
		shdw.setText(text);
	}

	public void setToZoom(double currentScale) {
		double font_size = getFontFor(size,currentScale);
		DOM.setStyleAttribute(reg.getElement(), "fontSize", font_size+"em");
		DOM.setStyleAttribute(shdw.getElement(), "fontSize", font_size+"em");
	}

	public void setSelected(boolean b) {
		if(b){
			shdw.addStyleName("Selected");
		}else{
			shdw.removeStyleName("Selected");
		}
	}
}

