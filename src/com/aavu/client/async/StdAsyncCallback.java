package com.aavu.client.async;

import com.aavu.client.gui.StatusCode;
import com.aavu.client.service.Manager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class StdAsyncCallback implements AsyncCallback {

	private static Manager manager;
	public static void setManager(Manager manager) {
		System.out.println("@@@@@@@@@@ set man "+manager);
		StdAsyncCallback.manager = manager;
	}

	private static int callNum = 0;
	
	private String call;
	private int myNum;
	
	public StdAsyncCallback(String call){
		this.call = call;
		myNum = callNum++;
		System.out.println("manager "+manager);
		
		//TODO shouldn't need null checks, but we do.
		if(manager != null)
			manager.updateStatus(myNum,call,StatusCode.SEND);		
	}
	
	public void onFailure(Throwable caught) {
		if(GWT.isScript()){
			Window.alert(call+" failed! "+caught);
		}
		System.out.println(call+" failed! "+caught);
		
		if(manager != null)
			manager.updateStatus(myNum, call, StatusCode.FAIL);
	}

	public void onSuccess(Object result) {
		if(manager != null)
			manager.updateStatus(myNum, call, StatusCode.SUCCESS);
	}

}
