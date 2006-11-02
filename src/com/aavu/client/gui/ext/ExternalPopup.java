package com.aavu.client.gui.ext;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

public class ExternalPopup {
	private JavaScriptObject window;

	public ExternalPopup (String url) {

		int left = Window.getClientWidth()/2 - 250;
		int top = Window.getClientHeight()/2 - 150;

		window = openNative(url,left,top);

		Timer t = new Timer(){
			public void run() {
				schedule(10000);
				System.out.println(getValue(window));				
			}};


			t.schedule(5000);		
		//	HTTPRequest.asyncGet(url, url, url, handler)

	}

	/**	
		http://www.myhippocampus.com/site/facebook.html?auth_token=f891c39a134bb167995836b33ebddc4b
	 */
	private native String getValue(JavaScriptObject popup)/*-{
		alert("pop: 1 "+popup);
		alert("pop: 2 "+popup.window);
		alert("pop: 2 "+popup.document);
		return popup.location.href;
	}-*/;

	private native JavaScriptObject openNative(String url,int left, int top)/*-{		

		var props = "menubar=no,toolbar=no,resizable=1,width=610,height=420";	
        props = props + ",top=" + top;
        props = props + ",left=" + left;

//alert("2");
		var myhippo_window = $wnd.open(url,"",props);
   		myhippo_window.focus();

//alert("3");
 
 		alert("my hip: "+myhippo_window);
 		alert("my hip loc: "+myhippo_window.location);
 		alert("3");
 	
 		alert("4");
 		alert("my hip win: "+myhippo_window.window);
 		alert("5");
 		alert("my hip doc: "+myhippo_window.document);
 		alert("6");
 		
// 		var gURLBar = document.getElementById("urlbar");     
//        var location, title;
//        var browser = window.getBrowser();
//        var webNav = browser.webNavigation;
//        if( webNav.currentURI){
//          location = webNav.currentURI.spec;
//        }
//        else{
//          location = gURLBar.value; 
//        }
 		
		return myhippo_window;	

	}-*/;


}
