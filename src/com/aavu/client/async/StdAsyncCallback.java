package com.aavu.client.async;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class StdAsyncCallback implements AsyncCallback {

	private String call;

	public StdAsyncCallback(String call){
		this.call = call;
	}
	
	public void onFailure(Throwable caught) {
		if(GWT.isScript()){
			Window.alert(call+" failed! "+caught);
		}
		System.out.println(call+" failed! "+caught);
	}

}
