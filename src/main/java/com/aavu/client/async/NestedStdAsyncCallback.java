package com.aavu.client.async;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class NestedStdAsyncCallback {

	private AsyncCallback callback;

	public NestedStdAsyncCallback(AsyncCallback callback) {
		super();
		this.callback = callback;
	}


	public void run(List nest) {
		
		System.out.println("run ");
		
		callback.onSuccess(null);
		
		if(nest.size() > 0){
			System.out.println("nest size now "+nest.size());
			NestedStdAsyncCallback next = (NestedStdAsyncCallback) nest.get(0);
			nest.remove(0);
			next.run(nest);
		}else{
			System.out.println("end of the line");
		}
		
	}
	

}
