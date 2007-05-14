package com.aavu.client.gui;

import com.aavu.client.gui.ext.tabbars.Orientation;
import com.aavu.client.gui.glossary.Glossary;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public class Sidebar extends Glossary implements MouseListener {
		
	private Timer hideTimer;
	
	public Sidebar(Manager manager){
		super(manager,Orientation.VERTICAL);
		
		addStyleName("H-AbsolutePanel");
		addStyleName("H-Sidebar");
		
		tabPanel.hideDeck();
		
		hideTimer = new Timer(){
			public void run() {				
				tabPanel.hideDeck();
			}};
		
	}


	public void onMouseEnter(Widget sender) {
		hideTimer.cancel();		
		tabPanel.showDeck();
	}

	public void onMouseLeave(Widget sender) {	
		hideTimer.schedule(800);				
	}
	
	public void onMouseDown(Widget sender, int x, int y) {}
	public void onMouseMove(Widget sender, int x, int y) {}
	public void onMouseUp(Widget sender, int x, int y) {}

	

}
