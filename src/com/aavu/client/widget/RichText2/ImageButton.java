/*
 * Created on 02.07.2006
 */
package com.aavu.client.widget.RichText2;


import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;

/**
 * This class represent an image button for using in toolbars. Via css you can setup the 
 * typical visual effects.
 * 
 * The ImageButton is using the follow stylenames:
 * <ul>
 * <li>ImageButton
 * <li>ImageButton-over
 * <li>ImageButton-pressed
 * <li>Tooltip
 * </ul>
 * 
 * @author Volker Berlin
 */
public class ImageButton extends Image{

    
    /**
     * The constructor of an ImageButton
     * @param url The url of the Image.
     * @param tooltip The text of a tooltip. Will be ignored if null or empty.
     */
    public ImageButton (String url, String tooltip){
        super(url);
        if(tooltip != null && tooltip.length()>0){
            super.addMouseListener(new Tooltip(tooltip,this));
        }
        setStyleName("ImageButton");
        addMouseListener(new MouseListener(){

            public void onMouseEnter (Widget sender){
                setStyleName("ImageButton-over");
            }

            public void onMouseMove(Widget sender, int x, int y) {
                setStyleName("ImageButton-over");
            }
            
            public void onMouseLeave (Widget sender){
                setStyleName("ImageButton");
            }
            
            public void onMouseDown(Widget sender, int x, int y){
                setStyleName("ImageButton-pressed");
            }
            public void onMouseUp(Widget sender, int x, int y) {
                setStyleName("ImageButton-over");
            }
        });
    }
    
    
    /**
     * Overriden to block the browser's default behaviour.
     */
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        //This is required to prevent a Drag & Drop of the Image in the edit text.
        DOM.eventPreventDefault(event);
    }
}
