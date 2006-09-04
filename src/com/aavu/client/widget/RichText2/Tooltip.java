/*
 * Created on 02.07.2006
 */
package com.aavu.client.widget.RichText2;


import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

/**
 * This class represent a tooltip that can be setup with css. The used css stylename is Tooltip.
 * A typical use is:
 * <code><pre>
 *      yourWidget.addMouseListener(new Tooltip(tooltip));
 * </pre></code>
 * 
 * @author Volker Berlin
 */
public class Tooltip extends MouseListenerAdapter {

    final SimplePanel tooltip;
    private Timer timer;
    private int lastX;
    private int lastY;
    boolean isShowing = false;
	private ImageButton button;
    

    /**
     * The Constructor of the Tooltip.
     * @param text The tooltip text. It should not be null.
     * @param button 
     */
    public Tooltip(String text, ImageButton button) {
    	this.button = button;
    	tooltip = new SimplePanel(){};
        tooltip.add( new HTML(text) );
        tooltip.setStyleName("Tooltip");        
    }
    
 
    public void onMouseEnter(Widget sender) {
        if(timer == null){
            timer = new Timer(){
                public void run() {
                    show();
                    setPopupPosition(lastX, lastY);
                }
            };
        }
        timer.schedule(1000);
    }
    
    
    public void onMouseMove(Widget sender, int x, int y) {
    	
        int left = sender.getAbsoluteLeft() + x + getScrollLeft() - 2;
        int top = sender.getAbsoluteTop() + y + getScrollTop() +20;
        
        if(left != lastX || top != lastY){
            if(isShowing){
                setPopupPosition(left, top);
            }
            lastX = left;
            lastY = top;
        }
    }


    public void onMouseLeave(Widget sender) {
        if(timer != null){
            timer.cancel();
        }
        if (tooltip != null) {
            hide();
            isShowing = false;
        }
    }
    
    public void onMouseDown(Widget sender, int x, int y) {
        onMouseLeave(sender);
    }

    
    /**
     * Show the tooltip.
     */
    void show(){
        if (isShowing){
            return;
        }

        RootPanel.get().add(tooltip);
        isShowing = true;
    }
    
    
    /**
     * Hide the tooltip.
     */
    private void hide() {
        if (!isShowing){
            return;
        }
        isShowing = false;
        RootPanel.get().remove(tooltip);
        
    }
    
    
    /**
     * Set the next position of the tooltip.
     */
    private void setPopupPosition(int left, int top) {    		
    	RootPanel.get().setWidgetPosition(tooltip, left, top);			
    }
    
    
    /**
     * A hack for a bug in GWT with the scroll location
     */
    static native int getScrollTop()/*-{
        return $doc.body.scrollTop;
    }-*/;
    
    /**
     * A hack for a bug in GWT with the scroll location
     */
    static native int getScrollLeft()/*-{
        return $doc.body.scrollLeft;
    }-*/;

} 
