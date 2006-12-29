package com.aavu.client.gui;

import java.util.HashMap;
import java.util.Map;

import org.gwtwidgets.client.wrap.Effect;
import org.gwtwidgets.client.wrap.EffectOption;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class StatusPanel extends SimplePanel {

	private Map map = new HashMap();
	
	private VerticalPanel displayPanel = new VerticalPanel();
	
	public StatusPanel(){
		
		displayPanel.setStyleName("H-StatusLabels");
		
		displayPanel.add(new Label("Messages:"));
		
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
			System.out.println("sl "+sl);
			if(sl != null){
				sl.setCode(statusCode);				
				Effect.fade(sl);
				removeInXSeconds(sl,3000);				
			}
		}
		//FAIL
		else { 
			StatusLabel sl = (StatusLabel) map.get(new Integer(id));
			if(sl != null){
				sl.setText(string);
				sl.setCode(statusCode);
				fadeAndRemove(sl,3000,6000);				
			}
		}
	}

	/**
	 * Utility to fade & remove a widget after a short time using 2 timers
	 * 
	 * @param w
	 * @param i
	 */
	private void fadeAndRemove(final Widget w, int fadeInX,int removeInX){		
		Timer t = new Timer() {
		      public void run() {
		    	  Effect.fade(w);
		      }
		    };
		t.schedule(fadeInX);		
		removeInXSeconds(w,removeInX);
	}
	
	/**
	 * Utility to remove a widget after a short time,
	 * for instance after we Effect.fade()
	 * @param w
	 * @param i
	 */
	private void removeInXSeconds(final Widget w, int i) {
		Timer t = new Timer() {
		      public void run() {
		    	  w.removeFromParent();
		      }
		    };
		t.schedule(i);		
	}

	/**
	 * Contain in a H-StatusLabels
	 * 
	 * @author Jeff Dwyer
	 *
	 */
	private class StatusLabel extends Label {
		
		public StatusLabel(String string, StatusCode statusCode) {
			super(string);
			setCode(statusCode);	
		}

		private void setCode(StatusCode statusCode) {			
			setStyleName(statusCode.getCode());			
		}
		
	}
	
	
}
