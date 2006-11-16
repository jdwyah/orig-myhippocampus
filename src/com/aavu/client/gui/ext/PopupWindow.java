package com.aavu.client.gui.ext;

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

public class PopupWindow extends ModablePopupPanel implements ClickListener{

    private DragListener dragListener = new DragListener();
    private FocusPanel caption = new FocusPanel();
    private Label captionText;    
    private VerticalPanel mainPanel;
    
    
    public PopupWindow(String text){
    	this(text,false);
    }
    public PopupWindow(String text,boolean modal){
    	super(modal);
    	addStyleName("PopWindow");
    	
    	mainPanel = new VerticalPanel();
    	
    	HorizontalPanel titleBar = new HorizontalPanel();
    	
    	captionText = new Label(text);
    	caption.addMouseListener(dragListener);
    	caption.add(captionText);
    	caption.setStyleName("Caption");
    	
    	titleBar.add(caption);
    	titleBar.add(getCloser());
    	
    	mainPanel.add(titleBar);
    	mainPanel.setStyleName("Content");
    	add(mainPanel);
    }
    
    
    public Image  getCloser(){
    	Image close = new Image("img/close.gif");
    	   close.setStyleName("close");
           close.addClickListener(this);
           return close;
    }
    

    public void onClick(Widget sender){
            close();
    }
    public void close(){
    	hide();
    }

    public void setContentPanel(Widget widget){
            mainPanel.add(widget);
            show();
    }

    public void setText(String text){
            captionText.setText(text);
    }

    private class DragListener extends MouseListenerAdapter {

      private boolean dragging;
      private int dragStartX;
      private int dragStartY;

      public void onMouseDown(Widget sender, int x, int y) {
                dragging = true;
                dragStartX = x;
                DOM.setCapture(caption.getElement());
                dragStartY = y;
      }

      public void onMouseMove(Widget sender, int x, int y) {
              if(dragging){
                      int absX = x +sender.getAbsoluteLeft();
                      int absY = y +sender.getAbsoluteTop();
                      setPopupPosition(absX - dragStartX, absY - dragStartY);
              }
      }

      public void onMouseUp(Widget sender, int x, int y) {
                    dragging = false;
                    DOM.releaseCapture(caption.getElement());
      }

    }

} 
