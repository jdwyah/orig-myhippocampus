package com.aavu.client.gui.ext;

import org.gwm.client.GInternalFrame;

import com.aavu.client.widget.tags.TagOrganizerView;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PopupWindow {

    private static final int WIDTH = 600;
	private static final int HEIGHT = 400;
	
	
	private GInternalFrame frame;
    
    public PopupWindow(GInternalFrame frame, String title) {
    	this(frame,title,false,WIDTH,HEIGHT);
	}
    public PopupWindow(GInternalFrame frame, String title, int width, int height) {
    	this(frame,title,false,width,height);	
	}
    public PopupWindow(GInternalFrame frame, String title,boolean modal, int width, int height) {
    
    	this.frame = frame;
		
		//This must be called before anything else.
		frame.setStyle("mac_os_x");
		frame.setWidth(width);
		frame.setHeight(height);
		frame.setMinimizable(true);
		frame.setMaximizable(true);
		frame.setDraggable(true);
		
		frame.showCenter(false);
		
		
		frame.setTitle(title);
		
		frame.setDestroyOnClose();
    }
	
    public void setTitle(String title) {
		frame.setTitle(title);
	}

	protected void setContent(Widget w) {
		frame.setContent(w);
	}
	public void close(){
		System.out.println("PopupWindow close()");
		try{
			frame.destroy();
		}catch(Exception e){
			System.out.println("CAUGHT frame.destroy() exception in PopupWindow.close()");
		}
	}
	public void hide(){
		System.out.println("PopupWindow hide()");
		frame.minimize();
	}

} 
