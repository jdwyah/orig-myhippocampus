package com.aavu.client.widget.edit;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.widget.autocompletion.RemoteTopicAutoCompletionItems;


public class TopicCompleter extends AutoCompleteTextBoxWithCompleteCallback {

	private static final int LENGTH = 40;

	private static TopicCache topicService;
		
	public static void setTopicService(TopicCache topicService) {
		TopicCompleter.topicService = topicService;
	}



	public TopicCompleter() {
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
	public void getTopicIdentForNameOrCreateNew(String completeText, StdAsyncCallback callback) {
		topicService.getTopicIdentForNameOrCreateNew(completeText, callback);
	}

	
}
