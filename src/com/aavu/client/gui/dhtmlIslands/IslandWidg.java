package com.aavu.client.gui.dhtmlIslands;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

/**
 * Connect the label & the island 
 * 
 * Could also put them both in the island absolute panel, but that screw up our sizing.
 * 
 * @author Jeff Dwyer
 *
 */
public class IslandWidg extends Composite implements SourcesMouseEvents, MouseListener{
	
	
	private MouseListenerCollection mouseListeners;
	private AbsolutePanel p;
	
	public IslandWidg(Island isle,IslandBanner banner){
		
		p = new AbsolutePanel();
		DOM.setStyleAttribute(p.getElement(), "position", "absolute");
		p.add(isle,0,0);
		p.add(banner,0,0);
		p.setPixelSize(50, 50);

		isle.addMouseListener(this);
		//banner.addMouseListener(this);
		
		initWidget(p);
	}
	public void addMouseListener(MouseListener listener) {
		if (mouseListeners == null)
			mouseListeners = new MouseListenerCollection();
		mouseListeners.add(listener);
	}

	public void removeMouseListener(MouseListener listener) {
		if (mouseListeners != null)
			mouseListeners.remove(listener);
	}
			
	public void onMouseDown(Widget sender, int x, int y) {
		mouseListeners.fireMouseDown(p, x, y);
	}		
	public void onMouseEnter(Widget sender) {
		System.out.println("passing on an enter");
		mouseListeners.fireMouseEnter(p);
	}
	public void onMouseLeave(Widget sender) {
		mouseListeners.fireMouseLeave(p);
	}
	public void onMouseMove(Widget sender, int x, int y) {
		mouseListeners.fireMouseMove(p, x, y);
	}
	public void onMouseUp(Widget sender, int x, int y) {
		System.out.println("passing on an up");
		mouseListeners.fireMouseUp(p, x, y);			
	}
}