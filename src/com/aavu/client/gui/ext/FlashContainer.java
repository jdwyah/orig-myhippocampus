package com.aavu.client.gui.ext;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;

public class FlashContainer extends SimplePanel {

	private String flashStr = 
			"<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0\" width=\"100%\" height=\"100%\" id=\"ocean\" align=\"middle\">"+
//	"<param name=\"allowScriptAccess\" value=\"sameDomain\" />"+
	"<param name=\"movie\" value=\"ocean.swf\" />"+
	"<param name=\"quality\" value=\"high\" />"+
	"<param name=\"bgcolor\" value=\"#ffffff\" />"+
	"<param name=\"allowScriptAccess\" value=\"always\" />"+
	"<param name='wmode' value='transparent' />"+
	"<param name=\"scale\" VALUE=\"\" />"+
	"<param name=\"swLiveConnect\" VALUE=\"true\" />"+
	"<embed src=\"ocean.swf\" swLiveConnect=\"true\" quality=\"high\" bgcolor=\"#ffffff\" allowScriptAccess='always' wmode='transparent' width=\"100%\" height=\"100%\" name=\"ocean\" scale=\"\" align=\"middle\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" />"+
	"</object>";

	public FlashContainer(){
		initFunctions();
//		/GWT.create(classLiteral);
		
		Element e = DOM.createDiv();		
		DOM.setInnerHTML(e, flashStr );

		setElement(e);
		setStyleName("GuiTest-Ocean");

	}

	native void initFunctions()/*-{
	alert("init start");
	var foo = '42';
	
	alert("setable"+$wnd.setable);
	alert("setable"+$wnd.setable);
	$wnd.setable = "i've set you";
	alert("init fin");
	
	function testmovie_DoFSCommand(command, args) { 
   		alert("Here's the Flash message: " + args);  			
	}
	alert("f1");
	$wnd.flashCommand = function ocean_DoFSCommand(command, args){
		alert("asdf");
		alert("command"+command);
	}
	alert("f2");
//	function helloWorld = function(){ 
//		alert("hellowWorldCalled");
//		return "YESSSSSSS";
//	}
    
}-*/;
	
//	
//	native void initFlash()/*-{
//	function callExternalInterface() {
//    	thisMovie("ocean").moveSq();
//	}
//
//	function thisMovie(movieName) {
//    	if (navigator.appName.indexOf("Microsoft") != -1) {
//        	return window[movieName]
//    	}
//    	else {
//        	return document[movieName]
//    	}
//	}
//
//	}-*/;
	
	
//	
//	native void callFlash(String function,Object[] args)/*-{
//
//	var a = $wnd["ocean"];
//
//	a.CallFunction("<invoke name=\"moveSq\" returntype=\"javascript\">" + 
//    	 + "</invoke>");
//	    	
//	}-*/;



}
