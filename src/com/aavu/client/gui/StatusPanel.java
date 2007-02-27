package com.aavu.client.gui;

import java.util.HashMap;
import java.util.Map;

import com.aavu.client.gui.ext.GUIEffects;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

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
	private class StatusLabel extends Label {
		
		public StatusLabel(String string, StatusCode statusCode) {
			super(" ");//string);
			setCode(statusCode);	
		}

		private void setCode(StatusCode statusCode) {			
			setStyleName(statusCode.getCode());			
		}
		
	}
	
	
}
