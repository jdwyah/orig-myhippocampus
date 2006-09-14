package com.aavu.client.gui;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.FlashContainer;
import com.aavu.client.gui.ext.MultiDivPanel;
import com.aavu.client.service.cache.HippoCache;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;

public class MainMap extends Composite {

	private Sidebar sideBar;
	private DockPanel mainDock;
	private HippoCache hippoCache;
	
	public MainMap(HippoCache hippoCache){
		this.hippoCache = hippoCache;
		
		MultiDivPanel mainP = new MultiDivPanel();	

		sideBar = new Sidebar(this,hippoCache);

		initFlash();
		
		//		
		//Label l = new Label("center!");
		//l.addStyleName("centered");
		
		Ocean o = new Ocean();
		
		
		mainP.add(new CompassRose());
		mainP.add(new FlashContainer());
		mainP.add(sideBar);
	
//		SVGPanel sp = new SVGPanel(500, 300);
//
//		sp.add(sp.createCircle(50, 50, 10)
//				.setFill(Color.BLACK)
//				.setStrokeWidth(15)
//				.setStroke(Color.WHITE));
		
//		initWidget(sp);
		
		
		initWidget(mainP);
	}
	
	
	public void bringUpChart(Topic topic) {
		
		
		PopupWindow p = new PopupWindow(topic.getTitle());
		p.setPopupPosition(100,100);
		p.show();
		
//		Chart c = new Chart();
//		c.setPopupPosition(200,200);
		
					
//		c.show();
		
		System.out.println("poke flash");
		pokeFlash();
	}
	
	native void initFlash()/*-{
	function callExternalInterface() {
    	thisMovie("ocean").moveSq();
	}

	function thisMovie(movieName) {
    	if (navigator.appName.indexOf("Microsoft") != -1) {
        	return window[movieName]
    	}
    	else {
        	return document[movieName]
    	}
	}

	}-*/;
	
	native void pokeFlash()/*-{
	
	var a = $doc["ocean"];
	var b = $wnd["ocean"];
	
	alert("b: "+b);
 
 	try{
	b.CallFunction("<invoke name=\"moveSq\" returntype=\"javascript\">" + 
    	 + "</invoke>");
    }catch(e){
     alert("fail "+e);
    }
	
	//b.moveSq();
	//alert("d");
	//a.moveSq();
	
	//thisMovie("ocean").moveSq();
	    	
	    	
	    	function thisMovie(movieName) {
    	if (navigator.appName.indexOf("Microsoft") != -1) {
        	return $wnd[movieName]
    	}
    	else {
        	return $doc[movieName]
    	}
	}
	function thisThing() {
    	if (navigator.appName.indexOf("Microsoft") != -1) {
        	return window
    	}
    	else {
        	return document
    	}
	}
	    	
	}-*/;
	
}
