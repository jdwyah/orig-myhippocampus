package com.aavu.client.widget.edit;

import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.widget.autocompletion.RemoteTopicAutoCompletionItems;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class TopicCompleter extends AutoCompleteTextBoxWithCompleteCallback {

	private static final int LENGTH = 40;
	private TopicCache topicService;


	public TopicCompleter(TopicCache topicService) {
		this.topicService = topicService;
		setCompletionItems(new RemoteTopicAutoCompletionItems(topicService));
		
		//doesn't seem to work for formatting.. what prop do I want?
		setMaxLength(LENGTH);

	}
	
	/**
	 * Convenience method to use our TopicService.
	 * 
	 * @param completeText
	 * @param callback
	 */
	public void getTopicIdentForNameOrCreateNew(String completeText, AsyncCallback callback) {
		topicService.getTopicIdentForNameOrCreateNew(completeText, callback);
	}

	

	
}
