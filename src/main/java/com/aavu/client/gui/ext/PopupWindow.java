package com.aavu.client.gui.ext;

import org.gwm.client.GInternalFrame;
import org.gwm.client.event.GFrameListener;

import com.aavu.client.gui.timeline.CloseListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PopupWindow implements CloseListener {

    private static final int WIDTH = 600;
	private static final int HEIGHT = 400;
	
	
	protected GInternalFrame frame;
    
    public PopupWindow(GInternalFrame frame, String title) {
    	this(frame,title,false,WIDTH,HEIGHT);
	}
    public PopupWindow(GInternalFrame frame, String title, int width, int height) {
    	this(frame,title,false,width,height);	
	}
    public PopupWindow(GInternalFrame frame, String title,boolean modal, int width, int height) {
    
    	this.frame = frame;
		
		//This must be called before anything else.
    	//NOTE: don't forget to add the css file to the html.
		frame.setTheme("alphacube");
    	
		frame.setWidth(width);
		frame.setHeight(height);
		frame.setMinimizable(false);
		frame.setMaximizable(true);
		frame.setDraggable(true);
		
		int w = (Window.getClientWidth() - width) / 2;
		int h = (Window.getClientHeight() - height) / 2;
		w = (w < 100) ? 100 : w;
		h = (h < 100) ? 100 : h;
		frame.setLocation(w, h);
		
		
		frame.setVisible(true);
		
		//This end up with windows where the title bar is hidden/undraggable
		//frame.showCenter(false);		
		
		frame.setCaption(title);
		
		
		//frame.setDestroyOnClose();
    }
	
    public void setTitle(String title) {
		frame.setCaption(title);
	}

	protected void setContent(Widget w) {
		HorizontalPanel hp = new HorizontalPanel();
		hp.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		hp.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);
		hp.addStyleName("H-FullSize");
		hp.add(w);
		setCenteredContent(hp);
//		SimplePanel outerPanel = new SimplePanel();
//		outerPanel.setStyleName("H-FullDiv");
//		outerPanel.add(w);		
//		setCenteredContent(outerPanel);		
	}

	protected void setCenteredContent(Widget w) {
		frame.setContent(w);		
	}
	
	public void close(){
//		try {
//			throw new Exception();
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
		System.out.println("PopupWindow close()");
		try{
			frame.close();			
		}catch(Exception e){
			System.out.println("CAUGHT frame.destroy() exception in PopupWindow.close()");
		}
	}
	public void hide(){
		System.out.println("PopupWindow hide()");
		frame.minimize();
	}
	public void addInternalFrameListener(GFrameListener listener){
		frame.addFrameListener(listener);
	}

} 
