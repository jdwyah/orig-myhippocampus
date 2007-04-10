/*
 * Created on 2006/07/26 11:07:23 David <davidge7@gmail.com>
 */
package com.aavu.client.widget.RichText;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTMLPanel;

public class CommandToolButton extends ToolButton {
	private HTMLPanel panel;
//	protected boolean selected = false;
	private String commandValue;
	private String command;
	private String toolTip;
	private int buttonHeight = 22;
	private int buttonWidth = 22;
	
	public CommandToolButton(String id, String innerHtml, String toolTip, String command){
		this.setId(id);
		panel =  new HTMLPanel(innerHtml);
		this.command = command;
		this.toolTip = toolTip;
		
		this.setElement(panel.getElement());
	}

	
	protected void onLoad() {
		super.onLoad();
		DOM.setStyleAttribute(panel.getElement(), "display", ""); //remove the default display:inline of the HTMLPanel
		DOM.setStyleAttribute(panel.getElement(), "borderStyle", "solid");
		DOM.setStyleAttribute(panel.getElement(), "borderWidth", "1px");
		DOM.setStyleAttribute(panel.getElement(), "textAlign", "center");
		DOM.setStyleAttribute(panel.getElement(), "verticalAlign", "middle");
		DOM.setStyleAttribute(panel.getElement(), "padding", "0px");
		DOM.setStyleAttribute(panel.getElement(), "width", buttonWidth + "px");
		DOM.setStyleAttribute(panel.getElement(), "height", buttonHeight + "px");
		DOM.setStyleAttribute(panel.getElement(), "cssFloat", "left");  //for firefox
		DOM.setStyleAttribute(panel.getElement(), "styleFloat", "left"); //for ie
		

		DOM.setAttribute(panel.getElement(), "title", toolTip);
		resetStyle();
		this.sinkEvents(Event.ONMOUSEOVER);
		this.sinkEvents(Event.ONMOUSEOUT);
		this.sinkEvents(Event.ONMOUSEDOWN);
		this.sinkEvents(Event.ONCLICK);
	}
	
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
        	case Event.ONMOUSEOVER: {
    			if(!isSelected()){
    				DOM.setStyleAttribute(panel.getElement(), "borderColor", richTextArea.getConfig().getToolButtonOverBorderColor());
    				DOM.setStyleAttribute(panel.getElement(), "backgroundColor", richTextArea.getConfig().getToolButtonOverBackgroundColor());
    			}
        		break;
        	}case Event.ONMOUSEOUT: { 
       			resetStyle();
       			break;
        	}case Event.ONMOUSEDOWN: { 
    			if(!isSelected()){
    				DOM.setStyleAttribute(panel.getElement(), "borderColor", richTextArea.getConfig().getToolButtonDownBorderColor());
    				DOM.setStyleAttribute(panel.getElement(), "backgroundColor", richTextArea.getConfig().getToolButtonDownBackgroundColor());
    			}
    			break;
        	}case Event.ONCLICK: {
        		//update states of tool buttons first.(close drop down panel if exists);
        		this.getRichTextArea().updateToolButtonStates();
    			onClick();
        		break;
        	}
		}
	}
	protected void onClick(){
		execCommand(command, null);
	}
	private void resetStyle(){
		if(isSelected()){
			DOM.setStyleAttribute(panel.getElement(), "borderColor", richTextArea.getConfig().getToolButtonOnBorderColor());
			DOM.setStyleAttribute(panel.getElement(), "backgroundColor", richTextArea.getConfig().getToolButtonOnBackgroundColor());
		}else{
			DOM.setStyleAttribute(panel.getElement(), "borderColor", richTextArea.getConfig().getToolButtonBorderColor());
			DOM.setStyleAttribute(panel.getElement(), "backgroundColor", richTextArea.getConfig().getToolButtonBackgroundColor());
		}
	}


	public boolean isSelected(){
		if(commandValue != null){
			if(commandValue.toString().equalsIgnoreCase("true")){
				return true;
			}
		}
		return false;
	}


	public String getCommandValue() {
		return commandValue;
	}


	public void setCommandValue(String commandValue) {
		this.commandValue = commandValue;
		resetStyle();
	}


	public String getCommand() {
		return command;
	}


	public void setCommand(String command) {
		this.command = command;
	}


	public String getToolTip() {
		return toolTip;
	}


	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}
	
	
	
}
