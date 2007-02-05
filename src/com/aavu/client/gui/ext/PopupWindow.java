package com.aavu.client.gui.ext;

import org.gwm.client.GInternalFrame;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
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
		frame.setMinimizable(false);
		frame.setMaximizable(false);
		frame.setDraggable(true);
		
		int w = (Window.getClientWidth() - width) / 2;
		int h = (Window.getClientHeight() - height) / 2;
		w = (w < 100) ? 100 : w;
		h = (h < 100) ? 100 : h;
		frame.setLocation(w, h);
		
		frame.show(false);
		
		//This end up with windows where the title bar is hidden/undraggable
		//frame.showCenter(false);		
		
		frame.setTitle(title);
		
		frame.setDestroyOnClose();
    }
	
    public void setTitle(String title) {
		frame.setTitle(title);
	}

	protected void setContent(Widget w) {
		frame.setContent(w);
	}

	protected void setCenteredContent(Widget panel) {
		SimplePanel outerPanel = new SimplePanel();
		outerPanel.setStyleName("H-CenterDiv");
		outerPanel.add(panel);
		setContent(outerPanel);
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
