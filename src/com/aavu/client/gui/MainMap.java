package com.aavu.client.gui;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.FlashContainer;
import com.aavu.client.gui.ext.MultiDivPanel;
import com.aavu.client.gui.ext.PopupWindow;
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
		
		Ocean o = new Ocean();
		
		mainP.add(new CompassRose());
		mainP.add(new FlashContainer());
		mainP.add(sideBar);
		mainP.add(new Dashboard(this,hippoCache));		
		
		initWidget(mainP);
	}
	
	
	public void bringUpChart(Topic topic) {
		

		TopicWindow tw = new TopicWindow(hippoCache,topic);
		tw.setPopupPosition(100,100);
		tw.show();
		
		
		//System.out.println("poke flash");
		//pokeFlash();
	}
	
	
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


	public void newTopic() {
		Topic blank = new Topic();
		blank.setTitle("new");
		bringUpChart(blank);		
	}


	public void showTagBoard() {
		TagEditorWindow tw = new TagEditorWindow(hippoCache);
		tw.setPopupPosition(100,100);
		tw.show();
		
		
	}
	
}
