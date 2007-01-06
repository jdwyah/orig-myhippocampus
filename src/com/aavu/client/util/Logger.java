package com.aavu.client.util;

public class Logger {
	public static void log(String msg){
		System.out.println("Problem initting services! "+msg);	
		logN(msg);
	}
	private static native void logN(String msg) /*-{
	if(window.console) {
	        window.console.log(msg);
	}
	}-*/; 
}
