package com.aavu.client.widget.edit;

import com.aavu.client.async.EZCallback;
import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.autocompletion.AutoCompleteTextBoxAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AutoCompleteTextBoxWithCompleteCallback extends AutoCompleteTextBoxAsync {
	
	private CompleteListener listener;
	

	public void complete() {
		super.complete();
		System.out.println("In AutoCompleteBox's complete()");
		
		if(listener != null){			
			
			TopicIdentifier ti = getTopicIdForString(this.getText());
			
			if(null == ti){
				System.out.println("Autocomplete MISS. Creating");
				getTopicIdentForNameOrCreateNew(this.getText(), new StdAsyncCallback(ConstHolder.myConstants.autocomplete_create_Async()){
					public void onSuccess(Object result) {
						super.onSuccess(result);
						listener.completed((TopicIdentifier) result);
					}});		
			}else{
				System.out.println("Autocomplete FOUND in set");
				listener.completed(ti);
			}
		}
			
	}

	protected abstract void getTopicIdentForNameOrCreateNew(String completeText, AsyncCallback callback);

	
	public void addListener(CompleteListener listener) {
		this.listener = listener;
	}
}
