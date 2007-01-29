package com.aavu.client.async;

import com.aavu.client.exception.HippoException;
import com.aavu.client.gui.StatusCode;
import com.aavu.client.service.Manager;
import com.aavu.client.util.Logger;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class StdAsyncCallback implements AsyncCallback {

	private static Manager manager;
	public static void setManager(Manager manager) {
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

		Logger.log(call+" failed! "+caught+" "+caught.getMessage());
		
		System.out.println(call+" failed! "+caught+" msg "+caught.getMessage());
		
		//TODO make the GWT AOP recast the UsernameNotFound Exception to a 
		//HippoUserException
		if(caught.getMessage().startsWith("Username not found")){
			manager.doLogin();
		}
		
		if(manager != null){
			try{
				HippoException hbe = (HippoException) caught;
			
				manager.updateStatus(myNum, call+" fail "+hbe.getMessage(), StatusCode.FAIL);
				
			}catch(Exception e){
				manager.updateStatus(myNum, call, StatusCode.FAIL);				
			}
		}
	}

	public void onSuccess(Object result) {
		if(manager != null)
			manager.updateStatus(myNum, call, StatusCode.SUCCESS);
	}

}
