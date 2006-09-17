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

	private String boolProp(String name,boolean val){
		return "<property id='"+name+"'><bool>"+val+"</bool></property>";
	}
	private String numberProp(String name,long val){
		return "<property id='"+name+"'><number>"+val+"</number></property>";
	}
	private String stringProp(String name,String val){
		return "<property id='"+name+"'><string>"+val+"</string></property>";
	}
	private String islandObj(long id, String name,int size){
		return "<object>"+numberProp("id",id)+stringProp("tag",name)+numberProp("size",size)+"</object>";
	}
 
	
    public String getCommand(){
    	StringBuffer sb = new StringBuffer();
    	sb.append("<invoke name=\"initLand\" returntype=\"javascript\"><arguments>");
    	sb.append("<number>56</number>");
    	sb.append("<array>");     	
     	sb.append("<property id='0'>"+islandObj(7,"Music",2)+"</property>");
     	sb.append("<property id='1'>"+islandObj(8,"Contacts",8)+"</property>");
     	sb.append("<property id='2'>"+islandObj(24,"Books",10)+"</property>");
     	sb.append("</array>");
     	sb.append("<number>6</number>");
     	sb.append("<number>100</number>");
     	sb.append("</arguments></invoke>");    				
		return sb.toString();
	}
	

	//initLand(56, [{id:7,tag:"iO",size:1},{id:8,tag:"iT",size:4},{id:24,tag:"i3",size:8}], 6, 100); 
	native void doIslands(String command)/*-{
	
	//$doc firefox
	//$wnd IE
	
	var b = $wnd["ocean"];
	
	alert("b: "+b);

 	alert(command);
 
 		try{
			b.CallFunction(command);
    	}catch(e){
     		alert("fail "+e);
    	}		    	
	}-*/;
	
	
	public void newTopic() {
		Topic blank = new Topic();
		blank.setTitle("new");
		bringUpChart(blank);		
	}


	public void showTagBoard() {
		doIslands(getCommand());
		TagEditorWindow tw = new TagEditorWindow(hippoCache);
		tw.setPopupPosition(100,100);
		tw.show();
		
		
	}
	
}
