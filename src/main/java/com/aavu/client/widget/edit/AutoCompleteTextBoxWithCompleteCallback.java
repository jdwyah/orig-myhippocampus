package com.aavu.client.widget.edit;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.autocompletion.AutoCompleteTextBoxAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Wraps getTopicIdentForNameOrCreateNew() functionality so that classes that want a 
 * topic completer will just get called back with a topic identifier, even if we have 
 * to go create it on the fly.
 * 
 * NOTE: Relies upon AutoCompleteTextBoxAsync.getTopicIdForString() functionality, which 
 * was originally going to happen at the TopicCache level. If we put the topicCacheing back 
 * in, we could take this out, but for now, this makes a significant performance improvement,
 * particularly for the AddLink stuff. 
 * 
 * @author Jeff Dwyer
 *
 */
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
