package com.aavu.client.async;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class StdAsyncCallback implements AsyncCallback {

	private String call;

	public StdAsyncCallback(String call){
		this.call = call;
	}
	
	public void onFailure(Throwable caught) {
		System.out.println(call+" failed! "+caught);
	}

}
