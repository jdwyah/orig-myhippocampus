package com.aavu.client.gui;

import com.aavu.client.gui.ext.ModablePopupPanel;
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
import com.google.gwt.user.client.ui.Widget;

public class PopupWindow extends ModablePopupPanel implements ClickListener{

    private DockPanel inner = new DockPanel();
    private DragListener dragListener = new DragListener();
    private FocusPanel caption = new FocusPanel();
    private Label captionText = new Label();
    private HorizontalPanel n = new HorizontalPanel();
    private AbsolutePanel c = new AbsolutePanel();
    private ScrollPanel content = new ScrollPanel();
    private Image close = new Image("img/close.gif");

    public PopupWindow(String text) {
    		super(true);
            captionText.setText(text);
            n.add(captionText);
            n.add(close);
            n.setCellHorizontalAlignment(close,HasAlignment.ALIGN_RIGHT);
            n.setWidth("100%");

            close.setStyleName("close");
            close.addClickListener(this);

            caption.setStyleName("Caption");
            caption.addMouseListener(dragListener);
            caption.add(n);

            c.add(content);
            content.setStyleName("Content");

            inner.add(caption,DockPanel.NORTH);
            inner.add(c,DockPanel.CENTER);
            inner.setStyleName("PopBody");

            add(inner);
            addStyleName("PopWindow");
    }

    public void onClick(Widget sender){
            hide();
    }

    public void setContentPanel(Widget widget){
            content.add(widget);
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
