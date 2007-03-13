package com.aavu.client.async;

import com.aavu.client.util.Logger;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Intended for local callbacks.
 * 
 * Won't put anything in the status window.
 * 
 * @author Jeff Dwyer
 *
 */
public abstract class EZCallback implements AsyncCallback {
	public void onFailure(Throwable caught) {
		Logger.log("EZCall failed! "+caught+" "+caught.getMessage());		
	}

}
