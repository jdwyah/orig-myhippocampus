package com.aavu.client.gui.ext;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;

public class FlashContainer extends SimplePanel {

	private String flashStr = 
			"<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0\" width=\"100%\" height=\"100%\" id=\"ocean\" align=\"middle\">"+
//	"<param name=\"allowScriptAccess\" value=\"sameDomain\" />"+
	"<param name=\"movie\" value=\"preOcean.swf\" />"+
	"<param name=\"quality\" value=\"high\" />"+
	"<param name=\"bgcolor\" value=\"#ffffff\" />"+
	"<param name=\"allowScriptAccess\" value=\"always\" />"+
	"<param name='wmode' value='transparent' />"+
	"<param name=\"scale\" VALUE=\"\" />"+
	"<param name=\"swLiveConnect\" VALUE=\"true\" />"+
	"<embed src=\"preOcean.swf\" swLiveConnect=\"true\" quality=\"high\" bgcolor=\"#ffffff\" allowScriptAccess='always' wmode='transparent' width=\"100%\" height=\"100%\" name=\"ocean\" scale=\"\" align=\"middle\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" />"+
	"</object>";

	public FlashContainer(){
	
		Element e = DOM.createDiv();		
		DOM.setInnerHTML(e, flashStr );

		setElement(e);
		

	}


	native void pokeFlash()/*-{
	
	var b = $doc["ocean"];
	
	alert("bb"+b);
	
	if(doc.all){
		alert("doc.all");
		b = $wnd["ocean"];
	}
			
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
 
	
    protected String getCommand(){
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
	protected native void doIslands(String command)/*-{
	
	//$doc firefox
	//$wnd IE
	
	var b = $doc["ocean"];	
	
	if($doc.all){
		b = $wnd["ocean"];
	}
			
	alert("b: "+b);

 	alert(command);
 
 		try{
			b.CallFunction(command);
    	}catch(e){
     		alert("fail "+e);
    	}		    	
	}-*/;
	
	
}
