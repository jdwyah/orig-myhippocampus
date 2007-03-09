package com.aavu.client.gui;

import java.util.HashMap;
import java.util.Map;

import com.aavu.client.gui.ext.GUIEffects;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class StatusPanel extends SimplePanel {

	private Map map = new HashMap();
	
	private CellPanel displayPanel = new HorizontalPanel();
	
	public StatusPanel(){
		
		displayPanel.setStyleName("H-StatusLabels");
		
		//displayPanel.add(new Label("Messages:"));
		
		add(displayPanel);
		
		addStyleName("H-AbsolutePanel");
		addStyleName("H-StatusPanel");				
	}
		
	public void update(int id, String string, StatusCode statusCode) {
		
		System.out.println("UPDATE "+id+" "+string+" "+statusCode.getCode());
		
		if(statusCode == StatusCode.SEND){
			StatusLabel lab = new StatusLabel(string,statusCode);
			
			map.put(new Integer(id),lab);
			displayPanel.add(lab);
		}
		else if(statusCode == StatusCode.SUCCESS){
			final StatusLabel sl = (StatusLabel) map.get(new Integer(id)); 
			//System.out.println("sl "+sl);
			if(sl != null){
				sl.setCode(statusCode);								
				GUIEffects.fadeAndRemove(sl,3000);				
			}
		}
		//FAIL
		else { 
			StatusLabel sl = (StatusLabel) map.get(new Integer(id));
			if(sl != null){
				sl.setText(string);
				sl.setCode(statusCode);
				GUIEffects.fadeAndRemove(sl,3000,6000);				
			}
		}
	}


	/**
	 * Contain in a H-StatusLabels
	 * 
	 * @author Jeff Dwyer
	 *
	 */
	private class StatusLabel extends Label implements MouseListener {		
		private String string;
		public StatusLabel(String string, StatusCode statusCode) {
			super(" ");
			this.string = string;
			setCode(statusCode);	
			addMouseListener(this);
		}

		private void setCode(StatusCode statusCode) {			
			setStyleName(statusCode.getCode());			
		}

		
		public void onMouseEnter(Widget sender) {
			setText(string);
		}

		public void onMouseLeave(Widget sender) {
			setText("");
		}
		public void onMouseDown(Widget sender, int x, int y) {}
		public void onMouseMove(Widget sender, int x, int y) {}
		public void onMouseUp(Widget sender, int x, int y) {}
	}
	
	
}
