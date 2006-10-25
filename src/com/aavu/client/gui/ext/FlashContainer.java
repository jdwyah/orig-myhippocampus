package com.aavu.client.gui.ext;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.TagStat;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.SimplePanel;

public class FlashContainer extends SimplePanel {

	private String flashStr1 = 
		"<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0\" width=\"100%\" height=\"100%\""+
		"id=\"";
	private String flashStr2 ="\" align=\"middle\">"+

	//	"<param name=\"allowScriptAccess\" value=\"sameDomain\" />"+

	"<param name=\"quality\" value=\"high\" />"+
	"<param name=\"bgcolor\" value=\"#ffffff\" />"+
	"<param name=\"allowScriptAccess\" value=\"always\" />"+
	"<param name='wmode' value='transparent' />"+
	"<param name=\"scale\" VALUE=\"\" />"+
	"<param name=\"swLiveConnect\" VALUE=\"true\" />"+
	"<param name=\"movie\" value=\"";

	String flashStr3 ="\" />";

	String flashStr4 = "<embed src=\"";
	String flashStr5 = "\" swLiveConnect=\"true\" quality=\"high\" bgcolor=\"#ffffff\" allowScriptAccess='always' wmode='transparent' width=\"100%\" height=\"100%\" name=\"";
	String flashStr6 = "\" scale=\"\" align=\"middle\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" /></object>";

	private String name;
	private String flashSourceFile;



	public FlashContainer(String src, String name){
		this.flashSourceFile = src;
		this.name = name;

		Element e = DOM.createDiv();		
		DOM.setInnerHTML(e, getFlashStr() );

		setElement(e);
		
		initFunctions();
	}

	/**
	 * munge together the strings 
	 * @return
	 */
	private String getFlashStr() {
		StringBuffer sb = new StringBuffer();
		sb.append(flashStr1);
		sb.append(name);
		sb.append(flashStr2);
		sb.append(flashSourceFile);
		sb.append(flashStr3);

		sb.append(flashStr4);
		sb.append(flashSourceFile);
		sb.append(flashStr5);
		sb.append(name);
		sb.append(flashStr6);

		return sb.toString();
	}

	/**
	 * TODO take out string concatenation 
	 * 
	 * @param name
	 * @param val
	 * @return
	 */
	protected String boolProp(String name,boolean val){
		return "<property id='"+name+"'><bool>"+val+"</bool></property>";
	}
	protected String numberProp(String name,long val){
		return "<property id='"+name+"'>"+number(val)+"</property>";
	}
	protected String stringProp(String name,String val){
		return "<property id='"+name+"'><string>"+val+"</string></property>";
	}
	protected String number(int val){
		return "<number>"+val+"</number>";
	}
	protected String number(long val){
		return "<number>"+val+"</number>";
	}
	protected String string(String val){
		return "<string>"+val+"</string>";
	}

	private void runCommand(String command){
		System.out.println("cmm:"+command);
		runCommand(name, command);
	}
	
	/**
	 * Tough to know when the Flash will load, and if we call methods on an unloaded Flash, FF crashes spectacularly.
	 * 
	 * Flash will now call flashReady() in HippoTest.html, which sets $wnd.flashReadyVar, which we check here.
	 * 
	 * TODO a little inefficient to create a new timer everytime. Just calling isloaded w/o Timer can cause
	 * flashReadyVar to return undefined though..
	 * 
	 * @param command
	 */
	protected void runCommandDeferred(final String command){
		
		Timer t = new Timer() {
			public void run() {
				if(isLoaded()){
					GWT.log("loaded, run Command", null);					
					runCommand(command);
				}else{
					GWT.log("not loaded, check in 1", null);
					schedule(1000);
				}
			}
		};
		t.schedule(100); 
	}
	
	private native boolean isLoaded()/*-{		
		return $wnd.flashReadyVar;	
	}-*/;
	
	private native void runCommandOld(String name,String command)/*-{
	
		$wnd.callTheFlash(name,command);		
		
	}-*/;
	
	//initLand(56, [{id:7,tag:"iO",size:1},{id:8,tag:"iT",size:4},{id:24,tag:"i3",size:8}], 6, 100); 
	private native void runCommand(String name,String command)/*-{

		//$doc firefox
		//$wnd IE

		var b = $doc[name];	

		if($doc.all){
			b = $wnd[name];
		}

    	
 		try{
			b.CallFunction(command);
    	}catch(e){
    		alert("fail "+e);
    	}		    	
	}-*/;

	public void callback(String command, int arg){		
		System.out.println("flash message 1"+command+" "+arg);
		callbackOverride(command, arg);
	}
	
	/**
	 * override this one for flash events
	 * 
	 * @param command
	 * @param arg
	 */
	protected void callbackOverride(String command, int arg){
	}
	
	/**
	 * link the FlashCommand
	 *
	 */
	native void initFunctions()/*-{
	
	var callBackTarget = this;
	$wnd.flashCommand = function(command,args){    				
		//alert("s "+(typeof command));
		//alert("s "+(typeof args));											    	                															    
    	callBackTarget.@com.aavu.client.gui.ext.FlashContainer::callback(Ljava/lang/String;I)(command,args);
    };
	
	}-*/;
}
