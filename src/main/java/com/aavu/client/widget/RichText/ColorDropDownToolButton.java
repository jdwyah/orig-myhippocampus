/*
 * Created on 2006/07/27 18:58:23 David <davidge7@gmail.com>
 */
package com.aavu.client.widget.RichText;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;

public class ColorDropDownToolButton extends DropDownToolButton {
	private String panelBackgroundColor;// = "#EEEEEE";
	private String panelBorderColor;// = "#BCBCBC";
	private String tdOverBorderColor;// = "#7D7D7D";
	private String tdOverBackgroundColor;// = "#DDDDDD";
	private boolean attached = false;
	
	private final String[][] colors = new String[][]{
			{"#FFFFFF", "#CCCCCC", "#C0C0C0", "#999999", "#666666", "#333333", "#000000"},
			{"#FFCCCC", "#FF6666", "#FF0000", "#CC0000", "#990000", "#660000", "#330000"},
			{"#FFCC99", "#FF9966", "#FF9900", "#FF6600", "#CC6600", "#993300", "#663300"},
			{"#FFFF99", "#FFFF66", "#FFCC66", "#FFCC33", "#CC9933", "#996633", "#663333"},
			{"#FFFFCC", "#FFFF33", "#FF0000", "#FFCC00", "#999900", "#660000", "#333300"},
			{"#99FF99", "#66FF99", "#33FF33", "#33CC00", "#009900", "#006600", "#003300"},
			{"#99FFFF", "#33FFFF", "#6666CC", "#00CCCC", "#339999", "#336666", "#003333"},
			{"#CCFFFF", "#66FFFF", "#33CCFF", "#3366FF", "#3333FF", "#000099", "#000066"},
			{"#CCCCFF", "#9999FF", "#6666CC", "#6633FF", "#6600CC", "#333399", "#330099"},
			{"#FFCCFF", "#FF99FF", "#CC66CC", "#CC33CC", "#993399", "#663366", "#330033"},
	};

	public ColorDropDownToolButton(String id, String imgUrl, String toolTip,
			String command) {
		super(id, imgUrl, toolTip, command);
		this.sinkEvents(Event.ONCLICK);
	}
	
	protected void onLoad(){
		super.onLoad();
		if(!attached){
			attachDropDown();
		}
	}
	private void attachDropDown(){
		loadColorsFromConfig();
		ColorPanel cp = new ColorPanel();
		this.addDropDownPanel(cp);
		attached = true;
	}
	public void updateStates(Object newValue) {
		super.updateStates(newValue);
		if(!attached){
			attachDropDown();
		}
	}
	
	protected void beforeShowPanelEvent() {
	}
	
	private void loadColorsFromConfig(){
		RichTextAreaConfig config = this.getRichTextArea().getConfig();
		if(config != null){
			panelBackgroundColor = config.getDropDownPanelBackgroundColor();
			panelBorderColor = config.getDropDownPanelBorderColor();
			tdOverBorderColor = config.getMenuItemMouseOverBorderColor();
			tdOverBackgroundColor = config.getMenuItemMouseOverBackgroundColor();
		}
	}
	public class ColorPanel extends Grid{
		public ColorPanel(){
			super(colors.length, colors[0].length);
			DOM.setStyleAttribute(this.getElement(), "border", "1px solid " + panelBorderColor);
			DOM.setStyleAttribute(this.getElement(), "background", panelBackgroundColor);
			init();
		}
		

		private void init(){
		    for(int i=0; i<colors.length; i++){
		    	for(int j=0; j<colors[0].length; j++){
		    		HTML color = new HTML("&nbsp;");
		    		DOM.setStyleAttribute(color.getElement(), "width", "100%");
		    		DOM.setStyleAttribute(color.getElement(), "height", "100%");
		    		DOM.setStyleAttribute(color.getElement(), "fontSize", "8px");
		    		DOM.setStyleAttribute(color.getElement(), "backgroundColor", colors[i][j]);
		    		this.setWidget(i, j, color);
		    		
		    		Element e = this.getCellFormatter().getElement(i, j);
		    		DOM.setStyleAttribute(e, "border", "1px solid " + panelBackgroundColor);
		    		DOM.setStyleAttribute(e, "padding", "2px");
		    		DOM.setStyleAttribute(e, "width", "12px");
		    		DOM.setStyleAttribute(e, "height", "12px");
		    		DOM.setStyleAttribute(e, "font-size", "8px");
					//ie
					DOM.setStyleAttribute(e, "cursor", "hand");
					//mozilla
					DOM.setStyleAttribute(e, "cursor", "pointer");
		    	}
		    }
		    this.addTableListener(new TableListener(){
				public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
					if(row < colors.length && cell < colors[0].length){
						hideDropDown();
						//restore the saved selection range before execcommand.
						restoreRange();
						execCommand(getCommand(), colors[row][cell]);
					}else{
						Window.alert("impossible, contact with vendor. row=" + row + "; col=" + cell);
					}
					
				}});
		    sinkEvents(Event.ONMOUSEOVER);
	        sinkEvents(Event.ONMOUSEOUT); 
		}
		
	    public void onBrowserEvent(Event event) {
			switch (DOM.eventGetType(event)) {
				case Event.ONMOUSEOVER: {
					Element td = getMouseEventTargetCell(event);
					if (td != null){
						DOM.setStyleAttribute(td, "borderColor", tdOverBorderColor);
						DOM.setStyleAttribute(td, "backgroundColor", tdOverBackgroundColor);
					}
					break;
				}
				case Event.ONMOUSEOUT: {
					Element td = getMouseEventTargetCell(event);
					if (td != null){
						DOM.setStyleAttribute(td, "borderColor", panelBackgroundColor);
						DOM.setStyleAttribute(td, "backgroundColor", panelBackgroundColor);
					}
					break;
				}
			}
			super.onBrowserEvent(event);
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

	
}
