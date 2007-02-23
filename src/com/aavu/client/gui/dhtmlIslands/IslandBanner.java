package com.aavu.client.gui.dhtmlIslands;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;


public class IslandBanner extends AbsolutePanel {
	
	private static final double HALF_MIN_EM = .4;
	private static final double SCALE_DIVISOR = 6;
	
	private static final double MAX_FONT = 4;
	
	
	public static final String BANNER_SELECTED = "Selected";
	
	private Label reg;
	private Label shdw;
	private int size;
	
	private static final String HOVER_STYLE = "H-IslandBanner-Hover";
	
	
	//private MouseListenerCollection mouseListeners;	
	


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
		
		sinkEvents( Event.MOUSEEVENTS );	    
		
		reg.addMouseListener(new MouseListenerAdapter(){
			public void onMouseEnter(Widget sender) {
				addStyleName(HOVER_STYLE);
			}
			public void onMouseLeave(Widget sender) {
				removeStyleName(HOVER_STYLE);
			}	
		});
				
		System.out.println("reg "+reg.getOffsetWidth()+" "+reg.getOffsetHeight());
		DOM.setStyleAttribute(getElement(), "position", "absolute");
		
	}
	

	//@Override
	protected void onLoad() {		
		super.onLoad();
		setDimensions();
	}
	
	private Widget setDimensions(){
		int width = reg.getOffsetWidth();
		int height = reg.getOffsetHeight();
		if(shdw.getText().equals("Person")){
			System.out.println("on load reg "+width+" ");
		}
		DOM.setStyleAttribute(getElement(), "width", width+"px");		
		DOM.setStyleAttribute(getElement(), "height", height+"px");		
		
		
		return reg;
	}


	public double getFontFor(int size) {
		return getFontFor(size,1);
	}
	public double getFontFor(int size,double zoom) {	
		
		if(size <= 0 ){
			size = 1;
		}
		double s = (Math.log(size) / SCALE_DIVISOR + HALF_MIN_EM + (zoom *HALF_MIN_EM));
		
		s = s > MAX_FONT ? MAX_FONT : s;
		
		return s;
	}

	/*
	 * TODO not working
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.UIObject#setWidth(java.lang.String)
	 */
//	public void setWidth(String str) {
//		DOM.setStyleAttribute(getElement(), "width", str);
//		reg.setWidth(str);
//		shdw.setWidth(str);
//	}

	public void addClickListener(ClickListener listener) {
		reg.addClickListener(listener);
	}
	public void setText(String text){
		reg.setText(text);
		shdw.setText(text);
	}

	public Widget setToZoom(double currentScale) {
		double font_size = getFontFor(size,currentScale);
		DOM.setStyleAttribute(reg.getElement(), "fontSize", font_size+"em");
		DOM.setStyleAttribute(shdw.getElement(), "fontSize", font_size+"em");
		return setDimensions();
	}

	public void setSelected(boolean b) {
		if(b){
			shdw.addStyleName(BANNER_SELECTED);
		}else{
			shdw.removeStyleName(BANNER_SELECTED);
		}
	}
	
	
//	public void addMouseListener(MouseListener listener) {
//		if (mouseListeners == null)
//			mouseListeners = new MouseListenerCollection();
//		mouseListeners.add(listener);
//	}
//
//	public void removeMouseListener(MouseListener listener) {
//		if (mouseListeners != null)
//			mouseListeners.remove(listener);
//	}
//	public void onBrowserEvent(Event event) {
//
//	    switch (DOM.eventGetType(event)) {
//	    case Event.ONCLICK: 
//	    case Event.ONMOUSEUP:
//	    case Event.ONMOUSEDOWN:
//	    case Event.ONMOUSEMOVE:
//	    case Event.ONMOUSEOVER:
//	    case Event.ONMOUSEOUT: {
//	    	if (mouseListeners != null)
//	    		mouseListeners.fireMouseEvent(this, event);
//	    	break;
//	    }
//	    }
//	}
}

