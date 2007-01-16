package com.aavu.client.gui;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwtwidgets.client.ui.PNGImage;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.gui.ext.tabbars.Orientation;
import com.aavu.client.gui.ext.tabbars.VertableTabPanel;
import com.aavu.client.gui.glossary.Glossary;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.VerticalPanel;
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
