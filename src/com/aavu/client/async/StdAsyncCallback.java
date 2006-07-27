package com.aavu.client.async;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class StdAsyncCallback implements AsyncCallback {

	public void onFailure(Throwable caught) {
		System.out.println("Failed! "+caught);
	}

}
