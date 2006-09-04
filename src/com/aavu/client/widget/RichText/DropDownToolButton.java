/*
 * Created on 2006/07/27 18:33:27 David <davidge7@gmail.com>
 */
package com.aavu.client.widget.RichText;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public abstract class DropDownToolButton extends ImageToolButton {
	private Widget dropDownPanel = null;
	private boolean appended = false;

	public DropDownToolButton(String id, String imgUrl, String toolTip,
			String command) {
		super(id, imgUrl, toolTip, command);
	}
	

	public void addDropDownPanel(Widget dropDownPanel){
		this.dropDownPanel = dropDownPanel;
	}

	/**
	 * hide the drop down menu/panel of this button.
	 */
	public void hideDropDown(){
		if(dropDownPanel != null){
			dropDownPanel.setVisible(false);
		}
	}
	
	/**
	 * show the drop down menu/panel of this button.
	 */
	public void showDropDown(){
		if(dropDownPanel == null){
			return;
		}
		DOM.setStyleAttribute(dropDownPanel.getElement(), "position", "absolute");
		if(!appended){
			this.getRichTextArea().addWidgetToLastToolBar(dropDownPanel);
			appended = true;
		}
		DOM.setStyleAttribute(dropDownPanel.getElement(), "position", "absolute");
		int left, top;

//		left = this.getAbsoluteLeft();
//		top = this.getAbsoluteTop() + this.getOffsetHeight();
		
		left = getLeft(this.getElement());
		top = getTop(this.getElement()) + this.getOffsetHeight();
		DOM.setStyleAttribute(dropDownPanel.getElement(), "left", left + "px");
		DOM.setStyleAttribute(dropDownPanel.getElement(), "top", top + "px");
		DOM.setStyleAttribute(dropDownPanel.getElement(), "zIndex", "779999");

		dropDownPanel.setVisible(true);
	}
	
	/**
	 * override the onclick event, show the drop down menu.
	 */
	protected void onClick(){
		//save range state, when select a color restore the range.
		this.saveRange();
		//cal the updateToolButtonStates to close other drop down panels.
		this.getRichTextArea().updateToolButtonStates();
		//fire the beforeShowPanel event.
		beforeShowPanelEvent();
		showDropDown();
	}
	
	/**
	 * event fired after clicked the icon and before show the drop down panel.
	 * for example: set the selected item in this event
	 */
	protected abstract void beforeShowPanelEvent();
	
	
	public void updateStates(Object newValue) {
		super.updateStates(newValue);
		hideDropDown();
	}
	
	private native int getLeft(Element elem) /*-{
	  var left = 0;
	  while (elem) {
	    left += elem.offsetLeft - elem.scrollLeft;
	    elem = elem.offsetParent;
        if(elem && elem.style.position == 'absolute'){
            return left;
        }
	  }
	  return left + $doc.body.scrollLeft;
	}-*/;
	private native int getTop(Element elem) /*-{
	  var top = 0;
	  while (elem) {
	    top += elem.offsetTop - elem.scrollTop;
	    elem = elem.offsetParent;
        if(elem && elem.style.position == 'absolute'){
          return top;
        }
	  }
	  return top + $doc.body.scrollTop;
	}-*/;
	
}
