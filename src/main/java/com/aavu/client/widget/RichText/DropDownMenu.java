/*
 * Created on 2006/07/28 13:45:22 David <davidge7@gmail.com>
 */
package com.aavu.client.widget.RichText;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;

public class DropDownMenu extends Grid{
	private String panelBackgroundColor = "#EEEEEE";
//	private String panelBorderColor = "#BCBCBC";
	private String menuItemOverBorderColor = "#7D7D7D";
	private String menuItemOverBackgroundColor = "#DDDDDD";
	
	private List menuItems = new ArrayList();
	private boolean itemsChanged = false;
	
	private DropDownMenuListener listener = null;
	
	public DropDownMenu(String panelBgColor, String panelBorderColor, String menuOverBgColor, String menuOverBorderColor) {
		super();

		this.panelBackgroundColor = panelBgColor;
//		this.panelBorderColor = panelBorderColor;
		this.menuItemOverBackgroundColor = menuOverBgColor;
		this.menuItemOverBorderColor = menuOverBorderColor;

	    sinkEvents(Event.ONMOUSEOVER);
        sinkEvents(Event.ONMOUSEOUT);
        sinkEvents(Event.ONCLICK); 

		DOM.setStyleAttribute(this.getElement(), "border", "1px solid " + panelBorderColor);
		DOM.setStyleAttribute(this.getElement(), "backgroundColor", panelBackgroundColor);
		this.setBorderWidth(0);
		this.setCellSpacing(0);
		init();
	}

	public void addDropDownMenuListener(DropDownMenuListener listener){
		this.listener = listener;
	}
	
	
	public void setVisible(boolean visible) {
		paint(true);
		super.setVisible(visible);
		if(listener != null){
			//fire the onShow event
			listener.onShow();
		}
	}
	public void addMenuItem(String value, String menuLabelHtml){
		addMenuItem(value, menuLabelHtml, false);
	}
	public void addMenuItem(String value, String menuLabelHtml, boolean checked){
		String[] item = new String[]{value, menuLabelHtml, (checked ? "t":"f")};
		menuItems.add(item);
		itemsChanged = true;
	}
	public void setMenuItemChecked(int index, boolean checked){
		if(index >= 0 && index < menuItems.size()){
			String[] item = (String[]) menuItems.get(index);
			item[2] = checked? "t":"f";
			itemsChanged = true;
		}
	}

	public void setMenuItemChecked(String value, boolean checked){
		for(Iterator it = menuItems.iterator(); it.hasNext();){
			String[] item = (String[]) it.next();
			if(item[0].equals(value)){
				item[2] = checked?"t":"f";
				itemsChanged = true;
			}
		}
	}
	public void pain(){
		paint(false);
	}
	public void paint(boolean force){
		if(itemsChanged || force){
			init();
		}
	}
	private void init(){
		resize(menuItems.size(), 1);
		for(int i=0; i<menuItems.size(); i++){
			String[] item = (String[]) menuItems.get(i);
			HTML html = new HTML(item[1]);
			this.setWidget(i, 0, html);
			Element td = this.getCellFormatter().getElement(i, 0);
			DOM.setStyleAttribute(td, "border", "1px solid " + panelBackgroundColor);
			DOM.setStyleAttribute(td, "padding", "2px 12px 2px 12px");
            //ie
            DOM.setStyleAttribute(td, "cursor", "hand");
            //mozilla
            DOM.setStyleAttribute(td, "cursor", "pointer");
            DOM.setStyleAttribute(td, "backgroundRepeat", "no-repeat");
            DOM.setStyleAttribute(td, "backgroundPosition", "left center");
            if(item[2].endsWith("t")){
            	//checked item
                DOM.setStyleAttribute(td, "backgroundImage", "url(\"richtext/img/indicator.gif\")");
            }else{
            	//unchecked
                DOM.setStyleAttribute(td, "backgroundImage", "");
            }
		}
		itemsChanged = false;
	}

	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
        switch (DOM.eventGetType(event)) {
          case Event.ONMOUSEOVER: {
              Element td = getMouseEventTargetCell(event);
              if (td == null){
            	  return;
              }
              DOM.setStyleAttribute(td, "backgroundColor", menuItemOverBackgroundColor);
              DOM.setStyleAttribute(td, "borderColor", menuItemOverBorderColor);
              DOM.setStyleAttribute(td, "borderColor", menuItemOverBorderColor);
        	  break;
          }
          case Event.ONMOUSEOUT: {
              Element td = getMouseEventTargetCell(event);
              if (td == null){
            	  return;
              }
              DOM.setStyleAttribute(td, "backgroundColor", panelBackgroundColor);
              DOM.setStyleAttribute(td, "borderColor", panelBackgroundColor);
        	  break;
          }
          case Event.ONCLICK: {
              Element td = getMouseEventTargetCell(event);
              if (td == null){
            	  return;
              }
              Element tr = DOM.getParent(td);
              Element body = DOM.getParent(tr);
              int row = DOM.getChildIndex(body, tr);
              //fire the click event
              if (listener != null){
            	  listener.onClick(row);
            	  listener.onClick(((String[]) menuItems.get(row))[0]);
              }
              DOM.setStyleAttribute(td, "backgroundColor", panelBackgroundColor);
              DOM.setStyleAttribute(td, "borderColor", panelBackgroundColor);
        	  break;
          }
        }
	}

    private Element getMouseEventTargetCell(Event event) {
        Element td = DOM.eventGetTarget(event);
        while (!DOM.getAttribute(td, "tagName").equalsIgnoreCase("td")) {
            if ((td == null) || DOM.compare(td, getElement())){
                return null;
            }
            td = DOM.getParent(td);
        }
        Element tr = DOM.getParent(td);
        Element body = DOM.getParent(tr);
        if (DOM.compare(body, this.getBodyElement())) {
            return td;
        }
        return null;
    } 		

 
}
